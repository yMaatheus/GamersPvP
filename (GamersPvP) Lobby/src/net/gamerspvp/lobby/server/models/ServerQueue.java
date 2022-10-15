package net.gamerspvp.lobby.server.models;

import java.util.HashMap;

import org.bukkit.entity.Player;

import lombok.Getter;

@Getter
public class ServerQueue {
	
	private String serverName;
	private HashMap<Player, Integer> queue;
	
	public ServerQueue(String serverName) {
		this.serverName = serverName;
		this.queue = new HashMap<Player, Integer>();
	}
}