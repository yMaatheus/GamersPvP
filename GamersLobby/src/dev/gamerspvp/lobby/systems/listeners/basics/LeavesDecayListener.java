package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

import dev.gamerspvp.lobby.Main;

public class LeavesDecayListener implements Listener {
	
	public LeavesDecayListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void LeavesDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}
}
