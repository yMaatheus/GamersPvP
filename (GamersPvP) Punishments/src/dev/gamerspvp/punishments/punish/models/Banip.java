package dev.gamerspvp.punishments.punish.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.gamerspvp.punishments.Main;

public class Banip {
	
	private String ip;
	private String reason;
	private String author;
	private String type;
	private Long   time;
	private String date;
	
	public Banip(String ip, String reason, String author, String type, Long time, String date) {
		this.ip = ip;
		this.reason = reason;
		this.author = author;
		this.type = type;
		this.time = time;
		this.date = date;
	}
	
	public void execute(Main instance) {
		try {
			String query = "INSERT INTO bansip (Ip, Reason, Author, Type, Time, Date) VALUES (?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = instance.getSQLite().getConnection().prepareStatement(query);
			pstmt.setString(1, ip);
			pstmt.setString(2, reason);
			pstmt.setString(3, author);
			pstmt.setString(4, type);
			pstmt.setLong(5, time);
			pstmt.setString(6, date);
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
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