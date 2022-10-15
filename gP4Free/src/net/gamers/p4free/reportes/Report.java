package net.gamers.p4free.reportes;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Report {
	
	private String playerName;
	private Player player;
	private HashMap<String, String> reporters; // Denunciador | Denuncia
	private int reports;
	
	public Report(Player player) {
		this.playerName = player.getName();
		this.player = player;
		this.reporters = new HashMap<String, String>();
		this.reports = 0;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public HashMap<String, String> getReporters() {
		return reporters;
	}
	
	public int getReports() {
		return reports;
	}
	
	public void setReports(int reports) {
		this.reports = reports;
	}
}