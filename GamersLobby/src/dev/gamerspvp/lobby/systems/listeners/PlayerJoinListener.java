package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.MakeItem;
import dev.gamerspvp.lobby.api.Tablist;
import dev.gamerspvp.lobby.systems.Arrays;
import dev.gamerspvp.lobby.systems.loads.ScoreBoard;

public class PlayerJoinListener implements Listener {

	public PlayerJoinListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		Tablist.setForPlayer(event.getPlayer(), "\n§2§lGAMERSPVP\n§f    mc.gamerspvp.net\n", "\n§2Discord: §fdiscord.gg/GAdvjxv\n\n§2Adquira pontos em nossa loja, acesse: §fgamerspvp.net");
		if (Arrays.invincible.contains(player) == false) {
			Arrays.invincible.add(player);
		}
		player.teleport(player.getWorld().getSpawnLocation());
		player.getInventory().clear();
		new ScoreBoard().build(player);
		player.setExp(0.0F);
		player.setLevel(0);
		player.setExhaustion(20.0F);
		player.setMaxHealth(20.0D);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));

		ItemStack compass = new MakeItem(Material.COMPASS).setName("§eServidores").build();
		ItemStack stick = new MakeItem(Material.STICK).setName("§cPAU DO PVP").build();

		player.getInventory().setItem(4, compass);
		player.getInventory().setItem(2, stick);
		player.updateInventory();
	}
}