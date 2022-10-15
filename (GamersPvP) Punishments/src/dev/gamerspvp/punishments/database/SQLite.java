package dev.gamerspvp.punishments.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

public class SQLite {
	
	private Connection connection;
	private Plugin instance;
	private String database;
	
	public SQLite(String database, Plugin instance) {
		try {
			this.instance = instance;
			this.database = database;
			File file = new File("plugins/" + instance.getDataFolder().getName());
			if (!file.exists()) {
				file.mkdirs();
			}
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:plugins/" + instance.getDataFolder().getName() + "/" + database);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SQLite(Plugin instance) {
		this("storage.db", instance);
	}
	
	public Connection getConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			try {
				Class.forName("org.sqlite.JDBC");
				String constr = "jdbc:sqlite:plugins/" + instance.getDataFolder().getName() + "/" + database;
				this.connection = DriverManager.getConnection(constr);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return this.connection;
	}
	
	public void executeUpdate(String query, boolean async) throws SQLException {
		if (async) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						PreparedStatement pst = getConnection().prepareStatement(query);
						pst.executeUpdate(); // insert, update, delete
						pst.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).run();
		} else {
			PreparedStatement pst = this.getConnection().prepareStatement(query);
			pst.executeUpdate(); // insert, update, delete
			pst.close();
		}
	}
	
	public void execute(String query, boolean async) throws SQLException {
		if (async) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						// Execute unsafe queries
						PreparedStatement pst = getConnection().prepareStatement(query);
						pst.execute(); // select, insert and update
						pst.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).run();
		} else {
			// Execute unsafe queries
			PreparedStatement pst = this.getConnection().prepareStatement(query);
			pst.execute(); // select, insert and update
			pst.close();
		}
	}
}