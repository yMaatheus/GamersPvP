package net.gamerspvp.punish.bukkit.listeners;

import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import br.com.devpaulo.legendchat.api.events.PrivateMessageEvent;
import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.controllers.DatabaseController;
import net.gamerspvp.punish.network.models.Mute;
import net.gamerspvp.punish.network.utils.TimeManager;

public class ChatMessage implements Listener {
	
	private Main instance;
	
	public ChatMessage(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onChatMessageEvent(ChatMessageEvent event) {
		Player player = event.getSender();
		Mute mute = instance.getMutes().get(player.getName().toLowerCase());
		if (mute == null) {
			return;
		}
		event.setCancelled(true);
		if (System.currentTimeMillis() < mute.getTime()) {
			if (mute.isMuteall()) {
				player.sendMessage("");
				player.sendMessage("§c(!) Voc§ est§ silenciado em todos os chat's por mais " + TimeManager.getTimeEnd(mute.getTime()));
				player.sendMessage("");
				player.sendMessage("§c(!) Autor: " + mute.getAuthor());
				player.sendMessage("§c(!) Motivo: " + mute.getReason());
				player.sendMessage("§c(!) Data: " + mute.getDate());
				player.sendMessage("");
			} else {
				player.sendMessage("");
				player.sendMessage("§c(!) Voc§ est§ silenciado nos chat's(global, local) por mais " + TimeManager.getTimeEnd(mute.getTime()));
				player.sendMessage("");
				player.sendMessage("§c(!) Autor: " + mute.getAuthor());
				player.sendMessage("§c(!) Motivo: " + mute.getReason());
				player.sendMessage("§c(!) Data: " + mute.getDate());
				player.sendMessage("");
			}
		} else {
			try {
				new DatabaseController().executeUnmute(mute.getName(), instance);
				instance.getMutes().remove(mute.getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/.")) {
			Player player = event.getPlayer();
			Mute mute = instance.getMutes().get(player.getName().toLowerCase());
			if (mute == null) {
				return;
			}
			if (!(mute.isMuteall())) {
				return;
			}
			event.setCancelled(true);
			player.sendMessage("");
			player.sendMessage("§c(!) Voc§ est§ silenciado em todos os chat's por mais " + TimeManager.getTimeEnd(mute.getTime()));
			player.sendMessage("");
			player.sendMessage("§c(!) Autor: " + mute.getAuthor());
			player.sendMessage("§c(!) Motivo: " + mute.getReason());
			player.sendMessage("§c(!) Data: " + mute.getDate());
			player.sendMessage("");
		}
	}
	
	@EventHandler
	public void onPrivateMessageEvent(PrivateMessageEvent event) {
		if (!(event.getSender() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getSender();
		Mute mute = instance.getMutes().get(player.getName().toLowerCase());
		if (mute == null) {
			return;
		}
		if (!(mute.isMuteall())) {
			return;
		}
		event.setCancelled(true);
		player.sendMessage("");
		player.sendMessage("§c(!) Voc§ est§ silenciado em todos os chat's por mais " + TimeManager.getTimeEnd(mute.getTime()));
		player.sendMessage("");
		player.sendMessage("§c(!) Autor: " + mute.getAuthor());
		player.sendMessage("§c(!) Motivo: " + mute.getReason());
		player.sendMessage("§c(!) Data: " + mute.getDate());
		player.sendMessage("");
	}
}