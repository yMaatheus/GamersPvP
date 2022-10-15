package dev.gamerspvp.auth.auth;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.auth.commands.ForceLoginCommand;
import dev.gamerspvp.auth.auth.commands.LoginCommand;
import dev.gamerspvp.auth.auth.commands.RegisterCommand;
import dev.gamerspvp.auth.auth.customevents.PlayerAuthEvent;
import dev.gamerspvp.auth.auth.models.AuthPlayer;
import dev.gamerspvp.auth.utils.FilterManager;
import dev.gamerspvp.auth.utils.PasswordManager;
import net.gamerspvp.commons.bukkit.utils.Titles;

public class AuthBukkitManager {
	
	private Main instance;
	
	private PasswordManager passwordManager;
	private HashMap<String, AuthPlayer> cache;
	
	public AuthBukkitManager(Main instance) throws Exception {
		this.instance = instance;
		this.passwordManager = new PasswordManager();
		this.cache = new HashMap<String, AuthPlayer>();
		instance.getDataCenter().getMysql().createTable("auth", "`name` VARCHAR(20),", "`playerName` VARCHAR(20),", "`password` TEXT,", "`lastIp` TEXT,", "`registerDate` VARCHAR(20)");
		new ForceLoginCommand(instance);
		new LoginCommand(instance);
		new RegisterCommand(instance);
		new AuthBukkitListener(instance);
		new FilterManager("/register", "/login", "/mudarsenha");
	}
	
	public void sendMessageAuth(Player player) {
		new Titles().clearTitle(player);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 1.0F);
		new BukkitRunnable() {
			public void run() {
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10.0F, 1.0F);
				instance.getDataCenter().getRedis().publish("auth;" + player.getName());
				Bukkit.getPluginManager().callEvent(new PlayerAuthEvent(player));
			}
		}.runTaskLater(instance, 20L);
	}
	
	public String passwordEncrypt(String password) {
		String salt = passwordManager.generateSalt();
		return passwordManager.computeHash(password, salt);
	}
	
	public boolean checkPassword(AuthPlayer authPlayer, String password) {
		if (authPlayer != null) {
			if (passwordManager.comparePassword(password, authPlayer.getPassword())) {
				return true;
			}
		}
		return false;
	}
	
	public void authInfo(String playerName, String message) {
		System.out.println("[Auth] " + playerName + "" + message);
	}
	
	public AuthPlayer getAuthPlayer(String playerName) throws SQLException {
		AuthPlayer authPlayer = cache.get(playerName.toLowerCase());
		if (authPlayer == null) {
			authPlayer = new AuthPlayer(playerName);
			authPlayer.loadData(instance);
		}
		return authPlayer;
	}
	
	public AuthPlayer getCache(String playerName) {
		return cache.get(playerName);
	}
	
	public void cache(AuthPlayer authPlayer) {
		cache.put(authPlayer.getName(), authPlayer);
	}
	
	public HashMap<String, AuthPlayer> getCache() {
		return cache;
	}
}