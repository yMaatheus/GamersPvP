package net.gamerspvp.commons.bukkit.cargos.user;

import org.bukkit.permissions.PermissionAttachment;

import lombok.Getter;

@Getter
public class UserAttachment {
	
	private String group;
	private PermissionAttachment permissionAttachment;
	
	public UserAttachment(String usingGroup, PermissionAttachment permissionAttachment) {
		this.group = usingGroup;
		this.permissionAttachment = permissionAttachment;
	}
}