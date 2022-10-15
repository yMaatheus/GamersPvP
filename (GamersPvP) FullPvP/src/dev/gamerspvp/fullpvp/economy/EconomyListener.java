package dev.gamerspvp.fullpvp.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import dev.gamerspvp.fullpvp.Main;

public class EconomyListener implements Listener {
	
	private Main instance;
	
	public EconomyListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST) //Baixa prioridade
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		EconomyManager economyManager = instance.getEconomyManager();
		if (economyManager.getCache(playerName.toLowerCase()) == null) {
			new VaultHandler().createPlayerAccount(playerName);
		}
	}
	
	@EventHandler
	public void onChatMessegeEvent(ChatMessageEvent event) {
		Player player = event.getSender();
		EconomyManager economyManager = instance.getEconomyManager();
		if (economyManager.getMagnata().equalsIgnoreCase(player.getName())) {
			if (event.getTags().contains("magnata")){
				event.setTagValue("magnata", "§2[$$$] ");
			}
		}
	}
}