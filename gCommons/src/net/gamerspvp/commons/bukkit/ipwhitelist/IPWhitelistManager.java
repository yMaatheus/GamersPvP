package net.gamerspvp.commons.bukkit.ipwhitelist;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.Setter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.listeners.custom.BukkitMessageEvent;
import net.gamerspvp.commons.network.database.Redis;
import redis.clients.jedis.Jedis;

@SuppressWarnings("deprecation")
@Getter
@Setter
public class IPWhitelistManager implements Listener {
	
	private CommonsBukkit instance;
	
	private HashSet<String> whitelist;
	private boolean active;
	private boolean debug;
	
	public IPWhitelistManager(FileConfiguration config, CommonsBukkit instance) {
		this.instance = instance;
		this.whitelist = new HashSet<String>();
		this.active = config.getBoolean("ipWhitelist.active");
		this.debug = config.getBoolean("ipWhitelist.debug");
		Redis redis = instance.getDataCenter().getRedis();
		Gson gson = redis.getGson();
		Jedis jedis = redis.getJedis();
		Type setType = new TypeToken<HashSet<String>>(){}.getType();
		whitelist.addAll(gson.fromJson(jedis.get("servers_registeredAddresses"), setType));
		redis.close(jedis);
		if (active) {
			instance.getServer().getPluginManager().registerEvents(this, instance);
		}
	}
	
	@EventHandler
	public void onBungeeMessageEvent(BukkitMessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("servers_registeredAddresses_update")) {
			Type setType = new TypeToken<HashSet<String>>() {
			}.getType();
			HashSet<String> whitelistIps = new Gson().fromJson(event.getMessage(), setType);
			whitelist = whitelistIps;
		}
	}
	
	public void onPacketEvent(PacketEvent event) {
		if (!active) {
			return;
		}
		InetAddress address = event.getPlayer().getAddress().getAddress();
		if (debug) {
			System.out.println("IP: " + address + " PACKET: " + event.getPacketType());
		}
		String ip = address.getHostAddress().replace("[\\[\\]]", "").replaceAll("/", "");
		if (whitelist.contains(ip)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		if (onlinePlayers.isEmpty()) {
			return;
		}
		for (Player player : onlinePlayers) {
			String playerName = player.getName();
			UUID uuid = player.getUniqueId();
			String uuidText = uuid.toString().toLowerCase().replaceAll("-", "");
			if (!playerName.equalsIgnoreCase(event.getName())) {
				continue;
			}
			if (!uuid.equals(event.getUniqueId())) {
				continue;
			}
			if (!uuidText.equalsIgnoreCase(event.getUniqueId().toString().toLowerCase().replaceAll("-", ""))) {
				continue;
			}
			event.setKickMessage("Disconnect");
			event.setResult(PlayerPreLoginEvent.Result.KICK_WHITELIST);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (whitelist.contains(event.getRealAddress().getHostAddress())) {
			return;
		}
		event.setKickMessage("Disconnect");
		event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
	}
}