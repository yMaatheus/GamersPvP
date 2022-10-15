package net.gamers.p4free.utils;

import org.bukkit.entity.Player;

import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class SC {
	
	public static String getTag(Player p) {
		String tagcolor = null;
		String tag = null;
		ClanPlayer cp = SimpleClans.getInstance().getClanManager().getClanPlayer(p);
		if (cp != null) {
			tagcolor = cp.getTagLabel().replaceAll("[\\[\\]\\s.]", "").replaceAll("(§([0-9|a-f|r]))+", "$1");
			tag = tagcolor.substring(0, tagcolor.length() - 2);
			return tag;
		} else {
			tag = "Nenhum";
			return tag;
		}
	}
	
	public static boolean hasClan(Player p) {
		ClanPlayer cp = SimpleClans.getInstance().getClanManager().getClanPlayer(p);
		if (cp != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getClanName(Player p) {
		ClanPlayer cp = SimpleClans.getInstance().getClanManager().getClanPlayer(p);
		return cp.getClan().getName();
	}
	
	public static int getOnlineMembers(Player p) {
		ClanPlayer cp = SimpleClans.getInstance().getClanManager().getClanPlayer(p);
		if (cp != null) {
			return cp.getClan().getOnlineMembers().size();
		} else {
			return -1;
		}
	}
	
	public static int getMaxMembers(Player p) {
		ClanPlayer cp = SimpleClans.getInstance().getClanManager().getClanPlayer(p);
		if (cp != null) {
			return cp.getClan().getMembers().size();
		} else {
			return -1;
		}
	}
}