package net.gamerspvp.central.cargos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;

import lombok.Getter;
import net.gamerspvp.central.Main;
import net.gamerspvp.central.cargos.models.Group;
import net.gamerspvp.central.database.Redis;
import net.gamerspvp.central.utils.BungeeConfig;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.Jedis;

public class CargosManager {
	
	// carregar grupos
	// enviar ao jedis

	// enviar mensagem que os grupos foram atualizados (??)
	// bungeecords recebem a mensagem e recarregar os grupos (??)
	
	//private Main instance;
	private Configuration config;
	
	@Getter
	private HashMap<String, Group> groups;
	@Getter
	private Group defaultGroup;
	
	public CargosManager(Main instance) {
		//this.instance = instance;
		this.config = new BungeeConfig("cargos.yml", instance).getConfig();
		this.groups = new HashMap<String, Group>();
		for (String name : config.getSection("Groups").getKeys()) {
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
			System.out.println("Grupo padrão não encontrado!");
			instance.getProxy().stop();
			return;
		}
		Redis redis = instance.getDataCenterManager().getRedis();
		Gson gson = redis.getGson();
		Jedis jedis = instance.getDataCenterManager().getRedis().getJedis();
		jedis.publish("general", "cargos_updateGroups;" + gson.toJson(groups));
		jedis.set("bungee_groups", gson.toJson(groups));
		jedis.set("bungee_defaultGroup", gson.toJson(defaultGroup));
		redis.close(jedis);
	}
}