package dev.gamerspvp.gladiador.minigladiador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.locations.LocationsManager;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import dev.gamerspvp.gladiador.utils.InventoryUtils;
import dev.gamerspvp.gladiador.utils.Item;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class MiniGladiadorManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private MiniGladiadorSettings settings;
	private MiniGladiador miniGladiador;
	private MiniGladiadorCommandManager miniGladiadorCommand;
	
	public String prefix = "§3[MiniGladiador] §r";
	private double premio;
	private HashSet<String> ganhadores;
	public Inventory kit = null;
	public ItemStack helmet = null;
	public ItemStack chestplate = null;
	public ItemStack leggings = null;
	public ItemStack boots = null;
	
	public MiniGladiadorManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.settings = new MiniGladiadorSettings();
		this.miniGladiador = null;
		this.miniGladiadorCommand = new MiniGladiadorCommandManager(instance);
		this.premio = config.getDouble("premio");
		this.ganhadores = new HashSet<String>(config.getStringList("ganhadores"));
		loadKit();
		new MiniGladiadorListener(instance);
	}
	
	public void executeStart() {
		this.miniGladiador = new MiniGladiador();
		int alerts = settings.chamando.alerts;
		int time = settings.chamando.time;
		miniGladiador.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (miniGladiador.getAlerts() >= alerts) {
					if ((getClans() < settings.minClans) || (getPlayers() < settings.minPlayers)) {
						miniGladiador.setStatus(statusType.CANCELADO);
						Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado por insuficiência de participantes.");
						miniGladiador.getParticipantes().keySet().forEach(player -> {
							player.getInventory().clear();
							player.getInventory().setArmorContents(null);
							player.teleport(getLocation("saida"));
							player.sendMessage("§aVocê foi teleportado até a saida.");
						});
						miniGladiador.reset();
						miniGladiador = null;
						return;
					}
					executeClearDrops();
					executeClose();
				} else {
					String tempo = String.valueOf((alerts - miniGladiador.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fEvento §3§lMINIGLADIADOR §fsendo iniciado!");
					Bukkit.broadcastMessage(prefix + "§fTempo restante: §3" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fPara participar digite: §3/MiniGladiador");
					Bukkit.broadcastMessage(prefix + "§fPremios: §2$§3" + premio + " + §fTag: §e[MiniGladiador]");
					Bukkit.broadcastMessage(prefix + "§fKit setado: §3Sim!");
					Bukkit.broadcastMessage(prefix + "§fMáximo Membros por clan: §3" + settings.getMaxMembersPerClan());
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					miniGladiador.setAlerts(miniGladiador.getAlerts() + 1);
				}
			}
		}.runTaskTimer(instance, 20L, time * 20L));
	}
	
	public void executeClose() {
		miniGladiador.setStatus(statusType.FECHADO);
		miniGladiador.reset();
		int alerts = settings.iniciando.alerts;
		int time = settings.iniciando.time;
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §3§lMINIGLADIADOR §ffechado!");
		Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
		Bukkit.broadcastMessage("");
		miniGladiador.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (miniGladiador.getAlerts() >= alerts) {
					miniGladiador.setStatus(statusType.PVP);
					miniGladiador.reset();
					miniGladiador.setTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(settings.forceDeathmatchTime));
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP foi do Evento §3§lMINIGLADIADOR §ffoi iniciado!");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
				} else {
					String tempo = String.valueOf((alerts - miniGladiador.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fIniciando PvP em: §3" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					miniGladiador.setAlerts(miniGladiador.getAlerts() + 1);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, time * 20L));
	}
	
	public void executeCheck() {
		if (miniGladiador == null) {
			return;
		}
		int clans = getClans();
		if (clans == 1) {
			executeEnd();
		} else {
			statusType status = miniGladiador.getStatus();
			if ((status != statusType.PVP) || (status == statusType.DEATHMATCH_PVPOFF) || (status == statusType.DEATHMATCH_PVPON)) {
				return;
			}
			int players = getPlayers();
			if ((System.currentTimeMillis() > miniGladiador.getTime()) || (clans <= settings.clansForStartDeathmatch)) {
				System.out.println("[MiniGladiador] " + players);
				if (players > 35) {
					System.out.println("[MiniGladiador] Evento com mais de 35 membros, deathmatch não será acionada");
					return;
				}
				executeDeathmatch();
			}
		}
	}
	
	public void executeDeathmatch() {
		miniGladiador.setStatus(statusType.DEATHMATCH_PVPOFF);
		miniGladiador.setTime(0);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				executeClearDrops();
				miniGladiador.getParticipantes().keySet().forEach(player -> {
					player.teleport(getLocation("deathmatch"));
					player.sendMessage("§aVocê foi teleportado até a Arena Deathmatch.");
				});
			}
		}.runTaskLater(instance, 20 * 20L);
		int alerts = settings.deathmatch.alerts;
		int time = settings.deathmatch.time;
		miniGladiador.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (miniGladiador.getAlerts() >= alerts) {
					miniGladiador.setStatus(statusType.DEATHMATCH_PVPON);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP foi do Evento §3§lMINIGLADIADOR §ffoi iniciado!");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					executeClearDrops();
					miniGladiador.reset();
				} else {
					String tempo = String.valueOf((alerts - miniGladiador.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP do evento §3§lMINIGLADIADOR §ffoi desativado!");
					Bukkit.broadcastMessage(prefix + "§fIniciando PvP em: §3" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					miniGladiador.setAlerts(miniGladiador.getAlerts() + 1);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, time * 20L));
	}
	
	public void executeEnd() {
		miniGladiador.setStatus(statusType.FINAL);
		miniGladiador.reset();
		ganhadores.clear();
		Clan clanWinner = miniGladiador.getClans().stream().findFirst().get();
		String clanTag = ClansAPI.getClanTag(clanWinner);
		HashSet<MiniGladiador.Kills> allValues = getGladiadorKills(clanWinner);
		HashSet<MiniGladiador.Kills> topList = new HashSet<MiniGladiador.Kills>();
		List<String> formatGladiadoresKills = new ArrayList<String>();
		
		topList.addAll(miniGladiador.top(3, allValues));
		topList.forEach(gladiador -> {
			Player player = gladiador.getPlayer();
			String playerName = player.getName();
			depositPremio(player);
			ganhadores.add(playerName);
			formatGladiadoresKills.add(killsFormat(player, gladiador.getKills()));
		});
		defineGladiadores(new ArrayList<String>(ganhadores));
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §3§lMINIGLADIADOR §ffoi finalizado");
		Bukkit.broadcastMessage(prefix + "§fClan ganhador: §3" + clanTag);
		Bukkit.broadcastMessage(prefix + "§fGladiadores: " + StringUtils.join(formatGladiadoresKills, "§3,"));
		Bukkit.broadcastMessage("");
		executeClearDrops();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Player player : miniGladiador.getParticipantes().keySet()) {
					player.closeInventory();
					player.teleport(getLocation("saida"));
					player.sendMessage("§aVocê foi teleportado até a saida.");
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
				}
				executeClearDrops();
				miniGladiador = null;
			}
		}.runTaskLater(instance, 600);
	}
	
	public void executeCancel() {
		Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado.");
		miniGladiador.setStatus(statusType.CANCELADO);
		miniGladiador.reset();
		miniGladiador.getParticipantes().keySet().forEach(player -> {
			PlayerInventory playerInventory = player.getInventory();
			playerInventory.clear();
			playerInventory.setArmorContents(null);
			player.teleport(getLocation("saida"));
			player.sendMessage("§aVocê foi teleportado até a saida.");
		});
		executeClearDrops();
		miniGladiador = null;
	}
	
	private void executeClearDrops() {
		Bukkit.getWorld("Eventos").getEntitiesByClass(org.bukkit.entity.Item.class).forEach(item -> {
			item.remove();
		});
	}
	
	public void loadKit() {
		try {
			String kit = config.getString("kit");
			String helmet = config.getString("helmet");
			String chestplate = config.getString("chestplate");
			String leggings = config.getString("leggings");
			String boots = config.getString("boots");
			if (!(kit.isEmpty())) {
				this.kit = InventoryUtils.stringToInventory(kit);
			}
			if (!(helmet.isEmpty())) {
				this.helmet = InventoryUtils.stringToItemStack(helmet);
			}
			if (!(chestplate.isEmpty())) {
				this.chestplate = InventoryUtils.stringToItemStack(chestplate);
			}
			if (!(leggings.isEmpty())) {
				this.leggings = InventoryUtils.stringToItemStack(leggings);
			}
			if (!(boots.isEmpty())) {
				this.boots = InventoryUtils.stringToItemStack(boots);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayerKit(Player player) {
		if (!(hasSetedKit())) {
			return;
		}
		PlayerInventory playerInventory = player.getInventory();
		for (ItemStack itemStack : kit) {
			if (itemStack == null) {
				continue;
			}
			playerInventory.addItem(itemStack);
		}
		playerInventory.setHelmet(helmet);
		playerInventory.setChestplate(chestplate);
		playerInventory.setLeggings(leggings);
		playerInventory.setBoots(boots);
	}
	
	public void setMiniGladiadorKit(Inventory inventory, ItemStack h, ItemStack c, ItemStack l, ItemStack b) {
		kit = inventory;
		helmet = h;
		chestplate = c;
		leggings = l;
		boots = b;
		config.set("kit", InventoryUtils.inventoryToString(inventory));
		config.set("helmet", InventoryUtils.itemstackToString(h));
		config.set("chestplate", InventoryUtils.itemstackToString(c));
		config.set("leggings", InventoryUtils.itemstackToString(l));
		config.set("boots", InventoryUtils.itemstackToString(b));
		saveConfig();
	}
	
	public Item setItem(Item item) {
		return item.setDisplayName("§3MiniGladiador").addString("minigladiador", "minigladiador_item");
	}
	
	public boolean isKitItem(ItemStack itemStack) {
		if (itemStack != null) {
			Item item = new Item(itemStack);
			if (item.hasKey("minigladiador") && item.getString("minigladiador").equalsIgnoreCase("minigladiador_item")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasSetedKit() {
		if (kit == null) {
			return false;
		}
		if ((helmet == null) || (chestplate == null)) {
			return false;
		}
		if ((leggings == null) || (boots == null)) {
			return false;
		}
		return true;
	}
	
	public boolean hasLocations() {
		LocationsManager locationsManager = instance.getLocationsManager();
		String category = "MiniGladiador";
		if (locationsManager.get(category, "spawn") == null) {
			return false;
		}
		if (locationsManager.get(category, "saida") == null) {
			return false;
		}
		if (locationsManager.get(category, "deathmatch") == null) {
			return false;
		}
		return true;
	}
	
	public Location getLocation(String locationName) {
		LocationsManager locationsManager = instance.getLocationsManager();
		return locationsManager.get("MiniGladiador", locationName);
	}
	
	public void setLocation(String locationName, Location location) {
		LocationsManager locationsManager = instance.getLocationsManager();
		locationsManager.setLocation("MiniGladiador", locationName, location);
	}
	
	public void defineGladiadores(List<String> formatGladiadores) {
		config.set("ganhadores", formatGladiadores);
		saveConfig();
	}
	
	public HashSet<MiniGladiador.Kills> getGladiadorKills(Clan clanVencedor) {
		HashSet<MiniGladiador.Kills> topAll = new HashSet<MiniGladiador.Kills>();
		for (String name : miniGladiador.getpKills().keySet()) {
			Player player = Bukkit.getPlayer(name);
			if (player == null) {
				continue;
			}
			ClanPlayer clanPlayer = instance.getSimpleClans().getClanManager().getClanPlayer(player);
			if (clanPlayer == null) {
				continue;
			}
			Clan clan = clanPlayer.getClan();
			if (clan == null) {
				continue;
			}
			if (clan != clanVencedor) {
				continue;
			}
			int kills = miniGladiador.getpKills().get(name);
			if (miniGladiador.getParticipantes().get(player) == null) {
				continue;
			}
			topAll.add(miniGladiador.kills(name, player, kills));
		}
		return topAll;
	}
	
	public void depositPremio(Player player) {
		player.sendMessage(prefix + "§eVocê recebeu §2$§f" + premio + " §ecomo prêmio.");
		instance.getEconomy().depositPlayer(player, premio);
	}
	
	public String killsFormat(Player player, int kills) {
		return "§f" + player.getName() + "§8(§f" + kills + "§8)§r";
	}
	
	public String getClans(MiniGladiador miniGladiador) {
		HashSet<String> clansTag = new HashSet<String>();
		for (Clan clan : miniGladiador.getClans()) {
			clansTag.add(ClansAPI.getClanTag(clan));
		}
		return StringUtils.join(clansTag, ",");
	}
	
	public String getParticipants(MiniGladiador miniGladiador) {
		HashSet<String> pk = new HashSet<String>();
		for (Player participant : miniGladiador.getParticipantes().keySet()) {
			String participantName = participant.getName();
			int kills = miniGladiador.getpKills().get(participantName.toLowerCase());
			pk.add(killsFormat(participant, kills));
		}
		return StringUtils.join(pk, ",");
	}
	
	public int getClans() {
		if (miniGladiador != null) {
			return miniGladiador.getClans().size();
		}
		return 0;
	}
	
	public int getPlayers() {
		if (miniGladiador != null) {
			return miniGladiador.getParticipantes().size();
		}
		return 0;
	}
	
	public void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "minigladiador.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class MiniGladiadorSettings {
		
		private int maxMembersPerClan;
		private int minPlayers;
		private int minClans;
		private long forceDeathmatchTime;
		private int clansForStartDeathmatch;
		private Announce chamando;
		private Announce iniciando;
		private Announce deathmatch;
		
		public MiniGladiadorSettings() {
			this.maxMembersPerClan = config.getInt("membrosMaximosPorClan");
			this.minPlayers = config.getInt("minJogadores");
			this.minClans = config.getInt("minClans");
			this.forceDeathmatchTime = config.getLong("deathmatch.forceDeathmatchtime");
			this.clansForStartDeathmatch = config.getInt("deathmatch.clans");
			this.chamando = new Announce(config.getInt("chamando.avisos"), config.getInt("chamando.tempoEntreAvisos"));
			this.iniciando = new Announce(config.getInt("iniciando.avisos"), config.getInt("iniciando.tempoEntreAvisos"));
			this.deathmatch = new Announce(config.getInt("deathmatch.avisos"), config.getInt("deathmatch.tempoEntreAvisos"));
		}
		
		public int getMaxMembersPerClan() {
			return maxMembersPerClan;
		}
		
		public class Announce {
			private int alerts;
			private int time;
			
			public Announce(int alerts, int time) {
				this.alerts = alerts;
				this.time = time;
			}
		}
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public MiniGladiadorSettings getSettings() {
		return settings;
	}
	
	public HashSet<String> getGanhadores() {
		return ganhadores;
	}
	
	public MiniGladiador getMiniGladiador() {
		return miniGladiador;
	}
	
	public MiniGladiadorCommandManager getMiniGladiadorCommand() {
		return miniGladiadorCommand;
	}
}