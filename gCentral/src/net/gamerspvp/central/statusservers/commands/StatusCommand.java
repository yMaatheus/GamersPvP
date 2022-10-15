package net.gamerspvp.central.statusservers.commands;

import net.gamerspvp.central.Main;
import net.gamerspvp.central.utils.ServerPingInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class StatusCommand extends Command {
	
	public StatusCommand(Main instance) {
		super("status");
		instance.getProxy().getPluginManager().registerCommand(instance, this);
	}
	
	// status 199.127.63.103 25566
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			int port = Integer.parseInt(args[1]);
			ServerPingInfo ping = new ServerPingInfo(args[0], port);
			sender.sendMessage(new TextComponent("Ping enviado! Pinged: " + ping.isClosedServer()));
			sender.sendMessage(new TextComponent("Jogadores " + ping.getOnlinePlayers()));
			sender.sendMessage(new TextComponent("Max " + ping.getMaxPlayers()));
			return;
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Commons] Comandos disponíveis:"));
		sender.sendMessage(new TextComponent("§7/status §a (address) (port)"));
		sender.sendMessage(new TextComponent(""));
		return;
	}
}