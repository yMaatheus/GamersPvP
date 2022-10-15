package net.gamerspvp.punish.network.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.database.MySQL;
import net.gamerspvp.punish.network.utils.TimeManager;

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
	
	public void execute(Main instance) throws SQLException {
		MySQL mysql = instance.getMySQL();
		Bukkit.broadcastMessage("");
		if (isMuteall()) {
			Bukkit.broadcastMessage("§c(!) " + this.getPlayerRealName() + " foi silenciado em §lTODOS §cos chat's §cpor " + this.getAuthor());
		} else {
			Bukkit.broadcastMessage("§c(!) " + this.getPlayerRealName() + " foi silenciado nos chat's(global, local) §cpor " + this.getAuthor());
		}
		Bukkit.broadcastMessage("§c(!) Dura§§o: " + TimeManager.getTimeEnd(this.getTime()));
		Bukkit.broadcastMessage("§c(!) Motivo: " + this.getReason());
		Bukkit.broadcastMessage("§c(!) Data: " + this.getDate());
		Bukkit.broadcastMessage("");
		instance.getMutes().put(name, this);
		String query = "INSERT INTO mutes (Name, PlayerRealName, MuteAll, Reason, Author, Time, Date) VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
		pstmt.setString(1, this.name);
		pstmt.setString(2, this.playerRealName);
		pstmt.setBoolean(3, this.muteall);
		pstmt.setString(4, this.reason);
		pstmt.setString(5, this.author);
		pstmt.setLong(6, this.time);
		pstmt.setString(7, this.date);
		pstmt.execute();
		pstmt.close();
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