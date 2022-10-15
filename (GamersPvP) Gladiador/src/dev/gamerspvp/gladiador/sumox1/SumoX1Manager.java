package dev.gamerspvp.gladiador.sumox1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.locations.LocationsManager;
import dev.gamerspvp.gladiador.sumox1.models.SumoX1;
import dev.gamerspvp.gladiador.sumox1.models.SumoX1.Participant;
import dev.gamerspvp.gladiador.sumox1.models.SumoX1.statusType;

public class SumoX1Manager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private Settings settings;
	private SumoX1 sumox1;
	
	private double premio;
	private String ganhador;
	public String prefix = "§c[SumoX1] §r";
	
	public SumoX1Manager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.settings = new Settings();
		this.sumox1 = null;
		this.premio = config.getDouble("premio");
		this.ganhador = config.getString("ganhador");
	}
	
	public void executeStart() {
		sumox1 = new SumoX1();
		int alerts = settings.chamando.alerts;
		int time = settings.chamando.time;
		sumox1.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (sumox1.getAlerts() >= alerts) {
					if (getPlayers() < settings.minPlayers) {
						Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado por insuficiência de participantes.");
						sumox1.setStatus(statusType.CANCELADO);
						sumox1.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
						sumox1.reset();
						sumox1 = null;
						return;
					}
					executeClose();
				} else {
					String tempo = String.valueOf((alerts - sumox1.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fEvento §c§lSUMOX1 §fsendo iniciado!");
					Bukkit.broadcastMessage(prefix + "§fTempo restante: §c" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fPara participar digite: §c/SumoX1");
					Bukkit.broadcastMessage(prefix + "§fPremios: §2$§c" + premio + " + §fTags: §c[SumoX1]");
					Bukkit.broadcastMessage(prefix + "§fJogadores: §c" + getPlayers());
					Bukkit.broadcastMessage("");
					sumox1.setAlerts(sumox1.getAlerts() + 1);
				}
			}
		}.runTaskTimer(instance, 20L, time * 20L));
	}
	
	private void executeClose() {
		sumox1.setStatus(statusType.FECHADO);
		sumox1.reset();
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §c§lSUMOX1 §ffechado!");
		Bukkit.broadcastMessage(prefix + "§fJogadores: §c" + getPlayers());
		Bukkit.broadcastMessage("");
		executeCheck();
	}
	
	public void executeCheck() {
		if (sumox1 == null) {
			return;
		}
		if (sumox1.getStatus() != statusType.FECHADO) {
			return;
		}
		if (sumox1.getParticipantes() == null) {
			return;
		}
		HashMap<Player, Participant> participantes = sumox1.getParticipantes();
		int players = participantes.size();
		if (players > 1) {
			executeEnd();
			return;
		}
		Player player1 = null;
		Player player2 = null;
		for (Player player : participantes.keySet()) {
			if (player1 == null) {
				player1 = player;
				continue;
			} else if (player2 == null) {
				player2 = player;
			}
			if ((player1 != null) && (player2 != null)) {
				executeDuel(player1, player2, participantes);
				break;
			}
		}
	}
	
	private void executeDuel(Player player1, Player player2, HashMap<Player, Participant> participantes) {
		Participant participant1 = participantes.get(player1);
		Participant participant2 = participantes.get(player2);
		Bukkit.broadcastMessage(prefix + "§fDuelo entre §c" + player1.getName() + " §fx §c" + player2.getName());
		Bukkit.broadcastMessage(prefix + "§fEnviando jogadores para PvP em §c5 §fsegundos.");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if ((participant1 == null) || (participant2 == null)) {
					executeCheck();
					return;
				}
				//teleportar jogadores para as localizações de pvp
				player1.teleport(getLocation("pos1"));
				player2.teleport(getLocation("pos2"));
				participant1.setDueling(true);
				participant2.setDueling(true);
				sumox1.getParticipantes().put(player1, participant1);
				sumox1.getParticipantes().put(player2, participant2);
			}
		}.runTaskLater(instance, 5 * 20L);
	}
	
	private void executeEnd() {
		sumox1.reset();
		Player player = null;
		String playerName = null;
		for (Player participante : sumox1.getParticipantes().keySet()) {
			player = participante;
			playerName = participante.getName();
			break;
		}
		depositPremio(player);
		ganhador = playerName;
		config.set("ganhador", ganhador);
		saveConfig();
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §c§lKILLER §ffoi finalizado");
		Bukkit.broadcastMessage(prefix + "§fVencedor: §c" + playerName);
		Bukkit.broadcastMessage("");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				sumox1.getParticipantes().keySet().forEach(player -> {
					player.teleport(getLocation("saida"));
					player.sendMessage("§aVocê foi teleportado até a saida.");
				});
				sumox1 = null;
			}
		}.runTaskLater(instance, 600);
	}
	
	public void executeCancel() {
		Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado.");
		sumox1.setStatus(statusType.CANCELADO);
		sumox1.reset();
		sumox1.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
		sumox1 = null;
	}
	
	public boolean hasLocations() {
		LocationsManager locationsManager = instance.getLocationsManager();
		String category = "SumoX1";
		if (locationsManager.get(category, "spawn") == null) {
			return false;
		}
		if (locationsManager.get(category, "saida") == null) {
			return false;
		}
		if (locationsManager.get(category, "pos1") == null) {
			return false;
		}
		if (locationsManager.get(category, "pos2") == null) {
			return false;
		}
		return true;
	}
	
	public Location getLocation(String locationName) {
		LocationsManager locationsManager = instance.getLocationsManager();
		return locationsManager.get("SumoX1", locationName);
	}
	
	public void setLocation(String locationName, Location location) {
		LocationsManager locationsManager = instance.getLocationsManager();
		locationsManager.setLocation("SumoX1", locationName, location);
	}
	
	public void depositPremio(Player player) {
		player.sendMessage(prefix + "§eVocê recebeu §2$§f" + premio + " §ecomo prêmio.");
		instance.getEconomy().depositPlayer(player, premio);
	}
	
	public int getPlayers() {
		if (sumox1 != null) {
			return sumox1.getParticipantes().size();
		}
		return 0;
	}
	
	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "sumox1.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SumoX1 getSumox1() {
		return sumox1;
	}

	public class Settings {
		
		private int minPlayers;
		private Announce chamando;
		
		public Settings() {
			this.minPlayers = config.getInt("minJogadores");
			this.chamando = new Announce(config.getInt("chamando.avisos"), config.getInt("chamando.tempoEntreAvisos"));
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
}