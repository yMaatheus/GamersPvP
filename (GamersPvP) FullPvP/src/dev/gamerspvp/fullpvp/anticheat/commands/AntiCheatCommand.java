package dev.gamerspvp.fullpvp.anticheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.anticheat.AntiCheatManager;

public class AntiCheatCommand extends Command {

	private Main instance;

	public AntiCheatCommand(Main instance) {
		super("anticheat");
		this.instance = instance;
		instance.registerCommand(this, "anticheat");
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!sender.hasPermission("anticheat.moderador")) {
			sender.sendMessage("§cSem permissão.");
			return false;
		}
		if (args.length > 0) {
			if (args.length > 1) {
				if (args.length > 2 && args[0].equalsIgnoreCase("cps")) {
					if (!(sender instanceof Player)) {
						return false;
					}
					Player targetPlayer = Bukkit.getPlayer(args[1]);
					if (targetPlayer == null) {
						sender.sendMessage("§cJogador não encontrado.");
						return false;
					}
					String time = args[2];
					Player player = (Player) sender;
					player.chat("/matrix cps " + targetPlayer.getName() + " " + time);
					return true;
				}
				if (args[0].equalsIgnoreCase("add")) {
					if (!(sender instanceof ConsoleCommandSender)) {
						sender.sendMessage("§cEsse comando só pode ser executado pelo Console.");
						return false;
					}
					Player targetPlayer = Bukkit.getPlayer(args[1]);
					if (targetPlayer == null) {
						sender.sendMessage("§cJogador não encontrado.");
						return false;
					}
					String name = targetPlayer.getName().toLowerCase();
					AntiCheatManager antiCheatManager = instance.getAntiCheatManager();
					String address = antiCheatManager.getAddress(name);
					if (address == null) {
						return false;
					}
					antiCheatManager.addPointKick(name, address);
					sender.sendMessage("§a[Essentials] Adicionado +1 pontos de AntiCheat para §f" + name + "§a(§f" + address + "§a).");
					return true;
				} else if (args[0].equalsIgnoreCase("remove")) {
					if (!sender.isOp()) {
						sender.sendMessage("§cSem permissão.");
						return true;
					}
					AntiCheatManager antiCheatManager = instance.getAntiCheatManager();
					String address = antiCheatManager.getAddress(args[1].toLowerCase());
					if (address == null) {
						return false;
					}
					antiCheatManager.removePointsKick(address);
					sender.sendMessage("§a[Essentials] Removido os pontos de AntiCheat de §f" + args[1].toLowerCase() + "§a(§f" + address + "§a).");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("gui")) {
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player = (Player) sender;
				player.chat("/matrix violations");
				return true;
			} else if (args[0].equalsIgnoreCase("toggle")) {
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player = (Player) sender;
				if (!player.hasPermission("anticheat.admin")) {
					player.sendMessage("§cSem permissão.");
					return false;
				}
				player.chat("/matrix togglenotify");
				return true;
			}
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Essentials] Comandos disponíveis:");
		if (sender.isOp()) {
			sender.sendMessage("§7/" + arg + "§a add (nick).");
			sender.sendMessage("§7/" + arg + "§a remove (nick).");
		}
		sender.sendMessage("§7/" + arg + "§a cps (nick) (tempo)");
		sender.sendMessage("§7/" + arg + "§a gui");
		sender.sendMessage("§7/" + arg + "§a toggle");
		sender.sendMessage("");
		return false;
	}
}