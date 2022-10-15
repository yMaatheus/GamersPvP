package net.gamers.center.games.status.models;

import java.util.*;

import lombok.*;

@Getter @Setter
public class GameStatus {
	
	private String name;
	private int maxPlayers, online;
	private gameStatus status;
	private ArrayList<Server> welcomeServers;
	private HashMap<String, Server> servers;
	
	public GameStatus(String name) {
		this.name = name;
		this.maxPlayers = 0;
		this.online = 0;
		this.status = gameStatus.OFFLINE;
		this.welcomeServers = new ArrayList<>();
		this.servers = new HashMap<>();
	}
	
	public Server server(String serverName, String address, int port, int maxPlayers) {
		return new Server(serverName, address, port, maxPlayers);
	}
	
	public enum gameStatus {
		ONLINE, MANUTENÇÃO, OFFLINE;
	}
	
	@Getter @Setter
	public class Server {
		
		private String serverName, address;
		private int port, online, maxPlayers;
		
		public Server(String serverName, String address, int port, int maxPlayers) {
			this.serverName = serverName;
			this.address = address;
			this.port = port;
			this.online = 0;
			this.maxPlayers = maxPlayers;
		}
	}
}