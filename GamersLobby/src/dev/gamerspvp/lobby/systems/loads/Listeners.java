package dev.gamerspvp.lobby.systems.loads;

import dev.gamerspvp.lobby.inventory.MenuServersInventory;
import dev.gamerspvp.lobby.systems.listeners.AuthMeLoginListener;
import dev.gamerspvp.lobby.systems.listeners.EntityDamageListener;
import dev.gamerspvp.lobby.systems.listeners.PlayerCommandPreProcessListener;
import dev.gamerspvp.lobby.systems.listeners.PlayerDeathListener;
import dev.gamerspvp.lobby.systems.listeners.PlayerInteractListener;
import dev.gamerspvp.lobby.systems.listeners.PlayerJoinListener;
import dev.gamerspvp.lobby.systems.listeners.PlayerRespawnListener;
import dev.gamerspvp.lobby.systems.listeners.basics.AsyncPlayerChatListener;
import dev.gamerspvp.lobby.systems.listeners.basics.BlockBreakListener;
import dev.gamerspvp.lobby.systems.listeners.basics.BlockPlaceListener;
import dev.gamerspvp.lobby.systems.listeners.basics.EntityExplodeListener;
import dev.gamerspvp.lobby.systems.listeners.basics.FoodLevelChangeListener;
import dev.gamerspvp.lobby.systems.listeners.basics.ItemSpawnListener;
import dev.gamerspvp.lobby.systems.listeners.basics.LeavesDecayListener;
import dev.gamerspvp.lobby.systems.listeners.basics.PlayerDropListener;
import dev.gamerspvp.lobby.systems.listeners.basics.PlayerQuitListener;
import dev.gamerspvp.lobby.systems.listeners.basics.WeatherChangeListener;

public class Listeners {
	
	public Listeners() {
		new AuthMeLoginListener();
		new AsyncPlayerChatListener();
		new BlockBreakListener();
		new PlayerDeathListener();
		new BlockPlaceListener();
		new EntityDamageListener();
		new EntityExplodeListener();
		new FoodLevelChangeListener();
		new ItemSpawnListener();
		new LeavesDecayListener();
		new MenuServersInventory();
		new PlayerCommandPreProcessListener();
		new PlayerDropListener();
		new PlayerJoinListener();
		new PlayerInteractListener();
		new PlayerQuitListener();
		new PlayerRespawnListener();
		new WeatherChangeListener();
	}
}