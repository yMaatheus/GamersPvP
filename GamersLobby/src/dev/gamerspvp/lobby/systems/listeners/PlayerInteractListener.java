package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.LocAPI;
import dev.gamerspvp.lobby.api.MakeItem;
import dev.gamerspvp.lobby.inventory.MenuServersInventory;
import dev.gamerspvp.lobby.systems.Arrays;

public class PlayerInteractListener implements Listener {
	
	public PlayerInteractListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.COMPASS) {
			new MenuServersInventory(player);
		}
		if (player.getItemInHand().getType() == Material.STICK) {
			LocAPI.ir(player, "pvp");
			player.sendMessage("");
			player.sendMessage("§aPara sair utilize: /sair.");
			player.sendMessage("");
			if (Arrays.invincible.contains(player)) {
				Arrays.invincible.remove(player);
			}
			player.getInventory().clear();
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999999, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
			player.getInventory().setItem(0, new MakeItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).addEnchant(Enchantment.FIRE_ASPECT, 2).addEnchant(Enchantment.DURABILITY, 3).build());
			player.getInventory().setItem(1, new MakeItem(Material.GOLDEN_APPLE).setId((short) 1).setQuantia(5).build());
			player.getInventory().setHelmet(new MakeItem(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
			player.getInventory().setChestplate(new MakeItem(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
			player.getInventory().setLeggings(new MakeItem(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
			player.getInventory().setBoots(new MakeItem(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
		}
	}
}