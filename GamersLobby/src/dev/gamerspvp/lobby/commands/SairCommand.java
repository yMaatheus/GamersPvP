package dev.gamerspvp.lobby.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.lobby.api.MakeItem;
import dev.gamerspvp.lobby.systems.Arrays;

public class SairCommand extends Command {

	public SairCommand() {
		super("sair");
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (sender instanceof Player == false) {
			return true;
		}
		Player player = (Player) sender;
		if (Arrays.invincible.contains(player) == false) {
			Arrays.invincible.add(player);
		}
		player.teleport(player.getWorld().getSpawnLocation());
		player.getInventory().clear();
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
		player.getActivePotionEffects().clear();

		ItemStack compass = new MakeItem(Material.COMPASS).setName("§eServidores").build();
		ItemStack stick = new MakeItem(Material.STICK).setName("§cPAU DO PVP").build();

		player.getInventory().setItem(4, compass);
		player.getInventory().setItem(2, stick);
		return false;
	}
}