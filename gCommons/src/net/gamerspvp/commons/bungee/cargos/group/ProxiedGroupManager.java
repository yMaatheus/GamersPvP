package net.gamerspvp.commons.bungee.cargos.group;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.Setter;
import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.Group;
import redis.clients.jedis.Jedis;

@Getter
@Setter
public class ProxiedGroupManager {
	
	private ProxiedCommons commons;
	
	private HashMap<String, Group> groups;
	private Group defaultGroup;
	
	public ProxiedGroupManager(ProxiedCommons instance) {
		this.commons = instance;
		Redis redis = instance.getDataCenter().getRedis();
		Gson gson = redis.getGson();
		Jedis jedis = redis.getJedis();
		this.groups = jsonToHashGroups(jedis.get("bungee_groups"), gson);
		this.defaultGroup = gson.fromJson(jedis.get("bungee_defaultGroup"), Group.class);
		redis.close(jedis);
		new ProxiedGroupListener(instance);
	}
	
	public void updateGroups(HashMap<String, Group> groups) {
		this.groups = groups;
		ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
		proxiedUserManager.refreshPermissions();
	}
	
	public HashMap<String, Group> jsonToHashGroups(String arg, Gson gson) {
		Type type = new TypeToken<HashMap<String, Group>>(){}.getType();
		HashMap<String, Group> groups = gson.fromJson(arg, type);
		return groups;
	}
	
	public Group getGroup(String groupName) {
		return groups.get(groupName);
	}
}