package net.gamers.lobby.scoreboard;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;
import net.gamers.lobby.Main;
import net.gamerspvp.commons.bukkit.cargos.CargosAPI;
import net.gamerspvp.commons.bukkit.cargos.user.customevents.UserUpdatedEvent;
import net.gamerspvp.commons.bukkit.utils.LineAdder;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class ScoreBoardListener implements Listener {
	
	@Getter
	private static HashSet<Team> teams = new HashSet<>();
	
	public ScoreBoardListener(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		ScoreBoard.loadTeamPlayer(player);
		
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("score", "dummy");
		obj.setDisplayName("§2§lGAMERS");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		String cargo = CargosAPI.getUserGroup(player).getPreffix();
		String playerName = ScoreBoard.centerText(player.getName(), 16);
		String serverIp = ScoreBoard.centerText(MessageUtils.SERVER_IP.getMessage(), 16);
		
		LineAdder la = new LineAdder(sb, obj);
		la.addLine("", "§1", "", 8);
		la.addLine("  ", " §f", playerName, 7);
		la.addLine("", "§2", "", 6);
		la.addLine("  §fCar", "go§8: §a", cargo, 5);
		la.addLine("", "§3", "", 4);
		la.addLine("  §fCa", "sh§8: §a", "§7Carregando", 3);
		la.addLine("  §fOnli", "ne§8: §a", "§7Carregando", 2);
		la.addLine("", "§4", "", 1);
		la.addLine(" §7", " ", serverIp, 0);
		
		player.setScoreboard(sb);
		ScoreBoard.updateTeams();
	}
	
	@EventHandler
	public void onUserUpdatedEvent(UserUpdatedEvent event) {
		Player player = event.getPlayer();
		if (player == null || !player.isOnline()) {
			return;
		}
		Scoreboard sb = player.getScoreboard();
		Team cargo = sb.getTeam("line5");
		cargo.setSuffix(event.getUser().getGroup());
		ScoreBoard.loadTeamPlayer(player);
		ScoreBoard.updateTeams();
	}
}