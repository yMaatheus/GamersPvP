package net.gamerspvp.commons.bukkit.cmdblacklist;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.gamerspvp.commons.bukkit.CommonsBukkit;

public class CmdBlacklistManager implements Listener {

	private HashSet<String> commands;

	public CmdBlacklistManager(CommonsBukkit instance) {
		this.commands = new HashSet<String>();
		commands.add("/");
		commands.add("//");
		commands.add("/ver");
		commands.add("/bukkit:ver");
		commands.add("/minecraft:tell");
		commands.add("/bukkit:pl");
		commands.add("/bukkit:plugins");
		commands.add("/bukkit:me");
		commands.add("/me");
		commands.add("/?");
		commands.add("/bukkit:help");
		commands.add("/help");
		commands.add("/bukkit:?");
		commands.add("/plugins");
		commands.add("/pl");
		commands.add("/version");
		commands.add("/bukkit:version");
		commands.add("/about");
		commands.add("/bukkit:about");
		commands.add("/minecraft:me");
		commands.add("/minecraft:gamemode");
		commands.add("/bukkit:gamemode");
		commands.add("/matrix");
		commands.add("/ncp");
		commands.add("/nocheatplus:ncp");
		commands.add("/nocheatplus");
		commands.add("/nocheatplus:nocheatplus");
		commands.add("//calc");
		commands.add("//calculate");
		commands.add("//eval");
		commands.add("//evaluate");
		commands.add("//solve");
		commands.add("/execute");
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}

	public void onPacketEvent(PacketEvent event) {
		Player player = event.getPlayer();
		if (player.isOp() || player.hasPermission("gamers.coordenador") || commands.isEmpty()) {
			return;
		}
		PacketContainer packet = event.getPacket();
		String message = ((String) packet.getSpecificModifier(String.class).read(0)).toLowerCase();
		for (String command : commands) {
			if ((!message.startsWith(command + " ")) && (!message.equals(command))) {
				continue;
			}
			event.setCancelled(true);
			break;
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		if (player.isOp() || player.hasPermission("gamers.coordenador") || commands.isEmpty()) {
			return;
		}
		for (String command : commands) {
			if ((!message.startsWith(command + " ")) && (!message.equals(command))) {
				continue;
			}
			event.setCancelled(true);
			event.getPlayer().sendMessage("§fComando desconhecido.");
			break;
		}
	}
}