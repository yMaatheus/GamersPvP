package dev.gamerspvp.gladiador.killer;

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
import dev.gamerspvp.gladiador.killer.Killer.statusType;

public class KillerListener implements Listener {
	
	private Main instance;
	
	public KillerListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String playerName = player.getName();
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		String prefix = killerManager.prefix;
		if (killer == null) {
			return;
		}
		if (killer.getParticipantes().get(player) == null) {
			return;
		}
		Player killerPlayer = null;
		if (player.getKiller() != null) {
			killerPlayer = player.getKiller();
			String killerName = killerPlayer.getName();
			event.setDeathMessage(prefix + "§f" + playerName + " §7foi morto por §f" + killerName);
		}
		killer.getParticipantes().remove(player);
		killerManager.executeCheck();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		String[] allowedCommands = new String[] { "/killer sair", "/g", "/report", "/tell", "/." };
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		if (killer == null) {
			return;
		}
		if (!(player.hasPermission("killer.admin"))) {
			if (killer.getParticipantes().containsKey(player)) {
				boolean find = false;
				for (String command : allowedCommands) {
					if ((message.startsWith(command + " ") || (message.equalsIgnoreCase(command)))) {
						find = true;
					}
				}
				if (!(find)) {
					event.setCancelled(true);
					player.sendMessage("§cComando bloqueado durante o Evento Killer.");
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		if (killer == null) {
			return;
		}
		if (killer.getParticipantes().get(player) == null) {
			return;
		}
		killer.getParticipantes().remove(player);
		killerManager.executeCheck();
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		KillerManager killerManager = instance.getKillerManager();
		Killer killer = killerManager.getKiller();
		if (killer == null) {
			return;
		}
		statusType status = killer.getStatus();
		if ((status == statusType.CHAMANDO) || (status == statusType.FECHADO) || (status == statusType.CANCELADO)) {
			if (killer.getParticipantes().get(player) == null) {
				return;
			}
			event.setCancelled(true);
		}
	}
}