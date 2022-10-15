package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class InvseeCommand extends Command {
	
	public InvseeCommand(Main instance) {
		super("invsee");
		Utils.registerCommand(this, instance, "invsee", "inv");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (!(sender.hasPermission("gamers.moderador"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		Player player = (Player) sender;
		if (args.length > 0) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			if (player.getName() == target.getName()) {
				player.sendMessage("§cVoc§ n§o pode ver seu pr§prio invent§rio.");
				return false;
			}
			player.openInventory(target.getInventory());
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[P4Free] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a (nick)");
		return false;
	}
}