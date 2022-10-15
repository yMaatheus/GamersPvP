package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChange implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onWeatherChangeEvent(WeatherChangeEvent event) {
		if (event.toWeatherState())
			event.setCancelled(true);
	}
}