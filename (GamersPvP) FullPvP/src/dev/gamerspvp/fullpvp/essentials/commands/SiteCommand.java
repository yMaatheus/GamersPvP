package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.essentials.EssentialsManager;

public class SiteCommand extends Command {
	
	private Main instance;
	
	public SiteCommand(Main instance) {
		super("site");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		sender.sendMessage("§aVisite nosso Site: §f" + essentialsManager.getSite());
		return false;
	}
}