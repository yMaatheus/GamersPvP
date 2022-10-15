package net.gamerspvp.commons.bungee.cargos.group;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.listeners.custom.BungeeMessageEvent;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.Group;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.Jedis;

public class ProxiedGroupListener implements Listener {
	
	private ProxiedCommons instance;
	
	public ProxiedGroupListener(ProxiedCommons instance) {
		this.instance = instance;
		instance.getProxy().getPluginManager().registerListener(instance, this);
	}
	
	@EventHandler
	public void onBungeeMessageEvent(BungeeMessageEvent event) {
		String channel = event.getChannel();
		if (channel.equalsIgnoreCase("cargos_updateGroups")) {
			Redis redis = instance.getDataCenter().getRedis();
			Gson gson = redis.getGson();
			Jedis jedis = redis.getJedis();
			
			Type type = new TypeToken<HashMap<String, Group>>(){}.getType();
			HashMap<String, Group> groups = gson.fromJson(event.getMessage(), type);
			ProxiedGroupManager proxiedGroupManager = ProxiedCargosManager.getGroupManager();
			proxiedGroupManager.updateGroups(groups);
			proxiedGroupManager.setDefaultGroup(gson.fromJson(jedis.get("bungee_defaultGroup"), Group.class));
			redis.close(jedis);
		}
	}
}