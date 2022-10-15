package net.gamerspvp.lobby.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.gamerspvp.lobby.Main;
import net.gamerspvp.lobby.server.ServerManager;

public class QueueCommand extends Command {
	
	private Main instance;
	
	public QueueCommand(Main instance) {
		super("queue");
		this.instance = instance;
		Utils.registerCommand(this, instance, "queue");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamers.diretor"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length > 0) {
			ServerManager serverManager = instance.getServerManager();
			sender.sendMessage(serverManager.getServerQueue(args[0]).getQueue().toString());
			return true;
		}
		return false;
	}
}