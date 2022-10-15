package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortal implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public static void onPlayerPortalEvent(PlayerPortalEvent event) {
		if (event.getPlayer().isOp()) {
			return;
		}
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
			event.setCancelled(true);
			return;
		}
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
			event.setCancelled(true);
			return;
		}
	}
}