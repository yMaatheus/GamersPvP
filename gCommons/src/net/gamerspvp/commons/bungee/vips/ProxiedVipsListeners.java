package net.gamerspvp.commons.bungee.vips;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxiedVipsListeners implements Listener {
	
	public ProxiedVipsListeners(ProxiedCommons commons) {
		commons.getProxy().getPluginManager().registerListener(commons, this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		String playerName = proxiedPlayer.getName();
		try {
			ProxiedVipsManager.loadPlayerVip(proxiedPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[Vips] N§o foi possivel carregar do jogador: " + playerName + " ERROR: " + e.getMessage());
			proxiedPlayer.disconnect(new TextComponent("§c[Vips] \nN§o § possivel conectar ao servidor momento, tente mais tarde."));
		}
	}
}