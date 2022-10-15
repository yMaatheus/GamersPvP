package dev.gamerspvp.punishments.punish.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import dev.gamerspvp.punishments.Main;

public class Mute {

	private String name;
	private String playerRealName;
	private Boolean muteall;
	private String reason;
	private String author;
	private Long   time;
	private String date;
	
	public Mute(String playerRealName, Boolean muteall, String reason, String author, Long time, String date) {
		this.name = playerRealName.toLowerCase();
		this.playerRealName = playerRealName;
		this.muteall = muteall;
		this.reason = reason;
		this.author = author;
		this.time = time;
		this.date = date;
	}
	
	public void execute(Main instance) {
		Bukkit.broadcastMessage("");
		if (isMuteall()) {
			Bukkit.broadcastMessage("§c(!) " + this.getPlayerRealName() + " foi silenciado em §lTODOS §cos chat's §cpor " + this.getAuthor());
		} else {
			Bukkit.broadcastMessage("§c(!) " + this.getPlayerRealName() + " foi silenciado nos chat's(global, local) §cpor " + this.getAuthor());
		}
		//Bukkit.broadcastMessage("§c(!) Duração: " + TimeManager.getTimeEnd(this.getTime()));
		Bukkit.broadcastMessage("§c(!) Motivo: " + this.getReason());
		Bukkit.broadcastMessage("§c(!) Data: " + this.getDate());
		Bukkit.broadcastMessage("");
		//Arrays.getMutes().put(this.getName(), this);
		try {
			String query = "INSERT INTO mutes (Name, PlayerRealName, MuteAll, Reason, Author, Time, Date) VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = instance.getSQLite().getConnection().prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, playerRealName);
			pstmt.setBoolean(3, muteall);
			pstmt.setString(4, reason);
			pstmt.setString(5, author);
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
	
	public boolean isMuteall() {
		return muteall;
	}
	
	public void setMuteall(boolean muteall) {
		this.muteall = muteall;
	}
}