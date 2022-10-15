package net.gamerspvp.commons.bukkit.cargos;

import org.bukkit.entity.Player;

import net.gamerspvp.commons.bukkit.cargos.group.GroupBukkit;
import net.gamerspvp.commons.bukkit.cargos.group.GroupManager;
import net.gamerspvp.commons.bukkit.cargos.user.UserAttachment;
import net.gamerspvp.commons.bukkit.cargos.user.UserManager;
import net.gamerspvp.commons.network.models.User;

public class CargosAPI {
	
	private static GroupManager groupManager = CargosManager.getGroupManager();
	private static UserManager userManager = CargosManager.getUserManager();
	
	public static UserAttachment getUserAttachment(Player player) {
		return userManager.getUserAttachment(player);
	}
	
	public static User getUser(String userName) throws Exception {
		return userManager.getUser(userName);
	}

	public static GroupBukkit getUserGroup(Player player) {
		UserAttachment user = getUserAttachment(player);
		if (user == null) {
			return null;
		}
		return getGroupBukkit(user.getGroup());
	}
	
	public static GroupBukkit getUserGroup(String userName) throws Exception {
		User user = getUser(userName);
		if (user == null) {
			return null;
		}
		return getGroupBukkit(user.getGroup());
	}
	
	public static GroupBukkit getGroupBukkit(String groupName) {
		return groupManager.getGroup(groupName);
	}
	
	public static GroupBukkit getDefaultGroup() {
		return getGroupBukkit(groupManager.getDefaultGroup().getName());
	}
}