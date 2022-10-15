package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import dev.gamerspvp.lobby.Main;

public class PlayerCommandPreProcessListener implements Listener {
	
	public PlayerCommandPreProcessListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
		String cmd = event.getMessage().toLowerCase();
		if (cmd.contains("/tell")) {
			event.setCancelled(true);
		} else if (cmd.contains("/r ")) {
			event.setCancelled(true);
		} else if (cmd.contains("/me")) {
			event.setCancelled(true);
		} else if (cmd.contains("/pl")) {
			event.setCancelled(true);
		} else if (cmd.contains("/bukkit:")) {
			event.setCancelled(true);
		} else if (cmd.contains("/minecraft:")) {
			event.setCancelled(true);
		}
	}
}