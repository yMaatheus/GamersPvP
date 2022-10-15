package net.gamerspvp.commons.bungee.auth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import lombok.Getter;
import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.auth.commands.AuthCommand;
import net.gamerspvp.commons.bungee.auth.commands.MudarSenhaCommand;
import net.gamerspvp.commons.bungee.auth.models.AuthPlayer;
import net.gamerspvp.commons.bungee.utils.PasswordManager;

@Getter
public class AuthProxyManager {
	
	private ProxiedCommons instance;
	
	private PasswordManager passwordManager;
	private AuthQueueManager authQueueManager;
	
	private HashMap<String, AuthProxy> cache;
	
	public AuthProxyManager(ProxiedCommons instance) throws Exception {
		this.instance = instance;
		this.passwordManager = new PasswordManager();
		this.authQueueManager = new AuthQueueManager(instance);
		this.cache = new HashMap<>();
		new AuthCommand(instance);
		new MudarSenhaCommand(instance);
		new AuthProxyListener(instance);
	}
	
	public AuthPlayer getAuthPlayer(String playerName) {
		try {
			PreparedStatement pstmt = instance.getDataCenter().getMysql().getConnection().prepareStatement("SELECT * FROM auth WHERE name=?");
			pstmt.setString(1, playerName.toLowerCase());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String password = rs.getString("password");
				String lastIp = rs.getString("lastIp");
				String registerDate = rs.getString("registerDate");
				return new AuthPlayer(playerName, password, lastIp, registerDate);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String passwordEncrypt(String password) {
		String salt = passwordManager.generateSalt();
		return passwordManager.computeHash(password, salt);
	}
	
	public boolean checkPassword(String playerName, String password) throws SQLException {
		AuthPlayer authPlayer = getAuthPlayer(playerName);
		checkPassword(authPlayer, password);
		return false;
	}
	
	public boolean checkPassword(AuthPlayer authPlayer, String password) {
		if (authPlayer != null) {
			if (passwordManager.comparePassword(password, authPlayer.getPassword())) {
				return true;
			}
		}
		return false;
	}
	
	public void cache(AuthProxy authProxy) {
		cache.put(authProxy.getName().toLowerCase(), authProxy);
	}
	
	public AuthProxy getCache(String playerName) {
		return cache.get(playerName.toLowerCase());
	}
}