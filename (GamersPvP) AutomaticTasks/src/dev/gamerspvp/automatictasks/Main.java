package dev.gamerspvp.automatictasks;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.gamerspvp.automatictasks.shutdown.ShutdownManager;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private FileConfiguration fileConfiguration;
	
	private ShutdownManager shutdownManager;
	
	private TimeZone timeZone;
	private HashSet<Thread> threads;
	
	@Override
	public void onEnable() {
		instance = this;
		this.fileConfiguration = loadConfig("config.yml");
		this.shutdownManager = new ShutdownManager(instance);
		this.timeZone = TimeZone.getTimeZone("America/Sao_Paulo");
		this.threads = new HashSet<Thread>();
		for (String id : fileConfiguration.getConfigurationSection("Threads").getKeys(false)) {
			List<String> horarios = fileConfiguration.getStringList("Threads." + id + ".horarios");
			String command = fileConfiguration.getString("Threads." + id + ".executeCommand");
			for (String horario : horarios) {
				int day = Integer.parseInt(horario.split(":")[0]);
				int hour = Integer.parseInt(horario.split(":")[1]);
				int minute = Integer.parseInt(horario.split(":")[2]);
				threads.add(new Thread(day, hour, minute, command));
			}
		}
		if (!(threads.isEmpty())) {
			new CheckTask().runTaskTimerAsynchronously(instance, 60 * 20L, 55 * 20L);
		}
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
	
	public ShutdownManager getShutdownManager() {
		return shutdownManager;
	}
	
	public HashSet<Thread> getThreads() {
		return threads;
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}
	
	public static Main getInstance() {
		return instance;
	}
}