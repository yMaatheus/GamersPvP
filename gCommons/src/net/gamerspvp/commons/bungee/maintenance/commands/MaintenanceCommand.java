package net.gamerspvp.commons.bungee.maintenance.commands;

import com.google.gson.Gson;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.maintenance.models.Maintenance;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.GameStatus;
import net.gamerspvp.commons.network.models.NetworkOptions;
import net.gamerspvp.commons.network.models.GameStatus.gameStatus;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.gamerspvp.commons.network.utils.TimeManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import redis.clients.jedis.Jedis;

public class MaintenanceCommand extends Command {
	
	private ProxiedCommons instance;
	
	public MaintenanceCommand(ProxiedCommons instance) {
		super("manutencao");
		this.instance = instance;
		instance.getProxy().getPluginManager().registerCommand(instance, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("gamers.admin")) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("network")) {
				NetworkOptions networkOptions = instance.getNetworkOptions();
				if (args.length >= 4) {
					if (args[1].equalsIgnoreCase("ativar")) {
						long time = 0;
						try {
							time = TimeManager.generateTime(args[3], Integer.parseInt(args[2]));
						} catch (NumberFormatException e) {
							sender.sendMessage(new TextComponent("§cHorário inválido."));
							return;
						}
						if (time == 0) {
							sender.sendMessage(new TextComponent("§cFormato de horário inválido."));
							return;
						}
						String reason = "";
						for (int i = 4; i < args.length; i++) {
							reason = reason + args[i] + " ";
						}
						String estimatedTime = TimeManager.getTimeEnd(time);
						networkOptions.setMaintenance(new Maintenance(reason, sender.getName(), System.currentTimeMillis(), estimatedTime));
						networkOptions.publish(instance);
						sender.sendMessage(new TextComponent("§aModo de segurança da network foi ativado com sucesso."));
						return;
					}
				}
				if (args[1].equalsIgnoreCase("desativar")) {
					networkOptions.setMaintenance(null);
					networkOptions.publish(instance);
					sender.sendMessage(new TextComponent("§cModo de segurança da network foi desativado com sucesso."));
				}
				return;
			}
			Redis redis = instance.getDataCenter().getRedis();
			Jedis jedis = redis.getJedis();
			Gson gson = redis.getGson();
			String key = "game_" + args[0];
			if (!jedis.exists(key)) {
				sender.sendMessage(new TextComponent("§cServidor não encontrado."));
				return;
			}
			GameStatus game = instance.getGameStatus(key);
			if (args[1].equalsIgnoreCase("ativar")) {
				game.setStatus(gameStatus.MANUTENÇÃO);
				String json = gson.toJson(game);
				jedis.set(key, json);
				jedis.publish("general", "maintenance;" + json);
				sender.sendMessage(new TextComponent("§aModo de segurança foi ativado no servidor §f" + args[0] + " §acom sucesso."));
				return;
			} else if (args[1].equalsIgnoreCase("desativar")) {
				game.setStatus(gameStatus.OFFLINE);
				String json = gson.toJson(game);
				jedis.set(key, json);
				jedis.publish("general", "maintenance;" + json);
				sender.sendMessage(new TextComponent("§aModo de segurança foi desativado no servidor §f" + args[0]  + " §acom sucesso."));
				return;
			}
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Commons] Comandos disponíveis:"));
		sender.sendMessage(new TextComponent("§7/maintenance§a (Modo de Jogo) (ativar/desativar)"));
		sender.sendMessage(new TextComponent("§7/maintenance§a network ativar (tempo) (horas, dias, semanas, meses) (motivo)"));
		sender.sendMessage(new TextComponent("§7/maintenance§a network desativar"));
		sender.sendMessage(new TextComponent(""));
		
	}
}