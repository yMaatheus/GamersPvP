package net.gamerspvp.commons.network;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import lombok.Getter;
import net.gamerspvp.commons.network.database.DataCenterManager;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.models.User;
import net.gamerspvp.commons.network.utils.ListString;

public class CargosController {
	
	@Getter
	private DataCenterManager dataCenter;
	
	public CargosController(DataCenterManager dataCenterManager) throws Exception {
		this.dataCenter = dataCenterManager;
		MySQL mysql = dataCenterManager.getMysql();
		mysql.createTable("cargos", "`name` TEXT,", "`playerName` TEXT,", "`group` TEXT,", "`departaments` TEXT,", "`permissions` TEXT,", "`firstLogin` BIGINT,", "`lastLogin` BIGINT");
	}
	
	public User getUser(String targetName) throws Exception {
		User user = null;
		MySQL mysql = dataCenter.getMysql();
		PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT * FROM cargos WHERE name=?;");
		ps.setString(1, targetName.toLowerCase());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String playerName = rs.getString("playerName");
			String group = rs.getString("group");
			List<String> departaments = ListString.stringToList(rs.getString("departaments"));
			HashSet<String> permissions = ListString.stringToHashSet(rs.getString("permissions"));
			long lastLogin = rs.getLong("lastLogin");
			long firstLogin = rs.getLong("firstLogin");
			
			user = new User(playerName, group, departaments, permissions, firstLogin, lastLogin);
		}
		rs.close();
		ps.close();
		return user;
	}
	
	public HashSet<User> searchGroupUsers(String groupName) throws Exception {
		MySQL mysql = dataCenter.getMysql();
		String query = "SELECT * FROM cargos WHERE `group`='" + groupName + "';";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		HashSet<User> users = new HashSet<User>();
		while (rs.next()) {
			String playerName = rs.getString("playerName");
			String group = rs.getString("group");
			List<String> departaments = ListString.stringToList(rs.getString("departaments"));
			HashSet<String> permissions = ListString.stringToHashSet(rs.getString("permissions"));
			long lastLogin = rs.getLong("lastLogin");
			long firstLogin = rs.getLong("firstLogin");
			
			users.add(new User(playerName, group, departaments, permissions, firstLogin, lastLogin));
		}
		rs.close();
		ps.close();
		return users;
	}
	
	public synchronized void updateUser(User user) throws Exception {
		MySQL mysql = dataCenter.getMysql();
		String name = user.getName();
		if (getUser(user.getName()) != null) {
			// update
			String query = "UPDATE cargos SET `group`=?, `departaments`=?, `permissions`=?, `lastLogin`=? WHERE `name`=?";
			PreparedStatement ps = mysql.getConnection().prepareStatement(query);
			ps.setString(1, user.getGroup());
			ps.setString(2, ListString.listToString(user.getDepartaments()));
			ps.setString(3, ListString.hashSetToString(user.getPermissions()));
			ps.setLong(4, user.getLastLogin());
			ps.setString(5, name);

			ps.executeUpdate();
			ps.close();
			return;
		}
		// insert
		String query = "INSERT INTO cargos (`name`, `playerName`, `group`, `departaments`, `permissions`, `firstLogin`, `lastLogin`) VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		
		ps.setString(1, name);
		ps.setString(2, user.getPlayerName());
		ps.setString(3, user.getGroup());
		ps.setString(4, ListString.listToString(user.getDepartaments()));
		ps.setString(5, ListString.hashSetToString(user.getPermissions()));
		ps.setLong(6, user.getFirstLogin());
		ps.setLong(7, user.getLastLogin());
		
		ps.execute();
		ps.close();
	}
	
	public void purgeUser(String targetName) throws SQLException {
		MySQL mysql = dataCenter.getMysql();
		String query = "DELETE FROM cargos WHERE name=?;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ps.setString(1, targetName.toLowerCase());
		
		ps.execute();
		ps.close();
	}
}