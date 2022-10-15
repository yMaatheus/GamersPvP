package dev.gamerspvp.lobby.api;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class LineAdder {

	// LineAdder, util para adicionar linhas em scoreboard +16
	
	private Scoreboard sb;
	private Objective obj;
	
	public LineAdder(Scoreboard sb, Objective obj){
		this.sb = sb;
		this.obj = obj;
	}
	@SuppressWarnings("deprecation")
	public void addLine(String prefix, String center, String suffix, int index){
		Team t = sb.registerNewTeam("line" + index);
		t.setPrefix(prefix);
		t.setSuffix(suffix);
		FastOfflinePlayer fast = new FastOfflinePlayer(center);
		t.addPlayer(fast);
		obj.getScore(fast.getName()).setScore(index);
	}
	
}
