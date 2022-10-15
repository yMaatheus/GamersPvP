package dev.gamerspvp.reportes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.reportes.Main;
import dev.gamerspvp.reportes.ReportManager;

public class ReportsCommand extends Command {
	
	private Main instance;
	
	public ReportsCommand(Main instance) {
		super("report");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (!(sender.hasPermission("reportes.moderador"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		ReportManager reportManager = instance.getReportManager();
		Player player = (Player) sender;
		player.openInventory(reportManager.getInventory());
		return false;
	}
}