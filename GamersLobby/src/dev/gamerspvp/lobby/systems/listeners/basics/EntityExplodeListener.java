package dev.gamerspvp.lobby.systems.listeners.basics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import dev.gamerspvp.lobby.Main;

public class EntityExplodeListener implements Listener {
	
	public EntityExplodeListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onExplodir(EntityExplodeEvent e) {
		for (Block p : e.blockList()) {
			float x = (float) -2 + (float) (Math.random() + ((2 - -2) + 1));
			float y = (float) -1 + (float) (Math.random() + ((2 - -2) + 1));
			float z = (float) -2.5 + (float) (Math.random() + ((2.5 - -2.5) + 1));

			FallingBlock fallingBlock = p.getWorld().spawnFallingBlock(p.getLocation(), p.getType(), p.getData());
			fallingBlock.setDropItem(false);
			fallingBlock.setVelocity(new Vector(x, y, z));

			p.setType(Material.AIR);
		}
	}
}
