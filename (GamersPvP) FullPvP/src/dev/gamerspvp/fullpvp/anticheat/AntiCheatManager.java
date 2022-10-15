package dev.gamerspvp.fullpvp.anticheat;

import java.util.HashMap;

import org.bukkit.Bukkit;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.anticheat.commands.AntiCheatCommand;

public class AntiCheatManager {
	
	//private Main instance;
	
	private HashMap<String, Integer> cache;
	private HashMap<String, String> ips;
	private String preffix;
	
	public AntiCheatManager(Main instance) {
		//this.instance = instance;
		this.cache = new HashMap<String, Integer>();
		this.ips = new HashMap<String, String>();
		this.preffix = "§4§lANTIHACK§7: §f";
		
		new AntiCheatCommand(instance);
		new AntiCheatListener(instance);
	}
	
	public void addPointKick(String name, String address) {
		if (cache.get(address) == null) {
			cache.put(address, 0);
		}
		Integer value = cache.get(address);
		int points = value +1;
		cache.put(address, points);
		if (points > 4) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ipban " + address + " -s Combat Hacks(AntiHack)");
			Bukkit.getConsoleSender().sendMessage("§c[AntiHack] §f" + name + "§c(§f" + address + "§c) foi banido pelo AntiHack.");
			return;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "matrix kick " + name + " Combat Hacks");
	}
	
	public void removePointsKick(String address) {
		cache.put(address, 0);
	}
	
	public Integer getCache(String address) {
		return cache.get(address);
	}
	
	public void cache(String address, int value) {
		cache.put(address, value);
	}
	
	public String getAddress(String name) {
		return ips.get(name.toLowerCase());
	}
	
	public void saveIp(String name, String address) {
		ips.put(name.toLowerCase(), address);
	}
	
	public void executeBroadcast(String message) {
		Bukkit.broadcastMessage(preffix + message);
	}
	
	public String getPreffix() {
		return preffix;
	}
}