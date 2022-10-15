package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.essentials.EssentialsManager;

public class CloseTellCommand extends Command {
	
	private Main instance;
	
	public CloseTellCommand(Main instance) {
		super("closetell");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		if (args.length > 0) {
			EssentialsManager essentialsManager = instance.getEssentialsManager();
			if (args.length > 1) {
				if (args[0].equalsIgnoreCase("ativar")) {
					StringBuilder reason = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						reason.append(args[i]).append(" ");
					}
					essentialsManager.executeCloseTell(playerName, reason.toString());
					sender.sendMessage("§aVocê fechou o recebimento de mensagens privadas pelo motivo: §f" + reason.toString());
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("desativar")) {
				essentialsManager.removeCloseTell(playerName);
				sender.sendMessage("§aSuas mensagens privadas foram ativadas com sucesso.");
				return true;
			}
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Essentials] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a ativar (motivo)");
		sender.sendMessage("§7/" + arg + "§a desativar");
		return false;
	}
}