package dev.gamerspvp.gladiador.dominar;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import dev.gamerspvp.gladiador.utils.TimeFormater;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class DominarListener implements Listener {
	
	private Main instance;
	
	private long timestamp;
	
	public DominarListener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance = instance;
		this.timestamp = System.currentTimeMillis();
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		Block block = event.getBlock();
		DominarManager dominarManager = instance.getDominarManager();
		if (dominarManager.getChestSet().contains(playerName.toLowerCase())) {
			if (block.getType() == Material.BEDROCK) {
				event.setCancelled(true);
				block.setType(Material.BEDROCK);
				dominarManager.setBlock(block.getLocation());
				player.sendMessage("§aBa§ setado com sucesso.");
			} else {
				player.sendMessage("§cBloco inv§lido, § uma BEDROCK.");
			}
			dominarManager.getChestSet().remove(playerName.toLowerCase());
			return;
		}
		if (!(dominarManager.isActive())) {
			return;
		}
		Location locationBlock = dominarManager.getLocation();
		if (locationBlock == null) {
			return;
		}
		double x = locationBlock.getBlockX();
		double y = locationBlock.getBlockY();
		double z = locationBlock.getBlockZ();
		if ((block.getX() == x) && (block.getY() == y) && (block.getZ() == z)) {
			if (dominarManager.getClan() == null) {
				return;
			}
			if (dominarManager.getInvencible() > System.currentTimeMillis()) {
				event.setCancelled(true);
				String timeFormat = TimeFormater.formatOfEnd(dominarManager.getInvencible());
				player.sendMessage("§cOpaa! Perai rapaz, o clan dominante tem direito de 5 minutos de invencibilidade! Aguarde: §f" + timeFormat);
				return;
			}
			SimpleClans simpleClans = instance.getSimpleClans();
			ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
			if (clanPlayer != null) {
				if (clanPlayer.getClan() != null) {
					if (clanPlayer.getClan() == dominarManager.getClan()) {
						player.sendMessage("§cVoc§ n§o pode quebrar o dominio do seu clan, bobinho!");
						return;
					}
				}
			}
			String clanTag = ClansAPI.getClanTag(dominarManager.getClan());
			Bukkit.broadcastMessage("§8[§f§lMinaPvP§8] §f" + playerName + " §cDestruiu o bloco de ouro do dominio e acabou com a festa da " + clanTag + "§c. Vaporizou os caras!");
			dominarManager.clearBuildArea(dominarManager.getProtectedRegion(player.getLocation()));
			HashSet<Player> onlinePlayers = dominarManager.getPlayers();
			if (!(onlinePlayers.isEmpty())) {
				for (Player online : onlinePlayers) {
					if (online.getWorld().getName().equalsIgnoreCase("Eventos")) {
						continue;
					}
					online.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					online.removePotionEffect(PotionEffectType.SPEED);
					online.removePotionEffect(PotionEffectType.FAST_DIGGING);
				}
			}
			dominarManager.reset();
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Block blockPlaced = event.getBlockPlaced();
		DominarManager dominarManager = instance.getDominarManager();
		Location locationBlock = dominarManager.getLocation();
		double x = locationBlock.getBlockX();
		double y = locationBlock.getBlockY();
		double z = locationBlock.getBlockZ();
		if ((block.getX() == x) && (block.getY() == y) && (block.getZ() == z)) {
			if (!(dominarManager.isActive())) {
				event.setCancelled(true);
				player.sendMessage("§cO sistema de dominio est§ desativado no momento.");
				return;
			}
			if (blockPlaced.getType() != Material.GOLD_BLOCK) {
				event.setCancelled(true);
				player.sendMessage("§cO bloco precisa ser um bloco de ouro para iniciar o dominio!");
				return;
			}
			if (Bukkit.getOnlinePlayers().size() < 30) {
				event.setCancelled(true);
				player.sendMessage("§cOpaa! Para dominar a MinaPvP § necess§rio ter pelomenos 30 jogadores online. Server falido § assim mesmo!");
				return;
			}
			SimpleClans simpleClans = instance.getSimpleClans();
			ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
			if (clanPlayer == null) {
				event.setCancelled(true);
				player.sendMessage("§cPara iniciar um dominio § necess§rio possuir um clan.");
				return;
			}
			Clan clan = clanPlayer.getClan();
			if (clan == null) {
				event.setCancelled(true);
				player.sendMessage("§cPara iniciar um dominio § necess§rio possuir um clan.");
				return;
			}
			dominarManager.setClan(clan);
			dominarManager.setInvencible(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(300));
			dominarManager.updatePlayersClan();
			String clanTag = ClansAPI.getClanTag(clan);
			Bukkit.broadcastMessage("§8[§f§lMinaPvP§8] §cO clan " + clanTag + " §cest§ dominando a §lMinaPvP§c! Destrua o bloco de ouro e conquiste a tag: §b[Dominador] §cou morra tentando!");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		DominarManager dominarManager = instance.getDominarManager();
		if (!(dominarManager.isActive())) {
			return;
		}
		Player player = event.getPlayer();
		Location dominarLocation = dominarManager.getLocation();
		if (dominarLocation == null) {
			return;
		}
		if (!(dominarLocation.getWorld().getName().equalsIgnoreCase(player.getWorld().getName()))) {
			return;
		}
		if (dominarManager.getClan() == null) {
			return;
		}
		SimpleClans simpleClans = instance.getSimpleClans();
		ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
		if (clanPlayer == null) {
			return;
		}
		if (clanPlayer.getClan() == null) {
			return;
		}
		if (dominarManager.getClan() != clanPlayer.getClan()) {
			if (dominarManager.isInRegion(player.getLocation(), "minapvp-dominar")) {
				if (!(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING))) {
					player.sendMessage("§aTooodo cansadoo! Voc§ recebeu cansa§o por estar na §rea area de dominio.");
				}
				player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 1));
			}
		}
		if (System.currentTimeMillis() <= timestamp) {
			return;
		}
		if (player.getWorld().getName().equalsIgnoreCase("Eventos")) {
			return;
		}
		dominarManager.updatePlayersClan();
		HashSet<Player> onlinePlayers = dominarManager.getPlayers();
		if (!(onlinePlayers.isEmpty())) {
			boolean find = false;
			for (Player online : onlinePlayers) {
				if (online.getWorld().getName().equalsIgnoreCase("Eventos")) {
					continue;
				}
				find = true;
				online.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				online.removePotionEffect(PotionEffectType.SPEED);
				online.removePotionEffect(PotionEffectType.FAST_DIGGING);
				online.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 70 * 20, 1));
				online.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 70 * 20, 1));
				online.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 70 * 20, 0));
			}
			if (find) {
				timestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		DominarManager dominarManager = instance.getDominarManager();
		HashSet<Player> onlinePlayers = dominarManager.getPlayers();
		if (onlinePlayers.isEmpty()) {
			return;
		}
		if (!(onlinePlayers.contains(player))) {
			return;
		}
		if (player.getWorld().getName().equalsIgnoreCase("Eventos")) {
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.removePotionEffect(PotionEffectType.SPEED);
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
			return;
		}
	}
}