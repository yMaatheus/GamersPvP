package dev.gamerspvp.gladiador.killer;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.killer.Killer.statusType;

public class KillerCommandManager {
	
	private Main instance;
	
	public KillerCommandManager(Main instance) {
		this.instance = instance;
		instance.registerCommand(new KillerCommand(instance), "killer");
	}
	
	public void start(CommandSender sender) {
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		if (!(killerManager.hasLocations())) {
			sender.sendMessage("§cDefina as localizações para iniciar o evento.");
			return;
		}
		if (killer != null) {
			sender.sendMessage("§cO evento killer já está acontencendo.");
			return;
		}
		killerManager.executeStart();
		sender.sendMessage("§7Iniciando evento killer.");
	}
	
	public void cancel(CommandSender sender) {
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		if (killer == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return;
		}
		killerManager.executeCancel();
	}
	
	public void info(CommandSender sender) {
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		if (killer == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return;
		}
		statusType status = killer.getStatus();
		String alivePlayers = StringUtils.join(killer.getParticipantes().values(), ",");
		sender.sendMessage("");
		sender.sendMessage("§7Status§8: §f" + status);
		sender.sendMessage("§7Jogadores vivos§8: §f" + alivePlayers);
		sender.sendMessage("");
	}
	
	public void sendHelpCommands(CommandSender sender, String arg) {
		sender.sendMessage("");
		sender.sendMessage("§a[Killer] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a sair");
		sender.sendMessage("§7/" + arg + "§a ajuda");
		if (sender.hasPermission("killer.admin")) {
			sender.sendMessage("");
			sender.sendMessage("§7/" + arg + "§a iniciar");
			sender.sendMessage("§7/" + arg + "§a parar");
			sender.sendMessage("§7/" + arg + "§a set (spawn | saida)");
			sender.sendMessage("§7/" + arg + "§a info");
		}
		sender.sendMessage("");
	}
}