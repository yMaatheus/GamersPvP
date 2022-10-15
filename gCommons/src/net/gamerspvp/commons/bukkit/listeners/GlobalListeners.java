package net.gamerspvp.commons.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.listeners.custom.BukkitMessageEvent;

public class GlobalListeners implements Listener {
	
	//private CommonsBukkit instance;
	
	public GlobalListeners(CommonsBukkit instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		//this.instance = instance;
	}

	@EventHandler
	public void onBukkitMessageEvent(BukkitMessageEvent event) {
		//Redis redis = instance.getDataCenter().getRedis();
		//Gson gson = redis.getGson();
		if (event.getChannel().equalsIgnoreCase("maintenance")) {
			/*ServerStatus serverStatus = gson.fromJson(event.getMessage(), ServerStatus.class);
			String serverName = serverStatus.getServerName().replace("server_", "");
			HashSet<Player> sending = new HashSet<Player>();
			if (instance.getServerName().equalsIgnoreCase(serverName)) {
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					onlinePlayer.sendMessage(new TextComponent("§a[Commons] Modo manutenção do servidor §f" + serverName + " §afoi ativado!"));
					if (!onlinePlayer.hasPermission("commons.admin")) {
						sending.add(onlinePlayer);
					}
				}
				instance.getServer().getPluginManager().getPlugin("GamersCombatLog").onDisable();
				new BukkitRunnable() {
					
					@Override
					public void run() {
						int i = 0;
						if (sending.isEmpty()) {
							cancel();
							return;
						}
						for (Player player : sending) {
							if (player == null) {
								sending.remove(player);
								continue;
							}
							if (i == 10) {
								continue;
							}
							player.sendMessage("§aEnviando você para o §fLobby§a.");
							instance.getBungeeChannelAPI().connect(player, "lobby");
							i++;
						}
					}
				}.runTaskTimer(instance, 1 * 20L, 3 * 20L);
			}*/
		}
	}
	 
}