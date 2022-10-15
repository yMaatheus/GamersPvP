package net.gamerspvp.commons.bukkit.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class BukkitConfig {
	
	public static FileConfiguration loadConfig(String arg, Plugin instance) {
		File file = new File(instance.getDataFolder(), arg);
		if (!file.exists()) {
			instance.saveResource(arg, false);
			file = new File(instance.getDataFolder(), arg);
		}
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static void saveConfig(String fileName, FileConfiguration config, Plugin instance) {
		try {
			config.save(new File(instance.getDataFolder(), fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}