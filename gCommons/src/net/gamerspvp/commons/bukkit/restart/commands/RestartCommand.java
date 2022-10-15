package net.gamerspvp.commons.bukkit.restart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.restart.RestartManager;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class RestartCommand extends Command {
	
	public RestartCommand(CommonsBukkit instance) {
		super("restart");
		Utils.registerCommand(this, instance, "restart", "stop");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamerspvp.admin"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		RestartManager.executeFastRestart();
		return false;
	}
}