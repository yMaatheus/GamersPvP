package net.gamers.p4free.kit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

import net.gamers.gladiador.minigladiador.MiniGladiadorManager;
import net.gamers.p4free.Main;

public class KitListener implements Listener {
	
	private Main instance;
	
	public KitListener(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public synchronized void onEntityDamageEvent1(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		KitManager kitManager = instance.getKitManager();
		if (!(event.getCause().toString().contains("FALL"))) {
			return;
		}
		if (kitManager.hasCachePlayers(player)) {
			return;
		}
		if (!(kitManager.hasInSpawn(player.getLocation()))) {
			return;
		}
		PlayerInventory playerInventory = player.getInventory();
		playerInventory.clear();
		playerInventory.setHelmet(null);
		playerInventory.setChestplate(null);
		playerInventory.setLeggings(null);
		playerInventory.setBoots(null);
		MiniGladiadorManager miniGladiadorManager = net.gamers.gladiador.Main.getInstance().getMiniGladiadorManager();
		miniGladiadorManager.setPlayerKit(player);
		kitManager.addCachePlayer(player);
	}
	
	@EventHandler
	public synchronized void onPlayerDeath(PlayerDeathEvent event) {
		KitManager kitManager = instance.getKitManager();
		Player player = event.getEntity();
		if (!(kitManager.hasCachePlayers(player))) {
			return;
		}
		kitManager.removeCachePlayer(player);
	}
	
	@EventHandler
	public synchronized void onPlayerQuitEvent(PlayerQuitEvent event) {
		KitManager kitManager = instance.getKitManager();
		Player player = event.getPlayer();
		if (!(kitManager.hasCachePlayers(player))) {
			return;
		}
		kitManager.removeCachePlayer(player);
	}
}