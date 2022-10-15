package net.gamerspvp.commons.network.log;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Log {
	
	private String key;
	private String playerName;
	private String text;
	private String reason;
	
	public Log(String key, String playerName, String text, String reason) {
		this.key = key;
		this.playerName = playerName;
		this.text = text;
		this.reason = reason;
	}
	
	public Log(String key, String playerName, String text) {
		this.key = key;
		this.playerName = playerName;
		this.text = text;
		this.reason = "";
	}
}