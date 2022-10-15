package net.gamers.p4free.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamers.p4free.game.GameManager;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class AdminCommand extends Command {
	
	private Main instance;
	
	
	public AdminCommand(Main instance) {
		super("admin");
		this.instance = instance;
		Utils.registerCommand(this, instance, "admin");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (!(sender.hasPermission("gamers.moderador"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		GameManager gameManager = instance.getGameManager();
		if (!(gameManager.hasAdminMode(playerName))) {
			gameManager.joinAdminMode(player);
			return true;
		}
        gameManager.leaveAdminMode(player);
		return false;
	}
}