package dev.gamerspvp.auth;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.auth.AuthBukkitManager;
import dev.gamerspvp.auth.auth.models.AuthPlayer;
import dev.gamerspvp.auth.captcha.CaptchaManager;
import dev.gamerspvp.auth.captcha.models.CaptchaPlayer;

public class GlobalListeners implements Listener {
	
	private Main instance;
	
	public GlobalListeners(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
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
		player.getInventory().clear();
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		AuthBukkitManager authManager = instance.getAuthManager();
		AuthPlayer authPlayer = authManager.getCache(playerName);
		if (captchaPlayer != null) {
			if (!(captchaPlayer.isConcluded())) {
				event.setCancelled(true);
				return;
			}
		}
		if (authPlayer != null) {
			if (!(authPlayer.isAuthenticated())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		AuthBukkitManager authManager = instance.getAuthManager();
		AuthPlayer authPlayer = authManager.getCache(playerName);
		if (captchaPlayer != null) {
			if (!(captchaPlayer.isConcluded())) {
				event.setCancelled(true);
				return;
			}
		}
		if (authPlayer != null) {
			if (!(authPlayer.isAuthenticated())) {
				event.setCancelled(true);
				return;
			}
		}
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
		if ((spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) || (spawnReason == CreatureSpawnEvent.SpawnReason.CHUNK_GEN)
				|| (spawnReason == CreatureSpawnEvent.SpawnReason.JOCKEY)
				|| (spawnReason == CreatureSpawnEvent.SpawnReason.MOUNT)) {
			event.setCancelled(true);
		}
	}
}