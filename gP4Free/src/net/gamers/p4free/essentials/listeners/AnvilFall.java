package net.gamers.p4free.essentials.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class AnvilFall implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.FALLING_BLOCK && event.getTo() == Material.AIR && event.getBlock().getType() == Material.ANVIL) {
			event.setCancelled(true);
			event.getBlock().getState().update(false, false);
		}
	}
}