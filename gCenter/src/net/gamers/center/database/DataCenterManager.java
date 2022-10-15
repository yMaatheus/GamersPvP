package net.gamers.center.database;

import org.simpleyaml.configuration.file.FileConfiguration;

import lombok.Getter;

public class DataCenterManager {
	@Getter
	private MySQL mysql;
	@Getter
	private Redis redis;
	
	public DataCenterManager(FileConfiguration config) throws Exception {
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