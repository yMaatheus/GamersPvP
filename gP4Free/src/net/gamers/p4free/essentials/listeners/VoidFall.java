package net.gamers.p4free.essentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent;

public class VoidFall implements Listener {
	
	private String spawnWorld;
	
	public VoidFall(String spawnWorld) {
		this.spawnWorld = spawnWorld;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (event.getCause() != DamageCause.VOID) {
			return;
		}
		Player player = (Player) event.getEntity();
		if (player.getLocation().getBlockY() < 0) {
			event.setCancelled(true);
			player.setFallDistance(1.0F);
			if (spawnWorld == null) {
				return;
			}
			World world = Bukkit.getWorld(spawnWorld);
			if (world == null) {
				player.teleport(player.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
				return;
			}
			player.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
		}
	}
}