package net.gamerspvp.punish.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.models.Banip;
import net.gamerspvp.punish.network.utils.DateUtils;
import net.gamerspvp.punish.network.utils.TimeManager;

public class BanIpCommand extends Command {
	
	private Main instance;
	
	public BanIpCommand(Main instance) {
		super("banip");
		instance.registerCommand(this, "ban", "ipban", "banirip", "ipbanir");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.banip"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		if (args.length > 1) {
			String ip = args[0];
			if (!(args[0].contains("."))) {
				if (args[0].equalsIgnoreCase(sender.getName())) {
					sender.sendMessage("§cVoc§ n§o pode banir-se.");
					return false;
				}
				String address = instance.getCachedAddress().get(args[0].toLowerCase());
				if (address == null) {
					sender.sendMessage("§cO endere§o ip n§o encontrado.");
					return false;
				}
				ip = address;
			}
			String author = sender.getName();
			String date = DateUtils.fromDateAndHour();
			if (args.length >= 4) {
				long time = TimeManager.generateTime(args[2], Integer.parseInt(args[1]));
				try {
					Integer.parseInt(args[1]);
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
				Banip banip = new Banip(ip, reason, author, "TEMPOR§RIO", System.currentTimeMillis(), date);
				banip.execute(instance);
				sender.sendMessage("§aA publica§§o do banimento foi enviada.");
				return false;
			}
			String reason = "";
			for (int i = 1; i < args.length; i++) {
				reason = reason + args[i] + " ";
			}
			Banip ban = new Banip(ip, reason, author, "PERMANENTE", System.currentTimeMillis(), date);
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