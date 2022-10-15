package net.gamers.center.cargos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.simpleyaml.configuration.file.FileConfiguration;

import com.google.gson.Gson;

import lombok.Getter;
import net.gamers.center.Main;
import net.gamers.center.database.Redis;
import redis.clients.jedis.Jedis;

public class CargosManager {
	
	@Getter
	private HashMap<String, Group> groups;
	@Getter
	private Group defaultGroup;
	
	public CargosManager(FileConfiguration config) throws Exception {
		this.groups = new HashMap<String, Group>();
		
		for (String name : config.getConfigurationSection("Groups").getKeys(false)) {
			String groupName = name.substring(0,1).toUpperCase().concat(name.toLowerCase().substring(1));
			String preffix = config.getString("Groups." + name + ".preffix").replace("&", "§");
			int rank = config.getInt("Groups." + name + ".rank");
			boolean vip = config.getBoolean("Groups." + name + ".vip");
			List<String> permissions = config.getStringList("Groups." + name + ".permissions");
			groups.put(groupName, new Group(groupName, preffix, rank, vip, new HashSet<String>(permissions)));
		}
		if (config.getString("default") != null) {
			String defaultGroupText = config.getString("default").substring(0,1).toUpperCase().concat(config.getString("default").toLowerCase().substring(1));
			this.defaultGroup = groups.get(defaultGroupText);
		}
		if (defaultGroup == null) {
			throw new Exception("Falied load groups! Not found default group.");
		}
		Redis redis = Main.getInstance().getDataCenter().getRedis();
		Gson gson = redis.getGson();
		Jedis jedis = redis.getJedis();
		jedis.publish("general", "cargos_updateGroups;" + gson.toJson(groups));
		jedis.set("bungee_groups", gson.toJson(groups));
		jedis.set("bungee_defaultGroup", gson.toJson(defaultGroup));
		redis.close(jedis);
	}
}