package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.essentials.EssentialsManager;

public class DiscordCommand extends Command {
	
	private Main instance;
	
	public DiscordCommand(Main instance) {
		super("discord");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		sender.sendMessage("§aEntre em nosso Discord: §f" + essentialsManager.getDiscord());
		return false;
	}
}