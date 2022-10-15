package dev.gamerspvp.gladiador.killer;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.minigladiador.MiniGladiadorManager;

public class Killer {
	
	private HashMap<Player, String> participantes;
	private statusType status;
	private BukkitTask task;
	private int alerts;
	
	public Killer() {
		this.participantes = new HashMap<Player, String>();
		this.status = statusType.CHAMANDO;
		this.task = null;
		this.alerts = 0;
	}
	
	public void teleportAll(String location, String message, int delay, Main instance) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
				participantes.keySet().forEach(player -> {
					player.teleport(miniGladiadorManager.getLocation(location));
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
	
	public HashMap<Player, String> getParticipantes() {
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
	
	public enum statusType {
		CHAMANDO, FECHADO, PVP, CANCELADO;
	}
}