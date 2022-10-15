package dev.gamerspvp.gladiador.topclans;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;
import net.sacredlabyrinth.phaed.simpleclans.events.DisbandClanEvent;

public class ClanTopListener implements Listener {
	
	private Main instance;
	
	public ClanTopListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onDisbandClanEvent(DisbandClanEvent event) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					ClanTopManager clanTopManager = instance.getClanTopManager();
					clanTopManager.delete(event.getClan().getTag());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(instance);
	}
}