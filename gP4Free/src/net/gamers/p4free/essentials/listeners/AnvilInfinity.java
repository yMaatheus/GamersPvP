package net.gamers.p4free.essentials.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.gamerspvp.commons.bukkit.utils.AnvilAPI;

public class AnvilInfinity implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ANVIL) {
			Player player = event.getPlayer();
			if (player.isSneaking()) {
				return;
			}
			AnvilAPI.openAnvil(player);
			event.setCancelled(true);
		}
	}
}