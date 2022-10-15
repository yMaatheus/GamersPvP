package net.gamers.gladiador.gladiador;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.gamers.gladiador.Main;
import net.gamers.gladiador.gladiador.Gladiador.statusType;
import net.gamers.gladiador.utils.ClansAPI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class GladiadorListener implements Listener {
	
	private Main instance;
	
	public GladiadorListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String playerName = player.getName();
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		String prefix = gladiadorManager.prefix;
		if (gladiador == null) {
			return;
		}
		ClanPlayer clanPlayer = gladiador.getParticipantes().get(player);
		if (clanPlayer == null) {
			return;
		}
		Player killer = null;
		if (player.getKiller() != null) {
			killer = player.getKiller();
			String killerName = killer.getName();
			ClanPlayer clanKiller = gladiador.getParticipantes().get(killer);
			if (clanKiller != null) {
				String clanPlayerTag = ClansAPI.getClanTag(clanPlayer.getClan());
				String clanKillerTag = ClansAPI.getClanTag(clanKiller.getClan());
				int kills = gladiador.getpKills().get(killerName.toLowerCase()) + 1;
				event.setDeathMessage(prefix + clanPlayerTag + " §f" + playerName + " §7foi morto por " + clanKillerTag + " §f" + killerName + "§8(§f" + kills + "§8)");
				gladiador.getpKills().put(killerName.toLowerCase(), kills);
			}
		}
		Clan clan = clanPlayer.getClan();
		gladiador.getParticipantes().remove(player);
		gladiador.removeClan(clan, instance);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		String[] allowedCommands = new String[] { "/gladiador sair", "/g", "/report", "/tell", "/." };
		String[] blockedGlobalCommands = new String[] { "/clan kick", "/clan resign", "/clan home", "/clan war", "/clan disband" };
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			return;
		}
		if (!(player.hasPermission("gladiador.admin"))) {
			if (gladiador.getParticipantes().containsKey(player)) {
				boolean find = false;
				for (String command : allowedCommands) {
					if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
						find = true;
					}
				}
				if (!(find)) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durante o Evento Gladiador.");
					return;
				}
			}
		}
		if (!(player.hasPermission("gladiador.bypass.globalcommands"))) {
			for (String command : blockedGlobalCommands) {
				if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durantea a realização o evento gladiador.");
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			return;
		}
		ClanPlayer clanPlayer = gladiador.getParticipantes().get(player);
		if (clanPlayer == null) {
			return;
		}
		Clan clan = clanPlayer.getClan();
		gladiador.getParticipantes().remove(player);
		gladiador.removeClan(clan, instance);
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			return;
		}
		statusType status = gladiador.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.FECHADO || status == statusType.DEATHMATCH_PVPOFF || status == statusType.CANCELADO) {
			if (gladiador.getParticipantes().get(player) == null) {
				return;
			}
			event.setCancelled(true);
		}
	}
}