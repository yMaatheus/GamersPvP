package dev.gamerspvp.fullpvp.sword;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.fullpvp.Main;

public class SwordListener implements Listener {
	
	//private Main instance;
	
	public SwordListener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		//this.instance = instance;
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			ItemStack hand = damager.getItemInHand();
			if (hand.getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
				hand.removeEnchantment(Enchantment.KNOCKBACK);
				damager.sendMessage("§cO encantamento de Knockback não é permitido no servidor, o knockback foi retirado de sua espada!");
			}
		}
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			ItemStack hand = player.getItemInHand();
			if (hand.getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
				hand.removeEnchantment(Enchantment.KNOCKBACK);
				player.sendMessage("§cO encantamento de Knockback não é permitido no servidor, o knockback foi retirado de sua espada!");
			}
		}
	}
}