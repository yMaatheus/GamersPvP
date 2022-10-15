package dev.gamerspvp.punishments;

import org.bukkit.plugin.java.JavaPlugin;

import dev.gamerspvp.punishments.database.SQLite;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private SQLite SQLite;
	
	@Override
	public void onEnable() {
		instance = this;
		this.SQLite = new SQLite(instance);
		
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public SQLite getSQLite() {
		return SQLite;
	}
}