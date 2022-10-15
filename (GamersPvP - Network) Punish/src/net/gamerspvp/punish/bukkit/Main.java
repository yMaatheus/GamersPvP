package net.gamerspvp.punish.bukkit;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;

import net.gamerspvp.punish.bukkit.commands.BanCommand;
import net.gamerspvp.punish.bukkit.commands.BanIpCommand;
import net.gamerspvp.punish.bukkit.commands.CheckBanCommand;
import net.gamerspvp.punish.bukkit.commands.KickCommand;
import net.gamerspvp.punish.bukkit.commands.MuteAllCommand;
import net.gamerspvp.punish.bukkit.commands.MuteCommand;
import net.gamerspvp.punish.bukkit.commands.UnBanCommand;
import net.gamerspvp.punish.bukkit.commands.UnMuteCommand;
import net.gamerspvp.punish.bukkit.listeners.ChatMessage;
import net.gamerspvp.punish.bukkit.listeners.PlayerJoin;
import net.gamerspvp.punish.bukkit.utils.BukkitConfig;
import net.gamerspvp.punish.network.database.MySQL;
import net.gamerspvp.punish.network.database.Redis;
import net.gamerspvp.punish.network.models.Ban;
import net.gamerspvp.punish.network.models.Banip;
import net.gamerspvp.punish.network.models.Mute;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private MySQL mysql;
	private Redis redis;
	private Gson gson;
	
	private HashMap<String, String> cachedAddress;
	private HashMap<String, Mute> mutes;
	
	@Override
	public void onEnable() {
		instance = this;
		FileConfiguration config = BukkitConfig.loadConfig("database.yml", this);
		String mysqlUser = config.getString("MySQL.user");
		String mysqlHost = config.getString("MySQL.host");
		String mysqlDatabase = config.getString("MySQL.database");
		String msqlPassword = config.getString("MySQL.password");
		int mysqlPort = config.getInt("MySQL.port");
		
		String redisHost = config.getString("Redis.host");
		String redisPassword = config.getString("Redis.password");
		int redisPort = config.getInt("Redis.port");
		this.mysql = new MySQL(mysqlUser, mysqlHost, mysqlDatabase, msqlPassword, mysqlPort);
		this.redis = new Redis(redisHost, redisPassword, redisPort);
		this.gson = new Gson();
		try {
			mysql.execute("CREATE TABLE IF NOT EXISTS bans (Name TEXT, PlayerRealName TEXT, Reason TEXT, Author TEXT, Type TEXT, Time BIGINT, Date TEXT)", false);
			mysql.execute("CREATE TABLE IF NOT EXISTS bansip (Ip TEXT, Reason TEXT, Author TEXT, Type TEXT, Time BIGINT, Date TEXT)", false);
			mysql.execute("CREATE TABLE IF NOT EXISTS mutes (Name TEXT, PlayerRealName TEXT, MuteAll BOOL, Reason TEXT, Author TEXT, Time BIGINT, Date TEXT)", false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.cachedAddress = new HashMap<String, String>();
		this.mutes = new HashMap<String, Mute>();
		loadSilenceData();
		if (getServer().getPluginManager().getPlugin("Legendchat") != null) {
			new MuteAllCommand(instance);
			new MuteCommand(instance);
			new UnMuteCommand(instance);
			new ChatMessage(instance);
		}
		new BanCommand(instance);
		new BanIpCommand(instance);
		new CheckBanCommand(instance);
		new KickCommand(instance);
		new MuteAllCommand(instance);
		new MuteCommand(instance);
		new UnBanCommand(instance);
		new UnMuteCommand(instance);
		new PlayerJoin(instance);
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}

	@Override
	public void onDisable() {

	}
	
	public Ban getBanned(String name) throws SQLException {
		String query = "SELECT Name FROM bans WHERE Name=?";
		PreparedStatement ps = instance.getMySQL().getConnection().prepareStatement(query);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String reason = rs.getString("Reason");
			String author = rs.getString("Author");
			String type = rs.getString("Type");
			Long time = rs.getLong("Time");
			String date = rs.getString("Date");
			return new Ban(name, reason, author, type, time, date);
		}
		rs.close();
		ps.close();
		return null;
	}
	
	public Banip getBannedIp(String ip) throws SQLException {
		String query = "SELECT * FROM bansip WHERE Ip=?";
		PreparedStatement ps = instance.getMySQL().getConnection().prepareStatement(query);
		ps.setString(1, ip);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String adress = rs.getString("Ip");
			String reason = rs.getString("Reason");
			String author = rs.getString("Author");
			String type = rs.getString("Type");
			Long time = rs.getLong("Time");
			String date = rs.getString("Date");
			return new Banip(adress, reason, author, type, time, date);
		}
		rs.close();
		ps.close();
		return null;
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
			map.register(command.getName(), instance.getDescription().getName(), command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadSilenceData() {
		int i = 0;
		try {
			String query = "SELECT * FROM mutes;";
			Statement stmt = mysql.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				i++;
				String playerRealName = rs.getString("PlayerRealName");
				Boolean muteall = rs.getBoolean("MuteAll");
				String reason = rs.getString("Reason");
				String author = rs.getString("Author");
				Long time = rs.getLong("Time");
				String date = rs.getString("Date");
				if (System.currentTimeMillis() < time) {
					Mute mute = new Mute(playerRealName, muteall, reason, author, time, date);
					mutes.put(mute.getName(), mute);
				} else {
					mysql.getConnection().createStatement().execute("DELETE FROM mutes WHERE Name='" + playerRealName.toLowerCase() + "'");
					System.out.println("DELETE FROM mutes WHERE Name='" + playerRealName.toLowerCase() + "'");
				}
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bukkit.getConsoleSender().sendMessage("§c§lMUTES§8: §7Foram carregados §f" + i + " §7mutes.");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public HashMap<String, String> getCachedAddress() {
		return cachedAddress;
	}
	
	public HashMap<String, Mute> getMutes() {
		return mutes;
	}

	public MySQL getMySQL() {
		return mysql;
	}

	public Redis getRedis() {
		return redis;
	}

	public Gson getGson() {
		return gson;
	}
}