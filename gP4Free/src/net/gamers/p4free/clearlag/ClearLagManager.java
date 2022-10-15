package net.gamers.p4free.clearlag;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import net.gamers.p4free.Main;

public class ClearLagManager {
	
	private Main instance;
	
	@Getter
	private int drop;
	private boolean closed;
	
	public ClearLagManager(Main instance) {
		this.instance = instance;
		this.drop = 0;
		this.closed = false;
		new LaggCommand(instance);
		new ClearLagListener(instance);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				executeClearDrops(false);
			}
		}.runTaskLater(instance, 20L);
	}
	
	public void executeClearDrops(boolean announce) {
		int cleared = 0;
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof Player) {
					continue;
				}
				cleared++;
				entity.remove();
			}
		}
		if (announce) {
			Bukkit.broadcastMessage("§c[!] §7Foram removidos §c§l" + cleared + " §7itens do ch§o.");
		}
		drop = 0;
		closed = false;
	}
	
	public void addDrops(int drops) {
		if (closed) {
			return;
		}
		this.drop = drop + drops;
		if (drop >= 500) {
			this.closed = true;
			Bukkit.broadcastMessage("§c[!] §7O Ch§o ser§ limpo em §c§l60§7 segundos!");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					Bukkit.broadcastMessage("§c[!] §7O Ch§o ser§ limpo em §c§l30§7 segundos!");
					new BukkitRunnable() {
						
						@Override
						public void run() {
							Bukkit.broadcastMessage("§c[!] §7O Ch§o ser§ limpo em §c§l10§7 segundos!");
							new BukkitRunnable() {
								
								@Override
								public void run() {
									executeClearDrops(true);
								}
							}.runTaskLaterAsynchronously(instance, 10 * 20L);
						}
					}.runTaskLaterAsynchronously(instance, 20 * 20L);
				}
			}.runTaskLaterAsynchronously(instance, 30 * 20L);
		}
	}
	
	public void removeDrops(int drops) {
		this.drop = drop - drops;
	}
}