package net.gamers.p4free.essentials.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerUseNameTag implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (player.isOp()) {
			return;
		}
		if (player.getItemInHand().getType() != Material.NAME_TAG) {
			return;
		}
		event.setCancelled(true);
	}
}