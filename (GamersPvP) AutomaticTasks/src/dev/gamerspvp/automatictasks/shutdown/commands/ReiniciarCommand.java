package dev.gamerspvp.automatictasks.shutdown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.automatictasks.Main;
import dev.gamerspvp.automatictasks.shutdown.ShutdownManager;

public class ReiniciarCommand extends Command {
	
	private Main instance;
	
	public ReiniciarCommand(Main instance) {
		super("restart");
		instance.registerCommand(this, "reiniciar");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamerspvp.restart"))) {
			sender.sendMessage("§cSem permiss§o.");
			return true;
		}
		ShutdownManager shutdownManager = instance.getShutdownManager();
		shutdownManager.executeRestart();
		return false;
	}
}