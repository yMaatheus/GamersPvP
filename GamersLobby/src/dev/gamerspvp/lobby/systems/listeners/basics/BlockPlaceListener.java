package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import dev.gamerspvp.lobby.Main;

public class BlockPlaceListener implements Listener {
	
	public BlockPlaceListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void BlockPlaceEvent(BlockPlaceEvent event) {
		Player jogador = event.getPlayer();
		if (jogador.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
}