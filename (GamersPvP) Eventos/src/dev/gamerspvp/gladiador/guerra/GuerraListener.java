package dev.gamerspvp.gladiador.guerra;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.guerra.models.Guerra;
import dev.gamerspvp.gladiador.guerra.models.Guerra.statusType;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class GuerraListener implements Listener {
	
	private Main instance;
	
	public GuerraListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String playerName = player.getName();
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		String prefix = guerraManager.prefix;
		if (guerra == null) {
			return;
		}
		ClanPlayer clanPlayer = guerra.getParticipantes().get(player);
		if (clanPlayer == null) {
			return;
		}
		Player killer = null;
		if (player.getKiller() != null) {
			killer = player.getKiller();
			String killerName = killer.getName();
			ClanPlayer clanKiller = guerra.getParticipantes().get(killer);
			if (clanKiller != null) {
				String clanPlayerTag = ClansAPI.getClanTag(clanPlayer.getClan());
				String clanKillerTag = ClansAPI.getClanTag(clanKiller.getClan());
				int kills = guerra.getpKills().get(killerName.toLowerCase()) + 1;
				event.setDeathMessage(prefix + clanPlayerTag + " §f" + playerName + " §7foi morto por " + clanKillerTag + " §f" + killerName + "§8(§f" + kills + "§8)");
				guerra.getpKills().put(killerName.toLowerCase(), kills);
			}
		}
		Clan clan = clanPlayer.getClan();
		guerra.getParticipantes().remove(player);
		guerra.removeClan(clan, instance);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		String[] allowedCommands = new String[] { "/guerra sair", "/g", "/report", "/tell", "/." };
		String[] blockedGlobalCommands = new String[] { "/clan kick", "/clan resign", "/clan home", "/clan war", "/clan disband" };
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		if (guerra == null) {
			return;
		}
		if (!(player.hasPermission("guerra.admin"))) {
			if (guerra.getParticipantes().containsKey(player)) {
				boolean find = false;
				for (String command : allowedCommands) {
					if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
						find = true;
					}
				}
				if (!(find)) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durante o Evento Guerra.");
					return;
				}
			}
		}
		if (!(player.hasPermission("guerra.bypass.globalcommands"))) {
			for (String command : blockedGlobalCommands) {
				if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durantea a realiza§§o o Evento Guerra.");
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		if (guerra == null) {
			return;
		}
		ClanPlayer clanPlayer = guerra.getParticipantes().get(player);
		if (clanPlayer == null) {
			return;
		}
		Clan clan = clanPlayer.getClan();
		guerra.getParticipantes().remove(player);
		guerra.removeClan(clan, instance);
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		GuerraManager guerraManager = instance.getGuerraManager();
		Guerra guerra = guerraManager.getGuerra();
		if (guerra == null) {
			return;
		}
		statusType status = guerra.getStatus();
		if (status == statusType.CHAMANDO || status == statusType.FECHADO || status == statusType.CANCELADO) {
			if (guerra.getParticipantes().get(player) == null) {
				return;
			}
			event.setCancelled(true);
		}
	}
}