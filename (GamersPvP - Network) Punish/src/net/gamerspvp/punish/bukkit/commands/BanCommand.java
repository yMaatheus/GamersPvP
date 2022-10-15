package net.gamerspvp.punish.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.models.Ban;
import net.gamerspvp.punish.network.utils.DateUtils;
import net.gamerspvp.punish.network.utils.TimeManager;

public class BanCommand extends Command {
	
	private Main instance;
	
	public BanCommand(Main instance) {
		super("ban");
		instance.registerCommand(this, "ban", "banir");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.ban"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		if (args.length > 1) {
			String target = args[0];
			if (target.equalsIgnoreCase(sender.getName())) {
				sender.sendMessage("§cVoc§ n§o pode banir-se.");
				return false;
			}
			Player targetPlayer = Bukkit.getPlayer(target);
			if (targetPlayer != null) {
				target = targetPlayer.getName();
			}
			String author = sender.getName();
			String date = DateUtils.fromDateAndHour();
			if (args.length >= 4) {
				long time = 0;
				try {
					time = TimeManager.generateTime(args[2], Integer.parseInt(args[1]));
				} catch (NumberFormatException e) {
					sender.sendMessage("§cDesculpe, mas deve-se usar apenas n§meros.");
					return false;
				}
				if (time == 0) {
					sender.sendMessage("§cFormato de hor§rio inv§lido.");
					return false;
				}
				String reason = "";
				for (int i = 3; i < args.length; i++) {
					reason = reason + args[i] + " ";
				}
				Ban ban = new Ban(target, reason, author, "TEMPOR§RIO", time, date);
				ban.execute(instance);
				sender.sendMessage("§aA publica§§o do banimento foi enviada.");
				return false;
			}
			String reason = "";
			for (int i = 1; i < args.length; i++) {
				reason = reason + args[i] + " ";
			}
			Ban ban = new Ban(target, reason, author, "PERMANENTE", System.currentTimeMillis(), date);
			ban.execute(instance);
			sender.sendMessage("§aA publica§§o do banimento foi enviada.");
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Punish] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a (nick) (motivo)");
		sender.sendMessage("§7/" + arg + "§a (nick) (tempo) (horas, dias, semanas, meses) (motivo)");
		sender.sendMessage("");
		return false;
	}
}