package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class ClearCommand extends Command {
	
	public ClearCommand(Main instance) {
		super("clear");
		Utils.registerCommand(this, instance, "clear");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (args.length > 0) {
			if (!(sender.isOp())) {
				sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
				return false;
			}
			Player player = (Player) sender;
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			target.getInventory().clear();
			target.getInventory().setHelmet(null);
			target.getInventory().setChestplate(null);
			target.getInventory().setLeggings(null);
			target.getInventory().setBoots(null);
			player.sendMessage("§aInventário de " + target.getName() + " limpo com sucesso.");
			return true;
		}
		if (!(sender.hasPermission("gamers.moderador"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		Player player = (Player) sender;
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.sendMessage("§aInventário limpo com sucesso.");
		return true;
	}
}