package dev.gamerspvp.lobby.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.BungeeAPI;
import dev.gamerspvp.lobby.api.MakeItem;

public class MenuServersInventory implements Listener {
	
	public MenuServersInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, 3 * 9, "§7Servers: ");
		
		inv.setItem(13, new MakeItem(Material.DIAMOND_SWORD).setName("§aFullPvP").addLore("", "§aOnline: §f" + BungeeAPI.getPlayers("fullpvp")).addFlags(ItemFlag.HIDE_ATTRIBUTES).build());
		
		player.openInventory(inv);
	}
	
	public MenuServersInventory() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String title = event.getInventory().getTitle();
		if (title.equalsIgnoreCase("§7Servers: ")) {
			event.setCancelled(true);
			if (event.getCurrentItem() == null) {
				return;
			}
			if (event.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aFullPvP")) {
				BungeeAPI.teleportToServer(player, "fullpvp");
				return;
			}
		}
	}
}