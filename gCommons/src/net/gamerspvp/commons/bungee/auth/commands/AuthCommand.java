package net.gamerspvp.commons.bungee.auth.commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.auth.AuthProxyManager;
import net.gamerspvp.commons.bungee.auth.models.AuthPlayer;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.utils.DateUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AuthCommand extends Command {
	
	private ProxiedCommons instance;
	
	public AuthCommand(ProxiedCommons instance) {
		super("auth");
		this.instance = instance;
		instance.getProxy().getPluginManager().registerCommand(instance, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender.hasPermission("auth.admin"))) {
			sender.sendMessage(new TextComponent("§cSem permiss§o."));
			return;
		}
		if (args.length > 1) {
			AuthProxyManager authProxyManager = instance.getAuthManager();
			String playerName = args[1];
			instance.getProxy().getScheduler().runAsync(instance, new Runnable() {
				
				@Override
				public void run() {
					try {
						MySQL mysql = instance.getDataCenter().getMysql();
						if (args.length > 2) {
							if ((args[0].equalsIgnoreCase("register")) || (args[0].equalsIgnoreCase("registrar"))) {
								String password = args[2];
								AuthPlayer authPlayer = authProxyManager.getAuthPlayer(playerName);
								if (authPlayer != null) {
									sender.sendMessage(new TextComponent("§cEssa conta j§ possui registro."));
									return;
								}
								String hashedPassword = authProxyManager.passwordEncrypt(password);
								String query = "INSERT INTO auth (name, playerName, password, lastIp, registerDate) VALUES (?, ?, ?, ?, ?);";
								PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
								pstmt.setString(1, playerName.toLowerCase());
								pstmt.setString(2, playerName);
								pstmt.setString(3, hashedPassword);
								pstmt.setString(4, "");
								pstmt.setString(5, DateUtils.getHourAndDate());
								
								pstmt.execute();
								pstmt.close();
								ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(playerName);
								if (proxiedPlayer != null) {
									proxiedPlayer.disconnect(new TextComponent("§cEssa conta acaba de ser registrada!"));
								}
								sender.sendMessage(new TextComponent("§aA conta foi registrada com sucesso!"));
							} else if (args[0].equalsIgnoreCase("mudarsenha")) {
								String password = args[2];
								AuthPlayer authPlayer = authProxyManager.getAuthPlayer(playerName);
								if (authPlayer == null) {
									sender.sendMessage(new TextComponent("§cEssa conta n§o possui registro no servidor."));
									return;
								}
								String hashedPassword = authProxyManager.passwordEncrypt(password);
								String query = "UPDATE auth SET password=? WHERE name=?";
								PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
								pstmt.setString(1, hashedPassword);
								pstmt.setString(2, playerName);
								
								pstmt.executeUpdate();
								pstmt.close();
								ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(playerName);
								if (proxiedPlayer != null) {
									proxiedPlayer.disconnect(new TextComponent("§cEssa conta acaba de ter a senha alterada!"));
								}
								sender.sendMessage(new TextComponent("§aSenha da conta alterada com sucesso."));
							}
						}
						if ((args[0].equalsIgnoreCase("unregister")) || (args[0].equalsIgnoreCase("desregistrar"))) {
							AuthPlayer authPlayer = authProxyManager.getAuthPlayer(playerName);
							if (authPlayer == null) {
								sender.sendMessage(new TextComponent("§cEssa conta n§o possui registro no servidor."));
								return;
							}
							String query = "DELETE FROM auth WHERE name=?";
							PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
							pstmt.setString(1, playerName);
							
					        pstmt.executeUpdate();
					        pstmt.close();
					        ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(playerName);
							if (proxiedPlayer != null) {
								proxiedPlayer.disconnect(new TextComponent("§cEssa conta acaba de ser desregistrada!"));
							}
							sender.sendMessage(new TextComponent("§aA conta foi desregistrada com sucesso!"));
						}
					} catch (SQLException e) {
						sender.sendMessage(new TextComponent("§cDesculpe, mas n§o foi possivel efetuar a opera§§o."));
						e.printStackTrace();
					}
				}
			});
			return;
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Auth] Comandos dispon§veis:"));
		sender.sendMessage(new TextComponent("§7/auth §aregistrar (jogador) (senha)"));
		sender.sendMessage(new TextComponent("§7/auth §amudarsenha (jogador) (senha)"));
		sender.sendMessage(new TextComponent("§7/auth §adesregistrar (jogador)"));
		sender.sendMessage(new TextComponent(""));
		return;
	}
}