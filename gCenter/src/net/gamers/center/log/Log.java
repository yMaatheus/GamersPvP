package net.gamers.center.log;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Log {
	
	private String date;
	private String key;
	private String playerName;
	private String text;
	private String reason;
	private long flagTime;
	
	public Log(String date, String key, String playerName, String text, String reason, long flagTime) {
		this.date = date;
		this.key = key;
		this.playerName = playerName;
		this.text = text;
		this.reason = reason;
		this.flagTime = flagTime;
	}
	
	public Log(String key, String playerName, String text, String reason) {
		this.date = "";
		this.key = key;
		this.playerName = playerName;
		this.text = text;
		this.reason = reason;
		this.flagTime = 0;
	}
	
	public Log(String key, String playerName, String text) {
		this.date = "";
		this.key = key;
		this.playerName = playerName;
		this.text = text;
		this.reason = "";
		this.flagTime = 0;
	}
}