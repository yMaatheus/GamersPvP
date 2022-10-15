package net.gamers.p4free;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.gamers.p4free.game.GameListener;

public class GlobalListeners implements Listener {
	
	private Main instance;
	
	public GlobalListeners(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		instance.getScoreboardManager().onPlayerJoinEvent(event);
		GameListener.onPlayerJoinEvent(event);
	}
}