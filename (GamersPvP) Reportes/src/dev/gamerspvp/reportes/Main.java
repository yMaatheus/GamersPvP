package dev.gamerspvp.reportes;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private ReportManager reportManager;
	
	@Override
	public void onEnable() {
		instance = this;
		File file = new File("plugins/" + instance.getDataFolder().getName());
		if (!file.exists()) {
			file.mkdirs();
		}
		saveResource("audios/audio1.wav", false);
		this.reportManager = new ReportManager(instance);
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
	
	public static Main getInstance() {
		return instance;
	}
	
	public ReportManager getReportManager() {
		return reportManager;
	}
}