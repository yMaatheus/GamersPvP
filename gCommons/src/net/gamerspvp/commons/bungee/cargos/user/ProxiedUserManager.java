package net.gamerspvp.commons.bungee.cargos.user;

import java.util.HashMap;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.group.ProxiedGroupManager;
import net.gamerspvp.commons.bungee.cargos.user.commands.CargoCommand;
import net.gamerspvp.commons.network.CargosController;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.Group;
import net.gamerspvp.commons.network.models.User;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxiedUserManager {
	
	private HashMap<ProxiedPlayer, User> cache;
	
	public ProxiedUserManager(ProxiedCommons commons) throws Exception {
		this.cache = new HashMap<ProxiedPlayer, User>();
		
		new CargoCommand(commons);
		
		new ProxiedUserListener(commons);
	}
	
	public void loadUser(ProxiedPlayer proxiedPlayer) throws Exception {
		String playerName = proxiedPlayer.getName();
		CargosController databaseController = ProxiedCargosManager.getDatabaseController();
		User user = databaseController.getUser(playerName);
		if (user == null) {
			user = new User(playerName, ProxiedCargosManager.getGroupManager().getDefaultGroup().getName());
			databaseController.updateUser(user);
		}
		user.setLastLogin(System.currentTimeMillis());
		calculePermissions(proxiedPlayer, user.getGroup());
		cache.put(proxiedPlayer, user);
		databaseController.updateUser(user);
	}
	
	public void reloadUser(ProxiedPlayer proxiedPlayer, User user) {
		HashMap<String, Group> groups = new HashMap<String, Group>();
		groups.putAll(ProxiedCargosManager.getGroupManager().getGroups());
		Group group = groups.get(user.getGroup());
		if (group == null) {
			proxiedPlayer.disconnect(new TextComponent("[Cargos]\n §cAconteceu um problema, contate a equipe do servidor. (BuNotFoundG-Reload)"));
			return;
		}
		for (String permission : group.getPermissions()) {
			proxiedPlayer.setPermission(permission, true);
		}
		cache.put(proxiedPlayer, user);
	}
	
	public void calculePermissions(ProxiedPlayer proxiedPlayer, String groupName) {
		if (cache.get(proxiedPlayer) != null) {
			executeClear(proxiedPlayer);
		}
		Group group = ProxiedCargosManager.getGroupManager().getGroup(groupName);
		if (group == null) {
			proxiedPlayer.disconnect(new TextComponent("[Cargos]\n §cAconteceu um problema, contate a equipe do servidor. (BuNotFoundG-Load)"));
			return;
		}
		for (String permission : group.getPermissions()) {
			proxiedPlayer.setPermission(permission, true);
		}
	}
	
	public boolean defineUserGroup(String userName, String groupName) throws Exception {
		CargosController databaseController = ProxiedCargosManager.getDatabaseController();
		Redis redis = databaseController.getDataCenter().getRedis();
		ProxiedGroupManager proxiedGroupManager = ProxiedCargosManager.getGroupManager();
		if (proxiedGroupManager.getGroup(groupName) == null) {
			return false;
		}
		User user = databaseController.getUser(userName);
		user.setGroup(groupName);
		databaseController.updateUser(user);
		redis.publish("cargo_reloadUser;" + redis.getGson().toJson(user, User.class));
		return true;
	}
	
	public User getUser(String userName) throws Exception {
		return ProxiedCargosManager.getDatabaseController().getUser(userName);
	}
	
	public User getCachedUser(ProxiedPlayer proxiedPlayer) {
		return cache.get(proxiedPlayer);
	}
	
	public boolean purgeUser(String userName) throws Exception {
		CargosController databaseController = ProxiedCargosManager.getDatabaseController();
		Redis redis = databaseController.getDataCenter().getRedis();
		ProxiedGroupManager proxiedGroupManager = ProxiedCargosManager.getGroupManager();
		
		databaseController.purgeUser(userName);
		User user = new User(userName, proxiedGroupManager.getDefaultGroup().getName());
		redis.publish("cargo_reloadUser;" + redis.getGson().toJson(user, User.class));
		return true;
	}
	
	public void executeClear(ProxiedPlayer proxiedPlayer) {
		if ((proxiedPlayer == null) || (proxiedPlayer.getPermissions() == null) || (proxiedPlayer.getPermissions().isEmpty())) {
			return;
		}
		for (String permission : proxiedPlayer.getPermissions()) {
			proxiedPlayer.setPermission(permission, false);
		}
	}
	
	public void refreshPermissions() {
		for (ProxiedPlayer proxiedPlayer : cache.keySet()) {
			User user = cache.get(proxiedPlayer);
			reloadUser(proxiedPlayer, user);
		}
	}
	
	public boolean hasCache(ProxiedPlayer proxiedPlayer) {
		if (cache.get(proxiedPlayer) != null) {
			return true;
		}
		return false;
	}
	
	public void removeCache(ProxiedPlayer proxiedPlayer) {
		cache.remove(proxiedPlayer);
	}
}