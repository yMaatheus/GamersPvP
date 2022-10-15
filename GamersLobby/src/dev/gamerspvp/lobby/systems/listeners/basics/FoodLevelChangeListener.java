package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import dev.gamerspvp.lobby.Main;

public class FoodLevelChangeListener implements Listener {
	
	public FoodLevelChangeListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void FoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}
}