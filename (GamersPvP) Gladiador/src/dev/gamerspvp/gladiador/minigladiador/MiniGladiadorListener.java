package dev.gamerspvp.gladiador.minigladiador;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class MiniGladiadorListener implements Listener {
	
	private Main instance;
	
	public MiniGladiadorListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
 	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String playerName = player.getName();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		String prefix = miniGladiadorManager.prefix;
		if (miniGladiador == null) {
			return;
		}
		ClanPlayer clanPlayer = miniGladiador.getParticipantes().get(player);
		if (clanPlayer == null) {
			return;
		}
		Player killer = null;
		if (player.getKiller() != null) {
			killer = player.getKiller();
			String killerName = killer.getName();
			ClanPlayer clanKiller = miniGladiador.getParticipantes().get(killer);
			if (clanKiller != null) {
				String clanPlayerTag = ClansAPI.getClanTag(clanPlayer.getClan());
				String clanKillerTag = ClansAPI.getClanTag(clanKiller.getClan());
				int kills = miniGladiador.getpKills().get(killerName.toLowerCase()) + 1;
				event.setDeathMessage(prefix + clanPlayerTag + " §f" + playerName + " §7foi morto por " + clanKillerTag + " §f" + killerName + "§8(§f" + kills + "§8)");
				miniGladiador.getpKills().put(killerName.toLowerCase(), kills);
			}
		}
		Clan clan = clanPlayer.getClan();
		miniGladiador.getParticipantes().remove(player);
		miniGladiador.removeClan(clan, instance);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		String[] allowedCommands = new String[] { "/minigladiador sair", "/g", "/report", "/tell", "/." };
		String[] blockedGlobalCommands = new String[] { "/clan kick", "/clan resign", "/clan home", "/clan war", "/clan disband" };
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			return;
		}
		if (!(player.hasPermission("gladiador.admin"))) {
			if (miniGladiador.getParticipantes().containsKey(player)) {
				boolean find = false;
				for (String command : allowedCommands) {
					if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
						find = true;
					}
				}
				if (!(find)) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durante o Evento MiniGladiador.");
					return;
				}
			}
		}
		if (!(player.hasPermission("gladiador.bypass.globalcommands"))) {
			for (String command : blockedGlobalCommands) {
				if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durantea a realização o evento minigladiador.");
					break;
				}
			}
		}
	}
	
	/*@EventHandler
	public void onPlayerAuthEvent(PlayerAuthEvent event) {
		Player player = event.getPlayer();
		PlayerInventory playerInventory = player.getInventory();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		for (ItemStack itemStack : playerInventory.getContents()) {
			if (!(miniGladiadorManager.isKitItem(itemStack))) {
				continue;
			}
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getHelmet())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getChestplate())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getLeggings())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getBoots())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
	}*/
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerInventory playerInventory = player.getInventory();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		for (ItemStack itemStack : playerInventory.getContents()) {
			if (!(miniGladiadorManager.isKitItem(itemStack))) {
				continue;
			}
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getHelmet())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getChestplate())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getLeggings())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
		if (miniGladiadorManager.isKitItem(playerInventory.getBoots())) {
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			return;
		}
		ClanPlayer clanPlayer = miniGladiador.getParticipantes().get(player);
		if (clanPlayer == null) {
			return;
		}
		Clan clan = clanPlayer.getClan();
		miniGladiador.getParticipantes().remove(player);
		miniGladiador.removeClan(clan, instance);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			return;
		}
		statusType status = miniGladiador.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.CANCELADO || status == statusType.FINAL) {
			ClanPlayer clanPlayer = miniGladiador.getParticipantes().get(player);
			if (clanPlayer == null) {
				return;
			}
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			return;
		}
		statusType status = miniGladiador.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.CANCELADO || status == statusType.FINAL) {
			ClanPlayer clanPlayer = miniGladiador.getParticipantes().get(player);
			if (clanPlayer == null) {
				return;
			}
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			return;
		}
		statusType status = miniGladiador.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.FECHADO || status == statusType.DEATHMATCH_PVPOFF || status == statusType.CANCELADO) {
			if (miniGladiador.getParticipantes().get(player) == null) {
				return;
			}
			event.setCancelled(true);
		}
	}
}