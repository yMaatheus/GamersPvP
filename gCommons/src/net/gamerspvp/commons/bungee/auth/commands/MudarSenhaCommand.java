package net.gamerspvp.commons.bungee.auth.commands;

import java.sql.PreparedStatement;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.auth.AuthProxyManager;
import net.gamerspvp.commons.bungee.auth.models.AuthPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MudarSenhaCommand extends Command {
	
	private ProxiedCommons instance;
	
	public MudarSenhaCommand(ProxiedCommons instance) {
		super("mudarsenha");
		this.instance = instance;
		instance.getProxy().getPluginManager().registerCommand(instance, this);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		String playerName = proxiedPlayer.getName();
		if (args.length > 1) {
			AuthProxyManager authProxyManager = instance.getAuthManager();
			String password = args[0];
			String currentPassword = args[1];
			if (password.length() < 5) {
				sender.sendMessage(new TextComponent("§cA nova senha escolhida n§o § segura o suficiente."));
				sender.sendMessage(new TextComponent("§cO perigo olha para voc§ e sente medo!"));
				return;
			}
			instance.getProxy().getScheduler().runAsync(instance, new Runnable() {
				
				@Override
				public void run() {
					try {
						AuthPlayer authPlayer = authProxyManager.getAuthPlayer(playerName);
						if (authPlayer == null) {
							sender.sendMessage(new TextComponent("§cEssa conta n§o possui registro no servidor."));
							return;
						}
						if (!(authProxyManager.checkPassword(authPlayer, currentPassword))) {
							sender.sendMessage(new TextComponent("§cSua senha atual est§ incorreta. Drogas fazem mal!"));
							return;
						}
						String hashedPassword = authProxyManager.passwordEncrypt(password);
						String query = "UPDATE auth SET password=? WHERE name=?";
						PreparedStatement pstmt = instance.getDataCenter().getMysql().getConnection().prepareStatement(query);
						pstmt.setString(1, hashedPassword);
						pstmt.setString(2, playerName.toLowerCase());
						
						pstmt.executeUpdate();
						pstmt.close();
						sender.sendMessage(new TextComponent("§aSenha da conta alterada com sucesso."));
					} catch (Exception e) {
						sender.sendMessage(new TextComponent("§cDesculpe, mas n§o foi possivel executar a tarefa."));
					}
				}
			});
			return;
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Auth] Comandos dispon§veis:"));
		sender.sendMessage(new TextComponent("§7/mudarsenha §a(nova senha) (senha)"));
		sender.sendMessage(new TextComponent(""));
	}
}