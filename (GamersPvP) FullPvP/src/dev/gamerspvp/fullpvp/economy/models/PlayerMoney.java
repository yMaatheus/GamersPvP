package dev.gamerspvp.fullpvp.economy.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.database.SQLite;

public class PlayerMoney {
	
	private String name;
	private String playerName;
	private double money;
	private boolean reciveMoney;
	
	public PlayerMoney(String playerName) {
		this.name = playerName.toLowerCase();
		this.playerName = playerName;
		this.money = 0;
		this.reciveMoney = true;
	}
	
	public void update(Main instance) {
		try {
			SQLite mysql = instance.getSQLite();
			String query = "UPDATE money SET money=?, reciveMoney=? WHERE name=?";
			PreparedStatement pstmt = mysql.getConnection().prepareStatement(query);
			pstmt.setDouble(1, money);
			pstmt.setBoolean(2, reciveMoney);
			pstmt.setString(3, name);
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	public boolean isReciveMoney() {
		return reciveMoney;
	}
	
	public void setReciveMoney(boolean reciveMoney) {
		this.reciveMoney = reciveMoney;
	}
}