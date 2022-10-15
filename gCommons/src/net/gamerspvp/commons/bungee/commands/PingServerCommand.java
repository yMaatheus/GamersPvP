package net.gamerspvp.commons.bungee.commands;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.gamerspvp.commons.network.utils.PingServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class PingServerCommand extends Command {
	
	//private CommonsBungeeManager instance;
	
	private String permission;
	
	public PingServerCommand(String name, String permission, ProxiedCommons instance) {
		super(name);
		this.permission = permission;
		//this.instance = instance;
		instance.getProxy().getConsole().sendMessage(new TextComponent("§a* " + name + " - " + permission));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		if (args.length > 1) {
			int port = Integer.parseInt(args[1]);
			sender.sendMessage(new TextComponent("Ping enviado! Pinged: " + PingServer.hasOnlineServer(args[0], port)));
			return;
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Commons] Comandos disponíveis:"));
		sender.sendMessage(new TextComponent("§7/pingserver §a (address) (port)"));
		sender.sendMessage(new TextComponent(""));
		return;
	}
}