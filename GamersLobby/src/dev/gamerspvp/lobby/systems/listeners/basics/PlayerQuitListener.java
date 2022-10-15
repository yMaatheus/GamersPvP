package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.gamerspvp.lobby.Main;

public class PlayerQuitListener implements Listener {

	public PlayerQuitListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
}