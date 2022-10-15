package dev.gamerspvp.gladiador.mito;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev.gamerspvp.gladiador.Main;

public class MitoListener implements Listener {
	
	private Main instance;
	
	public MitoListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String playerName = player.getName();
		MitoManager mitoManager = instance.getMitoManager();
		if (!(mitoManager.isMito(playerName))) {
			return;
		}
		if (!(player.getKiller() instanceof Player)) {
			return;
		}
		mitoManager.setNewMito(player.getKiller());
	}
}