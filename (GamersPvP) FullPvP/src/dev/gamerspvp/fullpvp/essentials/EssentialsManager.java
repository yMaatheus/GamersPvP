package dev.gamerspvp.fullpvp.essentials;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.essentials.commands.CloseTellCommand;
import dev.gamerspvp.fullpvp.essentials.commands.DiscordCommand;
import dev.gamerspvp.fullpvp.essentials.commands.EventosCommand;
import dev.gamerspvp.fullpvp.essentials.commands.FlyCommand;
import dev.gamerspvp.fullpvp.essentials.commands.PotEditCommand;
import dev.gamerspvp.fullpvp.essentials.commands.ScreenShareCommand;
import dev.gamerspvp.fullpvp.essentials.commands.SiteCommand;

public class EssentialsManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private String site;
	private String discord;
	private String spawnWorld;
	private String eventos;
	private String messageJoin;
	private String motd;
	private String maintanceMotd;
	private HashSet<String> vipWorlds;
	private String tablistHeader;
	private String tablistFooter;
	private HashMap<String, HashSet<Integer>> blockPlaceWorld;
	private HashMap<String, String> closedsTell;
	private HashSet<Player> screenshare;
	
	public EssentialsManager(Main instance) {
		this.instance = instance;
		this.config = instance.loadConfig("essentials.yml");
		load();
	}
	
	public void load() {
		this.site = config.getString("site");
		this.discord = config.getString("discord");
		this.spawnWorld = config.getString("spawnWorld");
		this.eventos = StringUtils.join(config.getStringList("eventos"), "\n").replace("&", "§");
		this.messageJoin = StringUtils.join(config.getStringList("mensagemEntrar"), "\n").replace("&", "§");
		this.motd = StringUtils.join(config.getStringList("motd"), "\n").replace("&", "§");
		this.maintanceMotd = StringUtils.join(config.getStringList("maintanceMotd"), "\n").replace("&", "§");
		this.vipWorlds = new HashSet<String>(config.getStringList("Fly.vipWorlds"));
		this.tablistHeader = StringUtils.join(config.getStringList("Tablist.header"), "\n").replace("&", "§");
		this.tablistFooter = StringUtils.join(config.getStringList("Tablist.footer"), "\n").replace("&", "§");
		this.blockPlaceWorld = new HashMap<String, HashSet<Integer>>();
		for (String worldName : config.getConfigurationSection("BlockPlaceInWorld").getKeys(false)) {
			HashSet<Integer> id = new HashSet<Integer>(config.getIntegerList("BlockPlaceInWorld." + worldName));
			blockPlaceWorld.put(worldName.toLowerCase(), id);
		}
		this.closedsTell = new HashMap<String, String>();
		this.screenshare = new HashSet<Player>();
		instance.registerCommand(new CloseTellCommand(instance), "closetell", "fechartell");
		instance.registerCommand(new DiscordCommand(instance), "discord");
		instance.registerCommand(new EventosCommand(instance), "eventos");
		instance.registerCommand(new FlyCommand(instance), "fly");
		instance.registerCommand(new PotEditCommand(instance), "potedit");
		instance.registerCommand(new ScreenShareCommand(instance), "ss", "screenshare");
		instance.registerCommand(new SiteCommand(instance), "site");
		new EssentialsListener(instance);
	}
	
	public void fly(Player player) {
		if (player.getAllowFlight()) {
			player.setAllowFlight(false);
			player.sendMessage("§a[Fly] Fly desativado");
			return;
		}
		player.sendMessage("§a[Fly] Fly ativado");
		player.setAllowFlight(true);
	}
	
	public boolean isVipWorld(String worldName) {
		if (vipWorlds.contains(worldName.toLowerCase())) {
			return true;
		}
		return false;
	}
	
	public boolean isDisableFlyRegion(Location location) {
		if (location == null) {
			return false;
		}
		RegionManager manager = instance.getWorldGuard().getRegionManager(location.getWorld());
		ApplicableRegionSet regionSet = manager.getApplicableRegions(location);
		for (ProtectedRegion each : regionSet) {
			if (each.getId().contains("spawn-pvp") || each.getId().contains("spawnnatal-pvp")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDenyPlaceBlock(int id, String worldName) {
		if (worldName == null) {
			return false;
		}
		if ((blockPlaceWorld == null) || (blockPlaceWorld.isEmpty())) {
			return false;
		}
		HashSet<Integer> ids = blockPlaceWorld.get(worldName.toLowerCase());
		if (ids == null) {
			return false;
		}
		if (ids.contains(id)) {
			return true;
		}
		return false;
	}
	
	public void executeCloseTell(String playerName, String reason) {
		closedsTell.put(playerName.toLowerCase(), reason);
	}
	
	public void removeCloseTell(String playerName) {
		closedsTell.remove(playerName.toLowerCase());
	}
	
	public String getCloseTell(String playerName) {
		return closedsTell.get(playerName.toLowerCase());
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
	
	public String getSite() {
		return site;
	}
	
	public String getDiscord() {
		return discord;
	}
	
	public World getSpawnWorld() {
		return Bukkit.getWorld(spawnWorld);
	}
	
	public String getMotd() {
		return motd;
	}
	
	public String getMaintanceMotd() {
		return maintanceMotd;
	}
	
	public String getTablistHeader() {
		return tablistHeader;
	}
	
	public String getTablistFooter() {
		return tablistFooter;
	}
	
	public String getMessageJoin() {
		return messageJoin;
	}

	public String getEventos() {
		return eventos;
	}
}