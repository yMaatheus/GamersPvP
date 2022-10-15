package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev.gamerspvp.lobby.Main;

public class BlockBreakListener implements Listener {
	
	public BlockBreakListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent event) {
		Player jogador = event.getPlayer();
		if (jogador.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
}