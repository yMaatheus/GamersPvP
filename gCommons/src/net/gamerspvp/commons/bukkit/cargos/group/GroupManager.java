package net.gamerspvp.commons.bukkit.cargos.group;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.Setter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.Group;
import redis.clients.jedis.Jedis;

@Getter
@Setter
public class GroupManager {
	
	private static HashMap<String, Group> proxiedGroups;
	private Group defaultGroup;
	
	private HashMap<String, GroupBukkit> groups;
	
	public GroupManager(CommonsBukkit commons) throws Exception {
		Redis redis = commons.getDataCenter().getRedis();
		Gson gson = redis.getGson();
		Jedis jedis = redis.getJedis();
		proxiedGroups = jsonToHashGroups(jedis.get("bungee_groups"), gson);
		this.defaultGroup = gson.fromJson(jedis.get("bungee_defaultGroup"), Group.class);
		redis.close(jedis);
		this.groups = new HashMap<String, GroupBukkit>();
		
		File file = new File(commons.getDataFolder(), "permissions.yml");
		FileConfiguration config;
		if (!file.exists()) {
			commons.saveResource("permissions.yml", false);
			file = new File(commons.getDataFolder(), "permissions.yml");
			if (proxiedGroups.isEmpty()) {
				throw new Exception("Not found proxied groups.");
			}
			//Gerar uma .yml customizada com os grupos já existentes
			config = YamlConfiguration.loadConfiguration(file);
			List<Group> values = new ArrayList<Group>();
			values.addAll(proxiedGroups.values());
			Collections.sort(values, new Comparator<Group>() {
			    @Override
			    public int compare(Group pt1, Group pt2) {
			        Integer f1 = pt1.getRank();
			        Integer f2 = pt2.getRank();
			        return f1.compareTo(f2);
			    }
			});
			for (Group group : values) {
				config.set("Groups." + group.getName() + ".permissions", new ArrayList<String>());
			}
			config.save(new File(commons.getDataFolder(), "permissions.yml"));
		}
		config = YamlConfiguration.loadConfiguration(new File(commons.getDataFolder(), "permissions.yml"));
		//Carregar .yml
		loadGroups(config);
		
		new GroupListener(commons);
	}
	
	public void loadGroups(FileConfiguration config) {
		for (String name : config.getConfigurationSection("Groups").getKeys(false)) {
			String groupName = name.substring(0,1).toUpperCase().concat(name.toLowerCase().substring(1));
			List<String> permissions = config.getStringList("Groups." + name + ".permissions");
			groups.put(groupName, new GroupBukkit(groupName, new HashSet<String>(permissions)));
		}
	}
	
	public static String getPreffix(String groupName) {
		return proxiedGroups.get(groupName).getPreffix();
	}
	
	public static int getRank(String groupName) {
		return proxiedGroups.get(groupName).getRank();
	}
	
	public static boolean isVipGroup(String groupName) {
		return proxiedGroups.get(groupName).isVip();
	}
	
	public void updateProxiedGroups(HashMap<String, Group> groups) {
		proxiedGroups = groups;
	}
	
	public HashMap<String, Group> jsonToHashGroups(String arg, Gson gson) {
		Type type = new TypeToken<HashMap<String, Group>>(){}.getType();
		HashMap<String, Group> groups = gson.fromJson(arg, type);
		return groups;
	}
	
	public GroupBukkit getGroup(String groupName) {
		return groups.get(groupName);
	}
}