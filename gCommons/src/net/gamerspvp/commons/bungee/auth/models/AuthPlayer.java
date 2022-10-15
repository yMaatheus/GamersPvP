package net.gamerspvp.commons.bungee.auth.models;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

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