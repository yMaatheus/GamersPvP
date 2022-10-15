package net.gamerspvp.commons.bukkit.restart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.restart.RestartManager;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class ReiniciarCommand extends Command {
	
	public ReiniciarCommand(CommonsBukkit instance) {
		super("restart");
		Utils.registerCommand(this, instance, "reiniciar");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamerspvp.admin"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		RestartManager.executeRestart();
		return true;
	}
}