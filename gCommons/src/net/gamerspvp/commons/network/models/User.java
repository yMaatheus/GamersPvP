package net.gamerspvp.commons.network.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	
	private String name;
	private String playerName;
	private String group;
	private List<String> departaments;
	private HashSet<String> permissions;
	private long firstLogin;
	private long lastLogin;
	
	public User(String playerName, String group, List<String> departaments, HashSet<String> permissions, long firstLogin, long lastLogin) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.group = group;
		this.departaments = departaments;
		this.permissions = permissions;
		this.firstLogin = firstLogin;
		this.lastLogin = lastLogin;
	}
	
	public User(String playerName, String group, long firstLogin, long lastLogin) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.group = group;
		this.departaments = new ArrayList<String>();
		this.permissions = new HashSet<String>();
		this.firstLogin = firstLogin;
		this.lastLogin = lastLogin;
	}
	
	public User(String playerName, String group) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.group = group;
		this.departaments = new ArrayList<String>();
		this.permissions = new HashSet<String>();
		this.firstLogin = System.currentTimeMillis();
		this.lastLogin = System.currentTimeMillis();
	}
}