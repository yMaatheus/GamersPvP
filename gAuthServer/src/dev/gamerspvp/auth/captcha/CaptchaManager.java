package dev.gamerspvp.auth.captcha;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.auth.Main;
import dev.gamerspvp.auth.captcha.models.CaptchaPlayer;
import net.gamerspvp.commons.bukkit.utils.Item;
import net.gamerspvp.commons.bukkit.utils.MakeItem;

public class CaptchaManager {
	
	private Main instance;
	
	private ItemStack skullCaptcha;
	private ItemStack skullGray;
	
	private HashMap<String, CaptchaPlayer> cache;
	
	public CaptchaManager(Main instance) {
		this.instance = instance;
		this.skullCaptcha = setCaptcha(skull("MHF_CoconutG").buildhead());
		this.skullGray = skull("CreeperkingNV").buildhead();
		this.cache = new HashMap<>();
		new CaptchaListener(instance);
	}
	
	public void openInventory(Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				CaptchaPlayer captchaPlayer = cache.get(player.getName());
				if (captchaPlayer == null) {
					return;
				}
				Inventory inventory = captchaPlayer.getInventory();
				if (inventory == null) {
					return;
				}
				player.openInventory(inventory);
			}
		}.runTaskLater(instance, 20L);
	}
	
	public void generateInventory(String playerName) {
		Inventory inventory = loadInventory();
		inventory.setItem(randomPosition(), skullCaptcha);
		putCache(new CaptchaPlayer(playerName, inventory));
	}
	
	public boolean isCaptchaItem(ItemStack itemStack) {
		if (itemStack != null) {
			Item item = new Item(itemStack);
			if ((item.hasKey("captcha")) && (item.getString("captcha").equalsIgnoreCase("captcha_correct"))) {
				return true;
			}
		}
		return false;
	}
	
	private Inventory loadInventory() {
		Inventory inventory = Bukkit.createInventory(null, 36, "§7Clique na cabeça §a§lVERDE");
		for (int i = 0; i < 36; i++) {
			inventory.setItem(i, skullGray);
		}
		return inventory;
	}
	
	private ItemStack setCaptcha(ItemStack itemStack) {
		Item item = new Item(itemStack);
		item.addString("captcha", "captcha_correct");
		return item.clone();
	}
	
	private MakeItem skull(String owner) {
		return new MakeItem(Material.SKULL_ITEM).setSkullOwner(owner).setName("§a").addFlags(ItemFlag.HIDE_ATTRIBUTES);
	}
	
	private int randomPosition() {
		return ThreadLocalRandom.current().nextInt(0, 35);
	}
	
	public CaptchaPlayer getCache(String playerName) {
		return cache.get(playerName);
	}
	
	public void putCache(CaptchaPlayer captchaPlayer) {
		cache.put(captchaPlayer.getPlayerName(), captchaPlayer);
	}
	
	public HashMap<String, CaptchaPlayer> getCache() {
		return cache;
	}
}