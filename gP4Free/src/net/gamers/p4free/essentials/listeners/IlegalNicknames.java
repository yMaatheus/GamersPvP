package net.gamers.p4free.essentials.listeners;

import java.util.HashSet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class IlegalNicknames implements Listener {
	
	private HashSet<String> nicks;
	
	public IlegalNicknames(HashSet<String> nicks) {
		this.nicks = nicks;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		String nick = event.getPlayer().getName().toLowerCase();
		if (!(nicks.contains(nick))) {
			return;
		}
		event.setKickMessage("Seu nickname está bloqueado de entrar no servidor, você precisar altera-lo para jogar!");
		event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
	}
}