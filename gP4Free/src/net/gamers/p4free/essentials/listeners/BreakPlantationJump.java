package net.gamers.p4free.essentials.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BreakPlantationJump implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getBlock().getType() == Material.SOIL)
			event.setCancelled(true);
	}
}