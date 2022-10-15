package dev.gamerspvp.gladiador.guerra;

import org.bukkit.command.CommandSender;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.guerra.commands.GuerraCommand;
import dev.gamerspvp.gladiador.guerra.models.Guerra;
import dev.gamerspvp.gladiador.guerra.models.Guerra.statusType;

public class GuerraCommandManager {
	
	private Main instance;
	
	public GuerraCommandManager(Main instance) {
		this.instance = instance;
		instance.registerCommand(new GuerraCommand(instance), "guerra");
	}
	
	public void start(CommandSender sender) {
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		if (!(guerraManager.hasLocations())) {
			sender.sendMessage("§cDefina as localiza§§es para iniciar o evento.");
			return;
		}
		if (guerra != null) {
			sender.sendMessage("§cO evento guerra j§ est§ acontencendo.");
			return;
		}
		guerraManager.executeStart();
		sender.sendMessage("§7Iniciando evento guerra.");
	}
	
	public void cancel(CommandSender sender) {
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		if (guerra == null) {
			sender.sendMessage("§cO evento n§o est§ acontecendo.");
			return;
		}
		guerraManager.executeCancel();
	}
	
	public void info(CommandSender sender) {
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		if (guerra == null) {
			sender.sendMessage("§cO evento n§o est§ acontecendo.");
			return;
		}
		statusType status = guerra.getStatus();
		sender.sendMessage("");
		sender.sendMessage("§7Status§8: §f" + status);
		sender.sendMessage("");
		sender.sendMessage("§7Clans vivos§8: §f" + guerraManager.getClans(guerra));
		sender.sendMessage("§7Jogadores vivos§8: §f" + guerraManager.getParticipants(guerra));
		sender.sendMessage("");
		sender.sendMessage("§7Quantidade clans vivos§8: §f" + guerraManager.getClans());
		sender.sendMessage("§7Quantidade jogadores vivos§8: §f" + guerraManager.getPlayers());
		sender.sendMessage("");
	}
	
	public void sendHelpCommands(CommandSender sender, String arg) {
		sender.sendMessage("");
		sender.sendMessage("§a[Guerra] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a sair");
		sender.sendMessage("§7/" + arg + "§a top");
		sender.sendMessage("§7/" + arg + "§a ajuda");
		if (sender.hasPermission("guerra.admin")) {
			sender.sendMessage("");
			sender.sendMessage("§7/" + arg + "§a iniciar");
			sender.sendMessage("§7/" + arg + "§a forcedeathmatch");
			sender.sendMessage("§7/" + arg + "§a parar");
			sender.sendMessage("§7/" + arg + "§a set (spawn | saida | deathmatch)");
			sender.sendMessage("§7/" + arg + "§a info");
		}
		sender.sendMessage("");
	}
}