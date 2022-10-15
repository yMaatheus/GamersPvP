package net.gamers.p4free.essentials.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class DerreterIceAndSnow implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockFadeEvent(BlockFadeEvent event) {
		if (event.getBlock().getType() == Material.ICE || event.getBlock().getType() == Material.SNOW)
			event.setCancelled(true);
	}
}