package net.gamerspvp.central.statusservers.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServerStatus {
	
	private String serverName;
	private int online;
	private int maxPlayers;
	private String serverIp;
	private int serverPort;
	private statusServer status;
	
	public ServerStatus(String serverName) {
		this.serverName = serverName;
		this.online = 0;
		this.serverIp = "";
		this.serverPort = 0;
		this.maxPlayers = 0;
		this.status = statusServer.OFFLINE;
	}
	
	public enum statusServer {
		ONLINE, MANUTENÇÃO, OFFLINE;
	}
}