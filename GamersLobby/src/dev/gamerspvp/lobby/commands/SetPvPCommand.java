package dev.gamerspvp.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.lobby.api.LocAPI;

public class SetPvPCommand extends Command {
	
	public SetPvPCommand() {
		super("setpvp");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (sender instanceof Player == false) {
			return true;
		}
		Player player = (Player) sender;
		if (player.isOp()) {
			LocAPI.Set(player, "pvp");
			player.sendMessage("§aO pvp foi setado");
			return true;
		}
		sender.sendMessage("§cSem permissão.");
		return false;
	}
}