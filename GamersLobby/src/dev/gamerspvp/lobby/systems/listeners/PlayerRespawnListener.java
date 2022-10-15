package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.MakeItem;
import dev.gamerspvp.lobby.systems.Arrays;

public class PlayerRespawnListener implements Listener {

	public PlayerRespawnListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (Arrays.invincible.contains(player) == false) {
			Arrays.invincible.add(player);
		}
		player.getInventory().clear();
		player.setExp(0.0F);
		player.setLevel(0);
		player.setExhaustion(20.0F);
		player.setMaxHealth(20.0D);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		ItemStack compass = new MakeItem(Material.COMPASS).setName("§eServidores").build();
		ItemStack stick = new MakeItem(Material.STICK).setName("§cPAU DO PVP").build();

		player.getInventory().setItem(4, compass);
		player.getInventory().setItem(2, stick);
	}
}