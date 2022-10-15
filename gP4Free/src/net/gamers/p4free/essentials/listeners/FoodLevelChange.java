package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
		event.setCancelled(true);
	}
}