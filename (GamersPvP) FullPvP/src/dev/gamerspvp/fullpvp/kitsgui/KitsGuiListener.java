package dev.gamerspvp.fullpvp.kitsgui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.fullpvp.Main;

public class KitsGuiListener implements Listener {
	
	public KitsGuiListener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equalsIgnoreCase("§7Kits")) {
			event.setCancelled(true);
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem == null) {
				return;
			}
			if (currentItem.getType() == Material.AIR) {
				return;
			}
			Player player = (Player)event.getWhoClicked();
			String name = currentItem.getItemMeta().getDisplayName();
			String kit = ChatColor.stripColor(name.replace(" Mensal", "Mensal"));
			if (currentItem.getType() == Material.REDSTONE_BLOCK) {
				kit = "kit youtuberm";
			} else if (currentItem.getType() == Material.EMERALD_BLOCK) {
				kit = "kit mastermens";
			}
			player.chat("/system:" + kit);
			player.closeInventory();
		}
	}
}