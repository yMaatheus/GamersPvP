package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

public class PropagacaoFogo implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBurnEvent(BlockBurnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockIgniteEvent(BlockIgniteEvent event) {
		if (event.getCause() == IgniteCause.LAVA || event.getCause() == IgniteCause.SPREAD)
			event.setCancelled(true);
	}
}