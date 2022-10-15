package net.gamerspvp.commons.bukkit;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.Gson;

import lombok.Getter;
import net.gamerspvp.commons.bukkit.cargos.CargosManager;
import net.gamerspvp.commons.bukkit.cmdblacklist.CmdBlacklistManager;
import net.gamerspvp.commons.bukkit.ipwhitelist.IPWhitelistManager;
import net.gamerspvp.commons.bukkit.listeners.GlobalListeners;
import net.gamerspvp.commons.bukkit.utils.BukkitConfig;
import net.gamerspvp.commons.bukkit.utils.BungeeChannel;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.bukkit.vips.VipsManager;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.log.LogManager;
import net.gamerspvp.commons.network.models.GameStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class CommonsBukkit extends JavaPlugin {

 	@Getter
	private static CommonsBukkit instance;
	
	@Getter
	private BungeeChannel bungeeChannelAPI;
	
	private FileConfiguration config;
	private DataCenterManager dataCenter;
	private LogManager logManager;
	
	private String serverName;
	private JedisPool jedisPool;
	
	private CargosManager cargosManager;
	private CmdBlacklistManager cmdBlacklistManager;
	private IPWhitelistManager ipWhitelistManager;
	private VipsManager vipsManager;
	
	public void onEnable() {
		instance = this;
		try {
			this.bungeeChannelAPI = new BungeeChannel(this);
			
			this.config = BukkitConfig.loadConfig("settings.yml", this);
			this.dataCenter = new DataCenterManager(config.getBoolean("reciveMessages"), BukkitConfig.loadConfig("database.yml", this), this);
			this.logManager = new LogManager(dataCenter);
			this.serverName = config.getString("server");
			this.jedisPool = dataCenter.getRedis().getJedisPool();
			
			this.cargosManager = new CargosManager(this);
			this.cmdBlacklistManager = new CmdBlacklistManager(this);
			this.ipWhitelistManager = new IPWhitelistManager(config, this);
			this.vipsManager = new VipsManager(this);
			
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, new PacketType[] { PacketType.Handshake.Client.SET_PROTOCOL, PacketType.Login.Client.START }) {
				@Override
				public void onPacketReceiving(final PacketEvent event) {
					ipWhitelistManager.onPacketEvent(event);
				}
			});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
				public void onPacketReceiving(PacketEvent event) {
					try {
						cmdBlacklistManager.onPacketEvent(event);
					} catch (Exception e) {
						getLogger().log(Level.SEVERE, "Couldn't access field.", e);
					}
				}
			});
			
			new GlobalListeners(instance);
			getServer().getScheduler().runTaskLater(this, () -> Utils.unregisterCommands("icanhasbukkit", "?", "scoreboard", "me", "say", "achievement", "blockdata", "clone", "entitydata", "fill", "help", "replaceitem", "testfor", "testforblock", "testforblocks"), 2L);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}
	
	@Override
	public void onDisable() {
		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}
	
	public void runAsync(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(instance, runnable);
	}
	
	public GameStatus getGameStatus(String gameName) {
		Redis redis = dataCenter.getRedis();
		Jedis jedis = redis.getJedis();
		Gson gson = redis.getGson();
		GameStatus value = new GameStatus(gameName);
		try {
			if (jedis.exists("game_" + gameName)) {
				String json = jedis.get("game_" + gameName);
				value = gson.fromJson(json, GameStatus.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		redis.close(jedis);
		return value;
	}
}