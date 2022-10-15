package dev.gamerspvp.auth.auth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.auth.AuthBukkitManager;
import dev.gamerspvp.auth.auth.models.AuthPlayer;

public class LoginCommand extends Command {
	
	private Main instance;
	
	public LoginCommand(Main instance) {
		super("login");
		this.instance = instance;
		instance.registerCommand(this, "login");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		if (args.length > 0) {
			String password = args[0];
			AuthBukkitManager authManager = instance.getAuthManager();
			AuthPlayer authPlayer = authManager.getCache(playerName.toLowerCase());
			if (authPlayer == null) {
				return false;
			}
			if (authPlayer.isAuthenticated()) {
				player.sendMessage("§cVoc§ j§ est§ autenticado, bobinho!");
				return false;
			}
			if (!(authPlayer.isRegistered())) {
				player.sendMessage("§cVoc§ precisa registra-se antes.");
				return false;
			}
			if (!(authManager.checkPassword(authPlayer, password))) {
				if (authPlayer.getAttempts() > 3) {
					player.kickPlayer("§cVoc§ errou muitas vezes a sua senha!");
					return false;
				}
				authPlayer.setAttempts(authPlayer.getAttempts() + 1);
				authManager.authInfo(playerName, " Errou a senha!");
				player.sendMessage("§cSenha errada. Tente novamente!");
				return true;
			}
			sender.sendMessage("§aAnalisando...");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					String playerAddress = player.getAddress().getHostName();
					authPlayer.setAuthenticated(true);
					authPlayer.setLastIp(playerAddress);
					
					authManager.cache(authPlayer);
					authManager.sendMessageAuth(player);
					player.sendMessage("§aVoc§ foi autenticado com sucesso. Tenha um §timo jogo!");
					authPlayer.update(instance);
					authManager.authInfo(playerName, " Efetuou login!");
				}
			}.runTaskAsynchronously(instance);
			return true;
		}
		sender.sendMessage("§aEfetue login utilizando: §f/" + arg + " (senha).");
		return false;
	}
}