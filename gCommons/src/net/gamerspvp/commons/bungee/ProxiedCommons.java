package net.gamerspvp.commons.bungee;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import net.gamerspvp.commons.bungee.auth.AuthProxyManager;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.commands.DebugCommand;
import net.gamerspvp.commons.bungee.commands.LobbyCommand;
import net.gamerspvp.commons.bungee.commands.LogCommand;
import net.gamerspvp.commons.bungee.commands.MotdCommand;
import net.gamerspvp.commons.bungee.commands.PingServerCommand;
import net.gamerspvp.commons.bungee.listeners.GlobalListeners;
import net.gamerspvp.commons.bungee.maintenance.MaintenanceManager;
import net.gamerspvp.commons.bungee.utils.BungeeConfig;
import net.gamerspvp.commons.bungee.vips.ProxiedVipsManager;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.log.LogManager;
import net.gamerspvp.commons.network.models.GameStatus;
import net.gamerspvp.commons.network.models.NetworkOptions;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;

@Getter
public class ProxiedCommons extends Plugin {
	
	@Getter
	private static ProxiedCommons instance;
	private DataCenterManager dataCenter;
	@Setter
	private NetworkOptions networkOptions;
	private LogManager logManager;
	
	private AuthProxyManager authManager;
	private ProxiedCargosManager proxiedCargosManager;
	private MaintenanceManager maintenanceManager;
	private ProxiedVipsManager vipsManager;
	
	public void onEnable() {
		instance = this;
		try {
			this.dataCenter = new DataCenterManager(new BungeeConfig("database.yml", this).getConfig());
			this.networkOptions = new NetworkOptions(this);
			this.logManager = new LogManager(dataCenter);
			
			this.authManager = new AuthProxyManager(this);
			this.proxiedCargosManager = new ProxiedCargosManager(this);
			this.maintenanceManager = new MaintenanceManager(this);
			this.vipsManager = new ProxiedVipsManager(this);
			
			getProxy().getPluginManager().registerCommand(this, new DebugCommand("gdebug", "gamers.diretor"));
			getProxy().getPluginManager().registerCommand(this, new LobbyCommand(this));
			getProxy().getPluginManager().registerCommand(this, new LogCommand("log", "gamers.coordenador"));
			getProxy().getPluginManager().registerCommand(this, new MotdCommand("motd", "gamers.coordenador", this));
			getProxy().getPluginManager().registerCommand(this, new PingServerCommand("pingserver", "gamers.diretor", this));
			new GlobalListeners(this);
		} catch (Exception e) {
			e.printStackTrace();
			BungeeCord.getInstance().stop();
		}
	}
	
	@Override
	public void onDisable() {
	}
	
	public void runAsync(Runnable runnable) {
		instance.getProxy().getScheduler().runAsync(this, runnable);
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