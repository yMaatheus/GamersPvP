package net.gamerspvp.commons.bungee.listeners;

import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.listeners.custom.BungeeMessageEvent;
import net.gamerspvp.commons.bungee.maintenance.models.Maintenance;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.NetworkOptions;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class GlobalListeners implements Listener {
	
	private ProxiedCommons instance;
	
	private long timestamp = 0;
	private String serverIp = MessageUtils.SERVER_IP.getMessage();
	private String discord = MessageUtils.SERVER_DISCORD.getMessage();
	private String site = MessageUtils.SERVER_SITE.getMessage();
	
	public GlobalListeners(ProxiedCommons instance) {
		instance.getProxy().getPluginManager().registerListener(instance, this);
		this.instance = instance;
	}
	
	@EventHandler
	public void onProxyPingEvent(ProxyPingEvent event) {
		//definir quantos jogadores tem em todos os bungee's online
		//sistema de motd e manutenção
		ServerPing response = event.getResponse();
		NetworkOptions networkOptions = instance.getNetworkOptions();
		Maintenance maintenance = networkOptions.getMaintenance();
		if (maintenance == null) {
			response.setDescriptionComponent(new TextComponent(networkOptions.getMotd()));
			response.getPlayers().setSample(networkOptions.getPlayerPingList());
			response.getVersion().setName("GAMERSPVP");
		} else {
			response.setDescriptionComponent(new TextComponent(networkOptions.getMotdMaintenance()));
			response.getVersion().setProtocol(1);
			response.getVersion().setName("§cManutenção");
			
			response.getPlayers().setSample(networkOptions.getPlayerPingListMaintenance());
		}
		event.setResponse(response);
	}
	
	@EventHandler
	public void onPostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		String header = "\n§2§lGAMERS\n§f    " + serverIp + "\n";
		String footer = "\n§2Discord:§f " + discord + "\n\n§2Adquira cash em nossa loja, acesse:§f " + site;
		proxiedPlayer.setTabHeader(new TextComponent(header), new TextComponent(footer));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPreLoginEvent(PreLoginEvent event) {
		if (timestamp > System.currentTimeMillis()) {
			event.setCancelled(true);
			event.setCancelReason(new TextComponent("§cMuitos jogadores estão tentando conectar no momento, tente novamente!"));
			return;
		}
		String name = event.getConnection().getName();
		int length = name.length();
		if (length < 3 || length > 16 || !name.matches("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_]+")) {
			event.setCancelled(true);
			event.setCancelReason(new TextComponent("§cSeu nome de usuário não é permitido no servidor."));
			return;
		}
		timestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1);
	}
	
	@EventHandler
	public void onBungeeMessageEvent(BungeeMessageEvent event) {
		Redis redis = instance.getDataCenter().getRedis();
		Gson gson = redis.getGson();
		if (event.getChannel().equalsIgnoreCase("network_options")) {
			NetworkOptions networkOptions = gson.fromJson(event.getMessage(), NetworkOptions.class);
			instance.setNetworkOptions(networkOptions);
		}
	}
}