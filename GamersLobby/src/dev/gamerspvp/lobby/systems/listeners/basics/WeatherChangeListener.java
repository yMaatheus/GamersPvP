package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import dev.gamerspvp.lobby.Main;

public class WeatherChangeListener implements Listener {
	
	public WeatherChangeListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void WeatherChangeEvent(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
}
