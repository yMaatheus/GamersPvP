package net.gamerspvp.commons.bungee.maintenance;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.maintenance.commands.MaintenanceCommand;

public class MaintenanceManager {
	
	public MaintenanceManager(ProxiedCommons instance) throws Exception {
		new MaintenanceCommand(instance);
		
		new MaintenanceListener(instance);
	}
}