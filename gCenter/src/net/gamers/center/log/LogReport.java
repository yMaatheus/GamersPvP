package net.gamers.center.log;

import lombok.Getter;

@Getter
public class LogReport {
	
	private String type;
	private String author;
	private long date;
	
	private String key;
	private String playerName;
	private int days;
	
	public LogReport(String query, String author, String key, String playerName, int days) {
		this.type = query;
		this.author = author;
		this.date = System.currentTimeMillis();
		this.key = key;
		this.playerName = playerName;
		this.days = days;
	}
}