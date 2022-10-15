package net.gamerspvp.commons.bukkit.cargos.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.google.gson.Gson;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.cargos.CargosManager;
import net.gamerspvp.commons.bukkit.cargos.user.customevents.UserUpdatedEvent;
import net.gamerspvp.commons.bukkit.listeners.custom.BukkitMessageEvent;
import net.gamerspvp.commons.network.models.User;

public class UserListener implements Listener {
	
	private CommonsBukkit commons;
	
	public UserListener(CommonsBukkit commons) {
		commons.getServer().getPluginManager().registerEvents(this, commons);
		this.commons = commons;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		try {
			CargosManager.getUserManager().loadUser(player);
		} catch (Exception e) {
			System.err.println("[Cargos] Não foi possivel carregar do jogador: " + playerName + " ERROR: " + e.getMessage());
			player.kickPlayer("§c[Cargos] \nNão é possivel conectar ao servidor momento, tente mais tarde.");
		};
	}
	
	@EventHandler
	public void onBukkitMessageEvent(BukkitMessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("cargo_reloadUser")) {
			Gson gson = commons.getDataCenter().getRedis().getGson();
			User user = gson.fromJson(event.getMessage(), User.class);
			Player player = commons.getServer().getPlayer(user.getName());
			if (player == null) {
				return;
			}
			CargosManager.getUserManager().reloadUser(player, user);
			Bukkit.getPluginManager().callEvent(new UserUpdatedEvent(player, user));
			return;
		}
	}
}
