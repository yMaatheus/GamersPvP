package dev.gamerspvp.automatictasks.shutdown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.automatictasks.Main;
import dev.gamerspvp.automatictasks.shutdown.ShutdownManager;

public class RestartCommand extends Command {
	
	private Main instance;
	
	public RestartCommand(Main instance) {
		super("restart");
		instance.registerCommand(this, "restart", "stop");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamerspvp.restart"))) {
			sender.sendMessage("§cSem permiss§o.");
			return true;
		}
		ShutdownManager shutdownManager = instance.getShutdownManager();
		shutdownManager.executeFastRestart();
		return false;
	}
}