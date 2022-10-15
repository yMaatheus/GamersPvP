package net.gamerspvp.commons.network.models;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerVip {
	
	private String name;
	private String playerName;
	private HashMap<String, Long> vips;
	
	public PlayerVip(String playerName) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.vips = new HashMap<String, Long>();
	}
	
	public PlayerVip(String playerName, HashMap<String, Long> vips) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.vips = vips;
	}
}