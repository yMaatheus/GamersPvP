package dev.gamerspvp.fullpvp;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import dev.gamerspvp.fullpvp.anticheat.AntiCheatManager;
import dev.gamerspvp.fullpvp.database.SQLite;
import dev.gamerspvp.fullpvp.economy.EconomyManager;
import dev.gamerspvp.fullpvp.economy.VaultHandler;
import dev.gamerspvp.fullpvp.essentials.EssentialsManager;
import dev.gamerspvp.fullpvp.kitsgui.KitsGuiManager;
import dev.gamerspvp.fullpvp.locations.LocationsManager;
import dev.gamerspvp.fullpvp.sword.SwordManager;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private static VaultHandler vault;
	private WorldGuardPlugin worldGuard;
	
	private SQLite sqlite;
	private LocationsManager locationsManager;
	
	private AntiCheatManager antiCheatManager;
	private EconomyManager economyManager;
	private EssentialsManager essentialsManager;
	private KitsGuiManager kitsGuiManager;
	private SwordManager swordManager;
	
	@Override
	public void onEnable() {
		instance = this;
		setupVault();
		if (!(setupWorldGuard())) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.sqlite = new SQLite("storage.db", this);
		this.locationsManager = new LocationsManager(loadConfig("locations.yml"), this);
		
		//this.antiCheatManager = new AntiCheatManager(this);
		this.economyManager = new EconomyManager(this);
		this.essentialsManager = new EssentialsManager(this);
		this.kitsGuiManager = new KitsGuiManager(this);
		//this.swordManager = new SwordManager(this);
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public FileConfiguration loadConfig(String arg0) {
		File file = new File(getDataFolder(), arg0);
		if (!file.exists()) {
			saveResource(arg0, false);
			file = new File(getDataFolder(), arg0);
		}
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public void registerCommand(Command command, String... allys) {
		try {
			List<String> Aliases = new ArrayList<String>();
			for (String s : allys) {
				Aliases.add(s);
			}
			command.setAliases(Aliases);
			Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			cmap.setAccessible(true);
			CommandMap map = (CommandMap) cmap.get(Bukkit.getServer());
			map.register(command.getName(), getDescription().getName(), command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupVault() {
		vault = new VaultHandler();
		getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, getVault(), this, ServicePriority.Highest);
	}
	
	public boolean setupWorldGuard() {
		if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			return false;
		}
		worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		return worldGuard != null;
	}
	
	public static VaultHandler getVault() {
		return vault;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public EconomyManager getEconomyManager() {
		return economyManager;
	}
	
	public EssentialsManager getEssentialsManager() {
		return essentialsManager;
	}
	
	public KitsGuiManager getKitsGuiManager() {
		return kitsGuiManager;
	}
	
	public SwordManager getSwordManager() {
		return swordManager;
	}
	
	public SQLite getSQLite() {
		return sqlite;
	}
	
	public LocationsManager getLocationsManager() {
		return locationsManager;
	}
	
	public AntiCheatManager getAntiCheatManager() {
		return antiCheatManager;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}
}