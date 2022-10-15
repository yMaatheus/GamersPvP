package net.gamerspvp.commons.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.network.models.NetworkOptions;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class MotdCommand extends Command {
	
	private ProxiedCommons instance;
	
	private String permission;
	
	public MotdCommand(String name, String permission, ProxiedCommons instance) {
		super(name);
		this.permission = permission;
		this.instance = instance;
		instance.getProxy().getConsole().sendMessage(new TextComponent("§a* " + name + " - " + permission));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		if (args.length > 1) {
			String motd = "";
			for (int i = 1; i < args.length; i++) {
				motd = MessageUtils.setStringReplaceLinhas(motd + args[i] + " ").replace("&", "§");
			}
			instance.getProxy().getConsole().sendMessage(new TextComponent(motd));
			if (motd.contains("%linha%")) {
				String linha1 = motd.split("%linha%")[0];
				String linha2 = motd.split("%linha%")[1];
				List<String> linhas = new ArrayList<String>();
				linhas.add(linha1);
				linhas.add(linha2);
				StringBuilder builder = new StringBuilder();
				for (String line : linhas) {
					builder.append(line);
					builder.append("\n");
				}
				motd = builder.toString();
			}
			if (args[0].equalsIgnoreCase("normal")) {
				NetworkOptions networkOptions = instance.getNetworkOptions();
				networkOptions.setMotd(motd);
				networkOptions.publish(instance);
				sender.sendMessage(new TextComponent("§aVocê definiu o motd normal para: §f" + motd));
				return;
			} else if (args[0].equalsIgnoreCase("maintenance")) {
				NetworkOptions networkOptions = instance.getNetworkOptions();
				networkOptions.setMotdMaintenance(motd);
				networkOptions.publish(instance);
				sender.sendMessage(new TextComponent("§aVocê definiu o motd de manutenção para: §f" + motd));
				return;
			}
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Commons] Comandos disponíveis:"));
		sender.sendMessage(new TextComponent("§7/motd §a normal (motd)"));
		sender.sendMessage(new TextComponent("§7/motd §a maintenance (motd)"));
	}
}