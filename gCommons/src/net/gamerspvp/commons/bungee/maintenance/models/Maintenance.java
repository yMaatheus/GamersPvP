package net.gamerspvp.commons.bungee.maintenance.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Maintenance {
	
	private String reason;
	private String player;
	private Long start;
	private String estimatedTime;
	
	public Maintenance(String reason, String player, Long start, String estimatedTime) {
		this.reason = reason;
		this.player = player;
		this.start = start;
		this.estimatedTime = estimatedTime;
	}
}