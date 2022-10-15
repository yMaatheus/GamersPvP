package net.gamerspvp.punish.network.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.database.MySQL;
import redis.clients.jedis.Jedis;

public class Ban {

	private String name;
	private String playerRealName;
	private String reason;
	private String author;
	private String type;
	private Long time;
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
		Jedis jedis = instance.getRedis().getJedis();
		jedis.publish("ban", new Gson().toJson(this, Ban.class));
	}
	
	public void compute(net.gamerspvp.punish.bungee.Main instance) {
		MySQL mysql = instance.getMySQL();
		String query = "INSERT INTO bans (Name, PlayerRealName, Reason, Author, Type, Time, Date) VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
			pstmt.setString(1, this.name);
			pstmt.setString(2, this.playerRealName);
			pstmt.setString(3, this.reason);
			pstmt.setString(4, this.author);
			pstmt.setString(5, this.type);
			pstmt.setLong(6, this.time);
			pstmt.setString(7, this.date);
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