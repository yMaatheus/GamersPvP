package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EssentialsListeners implements Listener {
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
}