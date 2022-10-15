package dev.gamerspvp.gladiador;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import dev.gamerspvp.gladiador.database.SQLite;
import dev.gamerspvp.gladiador.dominar.DominarManager;
import dev.gamerspvp.gladiador.eventochat.EventoChatManager;
import dev.gamerspvp.gladiador.gladiador.GladiadorManager;
import dev.gamerspvp.gladiador.guerra.GuerraManager;
import dev.gamerspvp.gladiador.killer.KillerManager;
import dev.gamerspvp.gladiador.listeners.GlobalListeners;
import dev.gamerspvp.gladiador.locations.LocationsManager;
import dev.gamerspvp.gladiador.minigladiador.MiniGladiadorManager;
import dev.gamerspvp.gladiador.mito.MitoManager;
import dev.gamerspvp.gladiador.sumox1.SumoX1Manager;
import dev.gamerspvp.gladiador.topclans.ClanTopManager;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private SQLite SQLite;
	
	private Economy economy = null;
	private SimpleClans simpleClans;
	private WorldGuardPlugin worldGuard;
	
	private LocationsManager locationsManager;
	private DominarManager dominarManager;
	private MitoManager mitoManager;
	private ClanTopManager clanTopManager;
	
	private EventoChatManager eventoChatManager;
	private GladiadorManager gladiadorManager;
	private GuerraManager guerraManager;
	private KillerManager killerManager;
	private MiniGladiadorManager miniGladiadorManager;
	private SumoX1Manager sumoX1Manager;
	
	@Override
	public void onEnable() {
		instance = this;
		if (!(setupEconomy())) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (!setupSimpleClans()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if (!(setupWorldGuard())) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.SQLite = new SQLite("storage.db", instance);
		this.locationsManager = new LocationsManager(loadConfig("locations.yml"), instance);
		this.dominarManager = new DominarManager(loadConfig("dominar.yml"), instance);
		this.mitoManager = new MitoManager(loadConfig("mito.yml"), instance);
		this.clanTopManager = new ClanTopManager(instance);
		
		this.eventoChatManager = new EventoChatManager(loadConfig("eventochat.yml"), instance);
		this.gladiadorManager = new GladiadorManager(loadConfig("gladiador.yml"), instance);
		this.guerraManager = new GuerraManager(loadConfig("guerra.yml"), instance);
		this.killerManager = new KillerManager(loadConfig("killer.yml"), instance);
		this.miniGladiadorManager = new MiniGladiadorManager(loadConfig("minigladiador.yml"), instance);
		new GlobalListeners(instance);
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private FileConfiguration loadConfig(String arg) {
		File file = new File(getDataFolder(), arg);
		if (!file.exists()) {
			saveResource(arg, false);
			file = new File(getDataFolder(), arg);
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
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return getEconomy() != null;
    }
	
	private boolean setupSimpleClans() {
		if (getServer().getPluginManager().getPlugin("SimpleClans") == null) {
			return false;
		}
		simpleClans = (SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans");
		return getSimpleClans() != null;
	}
	
	public boolean setupWorldGuard() {
		if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			return false;
		}
		worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		return worldGuard != null;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public Economy getEconomy() {
		return economy;
	}
	
	public SimpleClans getSimpleClans() {
		return simpleClans;
	}
	
	public EventoChatManager getEventoChatManager() {
		return eventoChatManager;
	}
	
	public GladiadorManager getGladiadorManager() {
		return gladiadorManager;
	}
	
	public KillerManager getKillerManager() {
		return killerManager;
	}
	
	public MiniGladiadorManager getMiniGladiadorManager() {
		return miniGladiadorManager;
	}
	
	public LocationsManager getLocationsManager() {
		return locationsManager;
	}
	
	public SQLite getSQLite() {
		return SQLite;
	}
	
	public MitoManager getMitoManager() {
		return mitoManager;
	}

	public ClanTopManager getClanTopManager() {
		return clanTopManager;
	}
	
	public GuerraManager getGuerraManager() {
		return guerraManager;
	}
	
	public DominarManager getDominarManager() {
		return dominarManager;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}
	
	public SumoX1Manager getSumoX1Manager() {
		return sumoX1Manager;
	}
}