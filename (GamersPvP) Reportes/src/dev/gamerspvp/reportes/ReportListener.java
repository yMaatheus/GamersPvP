package dev.gamerspvp.reportes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.reportes.models.Report;

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
			ReportManager reportManager = instance.getReportManager();
			if (event.getClick() == ClickType.LEFT) {
				Report report = reportManager.getCache(targetName.toLowerCase());
				Player target = report.getPlayer();
				if (target == null) {
					return;
				}
				player.teleport(target.getLocation());
			} else if (event.getClick() == ClickType.RIGHT) {
				if (!(player.hasPermission("reportes.admin"))) {
					return;
				}
				reportManager.removeReport(targetName);
			}
		}
	}
}