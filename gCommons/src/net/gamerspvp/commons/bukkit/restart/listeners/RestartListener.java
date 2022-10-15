package net.gamerspvp.commons.bukkit.restart.listeners;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.listeners.custom.BukkitMessageEvent;
import net.gamerspvp.commons.bukkit.restart.RestartManager;

public class RestartListener implements Listener {
	
	private CommonsBukkit instance;
	
	public RestartListener(CommonsBukkit instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onBukkitMessageEvent(BukkitMessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("backup_restartbukkit")) {
			//Habilitar enviar mensagem após o servidor reiniciar
			FileConfiguration config = instance.getConfig();
			try {
				config.set("startBackup", true);
				config.save(new File(instance.getDataFolder(), "settings.yml"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			RestartManager.executeRestart();
		}
	}
	
	@EventHandler
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		if (!(RestartManager.isClosed())) {
			return;
		}
		event.setKickMessage("§cServidor está reiniciando, Aguarde iniciar para jogar!");
		event.setLoginResult(Result.KICK_OTHER);
	}
}