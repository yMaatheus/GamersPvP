package dev.gamerspvp.auth;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import dev.gamerspvp.auth.auth.AuthBukkitManager;
import dev.gamerspvp.auth.captcha.CaptchaManager;
import lombok.Getter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.network.database.DataCenterManager;

@Getter
public class Main extends JavaPlugin {
	
	@Getter
	private static Main instance;
	
	private DataCenterManager dataCenter;
	
	private AuthBukkitManager authManager;
	private CaptchaManager captchaManager;
	
	@Override
	public void onEnable() {
		instance = this;
		try {
			if (getServer().getPluginManager().getPlugin("gCommons") == null) {
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			this.dataCenter = CommonsBukkit.getInstance().getDataCenter();
			
			this.authManager = new AuthBukkitManager(this);
			this.captchaManager = new CaptchaManager(this);
			new GlobalListeners(this);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}
	
	public void teleportToServer(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
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
	
	public FileConfiguration loadConfig(String arg) {
		File file = new File(getDataFolder(), arg);
		if (!file.exists()) {
			saveResource(arg, false);
			file = new File(getDataFolder(), arg);
		}
		return YamlConfiguration.loadConfiguration(file);
	}
}