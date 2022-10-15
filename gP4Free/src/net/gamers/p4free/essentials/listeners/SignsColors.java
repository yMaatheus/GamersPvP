package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignsColors implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChangeEvent(SignChangeEvent event) {
		if (event.getPlayer().hasPermission("essentials.vip")) {
			return;
		}
		for (int i = 0; i < (event.getLines()).length; i++)
			event.setLine(i, event.getLine(i).replace("&", "§"));
	}
}
