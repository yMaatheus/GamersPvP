package dev.gamerspvp.automatictasks.shutdown;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import dev.gamerspvp.automatictasks.Main;

public class ShutdownListener implements Listener {
	
	private Main instance;
	
	public ShutdownListener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		ShutdownManager shutdownManager = instance.getShutdownManager();
		if (!(shutdownManager.isClosed())) {
			return;
		}
		event.setKickMessage("§cServidor está reiniciando, Aguarde iniciar para jogar!");
		event.setLoginResult(Result.KICK_OTHER);
	}
}