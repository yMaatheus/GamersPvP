package net.gamerspvp.commons.bungee.listeners.custom;

import net.md_5.bungee.api.plugin.Event;

public class BungeeMessageEvent extends Event {
	
	private final String channel;
	private final String json;
	
	public BungeeMessageEvent(String channel, String json) {
		this.channel = channel;
		this.json = json;
	}
	
	public String getMessage() {
		return json;
	}
	
	public String getChannel() {
		return channel;
	}
}