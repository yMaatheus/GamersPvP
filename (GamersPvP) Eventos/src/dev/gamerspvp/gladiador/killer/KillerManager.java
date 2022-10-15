package dev.gamerspvp.gladiador.killer;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.killer.Killer.statusType;
import dev.gamerspvp.gladiador.locations.LocationsManager;
import dev.gamerspvp.gladiador.mito.MitoManager;

public class KillerManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private Settings settings;
	private Killer killer;
	private KillerCommandManager killerCommand;
	
	private double premio;
	private String ganhador;
	public String prefix = "§c[Killer] §r";
	
	public KillerManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.settings = new Settings();
		this.killer = null;
		this.killerCommand = new KillerCommandManager(instance);
		this.premio = config.getDouble("premio");
		this.ganhador = config.getString("ganhador");
		new KillerListener(instance);
	}
	
	public void executeStart() {
		killer = new Killer();
		int alerts = settings.chamando.alerts;
		int time = settings.chamando.time;
		killer.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (killer.getAlerts() >= alerts) {
					if (getPlayers() < settings.minPlayers) {
						Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado por insuficiência de participantes.");
						killer.setStatus(statusType.CANCELADO);
						killer.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
						killer.reset();
						killer = null;
						return;
					}
					executeClose();
				} else {
					String tempo = String.valueOf((alerts - killer.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fEvento §c§lKILLER §fsendo iniciado!");
					Bukkit.broadcastMessage(prefix + "§fTempo restante: §c" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fPara participar digite: §c/Killer");
					Bukkit.broadcastMessage(prefix + "§fPremios: §2$§c" + premio + " + §fTags: §c[Killer] §ce §5[Mito]");
					Bukkit.broadcastMessage(prefix + "§fJogadores: §c" + getPlayers());
					Bukkit.broadcastMessage("");
					killer.setAlerts(killer.getAlerts() + 1);
				}
			}
		}.runTaskTimer(instance, 20L, time * 20L));
	}
	
	private void executeClose() {
		killer.setStatus(statusType.FECHADO);
		killer.reset();
		int alerts = settings.iniciando.alerts;
		int time = settings.iniciando.time;
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §c§lKILLER §ffechado!");
		Bukkit.broadcastMessage(prefix + "§fJogadores: §c" + getPlayers());
		Bukkit.broadcastMessage("");
		killer.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (killer.getAlerts() >= alerts) {
					killer.setStatus(statusType.PVP);
					killer.reset();
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP foi do Evento §c§lKILLER §ffoi iniciado!");
					Bukkit.broadcastMessage(prefix + "§fJogadores: §c" + getPlayers());
					Bukkit.broadcastMessage("");
				} else {
					String tempo = String.valueOf((alerts - killer.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fIniciando PvP em: §c" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fJogadores: §c" + getPlayers());
					Bukkit.broadcastMessage("");
					killer.setAlerts(killer.getAlerts() + 1);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, time * 20L));
	}
	
	public void executeCheck() {
		if (killer == null) {
			return;
		}
		if (killer.getStatus() != statusType.PVP) {
			return;
		}
		int players = getPlayers();
		if (players == 1) {
			executeEnd();
		}
	}
	
	private void executeEnd() {
		killer.reset();
		Player player = null;
		String playerName = null;
		for (Player participante : killer.getParticipantes().keySet()) {
			player = participante;
			playerName = participante.getName();
			break;
		}
		depositPremio(player);
		ganhador = playerName;
		config.set("ganhador", ganhador);
		saveConfig();
		MitoManager mitoManager = instance.getMitoManager();
		if (!(mitoManager.isMito(playerName))) {
			mitoManager.setNewMito(player);
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §c§lKILLER §ffoi finalizado");
		Bukkit.broadcastMessage(prefix + "§fVencedor: §c" + playerName);
		Bukkit.broadcastMessage("");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				killer.getParticipantes().keySet().forEach(player -> {
					player.teleport(getLocation("saida"));
					player.sendMessage("§aVocê foi teleportado até a saida.");
				});
				executeClearDrops();
				killer = null;
			}
		}.runTaskLater(instance, 600);
	}
	
	public void executeCancel() {
		Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado.");
		killer.setStatus(statusType.CANCELADO);
		killer.reset();
		killer.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
		killer = null;
	}
	
	private void executeClearDrops() {
		Chunk spawnChunk = getLocation("spawn").getChunk();
		spawnChunk.load();
		for (Entity i : spawnChunk.getWorld().getEntities()) {
			if (i instanceof Item) {
				if (!i.isDead()) {
					i.remove();
				}
			}
		}
	}
	
	public boolean isWinner(String playerName) {
		if (ganhador.equalsIgnoreCase(playerName)) {
			return true;
		}
		return false;
	}
	
	public boolean hasLocations() {
		LocationsManager locationsManager = instance.getLocationsManager();
		String category = "Killer";
		if (locationsManager.get(category, "spawn") == null) {
			return false;
		}
		if (locationsManager.get(category, "saida") == null) {
			return false;
		}
		return true;
	}
	
	public Location getLocation(String locationName) {
		LocationsManager locationsManager = instance.getLocationsManager();
		return locationsManager.get("Killer", locationName);
	}
	
	public void setLocation(String locationName, Location location) {
		LocationsManager locationsManager = instance.getLocationsManager();
		locationsManager.setLocation("Killer", locationName, location);
	}
	
	public void depositPremio(Player player) {
		player.sendMessage(prefix + "§eVocê recebeu §2$§f" + premio + " §ecomo prêmio.");
		instance.getEconomy().depositPlayer(player, premio);
	}
	
	public int getPlayers() {
		if (killer != null) {
			return killer.getParticipantes().size();
		}
		return 0;
	}
	
	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "killer.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class Settings {
		
		private int minPlayers;
		private Announce chamando;
		private Announce iniciando;
		
		public Settings() {
			this.minPlayers = config.getInt("minJogadores");
			this.chamando = new Announce(config.getInt("chamando.avisos"), config.getInt("chamando.tempoEntreAvisos"));
			this.iniciando = new Announce(config.getInt("iniciando.avisos"), config.getInt("iniciando.tempoEntreAvisos"));
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
	
	public Killer getKiller() {
		return killer;
	}
	
	public KillerCommandManager getKillerCommand() {
		return killerCommand;
	}
}