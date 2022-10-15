package dev.gamerspvp.gladiador.gladiador;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.gladiador.commands.GladiadorCommand;
import dev.gamerspvp.gladiador.gladiador.models.Gladiador;
import dev.gamerspvp.gladiador.gladiador.models.Gladiador.statusType;
import dev.gamerspvp.gladiador.utils.TimeFormater;

public class GladiadorCommandManager {
	
	private Main instance;
	
	public GladiadorCommandManager(Main instance) {
		this.instance = instance;
		instance.registerCommand(new GladiadorCommand(instance), "gladiador", "glad");
		instance.registerCommand(new Command("gladiadores") {
			@Override
			public boolean execute(CommandSender sender, String arg, String[] args) {
				GladiadorManager gladiadorManager = instance.getGladiadorManager();
				sender.sendMessage("§aGladiadores: §f" + gladiadorManager.getGladiadores());
				return false;
			}
		}, "gladiadores");
	}
	
	public void start(CommandSender sender) {
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (!(gladiadorManager.hasLocations())) {
			sender.sendMessage("§cDefina as localizações para iniciar o evento.");
			return;
		}
		if (gladiador != null) {
			sender.sendMessage("§cO evento gladiador já está acontencendo.");
			return;
		}
		gladiadorManager.executeStart();
		sender.sendMessage("§7Iniciando evento gladiador.");
	}
	
	public void forceDeathmatch(CommandSender sender) {
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return;
		}
		if (gladiador.getStatus() != statusType.PVP) {
			sender.sendMessage("§cSó é possivel forçar o deathmatch durante o PvP.");
			return;
		}
		gladiadorManager.executeDeathmatch();
	}
	
	public void cancel(CommandSender sender) {
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return;
		}
		gladiadorManager.executeCancel();
	}
	
	public void info(CommandSender sender) {
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return;
		}
		statusType status = gladiador.getStatus();
		sender.sendMessage("");
		sender.sendMessage("§7Status§8: §f" + status);
		sender.sendMessage("§7Tempo§8: §f" + getInfoTime(gladiador));
		sender.sendMessage("");
		sender.sendMessage("§7Clans vivos§8: §f" + gladiadorManager.getClans(gladiador));
		sender.sendMessage("§7Jogadores vivos§8: §f" + gladiadorManager.getParticipants(gladiador));
		sender.sendMessage("");
		sender.sendMessage("§7Quantidade clans vivos§8: §f" + gladiadorManager.getClans());
		sender.sendMessage("§7Quantidade jogadores vivos§8: §f" + gladiadorManager.getPlayers());
		sender.sendMessage("");
	}
	
	private String getInfoTime(Gladiador gladiador) {
		String time = TimeFormater.formatOfEnd(gladiador.getTime());
		statusType status = gladiador.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.FECHADO) {
			return "Aguardando começo do Evento.";
		} else if (status.name().contains("DEATHMATCH")) {
			return "Deathmatch já encontra-se em andamento.";
		}
		return time;
	}
	
	public void sendHelpCommands(CommandSender sender, String arg) {
		sender.sendMessage("");
		sender.sendMessage("§a[Gladiador] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a sair");
		sender.sendMessage("§7/" + arg + "§a top");
		sender.sendMessage("§7/" + arg + "§a ajuda");
		if (sender.hasPermission("gladiador.admin")) {
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