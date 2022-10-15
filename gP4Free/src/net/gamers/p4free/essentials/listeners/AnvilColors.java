package net.gamers.p4free.essentials.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilColors implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if (inventory.getType() != InventoryType.ANVIL) {
			return;
		}
		if (!(event.getWhoClicked().hasPermission("essentials.vip"))) {
			return;
		}
		if (event.getSlotType() != InventoryType.SlotType.RESULT) {
			return;
		}
		ItemStack item = event.getCurrentItem();
		if (item == null) {
			return;
		}
		if (item.getType() == Material.AIR) {
			return;
		}
		if (!(item.getItemMeta().hasDisplayName())) {
			return;
		}
		ItemMeta meta = item.getItemMeta();
		String name = meta.getDisplayName();
		ItemStack oldItem = inventory.getItem(0);
		if (oldItem != null) {
			ItemMeta oldMeta = oldItem.getItemMeta();
			if (oldMeta.hasDisplayName()) {
				String oldName = oldMeta.getDisplayName().replace("", "");
				if (name.equals(oldName)) {
					meta.setDisplayName(oldMeta.getDisplayName());
					item.setItemMeta(meta);
					event.setCurrentItem(item);
					return;
				}
			}
		}
		meta.setDisplayName(name.replace("&", "§"));
		item.setItemMeta(meta);
		event.setCurrentItem(item);
	}
}