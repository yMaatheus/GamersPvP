package net.gamerspvp.lobby;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamerspvp.commons.bukkit.utils.MakeItem;

public class GlobalListeners implements Listener {
	
	public GlobalListeners(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		new BukkitRunnable() {
			
			public void run() {
				for (World world : Bukkit.getWorlds()) {
					world.setGameRuleValue("doDaylightCycle", "false");
					world.setTime(6000L);
				}
			}
		}.runTaskLater(instance, 600L);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		player.teleport(player.getLocation().getWorld().getSpawnLocation());
		player.setAllowFlight(false);
		player.setExp(0.0F);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.setGameMode(GameMode.ADVENTURE);
		MakeItem item = new MakeItem(Material.COMPASS).setName("§aServidores");
		player.getInventory().setItem(4, item.build());
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL && event.getMaterial() == Material.SOIL) {
			event.setCancelled(true);
			return;
		}
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		event.setCancelled(true);
 	}
	
	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onLeavesDecayEvent(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		event.setCancelled(true);
		Player player = (Player) event.getEntity();
		if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
			event.setCancelled(true);
			player.teleport(player.getWorld().getSpawnLocation());
		}
	}

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		event.getEntity().remove();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onWeatherChangeEvent(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
		if (spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER) {
			return;
		}
		if ((spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL)
				|| (spawnReason == CreatureSpawnEvent.SpawnReason.CHUNK_GEN)
				|| (spawnReason == CreatureSpawnEvent.SpawnReason.JOCKEY)
				|| (spawnReason == CreatureSpawnEvent.SpawnReason.MOUNT)) {
			event.setCancelled(true);
		}
	}
}