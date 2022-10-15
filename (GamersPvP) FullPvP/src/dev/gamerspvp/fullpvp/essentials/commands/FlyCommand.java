package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.essentials.EssentialsManager;

public class FlyCommand extends Command {
	
	private Main instance;
	
	public FlyCommand(Main instance) {
		super("fly");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		boolean vipPermission = player.hasPermission("fly.vip");
		boolean adminPermission = player.hasPermission("fly.admin");
		if (vipPermission || adminPermission) {
			if (essentialsManager.isVipWorld(player.getWorld().getName())) {
				essentialsManager.fly(player);
				return true;
			}
			if (!(adminPermission)) {
				player.sendMessage("§cO fly neste mundo est§ desativado para voc§.");
				return false;
			}
			essentialsManager.fly(player);
			return true;
		}
		sender.sendMessage("§cSem permiss§o.");
		return false;
	}
}