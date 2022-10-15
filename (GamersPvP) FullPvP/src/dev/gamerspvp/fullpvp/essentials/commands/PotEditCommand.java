package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import dev.gamerspvp.fullpvp.Main;

public class PotEditCommand extends Command {

	//private Main instance;

	public PotEditCommand(Main instance) {
		super("potedit");
		//this.instance = instance;
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (!sender.hasPermission("essentials.potedit")){
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		Player player = (Player) sender;
		Potion potion = new Potion(PotionType.STRENGTH);
		ItemStack potion3 = new ItemStack(potion.toItemStack(1));
		PotionMeta potionMeta = (PotionMeta) potion3.getItemMeta();
		potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20, 1), true);
		potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 1), true);
		potion3.setItemMeta(potionMeta);
		potion3.setAmount(64);
		player.getInventory().addItem(potion3);
		sender.sendMessage("§aPot editada adicionada ao seu invent§rio com sucesso.");
		return false;
	}
}