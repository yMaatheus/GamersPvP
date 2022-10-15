package net.gamerspvp.punish.bungee;

import java.sql.SQLException;

import net.gamerspvp.punish.network.controllers.DatabaseController;
import net.gamerspvp.punish.network.models.Ban;
import net.gamerspvp.punish.network.models.Banip;
import net.gamerspvp.punish.network.utils.TimeManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLogin implements Listener {
	
	private Main instance;
	
	public PostLogin(Main instance) {
		instance.getProxy().getPluginManager().registerListener(instance, this);
		this.instance = instance;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String name = event.getPlayer().getName().toLowerCase();
		String ip = event.getPlayer().getAddress().getHostString();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					DatabaseController mysql = new DatabaseController();
					String format = null;
					if (mysql.hasIpBanned(ip, true, true)) {
						Banip banip = mysql.getBanip();
						if (banip.getType().equalsIgnoreCase("TEMPORÁRIO")) {
							format = instance.getBanIPFormat(true).replace("@ip", banip.getIp()).replace("@type", banip.getType()).replace("@time", TimeManager.getTimeEnd(banip.getTime())).replace("@reason", banip.getReason()).replace("@author", banip.getAuthor()).replace("@date", banip.getDate());
							if (System.currentTimeMillis() >= banip.getTime()) {
								mysql.executeUnbanIP(banip.getIp(), true);
								return;
							}
						} else {
							format = instance.getBanIPFormat(false).replace("@ip", banip.getIp()).replace("@type", banip.getType()).replace("@reason", banip.getReason()).replace("@author", banip.getAuthor()).replace("@date", banip.getDate());
						}
						player.disconnect(new TextComponent(format));
					} else if (mysql.hasBanned(name, true, true)) {
						Ban ban = mysql.getBan();
						if (ban.getType().equalsIgnoreCase("TEMPORÁRIO")) {
							format = instance.getBanFormat(true).replace("@type", ban.getType()).replace("@time", TimeManager.getTimeEnd(ban.getTime())).replace("@reason", ban.getReason()).replace("@author", ban.getAuthor()).replace("@date", ban.getDate());
							if (System.currentTimeMillis() >= ban.getTime()) {
								mysql.executeUnban(ban.getName(), true);
								return;
							}
						} else {
							format = instance.getBanFormat(false).replace("@type", ban.getType()).replace("@reason", ban.getReason()).replace("@author", ban.getAuthor()).replace("@date", ban.getDate());
						}
						player.disconnect(new TextComponent(format));
					}
				} catch (SQLException e) {
					player.disconnect(new TextComponent("§cDesculpe, mas não é possivel fazer conexão com o servidor."));
					e.printStackTrace();
				}
			}
		}).run();
	}
}