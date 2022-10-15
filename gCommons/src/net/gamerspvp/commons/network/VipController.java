package net.gamerspvp.commons.network;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.database.Redis;
import net.gamerspvp.commons.network.models.PlayerVip;
import net.gamerspvp.commons.network.utils.RandomStringUtils;
import net.gamerspvp.commons.network.utils.TimeManager;

public class VipController {
	
	@Getter
	private DataCenterManager dataCenterManager;
	
	public VipController(DataCenterManager dataCenterManager) throws Exception {
		this.dataCenterManager = dataCenterManager;
		MySQL mysql = dataCenterManager.getMysql();
		mysql.createTable("vips", "`name` TEXT,", "`playerName` TEXT,", "`vips` TEXT");
		mysql.createTable("vipKeys", "`key` TEXT,",  "`author` TEXT,", "`group` TEXT,", "`time` BIGINT");
	}
	
	public PlayerVip getPlayerVip(String userName) throws Exception {
		PlayerVip playerVip = null;
		MySQL mysql = dataCenterManager.getMysql();
		PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT * FROM vips WHERE name=?;");
		ps.setString(1, userName.toLowerCase());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String playerName = rs.getString("playerName");
			String vips = rs.getString("vips");
			
			playerVip = new PlayerVip(playerName, jsonfromHash(vips, dataCenterManager.getRedis()));
		}
		rs.close();
		ps.close();
		return playerVip;
	}
	
	public synchronized void updatePlayerVip(PlayerVip playerVip) throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		String name = playerVip.getName();
		if (getPlayerVip(name) != null) {
			String query = "UPDATE vips SET `vips`=? WHERE `name`=?";
			PreparedStatement ps = mysql.getConnection().prepareStatement(query);
			ps.setString(1, toJsonHash(playerVip.getVips(), dataCenterManager.getRedis()));
			ps.setString(2, name);
			
			ps.executeUpdate();
			ps.close();
			return;
		}
		// insert
		String query = "INSERT INTO vips (`name`, `playerName`, `vips`) VALUES (?, ?, ?);";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		
		ps.setString(1, name);
		ps.setString(2, playerVip.getPlayerName());
		ps.setString(3, toJsonHash(playerVip.getVips(), dataCenterManager.getRedis()));
		
		ps.execute();
		ps.close();
	}
	
	public HashSet<PlayerVip> getAllVips() throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "SELECT * FROM vips;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		HashSet<PlayerVip> playersVips = new HashSet<PlayerVip>();
		while (rs.next()) {
			String playerName = rs.getString("playerName");
			String vips = rs.getString("vips");
			
			playersVips.add(new PlayerVip(playerName, jsonfromHash(vips, dataCenterManager.getRedis())));
		}
		rs.close();
		ps.close();
		return playersVips;
	}
	
	public void purgePlayerVip(String playerName) throws SQLException {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "DELETE FROM vips WHERE `name`=?;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ps.setString(1, playerName.toLowerCase());
		
		ps.execute();
		ps.close();
	}
	
	public HashSet<VipKey> getAllKeys() throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "SELECT * FROM vipKeys;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		HashSet<VipKey> keys = new HashSet<VipController.VipKey>();
		while (rs.next()) {
			String key = rs.getString("key");
			String author = rs.getString("author");
			String group = rs.getString("group");
			long time = rs.getLong("time");
			
			keys.add(new VipKey(key, author, group, time));
		}
		rs.close();
		ps.close();
		return keys;
	}
	
	public VipKey searchKey(String key) throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "SELECT * FROM vipKeys WHERE `key`='" + key + "';";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		VipKey vipKey = null;
		if (rs.next()) {
			String author = rs.getString("author");
			String group = rs.getString("group");
			long time = rs.getLong("time");
			
			vipKey = new VipKey(key, author, group, time);
		}
		rs.close();
		ps.close();
		return vipKey;
	}
	
	public void computeKey(String key, String author, String groupName, int days) throws Exception {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "INSERT INTO vipKeys (`key`, `author`, `group`, `time`) VALUES (?, ?, ?, ?);";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query.toString());
		ps.setString(1, key);
		ps.setString(2, author);
		ps.setString(3, groupName);
		ps.setLong(4, TimeManager.generateTime("dias", days));
		
		ps.execute();
		ps.close();
	}
	
	public void purgeKey(String key) throws SQLException {
		MySQL mysql = dataCenterManager.getMysql();
		String query = "DELETE FROM vipKeys WHERE `key`=?;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ps.setString(1, key);
		
		ps.execute();
		ps.close();
	}
	
	public String generateKey() throws Exception {
		String key = RandomStringUtils.generate(10).toUpperCase();
		if (searchKey(key) != null) {
			return generateKey();
		}
		return key;
	}
	
	private HashMap<String, Long> jsonfromHash(String json, Redis redis) {
		Type type = new TypeToken<HashMap<String, Long>>(){}.getType();
		HashMap<String, Long> vips = redis.getGson().fromJson(json, type);
		return vips;
	}
	
	private String toJsonHash(HashMap<String, Long> vips, Redis redis) {
		return redis.getGson().toJson(vips);
	}
	
	@Getter
	public class VipKey {
		
		private String key;
		private String author;
		private String group;
		private long time;
		
		public VipKey(String key, String author, String group, long time) {
			this.key = key;
			this.author = author;
			this.group = group;
			this.time = time;
		}
	}
}