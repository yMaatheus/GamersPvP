package net.gamerspvp.central.database;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;

public class DataCenterManager {
	@Getter
	private MySQL mysql;
	@Getter
	private Redis redis;
	
	public DataCenterManager(Configuration config) {
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
	}
}