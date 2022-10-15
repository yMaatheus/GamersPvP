package dev.gamerspvp.gladiador.listeners;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.dominar.DominarManager;
import dev.gamerspvp.gladiador.eventochat.EventoChatManager;
import dev.gamerspvp.gladiador.gladiador.GladiadorManager;
import dev.gamerspvp.gladiador.gladiador.models.Gladiador;
import dev.gamerspvp.gladiador.gladiador.models.Gladiador.statusType;
import dev.gamerspvp.gladiador.guerra.GuerraManager;
import dev.gamerspvp.gladiador.killer.Killer;
import dev.gamerspvp.gladiador.killer.KillerManager;
import dev.gamerspvp.gladiador.minigladiador.MiniGladiadorManager;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador;
import dev.gamerspvp.gladiador.mito.MitoManager;

public class GlobalListeners implements Listener {
	
	private Main instance;
	
	public GlobalListeners(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onChatMessageEvent(ChatMessageEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getSender();
		String playerName = player.getName();
		EventoChatManager eventoChatManager = instance.getEventoChatManager();
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		HashSet<String> gladiadores = gladiadorManager.getGladiadores();
		GuerraManager guerraManager = instance.getGuerraManager();
		HashSet<String> guerraGanhadores = guerraManager.getGanhadores();
		KillerManager killerManager = instance.getKillerManager();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		HashSet<String> miniGladiadores = miniGladiadorManager.getGanhadores();
		MitoManager mitoManager = instance.getMitoManager();
		DominarManager dominarManager = instance.getDominarManager();
		if (eventoChatManager.isWinner(playerName)) {
			event.setTagValue("genio", "§a[G§nio]");
		}
		if (gladiadores.contains(playerName)) {
			event.setTagValue("gladiador", "§6[Gladiador]");
		}
		if (guerraGanhadores.contains(playerName)) {
			event.setTagValue("guerra", "§a[Guerra]");
		}
		if (killerManager.isWinner(playerName)) {
			event.setTagValue("killer", "§c[Killer]");
		}
 		if (miniGladiadores.contains(playerName)) {
			event.setTagValue("minigladiador", "§e[MiniGladiador]");
		}
		if (mitoManager.isMito(playerName)) {
			event.setTagValue("mito", "§5[Mito]");
		}
		if (dominarManager.isActive()) {
			if (dominarManager.containsPlayer(player)) {
				event.setTagValue("dominar", "§b[Dominador]");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		if (gladiadorManager.getGladiador() != null) {
			Gladiador gladiador = gladiadorManager.getGladiador();
			statusType status = gladiador.getStatus();
			if ((status == statusType.CHAMANDO) || (status == statusType.FECHADO)) {
				if (gladiadorManager.getGladiador().getParticipantes().containsKey(player)) {
					if (!(from.equals(gladiadorManager.getLocation("saida")))) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		if (miniGladiadorManager.getMiniGladiador() != null) {
			MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
			dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType status = miniGladiador.getStatus();
			if ((status == dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType.CHAMANDO) || (status == dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType.FECHADO)) {
				if (miniGladiadorManager.getMiniGladiador().getParticipantes().containsKey(player)) {
					if (!(from.equals(miniGladiadorManager.getLocation("saida")))) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		KillerManager killerManager = instance.getKillerManager();
		if (killerManager.getKiller() != null) {
			Killer killer = killerManager.getKiller();
			dev.gamerspvp.gladiador.killer.Killer.statusType status = killer.getStatus();
			if ((status == dev.gamerspvp.gladiador.killer.Killer.statusType.CHAMANDO) || (status == dev.gamerspvp.gladiador.killer.Killer.statusType.FECHADO)) {
				if (killerManager.getKiller().getParticipantes().containsKey(player)) {
					if (!(from.equals(killerManager.getLocation("saida")))) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}