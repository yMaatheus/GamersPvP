package dev.gamerspvp.auth.auth.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import dev.gamerspvp.auth.Main;
import lombok.Getter;
import lombok.Setter;
import net.gamerspvp.commons.network.database.MySQL;

@Getter
@Setter
public class AuthPlayer {
	
	private String name;
	private String playerName;
	private Password password;
	private String lastIp;
	private String registerDate;
	
	private boolean registered = false;
	private boolean authenticated = false; // verificar se já está autenticado
	private long joinedTime = 0;
    private int attempts = 0; // tentativas
	
	public AuthPlayer(String playerName) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		
		this.joinedTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(180);
		this.authenticated = false;
	}
	
	public AuthPlayer(String playerName, String password, String lastIp, String registerDate) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.password = new Password(password);
		this.lastIp = lastIp;
		this.registerDate = registerDate;
		this.registered = true;
		this.authenticated = false;
	}
	
	public void loadData(Main instance) throws SQLException {
		MySQL MySQL = instance.getDataCenter().getMysql();
		PreparedStatement pstmt = MySQL.getConnection().prepareStatement("SELECT * FROM auth WHERE name=?");
		pstmt.setString(1, name);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			this.password = new Password(rs.getString("password"));
			this.lastIp = rs.getString("lastIp");
			this.registerDate = rs.getString("registerDate");
			this.registered = true;
			this.authenticated = false;
		}
		rs.close();
		pstmt.close();
	}
	
	public void update(Main instance) {
		MySQL MySQL = instance.getDataCenter().getMysql();
		String query = "UPDATE auth SET lastIp=? WHERE name=?";
		try {
			PreparedStatement pstmt = MySQL.getConnection().prepareStatement(query);
			pstmt.setString(1, lastIp);
			pstmt.setString(2, name);
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void register(Main instance) {
		MySQL MySQL = instance.getDataCenter().getMysql();
		String query = "INSERT INTO auth (name, playerName, password, lastIp, registerDate) VALUES (?, ?, ?, ?, ?);";
		try {
			PreparedStatement pstmt = MySQL.getConnection().prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, playerName);
			pstmt.setString(3, password.getHash());
			pstmt.setString(4, lastIp);
			pstmt.setString(5, registerDate);
			
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void unregister(Main instance) throws SQLException {
		String query = "DELETE FROM auth WHERE name=?";
		PreparedStatement pstmt = instance.getDataCenter().getMysql().getConnection().prepareStatement(query);
		pstmt.setString(1, playerName);
		
        pstmt.executeUpdate();
        pstmt.close();
	}
	
	public void changepassword(String playerName, String hashedPassword, Main instance) throws SQLException {
		MySQL MySQL = instance.getDataCenter().getMysql();
		String query = "UPDATE auth SET password=? WHERE name=?";
		PreparedStatement pstmt = MySQL.getConnection().prepareStatement(query);
		pstmt.setString(1, hashedPassword);
		pstmt.setString(2, playerName);
		
		pstmt.executeUpdate();
		pstmt.close();
	}
	
	public void reset() {
		this.authenticated = false;
		this.joinedTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(180);
		this.attempts = 0;
	}
	
	public Password password(String hashedPassword) {
		return new Password(hashedPassword);
	}
	
	public class Password {
		
		private String hash;
		private String salt;
		
		public Password(String hash, String salt) {
			this.hash = hash;
			this.salt = salt;
		}
		
		public Password(String hash) {
			this.hash = hash;
			this.salt = null;
		}
		
		public String getHash() {
			return hash;
		}
		
		public String getSalt() {
			return salt;
		}
	}
}