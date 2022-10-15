package net.gamers.p4free.essentials.listeners;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class PrepareItemCraft implements Listener {
	
	private HashSet<Integer> crafts;
	
	public PrepareItemCraft(HashSet<Integer> crafts) {
		this.crafts = crafts;
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {
		Recipe recipe = event.getRecipe();
		if (recipe == null) {
			return;
		}
		ItemStack recipeResult = recipe.getResult();
		if (recipeResult == null) {
			return;
		}
		@SuppressWarnings("deprecation")
		int itemType = recipeResult.getType().getId();
		if (!(crafts.contains(Integer.valueOf(itemType)))) {
			return;
		}
		event.getInventory().setResult(new ItemStack(Material.AIR));
	}
}