package net.gamerspvp.punish.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.controllers.DatabaseController;

public class UnBanCommand extends Command {

	private Main instance;

	public UnBanCommand(Main instance) {
		super("unban");
		instance.registerCommand(this, "unban", "desbanir");
		this.instance = instance;
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.unban"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		if (args.length > 0) {
			if (args[0].contains(".")) {
				String ip = args[0];
				DatabaseController mysql = new DatabaseController();
				new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							if (!(mysql.hasIpBanned(ip, true, false))) {
								sender.sendMessage("§cEsse endere§o de ip n§o est§ banido.");
								return;
							}
							mysql.executeUnbanIP(ip, false);
						} catch (Exception e) {
							sender.sendMessage("§cN§o foi possivel completar a opera§§o.");
							e.printStackTrace();
						}
					}
				}.runTaskAsynchronously(instance);
			} else {
				String target = args[0];
				DatabaseController mysql = new DatabaseController();
				new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							if (!(mysql.hasBanned(target.toLowerCase(), true, false))) {
								sender.sendMessage("§cEsse jogador n§o est§ banido.");
								return;
							}
							mysql.executeUnban(target.toLowerCase(), false);
							sender.sendMessage("§aPuni§§o revogada com sucesso.");
						} catch (Exception e) {
							sender.sendMessage("§cN§o foi possivel completar a opera§§o.");
							e.printStackTrace();
						}
					}
				}.runTaskAsynchronously(instance);
			}
			
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Punish] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + " (nick).");
		sender.sendMessage("");
		return false;
	}
}