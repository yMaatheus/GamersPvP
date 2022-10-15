package net.gamers.p4free.essentials.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerBypassBorda implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			return;
		}
		Player player = event.getPlayer();
		World world = player.getWorld();
		if (world.getWorldBorder() == null) {
			return;
		}
		double worldborder = world.getWorldBorder().getSize() / 2.0D;
		Location center = world.getWorldBorder().getCenter();
		Location to = event.getTo();
		if (center.getX() + worldborder < to.getX()) {
			player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL, 1) });
			event.setCancelled(true);
		} else if (center.getX() - worldborder > to.getX()) {
			player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL, 1) });
			event.setCancelled(true);
		} else if (center.getZ() + worldborder < to.getZ()) {
			player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL, 1) });
			event.setCancelled(true);
		} else if (center.getZ() - worldborder > to.getZ()) {
			player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL, 1) });
			event.setCancelled(true);
		}
	}
}