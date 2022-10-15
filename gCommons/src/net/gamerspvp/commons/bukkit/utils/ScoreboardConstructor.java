package net.gamerspvp.commons.bukkit.utils;

import com.google.common.base.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;
import java.util.*;

public class ScoreboardConstructor {
	private Scoreboard handler;
    private Objective sidebarObjective;
    private int index;
    
    public ScoreboardConstructor() {
        this.handler = Bukkit.getScoreboardManager().getNewScoreboard();
        (this.sidebarObjective = this.handler.registerNewObjective("sidebar", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
        this.index = 10;
    }
    
    public Objective getSidebarObjective() {
        return this.sidebarObjective;
    }
    
    public Scoreboard getHandler() {
        return this.handler;
    }
    
    public void add(String text) {
        this.add(this.index--, text);
    }
    
    public void add(int index, String text) {
        if (text.length() > 32) {
            text = text.substring(0, 32);
        }
        Team team = this.handler.getTeam("score-" + index);
		String str = this.getColor(index);
		if (team == null) {
			team = this.handler.registerNewTeam("score-" + index);
			this.sidebarObjective.getScore(str).setScore(index);
		}
		if (!team.hasEntry(str)) {
			team.addEntry(str);
		}
		Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
		String prefix = iterator.next();
		if (prefix.endsWith("§")) {
			prefix = this.cutStr(prefix);
			if (!prefix.equals(team.getPrefix())) {
				team.setPrefix(prefix);
			}
			if (iterator.hasNext()) {
				String suffix = this.cutStr(iterator.next());
				if (!suffix.equals(team.getSuffix())) {
					team.setSuffix(suffix);
				}
			} else if (!"".equals(team.getSuffix())) {
				team.setSuffix("");
			}
		} else if (iterator.hasNext()) {
			String suffix = iterator.next();
			suffix = this.cutStr(String.valueOf(ChatColor.getLastColors(prefix)) + suffix);
			if (!prefix.equals(team.getPrefix())) {
				team.setPrefix(prefix);
			}
			if (!suffix.equals(team.getSuffix())) {
				team.setSuffix(suffix);
			}
		} else {
			if (!prefix.equals(team.getPrefix())) {
				team.setPrefix(prefix);
			}
			if (!"".equals(team.getSuffix())) {
				team.setSuffix("");
			}
		}
	}

	protected String cutStr(String string) {
		int length = string.length();
		if (length > 16) {
			string = string.substring(0, 16);
		}
        for (int i = string.length(); i > 0 && string.substring(0, i).endsWith("§"); string = string.substring(0, i - 1), --i) {}
        return string;
	}

	public String getColor(int i) {
		return ChatColor.values()[i - 1].toString();
	}
}
