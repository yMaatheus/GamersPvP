package net.gamerspvp.commons.bungee.cargos.user;

import com.google.gson.Gson;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.listeners.custom.BungeeMessageEvent;
import net.gamerspvp.commons.network.models.User;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxiedUserListener implements Listener {
	
	private ProxiedCommons commons;
	
	public ProxiedUserListener(ProxiedCommons commons) {
		commons.getProxy().getPluginManager().registerListener(commons, this);
		this.commons = commons;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		String playerName = proxiedPlayer.getName();
		try {
			ProxiedCargosManager.getUserManager().loadUser(proxiedPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[Cargos] N§o foi possivel carregar do jogador: " + playerName + " ERROR: " + e.getMessage());
			proxiedPlayer.disconnect(new TextComponent("§c[Cargos] \nN§o § possivel conectar ao servidor momento, tente mais tarde."));
		}
	}
	
	@EventHandler
	public void onBungeeMessageEvent(BungeeMessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("cargo_reloadUser")) {
			Gson gson = commons.getDataCenter().getRedis().getGson();
			User user = gson.fromJson(event.getMessage(), User.class);
			ProxiedPlayer proxiedPlayer = commons.getProxy().getPlayer(user.getName());
			if (proxiedPlayer == null || (!proxiedPlayer.isConnected())) {
				return;
			}
			ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
			proxiedUserManager.reloadUser(proxiedPlayer, user);
			return;
		}
	}
}