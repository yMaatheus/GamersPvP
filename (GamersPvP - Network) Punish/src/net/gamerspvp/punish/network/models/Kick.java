package net.gamerspvp.punish.network.models;

import com.google.gson.Gson;

import net.gamerspvp.punish.bukkit.Main;
import redis.clients.jedis.Jedis;

public class Kick {

	private String name;
	private String playerRealName;
	private String reason;
	private String author;
	private String date;
	
	public Kick(String playerRealName, String reason, String author, String date) {
		this.name = playerRealName.toLowerCase();
		this.playerRealName = playerRealName;
		this.reason = reason;
		this.author = author;
		this.date = date;
	}
	
	public void execute(Main instance) {
		Jedis jedis = instance.getRedis().getJedis();
		jedis.publish("kick", new Gson().toJson(this, Kick.class));
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
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}