package net.gamerspvp.commons.bungee.cargos;

import lombok.Getter;
import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.group.ProxiedGroupManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.network.CargosController;

public class ProxiedCargosManager {
	
	@Getter
	private static CargosController databaseController;
	@Getter
	private static ProxiedGroupManager groupManager;
	@Getter
	private static ProxiedUserManager userManager;
	
	public ProxiedCargosManager(ProxiedCommons commons) throws Exception {
		databaseController = new CargosController(commons.getDataCenter());
		groupManager = new ProxiedGroupManager(commons);
		userManager = new ProxiedUserManager(commons);
	}
}