package dev.gamerspvp.lobby;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import dev.gamerspvp.lobby.commands.SairCommand;
import dev.gamerspvp.lobby.commands.SetPvPCommand;
import dev.gamerspvp.lobby.systems.Arrays;
import dev.gamerspvp.lobby.systems.loads.Listeners;
import dev.gamerspvp.lobby.systems.loads.ScoreBoard;

public class Main extends JavaPlugin implements PluginMessageListener {

	public static Main plugin;
	
	public File loc2;
	public YamlConfiguration loc;

	@Override
	public void onEnable() {
		plugin = this;
		((CraftServer) getServer()).getCommandMap().register(new SairCommand().getName(), new SairCommand());
		((CraftServer) getServer()).getCommandMap().register(new SetPvPCommand().getName(), new SetPvPCommand());
		new Listeners();
		new ScoreBoard().runUpdater();
		File loc = new File(getDataFolder(), "locais.yml");
		if (!loc.exists())
			saveResource("locais.yml", false);
		loc2 = new File(getDataFolder(), "locais.yml");
		plugin.loc = YamlConfiguration.loadConfiguration(loc2);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		if (!getServer().getMessenger().isOutgoingChannelRegistered(this, "BungeeCord"))
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		if (!getServer().getMessenger().isIncomingChannelRegistered(this, "Lobby")) {
			getServer().getMessenger().registerIncomingPluginChannel(this, "Lobby", this);
		}
	}

	@Override
	public void onDisable() {

	}
	
	public void save() {
		try {
			plugin.loc.save(plugin.loc2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (channel.equalsIgnoreCase("Lobby")) {
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String subchannel = in.readUTF();
			if (subchannel.equals("online")) {
				String value = in.readUTF();
				String server = value.split(";")[0].toLowerCase();
				int on = Integer.parseInt(value.split(";")[1]);
				Arrays.server_players.put(server, Integer.valueOf(on));
			}
			if (subchannel.equalsIgnoreCase("all")) {
				int i = Integer.parseInt(in.readUTF());
				Arrays.server_players.put("network", Integer.valueOf(i));
			}
		}
	}
}