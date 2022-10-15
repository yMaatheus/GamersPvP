package net.gamerspvp.commons.bungee.auth;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.listeners.custom.BungeeMessageEvent;
import net.gamerspvp.commons.bungee.utils.BungeeTitleAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthProxyListener implements Listener {
	
	private ProxiedCommons instance;
	
	public AuthProxyListener(ProxiedCommons instance) {
		this.instance = instance;
		instance.getProxy().getPluginManager().registerListener(instance, this);
	}
	
	@EventHandler
	public void onServerMessageEvent(BungeeMessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("auth")) {
			String playerName = event.getMessage();
			AuthProxyManager authProxyManager = instance.getAuthManager();
			AuthProxy authProxy = authProxyManager.getCache(playerName);
			if (authProxy == null) {
				authProxy = new AuthProxy(playerName);
			}
			authProxy.setAuthenticated(true);
			authProxyManager.cache(authProxy);
			ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(playerName);
			if (proxiedPlayer == null) {
				return;
			}
			try {
				BungeeTitleAPI.sendTitle(proxiedPlayer, "§2§lGAMERSPVP", "§fAutenticado!", 20, 20, 20);
				authProxyManager.getAuthQueueManager().add(proxiedPlayer);
			} catch (Exception e) {
				e.printStackTrace();
				proxiedPlayer.disconnect(new TextComponent("§c[Auth] \nN§o foi possivel processar o seu envio para o Sagu§o, contate um administrador."));
			}
		}
	}
	
	@EventHandler
	public void onServerConnectEvent(ServerConnectedEvent event) {
		ServerInfo serverInfo = event.getServer().getInfo();
		String serverName = serverInfo.getName();
		if (!serverName.contains("lobby")) {
			return;
		}
		instance.getAuthManager().getAuthQueueManager().remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String playerName = player.getName();
		AuthProxyManager authProxyManager = instance.getAuthManager();
		AuthProxy proxyAuthPlayer = authProxyManager.getCache(playerName);
		if (proxyAuthPlayer == null) {
			return;
		}
		proxyAuthPlayer.setAuthenticated(false);
		authProxyManager.cache(proxyAuthPlayer);
	}
	
	@EventHandler
	public void onChatEvent(ChatEvent event) {
		String message = event.getMessage().toLowerCase();
		if (!(event.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String playerName = player.getName();
		AuthProxyManager authProxyManager = instance.getAuthManager();
		AuthProxy proxyAuthPlayer = authProxyManager.getCache(playerName);
		if (proxyAuthPlayer != null) {
			if (proxyAuthPlayer.isAuthenticated()) {
				return;
			}
		}
		String[] allowedCmds = new String[] { "/login", "/logar", "/register", "/registrar" };
		boolean blocked = true;
		for (String cmd : allowedCmds) {
			if (message.startsWith(cmd + " ") || message.equalsIgnoreCase(cmd)) {
				blocked = false;
			}
		}
		if (blocked) {
			event.setCancelled(true);
			player.sendMessage(new TextComponent("§cVoc§ precisa estar autenticado para fazer isso."));
		}
	}
}