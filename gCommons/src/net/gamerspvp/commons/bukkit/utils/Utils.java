package net.gamerspvp.commons.bukkit.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

public class Utils {

	public static void registerCommand(Command command, Plugin instance, String... allys) {
		try {
			List<String> Aliases = new ArrayList<String>();
			for (String s : allys) {
				Aliases.add(s);
			}
			command.setAliases(Aliases);
			Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			cmap.setAccessible(true);
			CommandMap map = (CommandMap) cmap.get(Bukkit.getServer());
			map.register(command.getName(), instance.getDescription().getName(), command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void unregisterCommands(String... commands) {
		try {
			Field firstField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			firstField.setAccessible(true);
			CommandMap commandMap = (CommandMap) firstField.get(Bukkit.getServer());
			Field secondField = commandMap.getClass().getDeclaredField("knownCommands");
			secondField.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) secondField.get(commandMap);
			for (String command : commands) {
				if (knownCommands.containsKey(command)) {
					knownCommands.remove(command);
					List<String> aliases = new ArrayList<>();
					for (String key : knownCommands.keySet()) {
						if (!key.contains(":"))
							continue;
						String substr = key.substring(key.indexOf(":") + 1);
						if (substr.equalsIgnoreCase(command)) {
							aliases.add(key);
						}
					}
					for (String alias : aliases) {
						knownCommands.remove(alias);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String bytesToLegibleValue(double bytes) {
		if (bytes < 1024 * 1024)
			return String.format("%.2f KB", bytes);
		else if (bytes < Math.pow(2, 20) * 1024)
			return String.format("%.2f MB", bytes / Math.pow(2, 20));
		else if (bytes < Math.pow(2, 30) * 1024 )
			return String.format("%.2f GB", bytes / Math.pow(2, 30));
		else if (bytes < Math.pow(2, 40) * 1024)
			return String.format("%.2f TB", bytes / Math.pow(2, 40));
		else
			return "N/A (1TB?)";
	}
}