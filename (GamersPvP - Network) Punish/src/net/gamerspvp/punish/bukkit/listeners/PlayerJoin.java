package net.gamerspvp.punish.bukkit.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.gamerspvp.punish.bukkit.Main;

public class PlayerJoin implements Listener {
	
	private Main instance;
	
	public PlayerJoin(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String address = player.getAddress().getHostString();
		String name = player.getName().toLowerCase();
		HashMap<String, String> cache = instance.getCachedAddress();
		if (cache.containsKey(name)) {
			if ((cache.get(name).equalsIgnoreCase(address)) == false) {
				cache.put(name, address);
			}
		} else {
			cache.put(name, address);
		}
	}
}