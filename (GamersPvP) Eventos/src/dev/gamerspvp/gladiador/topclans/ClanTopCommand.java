package dev.gamerspvp.gladiador.topclans;

import java.sql.Statement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.database.SQLite;

public class ClanTopCommand extends Command {
	
	private Main instance;
	
	public ClanTopCommand(Main instance) {
		super("clantop");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("clantop.admin"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		if (args.length > 0) {
			ClanTopManager clanTopManager = instance.getClanTopManager();
			SQLite SQLite = instance.getSQLite();
			new BukkitRunnable() {
				
				@Override
				public void run() {
					try {
						if (args.length > 1) {
							String clanTag = args[1];
							if (args[0].equalsIgnoreCase("set")) {
								if (args.length < 2) {
									sender.sendMessage("§7/" + arg + "§a set (clan) (valor)");
									return;
								}
								int wins = Integer.parseInt(args[2]);
								ClanTop clanTop = clanTopManager.getClan(clanTag);
								if (clanTop == null) {
									clanTop = new ClanTop(clanTag);
									clanTop.create(instance);
								}
								clanTop.setWins(wins);
								clanTop.update(clanTopManager, instance);
								sender.sendMessage("§aO Clan §f" + clanTag + " §aagora possui §f" + wins + " §avit§rias!");
							} else if (args[0].equalsIgnoreCase("delete")) {
								ClanTop clanTop = clanTopManager.getClan(clanTag);
								if (clanTop == null) {
									sender.sendMessage("§cClan n§o encontrado no top clans.");
									return;
								}
								clanTopManager.delete(clanTop.getClan());
								sender.sendMessage("§f" + clanTag + " §adeletado do top clans.");
							}
						}
						if (args[0].equalsIgnoreCase("reset")) {
							Statement stmt = SQLite.getConnection().createStatement();
							stmt.execute("DELETE FROM clanTop;");
							stmt.execute("VACUUM");
							stmt.close();
							sender.sendMessage("§aClanTop foi resetado com sucesso.");
						}
						clanTopManager.updateTop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(instance);
			return false;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[ClanTop] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a reset");
		sender.sendMessage("§7/" + arg + "§a set (clan) (valor)");
		sender.sendMessage("§7/" + arg + "§a delete (clan)");
		sender.sendMessage("");
		return false;
	}
}