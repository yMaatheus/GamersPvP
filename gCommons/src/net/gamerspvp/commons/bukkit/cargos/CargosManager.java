package net.gamerspvp.commons.bukkit.cargos;

import lombok.Getter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.cargos.group.GroupManager;
import net.gamerspvp.commons.bukkit.cargos.user.UserManager;
import net.gamerspvp.commons.network.CargosController;

public class CargosManager {
	
	@Getter
	private static CargosController databaseController;
	@Getter
	private static GroupManager groupManager;
	@Getter
	private static UserManager userManager;
	
	public CargosManager(CommonsBukkit commons) throws Exception {
		databaseController = new CargosController(commons.getDataCenter());
		groupManager = new GroupManager(commons);
		userManager = new UserManager(commons);
	}
}