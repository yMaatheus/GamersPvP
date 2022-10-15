package net.gamerspvp.commons.bukkit.cargos.user;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.cargos.CargosManager;
import net.gamerspvp.commons.bukkit.cargos.group.GroupBukkit;
import net.gamerspvp.commons.bukkit.cargos.group.GroupManager;
import net.gamerspvp.commons.network.CargosController;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.User;

public class UserManager {
	
	private HashMap<Player, UserAttachment> cache;
	
	public UserManager(CommonsBukkit commons) throws Exception {
		this.cache = new HashMap<Player, UserAttachment>();
		
		new UserListener(commons);
	}
	
	public void loadUser(Player player) throws Exception {
		String playerName = player.getName();
		CargosController databaseController = CargosManager.getDatabaseController();
		User user = databaseController.getUser(playerName);
		if (user == null) {
			user = new User(playerName, CargosManager.getGroupManager().getDefaultGroup().getName());
			databaseController.updateUser(user);
		}
		calculePermissions(player, user.getGroup());
	}
	
	public void reloadUser(Player player, User user) {
		calculePermissions(player, user.getGroup());
	}
	
	public void calculePermissions(Player player, String groupName) {
		if (cache.get(player) != null) {
			removeAttachment(player);
		}
		HashMap<String, GroupBukkit> groups = new HashMap<String, GroupBukkit>();
		groups.putAll(CargosManager.getGroupManager().getGroups());
		GroupBukkit group = groups.get(groupName);
		if (group == null) {
			player.kickPlayer("[Cargos]\n §cAconteceu um problema, contate a equipe do servidor. (SpiNotFoundG-CAL)");
			return;
		}
		PermissionAttachment attachment = player.addAttachment(CommonsBukkit.getInstance());
		for (String permission : group.getPermissions()) {
			attachment.setPermission(permission, true);
		}
		cache.put(player, new UserAttachment(groupName, attachment));
	}
	
	public boolean defineUserGroup(String userName, String groupName) throws Exception {
		CargosController databaseController = CargosManager.getDatabaseController();
		Redis redis = databaseController.getDataCenter().getRedis();
		GroupManager proxiedGroupManager = CargosManager.getGroupManager();
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
		return CargosManager.getDatabaseController().getUser(userName);
	}
	
	public void removeAttachment(Player player) {
		UserAttachment userAttachment = cache.get(player);
		if (userAttachment == null) {
			return;
		}
		player.removeAttachment(userAttachment.getPermissionAttachment());
	}
	
	public void refreshPlayersPermissions() {
		System.out.println("[Cargos] Refreshing all players permissions");
		for (Player player : cache.keySet()) {
			calculePermissions(player, cache.get(player).getGroup());
		}
	}
	
	public UserAttachment getUserAttachment(Player player) {
		return cache.get(player);
	}
}