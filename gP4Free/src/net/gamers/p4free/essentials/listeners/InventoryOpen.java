package net.gamers.p4free.essentials.listeners;

import java.util.HashSet;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import net.gamerspvp.commons.network.utils.ListEnum;

public class InventoryOpen implements Listener {
	
	private HashSet<InventoryType> containers;
	
	public InventoryOpen(List<String> containers) {
		this.containers = new HashSet<InventoryType>(ListEnum.listToListEnum(InventoryType.class, containers));
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if (event.getPlayer().isOp()) {
			return;
		}
		if (!(containers.contains(event.getInventory().getType()))) {
			return;
		}
		event.setCancelled(true);
	}
}