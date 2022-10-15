package net.gamerspvp.commons.bukkit.cargos.group;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupBukkit {
	
	private String name;
	private HashSet<String> permissions;
	
	public GroupBukkit(String name) {
		this.name = name;
		this.permissions = new HashSet<String>();
	}
	
	public GroupBukkit(String name, HashSet<String> permissions) {
		this.name = name;
		this.permissions = permissions;
	}
	
	public String getPreffix() {
		return GroupManager.getPreffix(name);
	}
	
	public int getRank() {
		return GroupManager.getRank(name);
	}
	
	public boolean isVipGroup() {
		return GroupManager.isVipGroup(name);
	}
}