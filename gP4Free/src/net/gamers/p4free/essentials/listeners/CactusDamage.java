package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CactusDamage implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
		if (event.getCause() == DamageCause.CONTACT)
			event.setCancelled(true);
	}
}