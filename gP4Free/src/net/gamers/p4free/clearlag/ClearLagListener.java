package net.gamers.p4free.clearlag;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.gamers.p4free.Main;

public class ClearLagListener implements Listener {
	
	private Main instance;
	
	public ClearLagListener(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}

	@EventHandler
	public synchronized void ItemSpawnEvent(ItemSpawnEvent event) {
		Item item = event.getEntity();
		if (item == null) {
			return;
		}
		ItemStack itemStack = item.getItemStack();
		if (itemStack == null) {
			return;
		}
		if (itemStack.getType() == Material.AIR) {
			return;
		}
		ClearLagManager clearLagManager = instance.getClearLagManager();
		clearLagManager.addDrops(itemStack.getAmount());
	}

	@EventHandler
	public synchronized void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		Item item = event.getItem();
		if (item == null) {
			return;
		}
		ItemStack itemStack = item.getItemStack();
		if (itemStack == null) {
			return;
		}
		if (itemStack.getType() == Material.AIR) {
			return;
		}
		ClearLagManager clearLagManager = instance.getClearLagManager();
		if (clearLagManager.getDrop() <= 0) {
			return;
		}
		clearLagManager.removeDrops(itemStack.getAmount());
	}
}