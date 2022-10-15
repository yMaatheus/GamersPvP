package dev.gamerspvp.gladiador.utils;

import org.bukkit.entity.Player;

import dev.gamerspvp.gladiador.Main;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class ClansAPI {
	
	public static void setClanFF(Player player, boolean friendlyFire) {
		Main instance = Main.getInstance();
		SimpleClans simpleclans = instance.getSimpleClans();
		if (simpleclans.getClanManager().getClanPlayer(player) != null) {
			simpleclans.getClanManager().getClanPlayer(player).setFriendlyFire(friendlyFire);
		}
	}
	
	public static String getClanTag(Clan clan) {
		if (clan != null) {
			if (clan.getTagLabel() != null) {
				return clan.getTagLabel().replaceAll("[\\[\\]\\s.]", "").replaceAll("(§([0-9|a-f|r]))+", "$1");
			}
			return null;
		}
		return null;
	}
}