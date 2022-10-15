package net.gamers.lobby.games;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.gamers.lobby.Main;
import net.gamers.lobby.games.models.Game;
import net.gamerspvp.commons.bukkit.utils.MakeItem;

public class GamesListener implements Listener {
	
	private long timestamp;
	
	public GamesListener(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		inventory.setHeldItemSlot(4);
		inventory.setItem(4, new MakeItem(Material.COMPASS).setName("§aModos de Jogo").build());
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getInventory().getType() == InventoryType.PLAYER) {
			ItemStack item = event.getCurrentItem();
			if (item == null || item.getType() == Material.AIR) {
				return;
			}
			if (item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null) {
				return;
			}
			if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aModos de Jogo")) {
				event.setCancelled(true);
			}
			return;
		}
		if (event.getInventory().getName() != GamesManager.getInventory().getName()) {
			return;
		}
		event.setCancelled(true);
		ItemStack currentItem = event.getCurrentItem();
		if (currentItem == null || currentItem.getType() == Material.AIR) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		String gameName = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
		Game game = GamesManager.getCache(gameName);
		if (game == null) {
			return;
		}
		if (player.isOp() || (player.hasPermission("gamers.vip"))) {
			game.joinVIPGame(player);
			return;
		}
		game.joinQueue(player);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemHand = player.getItemInHand();
		if (itemHand == null || itemHand.getType() == Material.AIR) {
			return;
		}
		String itemName = itemHand.getItemMeta().getDisplayName();
		if (itemName == null || !itemName.contains("§aModos de Jogo")) {
			return;
		}
		player.openInventory(GamesManager.getInventory());
	}
	
	@EventHandler
	public void onNPCClickEvent(NPCRightClickEvent event) {
		if (timestamp > System.currentTimeMillis()) {
			return;
		}
		Player player = event.getClicker();
		NPC npc = event.getNPC();
		Game game = null;
		for (Game value : GamesManager.getCache().values()) {
			if (value.getNpc() != npc) {
				continue;
			}
			game = value;
			break;
		}
		if (game == null) {
			return;
		}
		timestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1);
		if (player.isOp() || player.hasPermission("gamers.vip")) {
			game.joinVIPGame(player);
			return;
		}
		game.joinQueue(player);
	}
}