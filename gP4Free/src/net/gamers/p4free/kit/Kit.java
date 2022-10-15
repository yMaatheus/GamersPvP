package net.gamers.p4free.kit;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Kit {
	
	private String name;
	private ItemStack icon;
	@Setter
	private String[] description;
	@Setter
	private String permission;
	@Setter
	private ItemStack[] items;
	@Setter
	private ItemStack[] armor;
	
	public Kit() {}
	
	public Kit(ItemStack icon, String name) {
		this.icon = icon;
		this.name = name;
	}
	
	public Kit(ItemStack icon, String name, int slot) {
		this.icon = icon;
		this.name = name;
	}
	
	public Kit(ItemStack icon, String name, String[] description, String permission, int slot) {
		this.icon = icon;
		this.name = name;
		this.description = description;
		this.permission = permission;
	}
}