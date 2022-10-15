package net.gamers.center.games.status;

import java.util.HashMap;

import org.simpleyaml.configuration.file.FileConfiguration;

import com.google.gson.Gson;

import lombok.Getter;
import net.gamers.center.Main;
import net.gamers.center.database.Redis;
import net.gamers.center.games.status.models.GameStatus;
import net.gamers.center.games.status.models.GameStatus.Server;
import net.gamers.center.games.status.models.GameStatus.gameStatus;
import net.gamers.center.utils.ServerPing;
import redis.clients.jedis.Jedis;

public class GamesStatusManager {
	
	@Getter
	private static HashMap<String, GameStatus> cache;
	
	public GamesStatusManager() throws Exception {
		cache = new HashMap<>();
		FileConfiguration config = Main.getInstance().getConfig();
		if (config == null) {
			throw new Exception("Falha ao pegar informações da config.");
		}
		for (String gameName : config.getConfigurationSection("games").getKeys(false)) {
			int gameMaxPlayers = 0;
			GameStatus gameStatus = new GameStatus(gameName);
			for (String serverName : config.getConfigurationSection("games." + gameName + ".servers").getKeys(false)) {
				String address = config.getString("games." + gameName + ".servers." + serverName + ".address");
				int port = config.getInt("games." + gameName + ".servers." + serverName + ".port");
				int maxPlayers = config.getInt("games." + gameName + ".servers." + serverName + ".maxPlayers");
				
				gameMaxPlayers = gameMaxPlayers + maxPlayers;
				gameStatus.getServers().put(serverName, gameStatus.server(serverName, address, port, maxPlayers));
			}
			for (String serverName : config.getStringList("games." + gameName + ".welcomeServers")) {
				Server server = gameStatus.getServers().get(serverName);
				gameStatus.getWelcomeServers().add(server);
			}
			gameStatus.setMaxPlayers(gameMaxPlayers);
			cache.put(gameName, gameStatus);
		}
		Main.getInstance().getTaskUpdater().add(() -> update());
	}
	
	public void update() {
		Redis redis = Main.getInstance().getDataCenter().getRedis();
		Jedis jedis = redis.getJedis();
		Gson gson = redis.getGson();
		int onlineGlobal = 0;
		for (GameStatus game : cache.values()) {
			int gameOnline = 0;
			boolean offline = false;
			for (Server server : game.getServers().values()) {
				ServerPing ping = new ServerPing(server.getAddress(), server.getPort());
				int online = ping.getOnlinePlayers();
				gameOnline = gameOnline + online;
				server.setOnline(online);
				if (!offline) {
					offline = ping.isClosedServer();
				}
			}
			game.setOnline(gameOnline);
			if (game.getStatus() != gameStatus.MANUTENÇÃO && offline) {
				game.setStatus(gameStatus.OFFLINE);
				//System.out.println("[INFO] Game: " + game.getName() + " is " + game.getStatus() + "!");
			} else if (game.getStatus() != gameStatus.ONLINE && (!offline)) {
				game.setStatus(gameStatus.ONLINE);
			}
			jedis.set("game_" + game.getName(), gson.toJson(game));
			cache.put(game.getName(), game);
			onlineGlobal = onlineGlobal + gameOnline;
		}
		jedis.set("network_online", String.valueOf(onlineGlobal));
		redis.close(jedis);
	}
	
	public static void executeMaintenanceAll() {
		for (GameStatus game : cache.values()) {
			executeMaintenance(game);
		}
	}
	
	public static void executeMaintenance(GameStatus game) {
		game.setStatus(gameStatus.MANUTENÇÃO);
		cache.put(game.getName(), game);
	}
	
	public static GameStatus getGameStatus(String gameName) {
		Redis redis = Main.getInstance().getDataCenter().getRedis();
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