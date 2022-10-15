package net.gamerspvp.commons.bukkit.cargos.user.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import net.gamerspvp.commons.network.models.User;

@Getter
public class UserUpdatedEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Player player;
	private final User user;
	
	public UserUpdatedEvent(Player player, User user) {
		this.player = player;
		this.user = user;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}