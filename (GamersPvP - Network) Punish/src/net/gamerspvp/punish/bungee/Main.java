package net.gamerspvp.punish.bungee;

import java.sql.SQLException;

import com.google.gson.Gson;

import net.gamerspvp.punish.bungee.utils.BungeeConfig;
import net.gamerspvp.punish.network.database.MySQL;
import net.gamerspvp.punish.network.database.Redis;
import net.gamerspvp.punish.network.models.Ban;
import net.gamerspvp.punish.network.models.Banip;
import net.gamerspvp.punish.network.models.Kick;
import net.gamerspvp.punish.network.utils.TimeManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Main extends Plugin {
	
	private static Main instance;
	private MySQL mysql;
	private Redis redis;
	private Gson gson;
	
	@Override
	public void onEnable() {
		instance = this;
		Configuration config = new BungeeConfig("database.yml", this).getConfig();
		String mysqlUser = config.getString("MySQL.user");
		String mysqlHost = config.getString("MySQL.host");
		String mysqlDatabase = config.getString("MySQL.database");
		String msqlPassword = config.getString("MySQL.password");
		int mysqlPort = config.getInt("MySQL.port");
		
		String redisHost = config.getString("Redis.host");
		String redisPassword = config.getString("Redis.password");
		int redisPort = config.getInt("Redis.port");
		this.mysql = new MySQL(mysqlUser, mysqlHost, mysqlDatabase, msqlPassword, mysqlPort);
		this.redis = new Redis(redisHost, redisPassword, redisPort);
		this.gson = new Gson();
		try {
			mysql.execute("CREATE TABLE IF NOT EXISTS bans (Name TEXT, PlayerRealName TEXT, Reason TEXT, Author TEXT, Type TEXT, Time BIGINT, Date TEXT)", false);
			mysql.execute("CREATE TABLE IF NOT EXISTS bansip (Ip TEXT, Reason TEXT, Author TEXT, Type TEXT, Time BIGINT, Date TEXT)", false);
			mysql.execute("CREATE TABLE IF NOT EXISTS mutes (Name TEXT, PlayerRealName TEXT, MuteAll BOOL, Reason TEXT, Author TEXT, Time BIGINT, Date TEXT)", false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		new PostLogin(instance);
		subscribe();
	}

	@Override
	public void onLoad() {
		instance = this;
	}

	@Override
	public void onDisable() {

	}

	public void subscribe() {
		Jedis jedis = redis.getJedis();
		jedis.connect();
		new Thread("Redis Subscriber") {
			@Override
			public void run() {
				jedis.subscribe(new JedisPubSub() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void onMessage(String channel, String message) {
						if (channel.equalsIgnoreCase("ban")) {
							Ban ban = gson.fromJson(message, Ban.class);
							ban.compute(instance);
							getProxy().broadcast(new TextComponent(""));
							getProxy().broadcast(new TextComponent("§c(!) " + ban.getPlayerRealName() + " foi banido §l" + ban.getType()));
							getProxy().broadcast(new TextComponent("§c(!) Autor: " + ban.getAuthor()));
							// if (ban.getType().equalsIgnoreCase("TEMPOR§RIO")){getProxy().broadcast(new
							// TextComponent("§c(!) Dura§§o: " + TimeManager.getTimeEnd(ban.getTime())));}
							getProxy().broadcast(new TextComponent("§c(!) Motivo: " + ban.getReason()));
							getProxy().broadcast(new TextComponent("§c(!) Data: " + ban.getDate()));
							getProxy().broadcast(new TextComponent(""));
							if (getProxy().getPlayer(ban.getPlayerRealName()) != null) {
								ProxiedPlayer proxiedPlayer = getProxy().getPlayer(ban.getPlayerRealName());
								String format = null;
								if (ban.getType().equalsIgnoreCase("TEMPOR§RIO")) {
									format = getBanFormat(true).replace("@type", ban.getType())
											.replace("@time", TimeManager.getTimeEnd(ban.getTime()))
											.replace("@reason", ban.getReason()).replace("@author", ban.getAuthor())
											.replace("@date", ban.getDate());
								} else {
									format = getBanFormat(false).replace("@type", ban.getType())
											.replace("@reason", ban.getReason()).replace("@author", ban.getAuthor())
											.replace("@date", ban.getDate());
								}
								proxiedPlayer.disconnect(new TextComponent(format));
							}
						}
						if (channel.equalsIgnoreCase("kick")) {
							Kick kick = new Gson().fromJson(message, Kick.class);
							if (getProxy().getPlayer(kick.getPlayerRealName()) == null) {
								getProxy().getPlayer(kick.getAuthor()).sendMessage(new TextComponent("§c" + kick.getPlayerRealName() + " n§o foi encontrado."));
								return;
							}
							if (getProxy().getPlayer(kick.getPlayerRealName()) != null) {
								ProxiedPlayer proxiedPlayer = getProxy().getPlayer(kick.getPlayerRealName());
								getProxy().broadcast(new TextComponent(""));
								getProxy().broadcast(new TextComponent("§c(!) " + kick.getPlayerRealName() + " foi expulso por " + kick.getAuthor()));
								getProxy().broadcast(new TextComponent("§c(!) Motivo: " + kick.getReason()));
								getProxy().broadcast(new TextComponent("§c(!) Data: " + kick.getDate()));
								getProxy().broadcast(new TextComponent(""));
								String format = getKickFormat().replace("@reason", kick.getReason()).replace("@author", kick.getAuthor()).replace("@date", kick.getDate());
								proxiedPlayer.disconnect(new TextComponent(format));
							}
						}
						if (channel.equalsIgnoreCase("banip")) {
							Banip banip = new Gson().fromJson(message, Banip.class);
							banip.compute(instance);
							getProxy().broadcast(new TextComponent(""));
							getProxy().broadcast(new TextComponent("§c(!) O Ip(***.***.***.***) foi banido §l" + banip.getType()));
							getProxy().broadcast(new TextComponent("§c(!) Autor: " + banip.getAuthor()));
							// if (banip.getType().equalsIgnoreCase("TEMPOR§RIO")) {getProxy().broadcast(new
							// TextComponent("§c(!) Dura§§o: " + TimeManager.getTimeEnd(banip.getTime())));}
							getProxy().broadcast(new TextComponent("§c(!) Motivo: " + banip.getReason()));
							getProxy().broadcast(new TextComponent("§c(!) Data: " + banip.getDate()));
							getProxy().broadcast(new TextComponent(""));
							if (!getProxy().getPlayers().isEmpty()) {
								for (ProxiedPlayer proxiedPlayer : getProxy().getPlayers()) {
									if (proxiedPlayer.getAddress().getHostString().equalsIgnoreCase(banip.getIp())) {
										String format;
										if (banip.getType().equalsIgnoreCase("TEMPOR§RIO")) {
											format = getBanIPFormat(true).replace("@ip", banip.getIp())
													.replace("@type", banip.getType())
													.replace("@time", TimeManager.getTimeEnd(banip.getTime()))
													.replace("@reason", banip.getReason())
													.replace("@author", banip.getAuthor())
													.replace("@date", banip.getDate());
										} else {
											format = getBanIPFormat(false).replace("@ip", banip.getIp())
													.replace("@type", banip.getType())
													.replace("@reason", banip.getReason())
													.replace("@author", banip.getAuthor())
													.replace("@date", banip.getDate());
										}
										proxiedPlayer.disconnect(new TextComponent(format));
									}
								}
							}
						}
					}
				}, "ban", "kick", "banip");
			};
		}.start();
		System.out.println("Subscribe complete!");
	}
	
	public String getBanIPFormat(boolean temporary) {
		String format;
		if (temporary) {
			format = "§c§lGAMERSPVP\n\n§cSeu §lIP§c(@ip) est§ banido §lTEMPOR§RIAMENTE §cdo servidor.\n§cTempo: §f@time\n§cMotivo: §f@reason\n§cPor: §f@author\n\n§fEm @date";
		} else {
			format = "§c§lGAMERSPVP\n\n§c§cSeu §lIP§c(@ip) est§ banido §l@type §cdo servidor.\n§cMotivo: §f@reason\n§cPor: §f@author\n\n§fEm @date";
		}
		return format;
	}
	
	public String getBanFormat(boolean temporary) {
		String format;
		if (temporary) {
			format = "§c§lGAMERSPVP\n\n§cVoc§ est§ banido §lTEMPOR§RIAMENTE §cdo servidor.\n§cTempo: §f@time\n§cMotivo: §f@reason\n§cPor: §f@author\n\n§fEm @date";
		} else {
			format = "§c§lGAMERSPVP\n\n§cVoc§ est§ banido §l@type §cdo servidor.\n§cMotivo: §f@reason\n§cPor: §f@author\n\n§fEm @date";
		}
		return format;
	}
	
	public String getKickFormat() {
		String format = "§c§lGAMERSPVP\n\n§cVoc§ foi expulso do servidor\n§cMotivo: §f@reason\n§cPor: §f@author\n\n§fEm @date";
		return format;
	}

	public static Main getInstance() {
		return instance;
	}
	
	public MySQL getMySQL() {
		return mysql;
	}

	public Redis getRedis() {
		return redis;
	}

	public Gson getGson() {
		return gson;
	}
}