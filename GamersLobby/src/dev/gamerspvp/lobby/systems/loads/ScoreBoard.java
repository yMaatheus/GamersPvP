package dev.gamerspvp.lobby.systems.loads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.BungeeAPI;
import dev.gamerspvp.lobby.api.LineAdder;

public class ScoreBoard {
	
	public void build(Player p){
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("score", "dummy");
		obj.setDisplayName("§2§lGAMERSPVP");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Objective health = sb.registerNewObjective("vida", "health");
		char s = '\u2764';
		health.setDisplayName("§c" + s);
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		LineAdder ld = new LineAdder(sb, obj);
		
		ld.addLine("", "§2", "", 6);
		ld.addLine("", " §fLobby: §7", "Loading...", 5);
		ld.addLine("", " §fFullPvP: §7", "Loading...", 4);
		ld.addLine("", "§1", "", 3);
		ld.addLine("", " §fNetwork: §7", "§7Loading...", 2);
		ld.addLine("", "§0", "", 1);
		ld.addLine("", " §fmc.gamerspvp.net", "", 0);
		
		p.setScoreboard(sb);
		
	}
	
	public void runUpdater(){
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					try {
						Scoreboard sb = p.getScoreboard();
						Team lobby = sb.getTeam("line5");
						Team fullpvp = sb.getTeam("line4");
						Team network = sb.getTeam("line2");
						lobby.setSuffix("" + BungeeAPI.getPlayers("lobby"));
						fullpvp.setSuffix("" + BungeeAPI.getPlayers("fullpvp"));
						network.setSuffix("" + BungeeAPI.getAllPlayers());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 0, 3*20L);
	}
}