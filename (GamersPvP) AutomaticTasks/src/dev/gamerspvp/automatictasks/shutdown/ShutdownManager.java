package dev.gamerspvp.automatictasks.shutdown;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.automatictasks.Main;
import dev.gamerspvp.automatictasks.shutdown.commands.ReiniciarCommand;
import dev.gamerspvp.automatictasks.shutdown.commands.RestartCommand;
import dev.gamerspvp.automatictasks.utils.Titles;

public class ShutdownManager {
	
	private Main instance;
	private boolean closed;
	
	public ShutdownManager(Main instance) {
		this.instance = instance;
		this.closed = false;
		new ReiniciarCommand(instance);
		new RestartCommand(instance);
		new ShutdownListener(instance);
	}
	
	public void executeRestart() {
		this.closed = true;
		Bukkit.getPluginManager().callEvent(new PreServerShutdownEvent());
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§c[AVISO] O Servidor ir§ reiniciar em §f§l60 §csegundos!");
		Bukkit.broadcastMessage("");
		Titles title = new Titles();
		title.setTitle("§c§lSERVER REINICIANDO");
		title.setSubtitle("§cReiniciando em §f60 §csegundos!");
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.playSound(online.getLocation(), Sound.ORB_PICKUP, 10.0F, 1.0F);
			title.send(online);
		}
		new BukkitRunnable() {
			public void run() {
				Bukkit.shutdown();
			}
		}.runTaskLater(instance, 1200L);
	}
	
	public void executeFastRestart() {
		this.closed = true;
		Bukkit.getPluginManager().callEvent(new PreServerShutdownEvent());
		Bukkit.broadcastMessage("§cServidor reiniciando..");
		new BukkitRunnable() {
			public void run() {
				Bukkit.shutdown();
			}
		}.runTaskLater(instance, 40L);
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}