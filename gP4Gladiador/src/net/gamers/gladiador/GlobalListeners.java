package net.gamers.gladiador;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import net.gamers.gladiador.gladiador.Gladiador;
import net.gamers.gladiador.gladiador.Gladiador.statusType;
import net.gamers.gladiador.gladiador.GladiadorManager;
import net.gamers.gladiador.minigladiador.MiniGladiador;
import net.gamers.gladiador.minigladiador.MiniGladiadorManager;
import net.gamers.gladiador.mito.MitoManager;

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
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		HashSet<String> gladiadores = gladiadorManager.getGladiadores();
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		HashSet<String> miniGladiadores = miniGladiadorManager.getGanhadores();
		MitoManager mitoManager = instance.getMitoManager();
		if (gladiadores.contains(playerName)) {
			event.setTagValue("gladiador", "§6[Gladiador]");
		}
 		if (miniGladiadores.contains(playerName)) {
			event.setTagValue("minigladiador", "§e[MiniGladiador]");
		}
		if (mitoManager.isMito(playerName)) {
			event.setTagValue("mito", "§5[Mito]");
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
			MiniGladiador.statusType status = miniGladiador.getStatus();
			if ((status == MiniGladiador.statusType.CHAMANDO) || (status == MiniGladiador.statusType.FECHADO)) {
				if (miniGladiadorManager.getMiniGladiador().getParticipantes().containsKey(player)) {
					if (!(from.equals(miniGladiadorManager.getLocation("saida")))) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}