package dev.gamerspvp.fullpvp.locations;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import dev.gamerspvp.fullpvp.Main;

public class LocationsManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	public LocationsManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
	}
	
	public void setLocation(String category, String locationName, Location location) {
		config.set(category + "." + locationName + ".world", location.getWorld().getName());
		config.set(category + "." + locationName + ".x", location.getX());
		config.set(category + "." + locationName + ".y", location.getY());
		config.set(category + "." + locationName + ".z", location.getZ());
		config.set(category + "." + locationName + ".yaw", location.getYaw());
		config.set(category + "." + locationName + ".pitch", location.getPitch());
		saveConfig();
	}
	
	public Location get(String category, String locationName) {
		String worldName = config.getString(category + "." + locationName + ".world");
		if (worldName == null) {
			return null;
		}
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			return null;
		}
		double x = config.getDouble(category + "." + locationName + ".x");
		double y = config.getDouble(category + "." + locationName + ".y");
		double z = config.getDouble(category + "." + locationName + ".z");
		float yaw = config.getFloat(category + "." + locationName + ".yaw");
		float pitch = config.getFloat(category + "." + locationName + ".pitch");
		Location location = new Location(world, x, y, z, yaw, pitch);
		return location;
	}
	
	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "locations.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}