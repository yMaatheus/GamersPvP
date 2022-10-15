package dev.gamerspvp.fullpvp.kitsgui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.utils.MakeItem;

public class KitsGuiManager {
	
	private Inventory inventory;
	
	public KitsGuiManager(Main instance) {
		inventory = Bukkit.createInventory(null, 4 * 9, "§7Kits");
		inventory.setItem(10, new MakeItem(Material.IRON_INGOT).setName("§f§lKit PvP").build());
		inventory.setItem(13, new MakeItem(Material.REDSTONE).setName("§c§lKit Youtuber").build());
		inventory.setItem(14, new MakeItem(Material.GOLD_INGOT).setName("§6§lKit PRO").build());
		inventory.setItem(15, new MakeItem(Material.DIAMOND).setName("§b§lKit ZEUS").build());
		inventory.setItem(16, new MakeItem(Material.EMERALD).setName("§a§lKit MASTER").build());
		
		inventory.setItem(19, new MakeItem(Material.IRON_BLOCK).setName("§f§lKit RESET").build());
		inventory.setItem(22, new MakeItem(Material.REDSTONE_BLOCK).setName("§c§lKit Youtuber Mensal").build());
		inventory.setItem(23, new MakeItem(Material.GOLD_BLOCK).setName("§6§lKit PRO Mensal").build());
		inventory.setItem(24, new MakeItem(Material.DIAMOND_BLOCK).setName("§b§lKit ZEUS Mensal").build());
		inventory.setItem(25, new MakeItem(Material.EMERALD_BLOCK).setName("§a§lKit MASTER Mensal").build());
		new KitCommand(instance);
		new KitsGuiListener(instance);
	}
	
	public void openInventory(Player player) {
		player.openInventory(inventory);
	}
}