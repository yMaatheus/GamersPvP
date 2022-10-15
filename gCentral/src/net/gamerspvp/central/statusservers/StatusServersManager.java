package net.gamerspvp.central.statusservers;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import net.gamerspvp.central.Main;
import net.gamerspvp.central.database.Redis;
import net.gamerspvp.central.statusservers.commands.StatusCommand;
import net.gamerspvp.central.statusservers.models.ServerStatus;
import net.gamerspvp.central.statusservers.models.ServerStatus.statusServer;
import net.gamerspvp.central.utils.ServerPingInfo;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;

public class StatusServersManager {
	
	private Main instance;
	
	private long timestamp;
	
	public StatusServersManager(Main instance) {
		this.instance = instance;
		this.timestamp = System.currentTimeMillis();
		new StatusCommand(instance);
		updateStatusServers();
	}
	
	@SuppressWarnings("deprecation")
	public void updateStatusServers() {
		if (timestamp > System.currentTimeMillis()) {
			return;
		}
		Redis redis = instance.getDataCenterManager().getRedis();
		Jedis jedis = redis.getJedis();
		Gson gson = redis.getGson();
		Map<String, ServerInfo> servers = BungeeCord.getInstance().getServers();
		for (String serverName : servers.keySet()) {
			ServerInfo serverInfo = servers.get(serverName);
			String address = serverInfo.getAddress().getHostString();
			int port = serverInfo.getAddress().getPort();
			
			ServerPingInfo ping = new ServerPingInfo(address, port);
			ServerStatus server = getServerStatus(serverName);
			server.setServerIp(address);
			server.setServerPort(port);
			server.setOnline(ping.getOnlinePlayers());
			server.setMaxPlayers(ping.getMaxPlayers());
			if (server.getStatus() != statusServer.MANUTENÇÃO) {
				if (ping.isClosedServer()) {
					server.setStatus(statusServer.OFFLINE);
				} else  {
					server.setStatus(statusServer.ONLINE);
				}
			}
			System.out.println(serverName + " " + server.getOnline() + "/" + server.getMaxPlayers() + " " + server.getStatus());
			jedis.set("server_" + serverName, gson.toJson(server));
		}
		redis.close(jedis);
		cooldown();
	}
	
	public ServerStatus getServerStatus(String serverName) {
		Redis redis = instance.getDataCenterManager().getRedis();
		Jedis jedis = redis.getJedis();
		Gson gson = redis.getGson();
		try {
			if (jedis.exists("server_" + serverName)) {
				String json = jedis.get("server_" + serverName);
				return gson.fromJson(json, ServerStatus.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redis.close(jedis);
		}
		return new ServerStatus(serverName);
	}
	
	private void cooldown() {
		this.timestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
	}
}