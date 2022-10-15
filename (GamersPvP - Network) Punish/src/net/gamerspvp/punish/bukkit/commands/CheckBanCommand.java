package net.gamerspvp.punish.bukkit.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.models.Ban;
import net.gamerspvp.punish.network.models.Banip;
import net.gamerspvp.punish.network.models.Mute;
import net.gamerspvp.punish.network.utils.TimeManager;

public class CheckBanCommand extends Command {
	
	private Main instance;
	
	public CheckBanCommand(Main instance) {
		super("checkban");
		instance.registerCommand(this, "checkban", "checkmute");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.check"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		if (args.length > 0) {
			sender.sendMessage(" §2* §aVerificando " + args[0]);
			if (args[0].contains(".")) {
				String ip = args[0];
				new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							Banip banned = instance.getBannedIp(ip);
							if (banned != null) {
								if (banned.getType().equalsIgnoreCase("PERMANENTE") || System.currentTimeMillis() < banned.getTime()) {
									sendCheckBanIP(banned, sender);
									return;
								}
							}
							sender.sendMessage(" §2* §aBanido§8: §7N§o.");
						} catch (SQLException e) {
							sender.sendMessage("§cN§o foi possivel completar a opera§§o.");
							e.printStackTrace();
						}
					}
				}.runTaskAsynchronously(instance);
			} else {
				String target = args[0];
				Mute mute = instance.getMutes().get(target.toLowerCase());
				if (mute != null && System.currentTimeMillis() < mute.getTime()) {
					sender.sendMessage(" §2* §aMutado§8: §7Sim.");
					if (mute.isMuteall()) {
						sender.sendMessage("   §a- Chat's Mutados§8: §7Todos");
					} else {
						sender.sendMessage("   §a- Chat's Mutados§8: §7Global e Local");
					}
					sender.sendMessage("   §a- Tempo§8: §7" + TimeManager.getTimeEnd(mute.getTime()));
					sender.sendMessage("   §a- Por§8: §7" + mute.getAuthor());
					sender.sendMessage("   §a- Motivo§8: §7" + mute.getReason());
					sender.sendMessage("   §a- Data§8: §7" + mute.getDate());
				} else {
					sender.sendMessage(" §2* §aMutado§8: §7N§o.");
				}
				new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							Ban banned = instance.getBanned(target.toLowerCase());
							if (banned != null) {
								if (banned.getType().equalsIgnoreCase("PERMANENTE") || System.currentTimeMillis() < banned.getTime()) {
									sendCheckBan(banned, sender);
								} else {
									sender.sendMessage(" §2* §aBanido§8: §7N§o.");
								}
							} else {
								sender.sendMessage(" §2* §aBanido§8: §7N§o.");
							}
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
		sender.sendMessage("§7/" + arg + " (nick|ip).");
		sender.sendMessage("");
		return false;
	}

	public void sendCheckBanIP(Banip banip, CommandSender sender) {
		sender.sendMessage(" §2* §aBanido§8: §7Sim.");
		sender.sendMessage("   §a- Tipo§8: §7" + banip.getType());
		if (banip.getType().equalsIgnoreCase("TEMPOR§RIO")) {
			sender.sendMessage("   §a- Tempo§8: §7" + TimeManager.getTimeEnd(banip.getTime()));
		}
		sender.sendMessage("   §a- Por§8: §7" + banip.getAuthor());
		sender.sendMessage("   §a- Motivo§8: §7" + banip.getReason());
		sender.sendMessage("   §a- Data§8: §7" + banip.getDate());
	}

	public void sendCheckBan(Ban ban, CommandSender sender) {
		sender.sendMessage(" §2* §aBanido§8: §7Sim.");
		sender.sendMessage("   §a- Tipo§8: §7" + ban.getType());
		if (ban.getType().equalsIgnoreCase("TEMPOR§RIO")) {
			sender.sendMessage("   §a- Tempo§8: §7" + TimeManager.getTimeEnd(ban.getTime()));
		}
		sender.sendMessage("   §a- Por§8: §7" + ban.getAuthor());
		sender.sendMessage("   §a- Motivo§8: §7" + ban.getReason());
		sender.sendMessage("   §a- Data§8: §7" + ban.getDate());
	}
}