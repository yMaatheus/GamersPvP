package dev.gamerspvp.auth.auth.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.auth.AuthBukkitManager;
import dev.gamerspvp.auth.auth.models.AuthPlayer;

public class ForceLoginCommand extends Command {
	
	private Main instance;
	
	public ForceLoginCommand(Main instance) {
		super("forcelogin");
		this.instance = instance;
		instance.registerCommand(this, "forcelogin");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof ConsoleCommandSender)) {
			return false;
		}
		if (args.length > 0) {
			AuthBukkitManager authBukkitManager = instance.getAuthManager();
			String playerName = args[0];
			AuthPlayer authPlayer = authBukkitManager.getCache(playerName.toLowerCase());
			Player player = Bukkit.getPlayer(playerName);
			if (authPlayer == null) {
				sender.sendMessage("§cJogador offline.");
				return false;
			}
			if (player == null) {
				sender.sendMessage("§cAutenticação inválida.");
				return false;
			}
			authPlayer.setAuthenticated(true);
			authBukkitManager.cache(authPlayer);
			authBukkitManager.sendMessageAuth(player);
			player.sendMessage("§aVocê foi autenticado com sucesso. Tenha um ótimo jogo!");
			authBukkitManager.authInfo(playerName, " Efetuou login! (Forçado por " + sender.getName() + ")");
			return false;
		}
		sender.sendMessage("§aComandos disponíveis:");
		sender.sendMessage("§7/" + arg + " §a (jogador).");
		return false;
	}
}