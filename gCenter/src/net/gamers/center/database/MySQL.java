package net.gamers.center.database;

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
	
	public MySQL(String user, String host, String database, String password, int port) throws SQLException, ClassNotFoundException {
		this.user = user;
		this.host = host;
		this.database = database;
		this.password = password;
		this.port = 3306;
		this.connection = getConnection();
	}
	
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		if (this.connection == null || this.connection.isClosed()) {
			System.out.println("[MySQL] Abrindo conexão...");
			String constr = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?verifyServerCertificate=false&useSSL=false";
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(constr, this.user, this.password);
			System.out.println("[MySQL] A conexão foi aberta com sucesso!");
		}
		return this.connection;
	}
	
	public void createTable(String tableName, String... strings) throws SQLException, ClassNotFoundException {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + tableName);
		builder.append(" (`id` MEDIUMINT(8) UNSIGNED AUTO_INCREMENT,PRIMARY KEY (id), ");
		for (String s : strings) {
			builder.append(s);
		}
		builder.append(");");
		execute(builder.toString(), false);
	}
	
	public void executeUpdate(String query, boolean async) throws SQLException, ClassNotFoundException {
		if (async) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						PreparedStatement pst = getConnection().prepareStatement(query);
						pst.executeUpdate(); // insert, update, delete
						pst.close();
					} catch (SQLException | ClassNotFoundException e) {
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
	
	public void execute(String query, boolean async) throws SQLException, ClassNotFoundException {
		if (async) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						// Execute unsafe queries
						PreparedStatement pst = getConnection().prepareStatement(query);
						pst.execute(); // select, insert and update
						pst.close();
					} catch (SQLException | ClassNotFoundException e) {
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
	
	public ResultSet executeResult(String query) throws SQLException, ClassNotFoundException {
		ResultSet result = null;
		PreparedStatement pst = this.getConnection().prepareStatement(query);
		result = pst.executeQuery();
		pst.execute(); // select, insert and update
		pst.close();
		return result;
	}
}