package net.gamerspvp.punish.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.models.Kick;
import net.gamerspvp.punish.network.utils.DateUtils;

public class KickCommand extends Command {
	
	private Main instance;
	
	public KickCommand(Main instance) {
		super("kick");
		instance.registerCommand(this, "kick", "expulsar");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.kick"))) {
			sender.sendMessage("§cSem permissão.");
			return false;
		}
		if (args.length > 1) {
			String target = args[0];
			if (target.equalsIgnoreCase(sender.getName())) {
				sender.sendMessage("§cVocê não pode expulsar-se.");
				return false;
			}
			Player targetPlayer = Bukkit.getPlayer(target);
			if (targetPlayer != null) {
				target = targetPlayer.getName();
			}
			String author = sender.getName();
			String reason = "";
			String date = DateUtils.fromDateAndHour();
			for (int i = 1; i < args.length; i++) {
				reason = reason + args[i] + " ";
			}
			Kick kick = new Kick(target, reason, author, date);
			kick.execute(instance);
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Punish] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + " (nick) (motivo).");
		sender.sendMessage("");
		return false;
	}
}