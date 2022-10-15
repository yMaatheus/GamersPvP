package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.systems.Arrays;

public class EntityDamageListener implements Listener {
	
	public EntityDamageListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (Arrays.invincible.contains(player)) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getDamager() instanceof Player) {
				Player player = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();
				if ((Arrays.invincible.contains(player)) || ((Arrays.invincible.contains(damager)))) {
					event.setCancelled(true);
				}
			} else {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
		event.setCancelled(true);
	}
}