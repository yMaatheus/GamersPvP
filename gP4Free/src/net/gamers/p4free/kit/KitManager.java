package net.gamers.p4free.kit;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.gamers.p4free.Main;

public class KitManager {
	
	private Main instance;
	private HashSet<Player> cachePlayers;
	
	public KitManager(Main instance) {
		this.instance = instance;
		this.cachePlayers = new HashSet<Player>();
		new KitListener(instance);
	}
	
	public boolean hasInSpawn(Location location) {
		if (location == null) {
			return false;
		}
		RegionManager manager = instance.getWorldGuard().getRegionManager(location.getWorld());
		ApplicableRegionSet regionSet = manager.getApplicableRegions(location);
		for (ProtectedRegion each : regionSet) {
			if (each.getId().contains("spawn-pvp")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCachePlayers(Player player) {
		if (cachePlayers.contains(player)) {
			return true;
		}
		return false;
	}
	
	public void addCachePlayer(Player player) {
		cachePlayers.add(player);
	}
	
	public void removeCachePlayer(Player player) {
		cachePlayers.remove(player);
	}
	
	public void loadKits() {
		
	}
	
	public void setKit(String name) {
		
	}
	
	public void setKitIcon(String name, ItemStack icon) {
		
	}
	
	public void deleteKit(String name) {
		
	}
}