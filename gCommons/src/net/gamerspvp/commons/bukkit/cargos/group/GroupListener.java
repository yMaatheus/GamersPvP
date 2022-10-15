package net.gamerspvp.commons.bukkit.cargos.group;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.cargos.CargosManager;
import net.gamerspvp.commons.bukkit.listeners.custom.BukkitMessageEvent;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.Group;
import redis.clients.jedis.Jedis;

public class GroupListener implements Listener {
	
	public GroupListener(CommonsBukkit instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onBukkitMessageEvent(BukkitMessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("cargos_updateGroups")) {
			Redis redis = CargosManager.getDatabaseController().getDataCenter().getRedis();
			Gson gson = redis.getGson();
			Jedis jedis = redis.getJedis();
			
			Type type = new TypeToken<HashMap<String, Group>>(){}.getType();
			HashMap<String, Group> groups = gson.fromJson(event.getMessage(), type);
			GroupManager groupManager = CargosManager.getGroupManager();
			groupManager.updateProxiedGroups(groups);
			groupManager.setDefaultGroup(gson.fromJson(jedis.get("bungee_defaultGroup"), Group.class));
			
			redis.close(jedis);
		}
	}
}