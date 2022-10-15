package net.gamerspvp.central;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GlobalListeners implements Listener {
	
	public GlobalListeners(Main instance) {
		instance.getProxy().getPluginManager().registerListener(instance, this);
	}
	
	@EventHandler
	public void onLoginEvent(LoginEvent event) {
		event.setCancelReason(new TextComponent("§cNão é possivel entrar nesse servidor."));
		event.setCancelled(true);
	}
}