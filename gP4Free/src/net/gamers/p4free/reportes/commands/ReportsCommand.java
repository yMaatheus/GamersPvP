package net.gamers.p4free.reportes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamers.p4free.reportes.ReportManager;
import net.gamerspvp.commons.bukkit.utils.Utils;

public class ReportsCommand extends Command {
	
	private Main instance;
	
	public ReportsCommand(Main instance) {
		super("report");
		Utils.registerCommand(this, instance, "reports");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (!(sender.hasPermission("gamers.moderador"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		ReportManager reportManager = instance.getReportManager();
		Player player = (Player) sender;
		player.openInventory(reportManager.getInventory());
		return false;
	}
}