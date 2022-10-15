package net.gamers.p4free.essentials.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class FlowWaterFire implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockFromToEvent(BlockFromToEvent event) {
		Material type = event.getBlock().getType();
		if (type != Material.WATER && type != Material.STATIONARY_WATER && type != Material.LAVA
				&& type != Material.STATIONARY_LAVA)
			return;
		Location partidaLoc = event.getBlock().getLocation();
		Location finalLoc = event.getToBlock().getLocation();
		if (partidaLoc.getY() != finalLoc.getY())
			event.setCancelled(true);
	}
}