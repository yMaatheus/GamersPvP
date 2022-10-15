package net.gamerspvp.lobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.utils.BukkitConfig;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.lobby.npcs.NpcManager;
import net.gamerspvp.lobby.server.ServerManager;

@Getter
public class Main extends JavaPlugin {
	
	private static Main instance;
	private CommonsBukkit commons;
	private DataCenterManager dataCenterManager;
	
	private NpcManager npcManager;
	
	private ServerManager serverManager;
	
	@Override
	public void onEnable() {
		instance = this;
		try {
			if (getServer().getPluginManager().getPlugin("gCommons") == null) {
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			if ((getServer().getPluginManager().getPlugin("Citizens") == null) || (!getServer().getPluginManager().getPlugin("Citizens").isEnabled())) {
				getServer().getPluginManager().disablePlugin(this);	
				return;
			}
			this.commons = CommonsBukkit.getInstance();
			this.dataCenterManager = commons.getDataCenter();
			
			this.npcManager = new NpcManager(BukkitConfig.loadConfig("npcs.yml", this), this);
			
			this.serverManager = new ServerManager(this);
			new GlobalListeners(this);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
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
}