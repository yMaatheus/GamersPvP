package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class TPCommand extends Command {

	public TPCommand(Main instance) {
		super("tp");
		Utils.registerCommand(this, instance, "tp");
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamers.moderador"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			if (target.getName().equals(sender.getName())) {
				sender.sendMessage("§cVoc§ n§o pode teleporta-se at§ voc§ mesmo.");
				return false;
			}
			Player player = (Player) sender;
			player.teleport(target, TeleportCause.COMMAND);
			player.sendMessage("§aTeleportado at§ " + target.getName() + " com sucesso.");
			return true;
		}
		if (args.length == 2) {
			if (sender.getName().equals(args[0]) && sender.getName().equals(args[1])) {
				sender.sendMessage("§cVoc§ n§o pode teleporta-se at§ voc§ mesmo.");
				return false;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			if (args[0].equals(args[1])) {
				sender.sendMessage("§cOs jogadores informados s§o os mesmos.");
				return false;
			}
			player.teleport(target, TeleportCause.COMMAND);
			sender.sendMessage("§a" + player.getName() + " teleportado at§ " + target.getName() + " com sucesso.");
			return true;
		}
		if (args.length == 3) {
			if (!(sender instanceof Player)) {
				return false;
			}
			double x, y, z;
			try {
				x = Double.parseDouble(args[0]);
				y = Double.parseDouble(args[1]);
				z = Double.parseDouble(args[2]);
			} catch (NumberFormatException e) {
				sender.sendMessage("§cCoordenadas inv§lidas.");
				return false;
			}
			Player player = (Player) sender;
			Location location = new Location(player.getWorld(), x, y, z);
			player.teleport(location, TeleportCause.COMMAND);
			player.sendMessage("§aTeleportado at§ as coordenadas X:" + args[0] + " Y: " + args[1] + " Z: " + args[2] + " com sucesso.");
			return true;
		}
		if (args.length == 4) {
			if (!(sender instanceof Player)) {
				return false;
			}
			World world = Bukkit.getWorld(args[0]);
			if (world == null) {
				sender.sendMessage("§cMundo inv§lido.");
				return true;
			}
			double x, y, z;
			try {
				x = Double.parseDouble(args[1]);
				y = Double.parseDouble(args[2]);
				z = Double.parseDouble(args[3]);
			} catch (NumberFormatException e) {
				sender.sendMessage("§cCoordenadas inv§lidas.");
				return false;
			}
			Player player = (Player) sender;
			Location location = new Location(world, x, y, z);
			player.teleport(location, TeleportCause.COMMAND);
			player.sendMessage("§aTeleportado at§ as coordenadas World:" + args[0] + " X: " + args[1] + " Y: " + args[2]+ " Z: " + args[3] + " com sucesso.");
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[P4Free] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a (nick)");
		sender.sendMessage("§7/" + arg + "§a (nick) (alvo)");
		sender.sendMessage("§7/" + arg + "§a (x) (y) (z)");
		sender.sendMessage("§7/" + arg + "§a (world) (x) (y) (z)");
		return false;
	}
}