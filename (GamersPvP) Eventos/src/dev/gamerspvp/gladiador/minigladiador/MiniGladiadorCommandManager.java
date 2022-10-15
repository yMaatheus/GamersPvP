package dev.gamerspvp.gladiador.minigladiador;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.minigladiador.commands.MiniGladiadorCommand;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType;
import dev.gamerspvp.gladiador.utils.TimeFormater;

public class MiniGladiadorCommandManager {
	
	private Main instance;
	
	public MiniGladiadorCommandManager(Main instance) {
		this.instance = instance;
		instance.registerCommand(new MiniGladiadorCommand(instance), "minigladiador");
		instance.registerCommand(new Command("minigladiadores") {
			@Override
			public boolean execute(CommandSender sender, String arg, String[] args) {
				MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
				sender.sendMessage("§fMiniGladiadores: §3" + miniGladiadorManager.getGanhadores());
				return false;
			}
		}, "gladiadores");
	}
	
	public void start(CommandSender sender) {
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (!(miniGladiadorManager.hasLocations())) {
			sender.sendMessage("§cDefina as localiza§§es para iniciar o evento.");
			return;
		}
		if (!(miniGladiadorManager.hasSetedKit())) {
			sender.sendMessage("§cDefina os itens que os jogadores ir§o receber.");
			return;
		}
		if (miniGladiador != null) {
			sender.sendMessage("§cO evento gladiador j§ est§ acontencendo.");
			return;
		}
		miniGladiadorManager.executeStart();
		sender.sendMessage("§7Iniciando evento MiniGladiador.");
	}
	
	public void forceDeathmatch(CommandSender sender) {
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			sender.sendMessage("§cO evento n§o est§ acontecendo.");
			return;
		}
		if (miniGladiador.getStatus() != statusType.PVP) {
			sender.sendMessage("§cS§ § possivel for§ar o deathmatch durante o PvP.");
			return;
		}
		miniGladiadorManager.executeDeathmatch();
	}
	
	public void cancel(CommandSender sender) {
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			sender.sendMessage("§cO evento n§o est§ acontecendo.");
			return;
		}
		miniGladiadorManager.executeCancel();
	}
	
	public void info(CommandSender sender) {
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			sender.sendMessage("§cO evento n§o est§ acontecendo.");
			return;
		}
		statusType status = miniGladiador.getStatus();
		sender.sendMessage("");
		sender.sendMessage("§7Status§8: §f" + status);
		sender.sendMessage("§7Tempo§8: §f" + getInfoTime(miniGladiador));
		sender.sendMessage("");
		sender.sendMessage("§7Clans vivos§8: §f" + miniGladiadorManager.getClans(miniGladiador));
		sender.sendMessage("§7Jogadores vivos§8: §f" + miniGladiadorManager.getParticipants(miniGladiador));
		sender.sendMessage("");
		sender.sendMessage("§7Quantidade clans vivos§8: §f" + miniGladiadorManager.getClans());
		sender.sendMessage("§7Quantidade jogadores vivos§8: §f" + miniGladiadorManager.getPlayers());
		sender.sendMessage("");
	}
	
	private String getInfoTime(MiniGladiador miniGladiador) {
		String time = TimeFormater.formatOfEnd(miniGladiador.getTime());
		statusType status = miniGladiador.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.FECHADO) {
			return "Aguardando come§o do Evento.";
		} else if (status.name().contains("DEATHMATCH")) {
			return "Deathmatch j§ encontra-se em andamento.";
		}
		return time;
	}
	
	public void sendHelpCommands(CommandSender sender, boolean permission, String arg) {
		sender.sendMessage("");
		sender.sendMessage("§3[MiniGladiador] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§3 sair");
		sender.sendMessage("§7/" + arg + "§3 ajuda");
		if (permission) {
			sender.sendMessage("");
			sender.sendMessage("§7/" + arg + "§3 iniciar");
			sender.sendMessage("§7/" + arg + "§3 forcedeathmatch");
			sender.sendMessage("§7/" + arg + "§3 parar");
			sender.sendMessage("§7/" + arg + "§3 set (spawn | saida | deathmatch)");
			sender.sendMessage("§7/" + arg + "§3 info");
			sender.sendMessage("§7/" + arg + "§3 setkit");
		}
		sender.sendMessage("");
	}
}