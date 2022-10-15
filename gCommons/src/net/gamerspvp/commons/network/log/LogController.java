package net.gamerspvp.commons.network.log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

import lombok.Getter;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.utils.DateUtils;

public class LogController {
	
	@Getter
	private DataCenterManager dataCenterManager;
	
	public LogController(DataCenterManager dataCenterManager) throws Exception {
		this.dataCenterManager = dataCenterManager;
		MySQL mysql = dataCenterManager.getMysql();
		String query = "CREATE TABLE IF NOT EXISTS logs (`id` MEDIUMINT(20) UNSIGNED AUTO_INCREMENT,PRIMARY KEY (id), `date` TEXT, `key` TEXT, `playerName` TEXT, `log` TEXT, `reason` TEXT, `flagTime` BIGINT);";
		mysql.execute(query, false);
	}
	
	public boolean executeLog(String key, String playerName, String log, String reason) throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		boolean i = false;
		String query = "INSERT INTO logs (`date`, `key`, `playerName`, `log`, `reason`, `flagTime`) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		
		long currentMillis = System.currentTimeMillis();
		
		ps.setString(1, DateUtils.getDate(currentMillis));
		ps.setString(2, key);
		ps.setString(3, playerName);
		ps.setString(4, log);
		ps.setString(5, reason);
		ps.setLong(6, currentMillis);
		
		i = ps.execute();
		ps.close();
		return i;
	}
	
	public HashSet<String> getKeys() throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "SELECT DISTINCT `key` FROM logs;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		HashSet<String> keys = new HashSet<String>();
		while (rs.next()) {
			keys.add(rs.getString("key"));
		}
		rs.close();
		ps.close();
		return keys;
	}
}