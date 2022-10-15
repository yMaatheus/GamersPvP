package net.gamerspvp.commons.bungee.redisbungee;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RedisBungeeListener implements Listener {
	
	public RedisBungeeListener() {
		
	}
	
	@EventHandler
	public void onLoginEvent(LoginEvent event) {
		//verificar se já existe um jogador com o nick
	}
	 
	@EventHandler
	public void onPostLoginEvent(PostLoginEvent event) {
		//contabilizar entrada de um novo jogador na network
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
		//contabilizar saida de um novo jogador
	}
}