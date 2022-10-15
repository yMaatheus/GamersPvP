package net.gamerspvp.commons.bungee.auth;

public class AuthProxy {
	
	private String name = null;
	private boolean authenticated = false;
	
	public AuthProxy(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAuthenticated() {
		return authenticated;
	}
	
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
}
