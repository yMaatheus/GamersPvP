package net.gamers.p4free.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class SpawnNatureMobs implements Listener {
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
		if (reason == SpawnReason.SPAWNER) {
			return;
		}
		if (reason == SpawnReason.NATURAL || reason == SpawnReason.CHUNK_GEN || reason == SpawnReason.JOCKEY
				|| reason == SpawnReason.MOUNT)
			event.setCancelled(true);
	}
}