package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class TphereCommand extends Command {
	
	public TphereCommand(Main instance) {
		super("tphere");
		Utils.registerCommand(this, instance, "tphere");
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
		if (args.length > 0) {
			Player player = (Player) sender;
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			if (target.getName().equals(sender.getName())) {
				sender.sendMessage("§cVocê não pode teleporta-se até você mesmo.");
				return false;
			}
			target.teleport(player, TeleportCause.COMMAND);
			sender.sendMessage("§a " + target.getName() + " foi teleportado até você com sucesso.");
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[P4Free] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a (nick)");
		return false;
	}
}