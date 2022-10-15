package net.gamers.p4free.essentials.listeners;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ExplodeItem implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Item)) {
			return;
		}
		event.setCancelled(true);
	}
}