package net.gamers.center;

import lombok.Getter;

@Getter
public class MessageEvent {
	
	private String channel;
	private String message;
	
	public MessageEvent(String channel, String json) {
		this.channel = channel;
		this.message = json;
	}
}