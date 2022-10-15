package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.gamerspvp.fullpvp.Main;

public class EventosCommand extends Command {
	
	private Main instance;
	
	public EventosCommand(Main instance) {
		super("eventos");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		sender.sendMessage(instance.getEssentialsManager().getEventos());
		/*sender.sendMessage("");
		sender.sendMessage("§aTarefas autom§ticas: ");
		sender.sendMessage("");
		sender.sendMessage("§eDiariamente §8- §a06:00§f, §a17:00§f, §a01:00 §8- §fReinicio");
		sender.sendMessage("§eDiariamente §8- §a09:00§f, §a16:00 §8- §fEvento Parkour");
		sender.sendMessage("§eDiariamente §8- §a09:20§f, §a16:20 §8- §fEvento Corrida");
		sender.sendMessage("§eDiariamente §8- §a09:30§f, §a16:30 §8- §fEvento Guerreiro");
		sender.sendMessage("§eDiariamente §8- §a10:30§f, §a14:30§f, §a18:30 §8- §fEvento Fight");
		sender.sendMessage("§eDiariamente §8- §a15:00§f, §a19:30 §8- §fEvento MiniGladiador");
		sender.sendMessage("§eSexta §8- §a19:30 §8- §fEvento Gladiador");
		sender.sendMessage("");*/
		return false;
	}
}