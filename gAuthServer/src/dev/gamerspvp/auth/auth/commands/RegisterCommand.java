package dev.gamerspvp.auth.auth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.auth.AuthBukkitManager;
import dev.gamerspvp.auth.auth.models.AuthPlayer;
import net.gamerspvp.commons.network.utils.DateUtils;

public class RegisterCommand extends Command {
	
	private Main instance;
	
	public RegisterCommand(Main instance) {
		super("register");
		this.instance = instance;
		instance.registerCommand(this, "register");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		if (args.length > 1) {
			String password = args[0];
			String confirmPassword = args[1];
			if (!(password.equals(confirmPassword))) {
				sender.sendMessage("§cA senha de confirmação não é igual a outra. Tenha mais atenção!");
				return false;
			}
			AuthBukkitManager authManager = instance.getAuthManager();
			if (password.length() < 5) {
				sender.sendMessage("§cA senha escolhida não é segura o suficiente.");
				return false;
			}
			if (password.equals("segura") || password.equals("seguraosuficiente")) {
				sender.sendMessage("§cAgora, literalmente, a senha é segura kkkk");
			}
			AuthPlayer authPlayer = authManager.getCache(playerName.toLowerCase());
			if (authPlayer == null) {
				return false;
			}
			if (authPlayer.isRegistered()) {
				sender.sendMessage("§cVocê já está registrado. Utilize /logar (senha).");
				return false;
			}
			sender.sendMessage("§aConcluindo registro...");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					String playerAddress = player.getAddress().getHostName();
					String hashedPassword = authManager.passwordEncrypt(password);
					
					authPlayer.setPassword(authPlayer.password(hashedPassword));
					authPlayer.setAuthenticated(true);
					authPlayer.setRegistered(true);
					authPlayer.setRegisterDate(DateUtils.getHourAndDate());
					authPlayer.setLastIp(playerAddress);
					
					authManager.cache(authPlayer);
					authManager.sendMessageAuth(player);
					player.sendMessage("\n §aCadastro efetuado com sucesso! Seja bem vindo ao servidor, divirta-se e não nós esqueça! \n");
					authPlayer.register(instance);
					authManager.authInfo(playerName, " Registrou-se!");
				}
			}.runTaskAsynchronously(instance);
			return true;
		}
		sender.sendMessage("§aEfetue seu cadastro utilizando: §f/" + arg + " (senha) (senha)");
		return false;
	}
}