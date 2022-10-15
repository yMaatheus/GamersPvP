package net.gamers.p4free.essentials.listeners;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import net.gamerspvp.commons.network.utils.ListEnum;

public class InventoryClickShift implements Listener {
	
	private HashSet<InventoryType> containers;
	
	public InventoryClickShift(List<String> containers) {
		this.containers = new HashSet<InventoryType>(ListEnum.listToListEnum(InventoryType.class, containers));
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) {
			return;
		}
		if (event.getCurrentItem().getType() == Material.AIR) {
			return;
		}
		if (!(event.getClick().isShiftClick())) {
			return;
		}
		if (event.getWhoClicked().isOp()) {
			return;
		}
		if (!(containers.contains(event.getClickedInventory().getType()))) {
			return;
		}
		event.setCancelled(true);
	}
}