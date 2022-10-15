package dev.gamerspvp.gladiador.sumox1.models;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.sumox1.SumoX1Manager;

public class SumoX1 {
	
	private HashMap<Player, Participant> participantes;
	private statusType status;
	private BukkitTask task;
	private int alerts;
	private HashSet<Player> dueling;
	
	public SumoX1() {
		this.participantes = new HashMap<Player, Participant>();
		this.status = statusType.CHAMANDO;
		this.task = null;
		this.alerts = 0;
		this.dueling = new HashSet<Player>();
	}
	
	public void teleportAll(String location, String message, int delay, Main instance) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				SumoX1Manager sumoX1Manager = instance.getSumoX1Manager();
				participantes.keySet().forEach(player -> {
					player.teleport(sumoX1Manager.getLocation(location));
					player.sendMessage(message);
				});
			}
		}.runTaskLater(instance, delay);
	}
	
	public void reset() {
		if (task != null) {
			task.cancel();
		}
		task = null;
		alerts = 0;
	}
	
	public HashMap<Player, Participant> getParticipantes() {
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
	
	public HashSet<Player> getDueling() {
		return dueling;
	}
	
	public class Participant {
		
		private String playerName;
		private boolean dueling;
		
		public Participant(String playerName) {
			this.playerName = playerName;
			this.dueling = false;
		}
		
		public String getPlayerName() {
			return playerName;
		}
		
		public boolean isDueling() {
			return dueling;
		}
		
		public void setDueling(boolean dueling) {
			this.dueling = dueling;
		}
	}
	
	public enum statusType {
		CHAMANDO, FECHADO, CANCELADO;
	}
}