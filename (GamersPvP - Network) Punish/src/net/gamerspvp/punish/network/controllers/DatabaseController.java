package net.gamerspvp.punish.network.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.models.Ban;
import net.gamerspvp.punish.network.models.Banip;

public class DatabaseController {
	
	private Banip banip = null;
	private Ban ban = null;
	
	public boolean hasIpBanned(String ip, boolean saveObject, boolean bungee) throws SQLException {
		String query = "SELECT * FROM bansip WHERE Ip=?";
		PreparedStatement ps;
		if (bungee) {
			ps = net.gamerspvp.punish.bungee.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		} else {
			ps = net.gamerspvp.punish.bukkit.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		}
		ps.setString(1, ip);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String adress = rs.getString("Ip");
			String reason = rs.getString("Reason");
			String author = rs.getString("Author");
			String type = rs.getString("Type");
			Long time = rs.getLong("Time");
			String date = rs.getString("Date");
			if (saveObject) {
				this.banip = new Banip(adress, reason, author, type, time, date);
			}
			return true;
		}
		rs.close();
		ps.close();
		return false;
	}
	
	public boolean hasBanned(String name, boolean saveObject, boolean bungee) throws SQLException {
		String query = "SELECT * FROM bans WHERE Name=?";
		PreparedStatement ps;
		if (bungee) {
			ps = net.gamerspvp.punish.bungee.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		} else {
			ps = net.gamerspvp.punish.bukkit.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		}
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String playerRealName = rs.getString("PlayerRealName");
			String reason = rs.getString("Reason");
			String author = rs.getString("Author");
			String type = rs.getString("Type");
			Long time = rs.getLong("Time");
			String date = rs.getString("Date");
			if (saveObject) {
				this.ban = new Ban(playerRealName, reason, author, type, time, date);
			}
			return true;
		}
		rs.close();
		ps.close();
		return false;
	}
	
	public void executeUnbanIP(String ip, boolean bungee) throws SQLException {
		String query = "DELETE FROM bansip WHERE Ip=?";
		PreparedStatement ps;
		if (bungee) {
			ps = net.gamerspvp.punish.bungee.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		} else {
			ps = net.gamerspvp.punish.bukkit.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		}
        ps.setString(1, ip);
        ps.execute();
        ps.close();
	}
	
	public void executeUnban(String name, boolean bungee) throws SQLException {
		String query = "DELETE FROM bans WHERE Name=?";
		PreparedStatement ps;
		if (bungee) {
			ps = net.gamerspvp.punish.bungee.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		} else {
			ps = net.gamerspvp.punish.bukkit.Main.getInstance().getMySQL().getConnection().prepareStatement(query);
		}
        ps.setString(1, name);
        ps.execute();
        ps.close();
	}
	
	public void executeUnmute(String name, Main instance) throws SQLException {
		String query = "DELETE FROM mutes WHERE Name=?";
		PreparedStatement pstmt = Main.getInstance().getMySQL().getConnection().prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.execute();
        pstmt.close();
	}
	
	public Banip getBanip() {
		return banip;
	}
	
	public void setBanip(Banip banip) {
		this.banip = banip;
	}
	
	public Ban getBan() {
		return ban;
	}
	
	public void setBan(Ban ban) {
		this.ban = ban;
	}
}