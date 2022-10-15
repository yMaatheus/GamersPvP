package dev.gamerspvp.lobby.systems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.api.TitleAPI;
import fr.xephi.authme.events.LoginEvent;

public class AuthMeLoginListener implements Listener {
	
	public AuthMeLoginListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLoginEvent(LoginEvent event) {
		Player player = event.getPlayer();
		TitleAPI.sendFullTitle(player, 1, 5, 1, "§aAutenticado!", "§fSeja bem vindo!");
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
	}
}