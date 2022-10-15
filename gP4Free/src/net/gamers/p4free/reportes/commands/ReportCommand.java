package net.gamers.p4free.reportes.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamers.p4free.reportes.ReportManager;
import net.gamerspvp.commons.bukkit.utils.Utils;

public class ReportCommand extends Command {
	
	private Main instance;
	
	public ReportCommand(Main instance) {
		super("report");
		Utils.registerCommand(this, instance, "report");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (args.length > 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage("§cJogador offline.");
				return false;
			}
			StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				reason.append(args[i]).append(" ");
			}
			ReportManager reportManager = instance.getReportManager();
			reportManager.executeReport(target, player, reason.toString());
			return true;
		}
		sender.sendMessage("§7/" + arg + " §a(player) (motivo).");
		return false;
	}
}