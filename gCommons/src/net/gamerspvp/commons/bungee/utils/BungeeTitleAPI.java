package net.gamerspvp.commons.bungee.utils;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Getter
@Setter
public class BungeeTitleAPI {
	
	private String title;
	private String subtitle;
	private int fadeIn;
	private int stay;
	private int fadeOut;
	
	public BungeeTitleAPI(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}
	
	public void send(ProxiedPlayer proxiedPlayer) {
		BungeeTitle packet = new BungeeTitle();
		packet.title(TextComponent.fromLegacyText(title));
		packet.subTitle(TextComponent.fromLegacyText(subtitle));
		packet.fadeIn(fadeIn);
		packet.fadeOut(fadeOut);
		packet.stay(stay);
		
		if (packet != null) {
			packet.send(proxiedPlayer);
		}
	}
	
	public static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		BungeeTitle packet = new BungeeTitle();
		packet.title(TextComponent.fromLegacyText(title));
		packet.subTitle(TextComponent.fromLegacyText(subtitle));
		packet.fadeIn(fadeIn);
		packet.fadeOut(fadeOut);
		packet.stay(stay);
		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			packet.send(player);
		}
	}
	
	public static void sendTitle(ProxiedPlayer proxiedPlayer, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		BungeeTitle packet = new BungeeTitle();
		packet.title(TextComponent.fromLegacyText(title));
		packet.subTitle(TextComponent.fromLegacyText(subtitle));
		packet.fadeIn(fadeIn);
		packet.fadeOut(fadeOut);
		packet.stay(stay);
		packet.send(proxiedPlayer);
	}
}