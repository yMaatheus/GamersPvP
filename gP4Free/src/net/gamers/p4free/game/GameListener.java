package net.gamers.p4free.game;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

import net.gamers.p4free.Main;

public class GameListener implements Listener {
	
	private Main instance;
	
	public GameListener(Main instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	public static void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("§a§m-----------------------------------------");
		player.sendMessage("§a§o> §7Salve Mano! Bem vindo ao servidor §a§lP4FREE§7, tamo testando as coisas tlgd? Ent§o qualquer §a§lBUG§7 manda a call nos ticket's do discord!");
		player.sendMessage("§a§o> §7Relaxa ai que isso aqui n§o § nada ainda! Tem muito c§digo para ser escrito por aqui.");
		player.sendMessage("§a§o> §7Tenha um §timo jogo! Ah sim, j§ ia esquecendo, desliga o hack a§ se n§o papai anticheat te pega!");
		player.sendMessage("§a§m-----------------------------------------");
		PlayerInventory playerInventory = player.getInventory();
		playerInventory.clear();
		playerInventory.setHelmet(null);
		playerInventory.setChestplate(null);
		playerInventory.setLeggings(null);
		playerInventory.setBoots(null);
		GameManager gameManager = Main.getInstance().getGameManager();
		World world = Bukkit.getWorld(gameManager.getSpawnWorld());
		if (world != null) {
			player.teleport(world.getSpawnLocation());
		}
		if (!(player.hasPermission("gamers.moderador"))) {
			for (String admin : gameManager.getAdmin()) {
				Player adminPlayer = Bukkit.getPlayer(admin);
				if (adminPlayer == null) {
					continue;
				}
				
				player.hidePlayer(adminPlayer);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (instance.getGameManager().hasInScreenShare(player)) {
			Bukkit.broadcast("§c" + player.getName()  + " deslogou na screenshare!", "gamers.moderador");
		}
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		if (instance.getGameManager().hasAdminMode(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		World world = Bukkit.getWorld(instance.getGameManager().getSpawnWorld());
		event.setRespawnLocation(world.getSpawnLocation());
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		Player player = event.getEntity();
		Player playerKiller = player.getKiller();
		if (playerKiller != null) {
			event.setDeathMessage("§7" + player.getName() + " foi morto por " + playerKiller.getName());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		event.setCancelled(true);
	}
}