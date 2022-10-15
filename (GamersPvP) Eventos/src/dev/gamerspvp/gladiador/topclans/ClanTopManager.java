package dev.gamerspvp.gladiador.topclans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.database.SQLite;

public class ClanTopManager {
	
	private Main instance;
	
	private List<ClanTop> topList;
	
	public ClanTopManager(Main instance) {
		this.instance = instance;
		this.topList = new ArrayList<ClanTop>();
		createTable();
		updateTop();
		instance.registerCommand(new ClanTopCommand(instance), "clantop");
		new ClanTopListener(instance);
	}
	
	public void updateTop() {
		try {
			HashSet<ClanTop> values = new HashSet<ClanTop>();
			String query = "SELECT * FROM clanTop;";
			PreparedStatement pstmt = instance.getSQLite().getConnection().prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("wins") > 0) {
					ClanTop clanTop = new ClanTop(rs.getString("clan"));
					clanTop.setWins(rs.getInt("wins"));
					values.add(clanTop);
				}
			}
			rs.close();
			pstmt.close();
			topList.clear();
			topList.addAll(calculateTop(10, values));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<ClanTop> calculateTop(int size, HashSet<ClanTop> values) {
		List<ClanTop> convert = new ArrayList<>();
		convert.addAll(values);
		Collections.sort(convert, new Comparator<ClanTop>() {
			
			@Override
			public int compare(ClanTop pt1, ClanTop pt2) {
				Integer f1 = pt1.getWins();
				Integer f2 = pt2.getWins();
				return f2.compareTo(f1);
			}
		});
		if (convert.size() > size) {
			convert = convert.subList(0, size);
		}
		return convert;
	}
	
	public ClanTop getClan(String clanTag) throws SQLException {
		SQLite SQLite = instance.getSQLite();
		PreparedStatement pstmt = SQLite.getConnection().prepareStatement("SELECT * FROM clanTop WHERE clan=?");
		pstmt.setString(1, clanTag);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			ClanTop clanTop = new ClanTop(clanTag);
			clanTop.setWins(rs.getInt("wins"));
			return clanTop;
		}
		rs.close();
		pstmt.close();
		ClanTop clanTop = new ClanTop(clanTag);
		clanTop.create(instance);
		return clanTop;
	}
	
	public void delete(String clanTag) throws SQLException {
		SQLite SQLite = instance.getSQLite();
		String query = "DELETE FROM clanTop WHERE clan=?";
		PreparedStatement pstmt = SQLite.getConnection().prepareStatement(query);
		pstmt.setString(1, clanTag);
		pstmt.execute();
		pstmt.close();
	}
	
	public void createTable() {
		try {
			SQLite SQLite = instance.getSQLite();
			SQLite.execute("CREATE TABLE IF NOT EXISTS clanTop (clan TEXT, wins INTEGER)", false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<ClanTop> getTopList() {
		return topList;
	}
}