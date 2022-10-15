package net.gamers.center.address;

import java.util.HashSet;

import org.simpleyaml.configuration.file.FileConfiguration;

import com.google.gson.Gson;

import net.gamers.center.Main;
import net.gamers.center.database.Redis;
import redis.clients.jedis.Jedis;

public class AddressManager {
	
	private HashSet<String> registeredAddresses;
	
	public AddressManager() throws Exception {
		this.registeredAddresses = new HashSet<String>();
		FileConfiguration config = Main.getInstance().getConfig();
		if (config == null) {
			throw new Exception("Falha ao pegar a config.");
		}
		registeredAddresses.addAll(config.getStringList("registeredAddresses"));
		Redis redis = Main.getInstance().getDataCenter().getRedis();
		Gson gson = redis.getGson();
		Jedis jedis = redis.getJedis();
		jedis.publish("general", "servers_registeredAddresses_update;" + gson.toJson(registeredAddresses));
		jedis.set("servers_registeredAddresses", gson.toJson(registeredAddresses));
		redis.close(jedis);
	}
}