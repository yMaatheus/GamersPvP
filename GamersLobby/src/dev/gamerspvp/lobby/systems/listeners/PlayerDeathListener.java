package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.MakeItem;

public class PlayerDeathListener implements Listener {
	
	public PlayerDeathListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		if (event.getEntity() instanceof Player) {
			if (event.getEntity().getKiller() instanceof Player) {
				Player player = event.getEntity();
				Player killer = event.getEntity().getKiller();
				event.setDeathMessage("§7" + player.getName() + " morreu para " + killer.getName());
				killer.getInventory().addItem(new MakeItem(Material.GOLDEN_APPLE).setId((short) 1).setQuantia(1).build());
				killer.getInventory().setItem(0, new MakeItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).addEnchant(Enchantment.FIRE_ASPECT, 2).addEnchant(Enchantment.DURABILITY, 3).build());
				killer.getInventory().setHelmet(new MakeItem(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
				killer.getInventory().setChestplate(new MakeItem(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
				killer.getInventory().setLeggings(new MakeItem(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
				killer.getInventory().setBoots(new MakeItem(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).build());
				killer.updateInventory();
			}
		}
	}
}