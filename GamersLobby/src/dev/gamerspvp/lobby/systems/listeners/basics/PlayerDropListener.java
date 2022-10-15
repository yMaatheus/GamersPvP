package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import dev.gamerspvp.lobby.Main;

public class PlayerDropListener implements Listener {

	public PlayerDropListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
}