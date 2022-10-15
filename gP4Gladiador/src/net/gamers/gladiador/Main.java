package net.gamers.gladiador;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.gamers.gladiador.gladiador.GladiadorManager;
import net.gamers.gladiador.minigladiador.MiniGladiadorManager;
import net.gamers.gladiador.mito.MitoManager;
import net.gamers.gladiador.utils.LocationsManager;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.utils.BukkitConfig;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

@Getter
public class Main extends JavaPlugin {
	
	@Getter
	private static Main instance;
	
	private CommonsBukkit commons;
	private SimpleClans simpleClans;
	
	private LocationsManager locationsManager;
	
	private GladiadorManager gladiadorManager;
	private MiniGladiadorManager miniGladiadorManager;
	private MitoManager mitoManager;
	
	@Override
	public void onEnable() {
		instance = this;
		this.commons = CommonsBukkit.getInstance();
		if (!setupSimpleClans()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.locationsManager = new LocationsManager(BukkitConfig.loadConfig("locations.yml", this), this);
		
		this.gladiadorManager = new GladiadorManager(BukkitConfig.loadConfig("gladiador.yml", this), this);
		this.miniGladiadorManager = new MiniGladiadorManager(BukkitConfig.loadConfig("minigladiador.yml", this), this);
		this.mitoManager = new MitoManager(BukkitConfig.loadConfig("mito.yml", this), this);
		new GlobalListeners(this);
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private boolean setupSimpleClans() {
		if (getServer().getPluginManager().getPlugin("SimpleClans") == null) {
			return false;
		}
		simpleClans = (SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans");
		return getSimpleClans() != null;
	}
}