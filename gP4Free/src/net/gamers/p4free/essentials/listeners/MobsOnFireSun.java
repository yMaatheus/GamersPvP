package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class MobsOnFireSun implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityCombustEvent(EntityCombustEvent event) {
		if (!(event instanceof EntityCombustByEntityEvent) && !(event instanceof EntityCombustByBlockEvent))
			event.setCancelled(true);
	}
}