package dev.gamerspvp.plotsaddons;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import dev.gamerspvp.plotsaddons.transfer.TransferPlotManager;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private TransferPlotManager transferPlotManager;
	
	@Override
	public void onEnable() {
		instance = this;
		if (getServer().getPluginManager().getPlugin("PlotSquared") == null) {
			getServer().getPluginManager().disablePlugin(this);
			return;
        }
		new TransferPlotManager(instance);
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
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
			map.register(command.getName(), Main.getInstance().getDescription().getName(), command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public TransferPlotManager getTransferPlotManager() {
		return transferPlotManager;
	}
}