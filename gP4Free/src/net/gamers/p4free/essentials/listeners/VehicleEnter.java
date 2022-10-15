package net.gamers.p4free.essentials.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class VehicleEnter implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onVehicleEnterEvent(VehicleEnterEvent event) {
		if (!(event.getEntered() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntered();
		if (player.isOp()) {
			return;
		}
		event.setCancelled(true);
	}
}