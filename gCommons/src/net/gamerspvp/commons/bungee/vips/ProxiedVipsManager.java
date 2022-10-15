package net.gamerspvp.commons.bungee.vips;

import java.util.HashMap;

import lombok.Getter;
import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.bungee.vips.commands.TrocarVipCommand;
import net.gamerspvp.commons.bungee.vips.commands.UsarKeyCommand;
import net.gamerspvp.commons.bungee.vips.commands.VipCommand;
import net.gamerspvp.commons.bungee.vips.commands.VipsCommand;
import net.gamerspvp.commons.network.VipController;
import net.gamerspvp.commons.network.models.PlayerVip;
import net.gamerspvp.commons.network.models.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxiedVipsManager {
	
	@Getter
	private static VipController controller;
	
	public ProxiedVipsManager(ProxiedCommons commons) throws Exception {
		controller = new VipController(commons.getDataCenter());
		
		new TrocarVipCommand(commons);
		new UsarKeyCommand(commons);
		new VipCommand(commons);
		new VipsCommand(commons);
		
		new ProxiedVipsListeners(commons);
	}
	
	public static void loadPlayerVip(ProxiedPlayer proxiedPlayer) throws Exception {
		String playerName = proxiedPlayer.getName();
		PlayerVip playerVip = getPlayerVip(playerName, false);
		if (playerVip == null || playerVip.getVips().isEmpty()) {
			return;
		}
		HashMap<String, Long> vips = new HashMap<String, Long>();
		for (String key : playerVip.getVips().keySet()) {
			long value = playerVip.getVips().get(key);
			if (System.currentTimeMillis() < value) {
				vips.put(key, value);
			}
		}
		ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
		User user = proxiedUserManager.getUser(playerName);
		if (user == null || vips.containsKey(user.getGroup())) {
			return;
		}
		if (vips.isEmpty()) {
			controller.purgePlayerVip(playerName);
			return;
		}
		playerVip.setVips(vips);
		controller.updatePlayerVip(playerVip);
		//proxiedPlayer.sendMessage(new TextComponent("[gVips] Olá! Olhei aqui e percebi que você é §lVIP §adesde já obrigado por ajudar o servidor."));
	}
	
	public static PlayerVip getPlayerVip(String playerName, boolean createNotExists) throws Exception {
		PlayerVip playerVip = controller.getPlayerVip(playerName);
		if (createNotExists && playerVip == null) {
			playerVip = new PlayerVip(playerName);
		}
		return playerVip;
	}
}