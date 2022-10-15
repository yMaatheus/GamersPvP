package net.gamerspvp.lobby.server;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.gamerspvp.lobby.Main;
import net.gamerspvp.lobby.server.models.ServerQueue;

public class ServerListener implements Listener {
	
	private Main instance;
	
	public ServerListener(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onNPCRightClickEvent(NPCRightClickEvent event) {
		Player player = event.getClicker();
		player.sendMessage("A");
		String NPCName = ChatColor.stripColor(event.getNPC().getName());
		if (NPCName.equalsIgnoreCase("fullpvp")) {
			if (player.isOp() || (player.hasPermission("gamers.vip"))) {
				player.sendMessage("§a[Lobby] Verifiquei aqui e voc§ § §lVIP§a! Voc§ acha mesmo que iriamos deixar voc§ em uma fila?! Me Poupe!");
				instance.getCommons().getBungeeChannelAPI().connect(player, NPCName);
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
				return;
			}
			ServerManager serverManager = instance.getServerManager();
			ServerQueue serverQueue = serverManager.getServerQueue(NPCName.toLowerCase());
			if (serverQueue == null) {
				return;
			}
			if (serverQueue.getQueue().containsKey(player)) {
				player.sendMessage("§cVoc§ j§ faz parte da fila do servidor, Bobinho!");
				player.sendMessage("§cHmmm, percebi que voc§ § curioso, o seu lugar da fila §: §f#" + serverQueue.getQueue().get(player));
				return;
			}
			player.sendMessage("§a[Lobby] Entrando na fila...");
			int position = serverQueue.getQueue().size() +1;
			serverQueue.getQueue().put(player, position);
			player.sendMessage("§a[Lobby] Voc§ est§ na posi§§o: §f#" + String.valueOf(position));
			player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
		} else if (NPCName.equalsIgnoreCase("p4free")) {
			if (player.isOp() || (player.hasPermission("gamers.vip"))) {
				player.sendMessage("§a[Lobby] Verifiquei aqui e voc§ § §lVIP§a! Voc§ acha mesmo que iriamos deixar voc§ em uma fila?! Me Poupe!");
				instance.getCommons().getBungeeChannelAPI().connect(player, NPCName);
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
				return;
			}
			ServerManager serverManager = instance.getServerManager();
			ServerQueue serverQueue = serverManager.getServerQueue(NPCName.toLowerCase());
			if (serverQueue == null) {
				return;
			}
			if (serverQueue.getQueue().containsKey(player)) {
				player.sendMessage("§cVoc§ j§ faz parte da fila do servidor, Bobinho!");
				player.sendMessage("§cHmmm, percebi que voc§ § curioso, o seu lugar da fila §: §f#" + serverQueue.getQueue().get(player));
				return;
			}
			player.sendMessage("§a[Lobby] Entrando na fila...");
			int position = serverQueue.getQueue().size() +1;
			serverQueue.getQueue().put(player, position);
			player.sendMessage("§a[Lobby] Voc§ est§ na posi§§o: §f#" + String.valueOf(position));
			player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		ServerManager serverManager = instance.getServerManager();
		if (event.getInventory().getName() == serverManager.getInventoryServers().getName()) {
			event.setCancelled(true);
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem == null) {
				return;
			}
			if (currentItem.getType() == Material.AIR) {
				return;
			}
			Player player = (Player) event.getWhoClicked();
			String serverName = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName()).toLowerCase();
			if (player.isOp() || (player.hasPermission("gamers.vip"))) {
				player.sendMessage("§a[Lobby] Verifiquei aqui e voc§ § §lVIP§a! Voc§ acha mesmo que iriamos deixar voc§ em uma fila?! Me Poupe!");
				instance.getCommons().getBungeeChannelAPI().connect(player, serverName);
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
				return;
			}
			ServerQueue serverQueue = serverManager.getServerQueue(serverName);
			if (serverQueue == null) {
				return;
			}
			if (serverQueue.getQueue().containsKey(player)) {
				player.sendMessage("§cVoc§ j§ faz parte da fila do servidor, Bobinho!");
				player.sendMessage("§cHmmm, percebi que voc§ § curioso, o seu lugar da fila §: §f#" + serverQueue.getQueue().get(player));
				return;
			}
			player.sendMessage("§a[Lobby] Entrando na fila...");
			int position = serverQueue.getQueue().size() +1;
			serverQueue.getQueue().put(player, position);
			player.sendMessage("§a[Lobby] Voc§ est§ na posi§§o: §f#" + String.valueOf(position));
			player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemHand = player.getItemInHand();
		if (itemHand == null) {
			return;
		}
		if (itemHand.getType() == Material.AIR) {
			return;
		}
		ItemMeta itemMeta = itemHand.getItemMeta();
		if (itemMeta == null) {
			return;
		}
		String itemName = itemMeta.getDisplayName();
		if (itemName == null) {
			return;
		}
		if (itemName.contains("§aServidores")) {
			player.openInventory(instance.getServerManager().getInventoryServers());
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		ServerManager serverManager = instance.getServerManager();
		for (String serverName : serverManager.getServers().keySet()) {
			ServerQueue serverQueue = serverManager.getServerQueue(serverName);
			if (serverQueue.getQueue().containsKey(player)) {
				serverQueue.getQueue().remove(player);
				serverManager.putServerQueue(serverQueue);
				continue;
			}
		}
	}
}