package dev.gamerspvp.gladiador.eventochat;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import dev.gamerspvp.gladiador.Main;

public class EventoChatListener implements Listener {
	
	private Main instance;
	
	public EventoChatListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatMessageEvent(ChatMessageEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getSender();
		String playerName = player.getName();
		String message = event.getMessage();
		EventoChatManager eventoChatManager = instance.getEventoChatManager();
		EventoChat eventoChat = eventoChatManager.getEventoChat();
		if (!(eventoChat.isAcontecendo())) {
			return;
		}
		if (!(message.contains(eventoChat.getResultado() + ""))) {
			return;
		}
		eventoChat.setAcontecendo(false);
		eventoChatManager.defineWinner(playerName);
		String resultado = eventoChat.getResultado() + "";
		double premio = eventoChatManager.getPremio();
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§8-=-=-=-=-=-=-=- §eEvento Chat§8 -=-=-=-=-=-=-=-");
		Bukkit.broadcastMessage("§eO Vencedor foi: §f" + playerName);
		Bukkit.broadcastMessage("§eO Resultado era: §f" + resultado);
		Bukkit.broadcastMessage("§ePremio: §2$§f" + premio);
		Bukkit.broadcastMessage("§8-=-=-=-=-=-=-=- §eEvento Chat§8 -=-=-=-=-=-=-=-");
		Bukkit.broadcastMessage("");
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
		player.sendMessage("§aParabens! Foram adicionados §2$§f" + premio + " em sua conta.");
		instance.getEconomy().depositPlayer(player, premio);
	}
}