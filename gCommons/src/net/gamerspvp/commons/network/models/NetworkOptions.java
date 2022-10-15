package net.gamerspvp.commons.network.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.maintenance.models.Maintenance;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.database.Redis;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import redis.clients.jedis.Jedis;

@Getter
@Setter
public class NetworkOptions {
	
	private String motd;
	private String motdMaintenance;
	private PlayerInfo[] playerPingList;
	private PlayerInfo[] playerPingListMaintenance;
	private Maintenance maintenance;
	
	public NetworkOptions(ProxiedCommons instance) throws Exception {
		DataCenterManager dataCenterManager = instance.getDataCenter();
		MySQL mysql = dataCenterManager.getMysql();
		createTable(mysql);
		Redis redis = dataCenterManager.getRedis();
		Jedis jedis = redis.getJedis();
		Gson gson = redis.getGson();
		this.motd = "";
		this.motdMaintenance = "";
		List<String> listPing = loadPlayerListPing();
		List<String> listPingMaintenance = loadPlayerListPingMaintenance();
		this.playerPingList = new PlayerInfo[listPing.size()];
		for (int i = 0; i < listPing.size(); i++) {
			playerPingList[i] = new PlayerInfo(listPing.get(i), "");
		}
		this.playerPingListMaintenance = new PlayerInfo[listPingMaintenance.size()];
		for (int i = 0; i < listPingMaintenance.size(); i++) {
			playerPingListMaintenance[i] = new PlayerInfo(listPingMaintenance.get(i), "");
		}
		this.maintenance = null;
		if (jedis == null) {
			return;
		}
		if (!jedis.exists("network_options")) {
			if (getServerOptions(jedis, mysql, gson) == null) {
				return;
			}
		}
		load(jedis, gson);
	}
	
	public void load(Jedis jedis, Gson gson) {
		String json = jedis.get("network_options");
		NetworkOptions networkOptions = gson.fromJson(json, NetworkOptions.class);
		this.motd = networkOptions.getMotd();
		this.motdMaintenance = networkOptions.getMotdMaintenance();
		this.maintenance = networkOptions.getMaintenance();
	}
	
	public void publish(ProxiedCommons instance) {
		DataCenterManager dataCenterManager = instance.getDataCenter();
		MySQL mysql = dataCenterManager.getMysql();
		Redis redis = dataCenterManager.getRedis();
		Jedis jedis = redis.getJedis();
		Gson gson = redis.getGson();
		if (jedis == null) {
			return;
		}
		String json = gson.toJson(this);
		jedis.set("network_options", json);
		jedis.publish("general", "network_options;" + json);
		saveDatabase(mysql, json);
	}
	
	public void createTable(MySQL mysql) {
		String query = "CREATE TABLE IF NOT EXISTS serverOptions (id MEDIUMINT(8) UNSIGNED AUTO_INCREMENT, PRIMARY KEY (id), chave TEXT, json TEXT);";
		try {
			mysql.execute(query, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveDatabase(MySQL mysql, String json) {
		try {
			Statement statement = mysql.getConnection().createStatement();
			statement.execute("DELETE FROM serverOptions WHERE chave='network_options';");
			statement.execute("REPLACE INTO serverOptions (chave, json) VALUES ('network_options', '" + json +"');");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkOptions getServerOptions(Jedis jedis, MySQL mysql, Gson gson) {
		String query = "SELECT * FROM serverOptions WHERE chave=?";
		try {
			PreparedStatement ps = mysql.getConnection().prepareStatement(query);
			ps.setString(1, "network_options");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				NetworkOptions networkOptions = gson.fromJson(rs.getString("json"), NetworkOptions.class);
				this.motd = networkOptions.getMotd();
				this.motdMaintenance = networkOptions.getMotdMaintenance();
				this.maintenance = networkOptions.getMaintenance();
				jedis.set("network_options", gson.toJson(this));
				return networkOptions;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {}
		return null;
	}
	
	public List<String> loadPlayerListPing() {
		List<String> listPing = new ArrayList<String>();
		listPing.add("§2§lGAMERSPVP §71.8.x");
		listPing.add("§fClique duas vezes para se conectar.");
		listPing.add("§a");
		listPing.add("§2Informações:");
		listPing.add("§6* Loja:§f loja.gamerspvp.net");
		listPing.add("§6* Discord:§f https://discord.gg/GAdvjxv");
		listPing.add("§6* Twitter:§f @GamersPvPServer");
		listPing.add("mc.gamerspvp.net");
		return listPing;
	}
	
	public List<String> loadPlayerListPingMaintenance() {
		List<String> listPingMaintenance = new ArrayList<String>();
		listPingMaintenance.add("§2§lGAMERSPVP §71.8.x §eloja.gamerspvp.net");
		listPingMaintenance.add("§a");
		listPingMaintenance.add("§cManutenção!");
		listPingMaintenance.add("§b");
		listPingMaintenance.add("mc.gamerspvp.net");
		return listPingMaintenance;
	}
}