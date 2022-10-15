package dev.gamerspvp.auth.captcha.models;

import java.util.concurrent.TimeUnit;

import org.bukkit.inventory.Inventory;

public class CaptchaPlayer {
	
	private String playerName;
	private Inventory inventory;
	
	private boolean concluded;
	private long time;
	private long closeTimestamp;
	
	public CaptchaPlayer(String playerName, Inventory inventory) {
		this.playerName = playerName;
		this.inventory = inventory;
		this.concluded = false;
		this.time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(120);
		this.closeTimestamp = System.currentTimeMillis();
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public boolean isConcluded() {
		return concluded;
	}
	
	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}
	
	public long getTime() {
		return time;
	}
	
	public long getCloseTimestamp() {
		return closeTimestamp;
	}
	
	public void setCloseTimestamp(long closeTimestamp) {
		this.closeTimestamp = closeTimestamp;
	}
}