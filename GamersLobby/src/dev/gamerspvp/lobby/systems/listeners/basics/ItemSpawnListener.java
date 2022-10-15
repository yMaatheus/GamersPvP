package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import dev.gamerspvp.lobby.Main;

public class ItemSpawnListener implements Listener {

	public ItemSpawnListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		event.setCancelled(true);
		event.getEntity().remove();
	}
}