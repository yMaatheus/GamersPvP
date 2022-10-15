package dev.gamerspvp.auth.captcha;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.captcha.customevents.CaptchaEvent;
import dev.gamerspvp.auth.captcha.models.CaptchaPlayer;

public class CaptchaListener implements Listener {
	
	private Main instance;
	
	public CaptchaListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		if (captchaPlayer == null) {
			return;
		}
		if (captchaPlayer.isConcluded()) {
			player.sendMessage("§a[Captcha] Bem vindo novamente! Mexi meus pauzinhos e voc§ n§o vai precisar me fazer novamente, legal n§?");
			Event captchaEvent = new CaptchaEvent(player);
			Bukkit.getPluginManager().callEvent(captchaEvent);
			return;
		}
		if (player.getGameMode() == GameMode.SPECTATOR) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		captchaManager.openInventory(player);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		if ((captchaPlayer == null) || (!captchaPlayer.isConcluded())) {
			captchaManager.generateInventory(playerName);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equalsIgnoreCase("§7Clique na cabe§a §a§lVERDE")) {
			event.setCancelled(true);
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem == null) {
				return;
			}
			if (currentItem.getType() == Material.AIR) {
				return;
			}
			Player player = (Player) event.getWhoClicked();
			String playerName = player.getName();
			CaptchaManager captchaManager = instance.getCaptchaManager();
			CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
			if (captchaPlayer == null) {
				return;
			}
			if (!(captchaManager.isCaptchaItem(currentItem))) {
				player.kickPlayer("§cVoc§ escolheu o captcha errado.\n\nEntre novamente e selecione o bloco verde!");
				return;
			}
			if (captchaPlayer.isConcluded()) {
				player.closeInventory();
				return;
			}
			captchaPlayer.setConcluded(true);
			captchaManager.putCache(captchaPlayer);
			player.sendMessage("\n §aParab§ns voc§ passou pelo sistema de §lCAPTCHA§a! Agora efetue o login no servidor. ");
			player.sendMessage(" §aLogo ap§s isso poder§ come§ar a jogar. Obrigado pela prefer§ncia tenha um bom jogo!");
			player.sendMessage("");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					Event captchaEvent = new CaptchaEvent(player);
					Bukkit.getPluginManager().callEvent(captchaEvent);
					player.closeInventory();
				}
			}.runTaskLater(instance, 15L);
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = (Player) event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		if (captchaPlayer == null) {
			return;
		}
		if (captchaPlayer.isConcluded()) {
			return;
		}
		if (System.currentTimeMillis() < captchaPlayer.getCloseTimestamp()) {
			return;
		}
		Inventory inventory = captchaPlayer.getInventory();
		if (inventory == null) {
			return;
		}
		captchaPlayer.setCloseTimestamp(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1));
		captchaManager.putCache(captchaPlayer);
		player.openInventory(inventory);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = (Player) event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		if (captchaPlayer == null) {
			return;
		}
		if (captchaPlayer.isConcluded()) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		CaptchaManager captchaManager = instance.getCaptchaManager();
		CaptchaPlayer captchaPlayer = captchaManager.getCache(playerName);
		if (captchaPlayer == null) {
			return;
		}
		if (captchaPlayer.isConcluded()) {
			return;
		}
		event.setCancelled(true);
	}
}