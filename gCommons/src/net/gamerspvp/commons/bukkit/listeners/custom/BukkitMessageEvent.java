package net.gamerspvp.commons.bukkit.listeners.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitMessageEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private String json;
	private String channel;
	
	public BukkitMessageEvent(String channel, String json) {
		this.channel = channel;
		this.json = json;
	}
	
	public String getMessage() {
        return json;
    }
	
	public String getChannel() {
		return channel;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}