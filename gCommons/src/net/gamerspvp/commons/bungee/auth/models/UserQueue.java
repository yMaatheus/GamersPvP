package net.gamerspvp.commons.bungee.auth.models;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Getter
@Setter
public class UserQueue {
	
	private ProxiedPlayer proxiedPlayer;
	private long flagTime;
	private boolean vip;
	
	public UserQueue(ProxiedPlayer proxiedPlayer, boolean vip) {
		this.proxiedPlayer = proxiedPlayer;
		this.flagTime = System.currentTimeMillis();
		this.vip = vip;
	}
	
	public UserQueue(ProxiedPlayer proxiedPlayer, long flagTime, boolean vip) {
		this.proxiedPlayer = proxiedPlayer;
		this.flagTime = flagTime;
		this.vip = vip;
	}
}