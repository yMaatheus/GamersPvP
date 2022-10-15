package dev.gamerspvp.lobby.api;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import dev.gamerspvp.lobby.Main;
import dev.gamerspvp.lobby.systems.Arrays;

public class BungeeAPI {

	public static void teleportToServer(Player p, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		p.sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
	}

	public static int getPlayers(String server) {
		if (Arrays.server_players.containsKey(server)) {
			return ((Integer) Arrays.server_players.get(server)).intValue();
		}
		return 0;
	}

	public static int getAllPlayers() {
		if (Arrays.server_players.containsKey("network")) {
			return ((Integer) Arrays.server_players.get("network")).intValue();
		}
		return 0;
	}
}