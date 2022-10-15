package net.gamers.p4free.game;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import net.gamers.p4free.Main;
import net.gamers.p4free.game.commands.AdminCommand;
import net.gamers.p4free.game.commands.ClearCommand;
import net.gamers.p4free.game.commands.GamemodeCommand;
import net.gamers.p4free.game.commands.HostCommand;
import net.gamers.p4free.game.commands.InvseeCommand;
import net.gamers.p4free.game.commands.PingCommand;
import net.gamers.p4free.game.commands.SSCommand;
import net.gamers.p4free.game.commands.SpawnCommand;
import net.gamers.p4free.game.commands.TPCommand;
import net.gamers.p4free.game.commands.TphereCommand;

public class GameManager {
	
	/*
	 * Manager principal do p4 free, ele vai manipular o sistema de almas, kits e
	 * etc.
	 */
	
	//private Main instance;
	
	@Getter
	private String spawnWorld;
	@Getter
	private HashSet<String> admin;
	private HashMap<String, ItemStack[]> saveinv;
	private HashMap<String, ItemStack[]> savearmor;
	private HashSet<Player> screenshare;
	
	public GameManager(FileConfiguration config, Main instance) throws Exception {
		//this.instance = instance;
		this.spawnWorld = config.getString("spawnWorld");
		this.admin = new HashSet<String>();
		this.saveinv = new HashMap<String, ItemStack[]>();
		this.savearmor = new HashMap<String, ItemStack[]>();
		this.screenshare = new HashSet<Player>();
		
		new AdminCommand(instance);
		new ClearCommand(instance);
		new GamemodeCommand(instance);
		new HostCommand(instance);
		new InvseeCommand(instance);
		new PingCommand(instance);
		new SpawnCommand(instance);
		new SSCommand(instance);
		new TPCommand(instance);
		new TphereCommand(instance);
		
		new GameListener(instance);
	}
	
	public void joinAdminMode(Player player) {
		String playerName = player.getName();
		admin.add(playerName.toLowerCase());
		saveinv.put(playerName.toLowerCase(), player.getInventory().getContents());
		savearmor.put(playerName.toLowerCase(), player.getInventory().getArmorContents());
		player.getInventory().clear();
		player.setAllowFlight(true);
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online.hasPermission("gamers.moderador")) {
				online.showPlayer(player);
			} else {
				online.hidePlayer(player);
			}
		}
		player.sendMessage("§aVocê entrou do modo §5Admin");
	}
	
	public void leaveAdminMode(Player player) {
		String playerName = player.getName();
		admin.remove(playerName.toLowerCase());
		player.getInventory().clear();
		player.getInventory().setContents(saveinv.get(playerName.toLowerCase()));
        player.getInventory().setArmorContents(savearmor.get(playerName.toLowerCase()));
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.showPlayer(player);
		}
		player.sendMessage("§aVocê saiu do modo §5Admin");
	}
	
	public boolean hasAdminMode(String playerName) {
		return admin.contains(playerName.toLowerCase());
	}
	
	public boolean hasInScreenShare(Player player) {
		return screenshare.contains(player);
	}
	
	public void addScreenShare(Player player) {
		screenshare.add(player);
	}
	
	public void removeScreenShare(Player player) {
		screenshare.remove(player);
	}
}