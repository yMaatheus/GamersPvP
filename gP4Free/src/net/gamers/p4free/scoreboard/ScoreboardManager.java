package net.gamers.p4free.scoreboard;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.gamers.p4free.Main;
import net.gamers.p4free.utils.LineAdder;
import net.gamers.p4free.utils.SC;

public class ScoreboardManager {
	
	// private Main instance;
	
	private HashMap<Player, String> cachePlayerClan;
	
	public ScoreboardManager(Main instance) {
		// this.instance = instance;
		this.cachePlayerClan = new HashMap<Player, String>();
		update.runTaskTimerAsynchronously(instance, 30 * 20L, 10 * 20L);
	}
	
	public synchronized void updateScoreboard(Player player, String onlineCount) {
		Scoreboard sb = player.getScoreboard();
		Team online = sb.getTeam("line6");
		String playerClan = cachePlayerClan.get(player);
		if (SC.hasClan(player)) {
			if (playerClan == null) {
				String clan_name = SC.getClanName(player);
				LineAdder la = new LineAdder(sb, sb.getObjective("score"));
				if (sb.getTeam("line4") != null) {
					sb.getTeam("line4").unregister();
				}
				if (sb.getTeam("line3") != null) {
					sb.getTeam("line3").unregister();
				}
				if (sb.getTeam("line2") != null) {
					sb.getTeam("line2").unregister();
				}
				if (sb.getTeam("line1") != null) {
					sb.getTeam("line1").unregister();
				}
				la.addLine(" §a", clan_name, "", 4);
				la.addLine("  §f", "Membros: §7", SC.getOnlineMembers(player) + "/" + SC.getMaxMembers(player), 3);
				la.addLine("  §f", "Tag: §7", SC.getTag(player), 2);
				la.addLine("", "§4", "", 1);
				cachePlayerClan.put(player, clan_name);
				return;
			}
			sb.getTeam("line3").setSuffix(SC.getOnlineMembers(player) + "/" + SC.getMaxMembers(player));
			sb.getTeam("line2").setSuffix(SC.getTag(player));
		} else {
			if (playerClan != null) {
				sb.resetScores(cachePlayerClan.get(player));
				sb.resetScores("Membros: §7");
				sb.resetScores("Tag: §7");
				sb.resetScores("§4");
				sb.getTeam("line1").unregister();
				cachePlayerClan.remove(player);
			}
		}
		online.setSuffix(onlineCount);
	}
	
	public void build(Player player) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("score", "dummy");
		obj.setDisplayName("§2§lP4FREE");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		LineAdder la = new LineAdder(sb, obj);
		la.addLine("", "§1", "", 7);
		la.addLine(" §aOnli", "ne: §f", "Carregando", 6);
		la.addLine("", "§0", "", 5);
		// 4
		// 3
		// 2
		// 1
		la.addLine(" §f", "br.mc-gamers.net", "", 0);
		player.setScoreboard(sb);
	}
	
	public BukkitRunnable update = new BukkitRunnable() {
		
		@Override
		public void run() {
			Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
			if (onlinePlayers.isEmpty()) {
				return;
			}
			String onlineCount = String.valueOf(onlinePlayers.size());
			for (Player player : onlinePlayers) {
				if (player == null) {
					return;
				}
				updateScoreboard(player, onlineCount);
			}
		}
	};
	
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		build(event.getPlayer());
	}
}