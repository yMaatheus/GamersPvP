package dev.gamerspvp.punishments.punish.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.gamerspvp.punishments.Main;

public class Ban {
	
	private String name;
	private String playerRealName;
	private String reason;
	private String author;
	private String type;
	private Long   time;
	private String date;
	
	public Ban(String playerRealName, String reason, String author, String type, Long time, String date) {
		this.name = playerRealName.toLowerCase();
		this.playerRealName = playerRealName;
		this.reason = reason;
		this.author = author;
		this.type = type;
		this.time = time;
		this.date = date;
	}
	
	public void execute(Main instance) {
		try {
			String query = "INSERT INTO bans (Name, PlayerRealName, Reason, Author, Type, Time, Date) VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = instance.getSQLite().getConnection().prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, playerRealName);
			pstmt.setString(3, reason);
			pstmt.setString(4, author);
			pstmt.setString(5, type);
			pstmt.setLong(6, time);
			pstmt.setString(7, date);
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPlayerRealName() {
		return playerRealName;
	}
	
	public void setPlayerRealName(String playerRealName) {
		this.playerRealName = playerRealName;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}