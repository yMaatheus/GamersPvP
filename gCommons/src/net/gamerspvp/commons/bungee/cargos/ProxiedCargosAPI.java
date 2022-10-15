package net.gamerspvp.commons.bungee.cargos;

import net.gamerspvp.commons.bungee.cargos.group.ProxiedGroupManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.network.models.Group;
import net.gamerspvp.commons.network.models.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxiedCargosAPI {

	private static ProxiedGroupManager groupManager = ProxiedCargosManager.getGroupManager();
	private static ProxiedUserManager userManager = ProxiedCargosManager.getUserManager();
	
	public static User getCachedUser(ProxiedPlayer player) {
		return userManager.getCachedUser(player);
	}
	
	public static User getUser(String userName) throws Exception {
		return userManager.getUser(userName);
	}

	public static Group getUserGroup(ProxiedPlayer player) {
		User user = getCachedUser(player);
		if (user == null) {
			return null;
		}
		return getGroup(user.getGroup());
	}
	
	public static Group getUserGroup(String userName) throws Exception {
		User user = getUser(userName);
		if (user == null) {
			return null;
		}
		return getGroup(user.getGroup());
	}
	
	public static Group getGroup(String groupName) {
		return groupManager.getGroup(groupName);
	}
}