package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.gamerspvp.lobby.Main;

public class AsyncPlayerChatListener implements Listener {
	
	public AsyncPlayerChatListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
	}
}