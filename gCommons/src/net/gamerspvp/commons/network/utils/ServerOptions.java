package net.gamerspvp.commons.network.utils;

import lombok.Getter;

public enum ServerOptions {

 CHAT(false),
 PVP(false),
 MANUTENCAO(false),
 DAMAGE(false);

 @Getter
 private boolean enabled;

 private ServerOptions(boolean enabled) {
	this.enabled = enabled;
 }

}
