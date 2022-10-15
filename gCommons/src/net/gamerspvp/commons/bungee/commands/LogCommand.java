package net.gamerspvp.commons.bungee.commands;

import java.util.HashSet;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.log.LogManager;
import net.gamerspvp.commons.network.log.LogReport;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class LogCommand extends Command {
	
	private String permission;
	
	public LogCommand(String name, String permission) {
		super(name, permission);
		this.permission = permission;
		ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§a* " + name + " - " + permission));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		if (args.length > 0) {
			try {
				if (args.length > 1 && args[0].equalsIgnoreCase("report")) {
					String type = null;
					LogReport logReport = null;
					if (args.length > 2 && args[1].equalsIgnoreCase("all")) {
						int days = Integer.valueOf(args[2]);
						//long currentMillis = System.currentTimeMillis();
						//long lastDays = currentMillis - TimeUnit.DAYS.toMillis(days);
						//query = "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "';";
						type = "1";
						logReport = new LogReport(type, sender.getName(), "", "", days);
						LogManager.log("Compliance", sender.getName(), sender.getName() + " criou um relat§rio GERAL dos §ltimos " + days + " dias");
					} else if (args.length > 3 && args[1].equalsIgnoreCase("key")) {
						String key = args[2];
						int days = Integer.valueOf(args[3]);
						//long currentMillis = System.currentTimeMillis();
						//long lastDays = currentMillis - TimeUnit.DAYS.toMillis(days);
						//query = "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "' AND `key`='" + key + "';";
						type = "2";
						logReport = new LogReport(type, sender.getName(), key, "", days);
						LogManager.log("Compliance", sender.getName(), sender.getName() + " criou um relat§rio dos §ltimos " + days + " dias buscando a key " + key, "");
					} else if (args.length > 3 && args[1].equalsIgnoreCase("nick")) {
						String playerName = args[2].toLowerCase();
						int days = Integer.valueOf(args[3]);
						//long currentMillis = System.currentTimeMillis();
						//long lastDays = currentMillis - TimeUnit.DAYS.toMillis(days);
						//query = "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "' AND `playerName`='" + playerName + "';";
						type = "3";
						logReport = new LogReport(type, sender.getName(), "", playerName, days);
						LogManager.log("Compliance", sender.getName(), sender.getName() + " criou um relat§rio dos §ltimos " + days + " dias buscando o jogador " + playerName, "");
					} else if (args.length > 3) {
						String key = args[2];
						String playerName = args[3].toLowerCase();
						int days = Integer.valueOf(args[4]);
						//long currentMillis = System.currentTimeMillis();
						//long lastDays = currentMillis - TimeUnit.DAYS.toMillis(days);
						//query = "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "' AND `key`='" + key + "' AND `playerName`='" + playerName + "';";
						type = "4";
						logReport = new LogReport(type, sender.getName(), key, playerName, days);
						LogManager.log("Compliance", sender.getName(), sender.getName() + " criou um relat§rio dos §ltimos " + days + " dias buscando a key " + key + " e jogador " + playerName, "");
					}
					if (type != null) {
						Redis redis = ProxiedCommons.getInstance().getDataCenter().getRedis();
						redis.publish("log_report;" + redis.getGson().toJson(logReport, LogReport.class));
						sender.sendMessage(new TextComponent("§aRelat§rio enviado para central e ser§ baixado com sucesso."));
						return;
					}
				} else if (args[0].equalsIgnoreCase("keys")) {
					HashSet<String> keys = LogManager.getController().getKeys();
					sender.sendMessage(new TextComponent("§aAs keys encontradas para busca s§o: §f" + keys.toString().replace("[", "").replace("]", "")));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Commons] Comandos dispon§veis:"));
		sender.sendMessage(new TextComponent("§7/log §areport all (lastDays)"));
		sender.sendMessage(new TextComponent("§7/log §areport key (key) (lastDays)"));
		sender.sendMessage(new TextComponent("§7/log §areport nick (playerName) (lastDays)"));
		sender.sendMessage(new TextComponent("§7/log §areport (key) (playerName) (lastDays)"));
		sender.sendMessage(new TextComponent("§7/log §akeys"));
	}
}