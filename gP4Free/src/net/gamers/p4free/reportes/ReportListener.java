package net.gamers.p4free.reportes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.gamers.p4free.Main;

public class ReportListener implements Listener {
	
	private Main instance;
	
	public ReportListener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equalsIgnoreCase("§7Reports")) {
			event.setCancelled(true);
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem == null) {
				return;
			}
			if (currentItem.getType() == Material.AIR) {
				return;
			}
			Player player = (Player) event.getWhoClicked();
			String targetName = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
			Player target = Bukkit.getPlayer(targetName);
			if (target == null) {
				return;
			}
			ReportManager reportManager = instance.getReportManager();
			if (event.getClick() == ClickType.LEFT) {
				player.teleport(target.getLocation());
			} else if (event.getClick() == ClickType.RIGHT) {
				if (!(player.hasPermission("gamers.admin"))) {
					return;
				}
				reportManager.removeReport(target);
			}
		}
	}
}