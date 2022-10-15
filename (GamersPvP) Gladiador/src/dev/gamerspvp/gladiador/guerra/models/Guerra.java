package dev.gamerspvp.gladiador.guerra.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.guerra.GuerraManager;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class Guerra {
	
	private Set<Clan> clans;
	private HashMap<String, Integer> pKills;
	private HashMap<Player, ClanPlayer> participantes;
	private statusType status;
	private BukkitTask task;
	private int alerts;
	
	public Guerra() {
		this.clans = new HashSet<Clan>();
		this.pKills = new HashMap<String, Integer>();
		this.participantes = new HashMap<Player, ClanPlayer>();
		this.status = statusType.CHAMANDO;
		this.task = null;
		this.alerts = 0;
	}
	
	public void teleportAll(String location, String message, int delay, Main instance) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				GuerraManager guerraManager = instance.getGuerraManager();
				participantes.keySet().forEach(player -> {
					player.teleport(guerraManager.getLocation(location));
					player.sendMessage(message);
				});
			}
		}.runTaskLater(instance, delay);
	}
	
	public int getParticipantsClan(Clan clan) {
		int i = 0;
		if (participantes.isEmpty()) {
			return i;
		}
		for (ClanPlayer participantes : participantes.values()) {
			if (participantes.getClan() == clan) {
				i++;
			}
		}
		return i;
	}
	
	public void removeClan(Clan clan, Main instance) {
		boolean find = false;
		for (ClanPlayer member : clan.getMembers()) {
			if (participantes.containsValue(member)) {
				find = true;
			}
		}
		if (!(find)) {
			GuerraManager guerraManager = instance.getGuerraManager();
			clans.remove(clan);
			if (!(status == statusType.CHAMANDO)) {
				Bukkit.broadcastMessage(guerraManager.prefix + "§fClans restantes: §3" + guerraManager.getClans(this));
				guerraManager.executeCheck();
			}
		}
	}
	
	public void reset() {
		if (task != null) {
			task.cancel();
		}
		task = null;
		alerts = 0;
	}
	
	public Kills kills(String name, Player player, Integer kills) {
		return new Kills(name, player, kills);
	}
	
	public List<Kills> top(int size, HashSet<Kills> topAll) {
		List<Kills> convert = new ArrayList<>();
		convert.addAll(topAll);
		Collections.sort(convert, new Comparator<Kills>() {
			
			@Override
			public int compare(Kills pt1, Kills pt2) {
				Integer f1 = pt1.getKills();
				Integer f2 = pt2.getKills();
				return f2.compareTo(f1);
			}
		});
		if (convert.size() > size) {
			convert = convert.subList(0, size);
		}
		return convert;
	}
	
	public Set<Clan> getClans() {
		return clans;
	}
	
	public HashMap<String, Integer> getpKills() {
		return pKills;
	}
	
	public HashMap<Player, ClanPlayer> getParticipantes() {
		return participantes;
	}
	
	public statusType getStatus() {
		return status;
	}

	public void setStatus(statusType status) {
		this.status = status;
	}
	
	public BukkitTask getTask() {
		return task;
	}
	
	public void setTask(BukkitTask task) {
		this.task = task;
	}
	
	public int getAlerts() {
		return alerts;
	}
	
	public void setAlerts(int alerts) {
		this.alerts = alerts;
	}
	
	public class Kills {
		
		private String name;
		private Player player;
		private Integer kills;
		
		public Kills(String name, Player player, Integer kills) {
			this.name = name;
			this.player = player;
			this.kills = kills;
		}
		
		public String getName() {
			return name;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public Integer getKills() {
			return kills;
		}
		
		public void setKills(Integer kills) {
			this.kills = kills;
		}
	}
	
	public enum statusType {
		CHAMANDO, FECHADO, PVP, CANCELADO;
	}
}