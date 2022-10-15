package dev.gamerspvp.automatictasks.shutdown;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PreServerShutdownEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}