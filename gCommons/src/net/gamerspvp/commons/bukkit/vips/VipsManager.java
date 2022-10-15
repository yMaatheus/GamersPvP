package net.gamerspvp.commons.bukkit.vips;

import lombok.Getter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.vips.commands.DarVipCommand;
import net.gamerspvp.commons.network.VipController;
import net.gamerspvp.commons.network.models.PlayerVip;

public class VipsManager {
	
	@Getter
	private static VipController controller;
	
	public VipsManager(CommonsBukkit commons) throws Exception {
		controller = new VipController(commons.getDataCenter());
		
		new DarVipCommand(commons);
	}
	
	public static PlayerVip getPlayerVip(String playerName, boolean createNotExists) throws Exception {
		PlayerVip playerVip = controller.getPlayerVip(playerName);
		if (createNotExists && playerVip == null) {
			playerVip = new PlayerVip(playerName);
		}
		return playerVip;
	}
}