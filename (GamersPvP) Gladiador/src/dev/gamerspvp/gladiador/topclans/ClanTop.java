package dev.gamerspvp.gladiador.topclans;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.database.SQLite;

public class ClanTop {
	
	private String clan;
	private int wins;
	
	public ClanTop(String clan) {
		this.clan = clan;
		this.wins = 0;
	}
	
	public void create(Main instance) throws SQLException {
		SQLite SQLite = instance.getSQLite();
		String query = "INSERT INTO clanTop (clan, wins) VALUES (?, ?);";
        PreparedStatement pstmt = SQLite.getConnection().prepareStatement(query);
        pstmt.setString(1, clan);
        pstmt.setInt(2, wins);
        pstmt.execute();
        
        pstmt.close();
	}
	
	public void update(ClanTopManager clanTopManager, Main instance) throws SQLException {
		SQLite SQLite = instance.getSQLite();
		String query = "UPDATE clanTop SET wins=? WHERE clan=?";
		PreparedStatement pstmt = SQLite.getConnection().prepareStatement(query);
		pstmt.setInt(1, wins);
		pstmt.setString(2, clan);
		pstmt.execute();
		
		pstmt.close();
		clanTopManager.updateTop();
	}
	
	public String getClan() {
		return clan;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void setWins(int wins) {
		this.wins = wins;
	}
}