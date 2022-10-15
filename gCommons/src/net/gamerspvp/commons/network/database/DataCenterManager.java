package net.gamerspvp.commons.network.database;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.listeners.custom.BukkitMessageEvent;
import net.gamerspvp.commons.bungee.listeners.custom.BungeeMessageEvent;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.JedisPubSub;

public class DataCenterManager {
	@Getter
	private MySQL mysql;
	@Getter
	private Redis redis;
	
	public DataCenterManager(Configuration config) throws Exception {
		String mysqlUser = config.getString("MySQL.user");
		String mysqlHost = config.getString("MySQL.host");
		String mysqlDatabase = config.getString("MySQL.database");
		String msqlPassword = config.getString("MySQL.password");
		int mysqlPort = config.getInt("MySQL.port");
		
		String redisHost = config.getString("Redis.host");
		String redisPassword = config.getString("Redis.password");
		int redisPort = config.getInt("Redis.port");
		int redisDatabase = config.getInt("Redis.database");
		
		this.mysql = new MySQL(mysqlUser, mysqlHost, mysqlDatabase, msqlPassword, mysqlPort);
		this.redis = new Redis(redisHost, redisPassword, redisPort, redisDatabase);
		new Thread("Redis Subscriber") {
			@Override
			public void run() {
				redis.getJedis().subscribe(new JedisPubSub() {
					@Override
					public void onMessage(String channel, String message) {
						System.out.println("Redis Subscriber: Message recivied " + message);
						String channelMessage = message.split(";")[0];
						String json = message.split(";")[1];
						BungeeCord.getInstance().getPluginManager().callEvent(new BungeeMessageEvent(channelMessage, json));
					}
				}, "general");
			}
		}.start();
	}
	
	public DataCenterManager(boolean reciveMessages, FileConfiguration config, CommonsBukkit instance) {
		String mysqlUser = config.getString("MySQL.user");
		String mysqlHost = config.getString("MySQL.host");
		String mysqlDatabase = config.getString("MySQL.database");
		String msqlPassword = config.getString("MySQL.password");
		int mysqlPort = config.getInt("MySQL.port");
		
		String redisHost = config.getString("Redis.host");
		String redisPassword = config.getString("Redis.password");
		int redisPort = config.getInt("Redis.port");
		int redisDatabase = config.getInt("Redis.database");
		
		this.mysql = new MySQL(mysqlUser, mysqlHost, mysqlDatabase, msqlPassword, mysqlPort);
		this.redis = new Redis(redisHost, redisPassword, redisPort, redisDatabase);
		if (reciveMessages) {
			new Thread("Redis Subscriber") {
				@Override
				public void run() {
					redis.getJedis().subscribe(new JedisPubSub() {
						@Override
						public void onMessage(String channel, String message) {
							String channelMessage = StringUtils.split(message, ";")[0];
							String json = StringUtils.split(message, ";")[1];
							new BukkitRunnable() {
								
								@Override
								public void run() {
									Event serverMessageEvent = new BukkitMessageEvent(channelMessage, json);
									Bukkit.getPluginManager().callEvent(serverMessageEvent);
								}
							}.runTask(instance);
						}
					}, "general");
				}
			}.start();
		}
	}
}