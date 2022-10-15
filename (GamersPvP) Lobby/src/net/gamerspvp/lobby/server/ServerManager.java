package net.gamerspvp.lobby.server;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.utils.BungeeChannel;
import net.gamerspvp.commons.bukkit.utils.MakeItem;
import net.gamerspvp.commons.network.models.ServerStatus;
import net.gamerspvp.commons.network.models.ServerStatus.statusServer;
import net.gamerspvp.commons.network.utils.PingServer;
import net.gamerspvp.lobby.Main;
import net.gamerspvp.lobby.server.commands.NpcServerCommand;
import net.gamerspvp.lobby.server.commands.QueueCommand;
import net.gamerspvp.lobby.server.models.ServerQueue;

public class ServerManager {
	
	private Main instance;
	
	private HashMap<String, ServerQueue> servers;
	private HashMap<String, ServerStatus> statusServers;
	private Inventory inventoryServers;
	
	public ServerManager(Main instance) {
		this.instance = instance;
		this.servers = new HashMap<String, ServerQueue>();
		this.statusServers = new HashMap<String, ServerStatus>();
		this.inventoryServers = Bukkit.createInventory(null, InventoryType.CHEST, "§7Servidores:");
		load();
	}
	
	public void load() {
		servers.put("fullpvp", new ServerQueue("fullpvp"));
		statusServers.put("fullpvp", null);
		servers.put("p4free", new ServerQueue("p4free"));
		statusServers.put("p4free", null);
		CommonsBukkit commons = instance.getCommons();
		commons.runLoopAsync(new Runnable() {
			
			@Override
			public void run() {
				if (!statusServers.isEmpty()) {
					for (String key : statusServers.keySet()) {
						ServerStatus value = commons.getServerStatus(key);
						statusServers.put(key, value);
					}
				}
				for (ServerQueue value : servers.values()) {
					ServerStatus serverStatus = statusServers.get(value.getServerName());
					if (serverStatus == null) {
						continue;
					}
					if (serverStatus.getStatus() != statusServer.ONLINE) {
						continue;
					}
					if (!(PingServer.hasOnlineServer(serverStatus.getServerIp(), serverStatus.getServerPort()))) {
						continue;
					}
					if (serverStatus.getOnline() >= serverStatus.getMaxPlayers()) {
						continue;
					}
					if (Bukkit.getOnlinePlayers().isEmpty()) {
						continue;
					}
					executeQueue(value, commons);
				}
			}
		});
		MakeItem fullpvp = new MakeItem(Material.SKULL_ITEM).setSkullOwner("RagnarLodbrok");
		fullpvp.setName("§aFullPvP");
		fullpvp.addLore("", "§7Servidor FullPvP Cl§ssico com Economia OP", "");
		fullpvp.addFlags(ItemFlag.HIDE_ATTRIBUTES);
		inventoryServers.setItem(11, fullpvp.buildhead());
		MakeItem p4free = new MakeItem(Material.SKULL_ITEM).setSkullOwner("dropeyMARTELO");
		p4free.setName("§aP4Free");
		p4free.addLore("", "§7Servidor P4Free, em fase de testes §e§lBETA", "");
		p4free.addFlags(ItemFlag.HIDE_ATTRIBUTES);
		inventoryServers.setItem(13, p4free.buildhead());
		new NpcServerCommand(instance);
		new QueueCommand(instance);
		new ServerListener(instance);
	}
	
	public void executeQueue(ServerQueue serverInfo, CommonsBukkit commons) {
		String serverName = serverInfo.getServerName();
		BungeeChannel bungeeChannel = commons.getBungeeChannelAPI();
		int i = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player == null) {
				serverInfo.getQueue().remove(player);
				servers.put(serverInfo.getServerName(), serverInfo);
				continue;
			}
			if (!serverInfo.getQueue().containsKey(player)) {
				continue;
			}
			Integer value = serverInfo.getQueue().get(player);
			int position = value.intValue();
			if (position <= 1) {
				if (i > 2) {
					break;
				}
				serverInfo.getQueue().remove(player);
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
				player.sendMessage("§aChegou sua vez! Estamos enviando voc§ para o servidor §f" + serverName);
				i++;
				bungeeChannel.connect(player, serverName.toLowerCase());
				continue;
			}
			serverInfo.getQueue().put(player, position -1);
			servers.put(serverInfo.getServerName(), serverInfo);
			player.sendMessage("§aVoc§ est§ na posi§§o §f#" + value.toString());
		}
	}
	
	public ServerQueue getServerQueue(String serverName) {
		return servers.get(serverName);
	}
	
	public void putServerQueue(ServerQueue serverQueue) {
		servers.put(serverQueue.getServerName(), serverQueue);
	}
	
	public Inventory getInventoryServers() {
		return inventoryServers;
	}
	
	public HashMap<String, ServerQueue> getServers() {
		return servers;
	}
}