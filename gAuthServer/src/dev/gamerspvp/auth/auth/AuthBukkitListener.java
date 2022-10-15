package dev.gamerspvp.auth.auth;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.auth.models.AuthPlayer;
import dev.gamerspvp.auth.captcha.customevents.CaptchaEvent;
import net.gamerspvp.commons.bukkit.utils.Titles;

public class AuthBukkitListener implements Listener {
	
	private Main instance;
	//private ReentrantLock lock;
	
	public AuthBukkitListener(Main instance) {
		this.instance = instance;
		//this.lock = new ReentrantLock();
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onCaptchaEvent(CaptchaEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					AuthPlayer authPlayer = new AuthPlayer(playerName);
					authPlayer.loadData(instance);
					AuthBukkitManager authManager = instance.getAuthManager();
					Titles title = new Titles();
					title.clearTitle(player);
					title.setTitle("§2§lGAMERSPVP");
					title.setFadeInTime(1);
					title.setFadeOutTime(1);
					title.setStayTime(600);
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 1);
					if (!(authPlayer.isRegistered())) {
					    title.setSubtitle("§fUtilize /register (senha) (senha)");
						player.sendMessage("§a[Auth] Efetue seu cadastro utilizando: §f/register (senha) (senha)");
					} else {
						title.setSubtitle("§fUtilize /login (senha)");
						player.sendMessage("§a[Auth] Efetue seu login utilizando: §f/login (senha)");
					}
					title.send(player);
					authPlayer.reset();
					authManager.cache(authPlayer);
				} catch (SQLException e) {
					e.printStackTrace();
					player.kickPlayer("§cDesculpe, não é possivel efetuar login no servidor no momento.");
				}
			}
		}.runTaskAsynchronously(instance);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String message = event.getMessage().toLowerCase();
		AuthPlayer authPlayer = instance.getAuthManager().getCache(playerName.toLowerCase());
		if (authPlayer == null) {
			return;
		}
		String[] allowedCmds = new String[] { "/login", "/register" };
		boolean blocked = true;
		for (String cmd : allowedCmds) {
			if ((message.startsWith(cmd + " ")) || (message.equalsIgnoreCase(cmd))) {
				blocked = false;
			}
		}
		if ((blocked) && (!(authPlayer.isAuthenticated()))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		AuthPlayer authPlayer = instance.getAuthManager().getCache(playerName.toLowerCase());
		if (authPlayer == null) {
			return;
		}
		if (authPlayer.isAuthenticated()) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		AuthPlayer authPlayer = instance.getAuthManager().getCache(player.getName().toLowerCase());
		if (authPlayer == null) {
			return;
		}
		if (authPlayer.isAuthenticated()) {
			return;
		}
		event.setCancelled(true);
	}
}