package dev.gamerspvp.lobby.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev.gamerspvp.lobby.Main;

public class LocAPI implements Listener {

	public static void Set(Player p, String w) {
		Main.plugin.loc.set("locais." + w + ".X", p.getLocation().getX());
		Main.plugin.loc.set("locais." + w + ".Y", p.getLocation().getY());
		Main.plugin.loc.set("locais." + w + ".Z", p.getLocation().getZ());
		Main.plugin.loc.set("locais." + w + ".Pitch", p.getLocation().getPitch());
		Main.plugin.loc.set("locais." + w + ".Yam", p.getLocation().getYaw());
		Main.plugin.loc.set("locais." + w + ".World", p.getLocation().getWorld().getName());
		Main.plugin.save();

	}

	public static void ir(Player p, String w) {
		if (Main.plugin.loc.contains("locais." + w)) {
			Double X = Main.plugin.loc.getDouble("locais." + w + ".X");
			Double Y = Main.plugin.loc.getDouble("locais." + w + ".Y");
			Double Z = Main.plugin.loc.getDouble("locais." + w + ".Z");
			float Pitch = (float) Main.plugin.loc.getDouble("locais." + w + ".Pitch");
			float Yam = (float) Main.plugin.loc.getDouble("locais." + w + ".Yam");
			World world = Bukkit.getWorld(Main.plugin.loc.getString("locais." + w + ".World"));
			Location loc = new Location(world, X, Y, Z, Yam, Pitch);
			p.teleport(loc);
			return;
		}
		if (Main.plugin.loc.contains("locais." + w)) {
			p.sendMessage("§c§lAinda nao foi setada!");
			return;
		}
	}
}