package net.gamers.lobby.games.models;

import org.bukkit.entity.Player;

import lombok.*;

@Getter @Setter
public class PlayerQueue {
	
	private Player player;
	private long flagTime;
	private int position;
	
	public PlayerQueue(Player player, int position) {
		this.player = player;
		this.flagTime = System.currentTimeMillis();
		this.position = position;
	}
}