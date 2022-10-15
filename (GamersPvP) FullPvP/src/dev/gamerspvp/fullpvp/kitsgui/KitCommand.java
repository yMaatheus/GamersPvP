package dev.gamerspvp.fullpvp.kitsgui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.fullpvp.Main;

public class KitCommand extends Command {
	
	private Main instance;
	
	public KitCommand(Main instance) {
		super("kit");
		this.instance = instance;
		instance.registerCommand(this, "kit", "kits");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player)sender;
		KitsGuiManager kitsGuiManager = instance.getKitsGuiManager();
		kitsGuiManager.openInventory(player);
		return false;
	}
}