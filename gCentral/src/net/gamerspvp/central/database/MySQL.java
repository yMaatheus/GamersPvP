package net.gamerspvp.central.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {
	
	private Connection connection;
	
	private String user = null;
	private String host = null;
	private String database = null;
	private String password = null;
	private int port = 3306;
	
	public MySQL(String user, String host, String database, String password, int port) {
		this.user = user;
		this.host = host;
		this.database = database;
		this.password = password;
		this.port = 3306;
	}

	public Connection getConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			String constr = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?verifyServerCertificate=false&useSSL=false";
			this.connection = DriverManager.getConnection(constr, this.user, this.password);
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
	
	public ResultSet executeResult(String query) throws SQLException {
		ResultSet result = null;
		PreparedStatement pst = this.getConnection().prepareStatement(query);
		result = pst.executeQuery();
		pst.execute(); // select, insert and update
		pst.close();
		return result;
	}
}