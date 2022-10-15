package net.gamerspvp.punish.network.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.database.MySQL;
import redis.clients.jedis.Jedis;

public class Banip {

	private String ip;
	private String reason;
	private String author;
	private String type;
	private Long time;
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
		Jedis jedis = instance.getRedis().getJedis();
		jedis.publish("banip", new Gson().toJson(this, Banip.class));
	}
	
	public void compute(net.gamerspvp.punish.bungee.Main instance) {
		MySQL mysql = instance.getMySQL();
		String query = "INSERT INTO bansip (Ip, Reason, Author, Type, Time, Date) VALUES (?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
			pstmt.setString(1, this.ip);
			pstmt.setString(2, this.reason);
			pstmt.setString(3, this.author);
			pstmt.setString(4, this.type);
			pstmt.setLong(5, this.time);
			pstmt.setString(6, this.date);
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