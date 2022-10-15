package dev.gamerspvp.fullpvp.anticheat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.utils.Titles;

public class AntiCheatListener implements Listener {
	
	private Main instance;
	
	public AntiCheatListener(Main instance) {
		this.instance = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String address = player.getAddress().getAddress().getHostAddress();
		AntiCheatManager antiCheatManager = instance.getAntiCheatManager();
		antiCheatManager.saveIp(player.getName(), address);
		if (antiCheatManager.getCache(address) == null) {
			antiCheatManager.cache(address, 0);
			return;
		}
		int value = antiCheatManager.getCache(address);
		if (value != 0) {
			String preffix = antiCheatManager.getPreffix();
			StringBuilder builder = new StringBuilder(preffix);
			builder.append("Vi que você foi expulso por mim. Desligue o Hack aí, §c");
			builder.append(value);
			builder.append("/5 §fpara você ser banido rapaz! ");
			builder.append("Fique esperto aí, jájá eu te pego seu xitadinho!");
			player.sendMessage(builder.toString());
			Titles title = new Titles();
			title.setTitle("§4§lANTIHACK");
			title.setSubtitle("§c" + value + "/5 para tu rodar!");
			title.send(player);
		}
	}
}