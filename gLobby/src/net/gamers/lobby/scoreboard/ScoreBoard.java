package net.gamers.lobby.scoreboard;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;
import net.gamers.lobby.Main;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.cargos.CargosAPI;
import net.gamerspvp.commons.bukkit.cargos.group.GroupBukkit;
import net.gamerspvp.commons.network.database.Redis;
import redis.clients.jedis.Jedis;

public class ScoreBoard {
	
	private Main instance;
	private static ReentrantLock lock;
	private static Scoreboard scoreboard;
	@Getter
	private static HashSet<Team> teams;
	
	public ScoreBoard(Main instance) {
		this.instance = instance;
		ScoreBoard.lock = new ReentrantLock();
		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		teams = new HashSet<>();
		
		instance.runAsyncLoopTask(() -> updateScore());
		new ScoreBoardListener(instance);
	}
	
	public void updateScore() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		if (onlinePlayers.isEmpty()) {
			return;
		}
		Redis redis = instance.getDataCenter().getRedis();
		Jedis jedis = redis.getJedis();
		String onlineCount = jedis.get("network_online");
		if (onlineCount == null) {
			onlineCount = "0";
		}
		for (Player player : onlinePlayers) {
			try {
				Scoreboard sb = player.getScoreboard();
				Team cash = sb.getTeam("line3");
				Team online = sb.getTeam("line2");
				
				cash.setSuffix("0");
				online.setSuffix(onlineCount);
			} catch (Exception e) {}
		}
		redis.close(jedis);
	}
	
	public static void updateTeams() {
		if (lock.isLocked()) {
			return;
		}
		CommonsBukkit.getInstance().runAsync(() -> {
			lock.lock();
			for (Player online : Bukkit.getOnlinePlayers()) {
				Scoreboard scoreboard = online.getScoreboard();
				for (Team team : teams) {
					Team teamScore = scoreboard.getTeam(team.getName());
					if (teamScore == null) {
						teamScore = scoreboard.registerNewTeam(team.getName());
					}
					teamScore.setPrefix(team.getPrefix());
					teamScore.setNameTagVisibility(team.getNameTagVisibility());
					for (String entry : team.getEntries()) {
						if (teamScore.hasEntry(entry)) {
							continue;
						}
						teamScore.addEntry(entry);
					}
				}
				Team team = scoreboard.getTeam("NPCs");
				if (team == null) {
					team = scoreboard.registerNewTeam("NPCs");
				}
				if (!team.hasEntry("[NPC]")) {
					team.addEntry("[NPC]");
				}
				team.setPrefix("§8");
				team.setNameTagVisibility(NameTagVisibility.NEVER);
			}
			lock.unlock();
		});
	}
	
	public static void loadTeamPlayer(Player player) {
		GroupBukkit group = CargosAPI.getUserGroup(player);
		String groupName = null;
		String preffix = "§7";
		int rank = 999;
		if (group != null) {
			groupName = group.getName();
			preffix = group.getPreffix();
			rank = group.getRank();
		}
		if (groupName == null) {
			groupName = CargosAPI.getDefaultGroup().getName();
		}
		if (preffix.length() > 16) {
			preffix = preffix.substring(0, 16);
		}
		Team team = scoreboard.getTeam(rank + groupName);
		if (team == null) {
			team = scoreboard.registerNewTeam(rank + groupName);
		}
		if (!team.hasEntry(player.getName())) {
			team.addEntry(player.getName());
		}
		team.setPrefix(preffix);
		teams.add(team);
	}
	
	public static String centerText(String text, int lineLength) {
		StringBuilder builder = new StringBuilder(text);
        String space = " ";
        int distance = (lineLength - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
	}
}