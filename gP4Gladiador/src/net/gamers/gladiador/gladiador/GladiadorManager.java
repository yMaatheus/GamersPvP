package net.gamers.gladiador.gladiador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamers.gladiador.Main;
import net.gamers.gladiador.gladiador.Gladiador.statusType;
import net.gamers.gladiador.mito.MitoManager;
import net.gamers.gladiador.utils.ClansAPI;
import net.gamers.gladiador.utils.LocationsManager;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class GladiadorManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private GladiadorSettings settings;
	private Gladiador gladiador;
	private GladiadorCommandManager gladiadorCommandManager;
	
	private HashSet<String> gladiadores;
	public String prefix = "§3[Gladiador] §r";
	
	public GladiadorManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.settings = new GladiadorSettings();
		this.gladiador = null;
		this.gladiadorCommandManager = new GladiadorCommandManager(instance);
		this.gladiadores = new HashSet<String>(config.getStringList("gladiadores"));
		new GladiadorListener(instance);
	}
	
	public void executeStart() {
		executeClearDrops();
		this.gladiador = new Gladiador();
		int alerts = settings.chamando.alerts;
		int time = settings.chamando.time;
		gladiador.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (gladiador.getAlerts() >= alerts) {
					if ((getClans() < settings.minClans) || (getPlayers() < settings.minPlayers)) {
						Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado por insuficiência de participantes.");
						gladiador.setStatus(statusType.CANCELADO);
						gladiador.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
						gladiador.reset();
						gladiador = null;
						return;
					}
					executeClose();
				} else {
					String tempo = String.valueOf((alerts - gladiador.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fEvento §3§lGLADIADOR §fsendo iniciado!");
					Bukkit.broadcastMessage(prefix + "§fTempo restante: §3" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fPara participar digite: §3/Gladiador");
					Bukkit.broadcastMessage(prefix + "§fTags: §6[Gladiador] §3e §5[Mito]");
					Bukkit.broadcastMessage(prefix + "§fKit setado: §3Não!");
					Bukkit.broadcastMessage(prefix + "§fMáximo Membros por clan: §3" + settings.getMaxMembersPerClan());
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					gladiador.setAlerts(gladiador.getAlerts() + 1);
				}
			}
		}.runTaskTimer(instance, 20L, time * 20L));
	}
	
	private void executeClose() {
		gladiador.setStatus(statusType.FECHADO);
		gladiador.reset();
		int alerts = settings.iniciando.alerts;
		int time = settings.iniciando.time;
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §3§lGLADIADOR §ffechado!");
		Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
		Bukkit.broadcastMessage("");
		gladiador.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (gladiador.getAlerts() >= alerts) {
					gladiador.setStatus(statusType.PVP);
					gladiador.reset();
					gladiador.setTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(settings.forceDeathmatchTime));
					logReportClansMembers();
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP foi do Evento §3§lGLADIADOR §ffoi iniciado!");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
				} else {
					String tempo = String.valueOf((alerts - gladiador.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fIniciando PvP em: §3" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					gladiador.setAlerts(gladiador.getAlerts() + 1);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, time * 20L));
	}
	
	public void executeCheck() {
		if (gladiador == null) {
			return;
		}
		int clans = getClans();
		if (clans == 1) {
			executeEnd();
		} else {
			statusType status = gladiador.getStatus();
			if ((status != statusType.PVP) || (status == statusType.DEATHMATCH_PVPOFF) || (status == statusType.DEATHMATCH_PVPON)) {
				return;
			}
			int players = getPlayers();
			if ((System.currentTimeMillis() > gladiador.getTime()) || (clans <= settings.clansForStartDeathmatch)) {
				if (players > 35) {
					System.out.println("[Gladiador] Evento com mais de 35 membros, deathmatch não será acionada");
					return;
				}
				executeDeathmatch();
			}
		}
	}
	
	public void executeDeathmatch() {
		gladiador.setStatus(statusType.DEATHMATCH_PVPOFF);
		gladiador.setTime(0);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				executeClearDrops();
				gladiador.getParticipantes().keySet().forEach(player -> {
					player.teleport(getLocation("deathmatch"));
					player.sendMessage("§aVocê foi teleportado até a Arena Deathmatch.");
				});
			}
		}.runTaskLater(instance, 20 * 20L);
		int alerts = settings.deathmatch.alerts;
		int time = settings.deathmatch.time;
		gladiador.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (gladiador.getAlerts() >= alerts) {
					gladiador.setStatus(statusType.DEATHMATCH_PVPON);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP foi do Evento §3§lGLADIADOR §ffoi iniciado!");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					gladiador.reset();
				} else {
					String tempo = String.valueOf((alerts - gladiador.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP do evento §3§lGLADIADOR §ffoi desativado!");
					Bukkit.broadcastMessage(prefix + "§fIniciando PvP em: §3" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fClans: §3" + getClans() + " §7- §fJogadores: §3" + getPlayers());
					Bukkit.broadcastMessage("");
					gladiador.setAlerts(gladiador.getAlerts() + 1);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, time * 20L));
	}
	
	private void executeEnd() {
		gladiador.reset();
		gladiadores.clear();
		Clan clanWinner = gladiador.getClans().stream().findFirst().get();
		String clanWinnerTag = clanWinner.getTag();
		String clanTag = ClansAPI.getClanTag(clanWinner);
		HashSet<Gladiador.Kills> allValues = getGladiadorKills(clanWinner);
		HashSet<Gladiador.Kills> topList = new HashSet<Gladiador.Kills>();
		Gladiador.Kills mito = gladiador.top(1, allValues).get(0);
		Player mitoPlayer = mito.getPlayer();
		String formatMitoKills = killsFormat(mitoPlayer, mito.getKills());
		List<String> formatGladiadoresKills = new ArrayList<String>();
		
		topList.addAll(gladiador.top(3, allValues));
		MitoManager mitoManager = instance.getMitoManager();
		if (!(mitoManager.isMito(mito.getName()))) {
			mitoManager.setNewMito(mitoPlayer);
		}
		topList.forEach(gladiador -> {
			Player player = gladiador.getPlayer();
			String playerName = player.getName();
			gladiadores.add(playerName);
			formatGladiadoresKills.add(killsFormat(player, gladiador.getKills()));
		});
		defineGladiadores(new ArrayList<String>(gladiadores));
		logWinnersGladiador(clanWinnerTag, new ArrayList<String>(gladiadores));
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §3§lGLADIADOR §ffoi finalizado");
		Bukkit.broadcastMessage(prefix + "§fClan ganhador: §3" + clanTag);
		Bukkit.broadcastMessage(prefix + "§fMito: §3" + formatMitoKills);
		Bukkit.broadcastMessage(prefix + "§fGladiadores: " + StringUtils.join(formatGladiadoresKills, "§3,"));
		Bukkit.broadcastMessage("");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				gladiador.getParticipantes().keySet().forEach(player -> {
					player.teleport(getLocation("saida"));
					player.sendMessage("§aVocê foi teleportado até a saida.");
				});
				executeClearDrops();
				gladiador = null;
			}
		}.runTaskLater(instance, 600);
	}
	
	public void executeCancel() {
		Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado.");
		gladiador.setStatus(statusType.CANCELADO);
		gladiador.reset();
		gladiador.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
		gladiador = null;
	}
	
	public boolean hasLocations() {
		LocationsManager locationsManager = instance.getLocationsManager();
		String category = "Gladiador";
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
	
	private void executeClearDrops() {
		Chunk spawnChunk = getLocation("spawn").getChunk();
		Chunk deathmatchChunk = getLocation("deathmatch").getChunk();
		spawnChunk.load();
		deathmatchChunk.load();
		for (Entity i : spawnChunk.getWorld().getEntities()) {
			if (i instanceof Item) {
				if (!i.isDead()) {
					i.remove();
				}
			}
		}
		for (Entity i : deathmatchChunk.getWorld().getEntities()) {
			if (i instanceof Item) {
				if (!i.isDead()) {
					i.remove();
				}
			}
		}
	}
	
	public void logReportClansMembers() {
		HashMap<Player, ClanPlayer> participants = new HashMap<Player, ClanPlayer>();
		participants.putAll(gladiador.getParticipantes());
		Set<String> list = new HashSet<String>();
		for (Clan clan : gladiador.getClans()) {
			int i = 0;
			for (ClanPlayer member : clan.getAllMembers()) {
				Player player = Bukkit.getPlayer(member.getName());
				if (player == null) {
					continue;
				}
				ClanPlayer clanPlayer = participants.get(player);
				if (clanPlayer == null) {
					continue;
				}
				i++;
			}
			list.add(clan.getTag() + "(" + i + ")");
		}
		log(list.toString());
	}
	
	public void logWinnersGladiador(String ClanWinnerTag, List<String> gladiadores) {
		log("Clan ganhador: " + ClanWinnerTag + " ganhadores: " + gladiadores);
	}
	
	public void log(String message) {
		try {
			File saveTo = new File(instance.getDataFolder(), "gladiador-log.txt");
			if (!saveTo.exists()) {
				saveTo.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(saveTo, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[dd/MM/yyyy hh:mm:ss] ");
			String data = simpleDateFormat.format(calendar.getTime());
			printWriter.println(data + message);
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Location getLocation(String locationName) {
		LocationsManager locationsManager = instance.getLocationsManager();
		return locationsManager.get("Gladiador", locationName);
	}
	
	public void setLocation(String locationName, Location location) {
		LocationsManager locationsManager = instance.getLocationsManager();
		locationsManager.setLocation("Gladiador", locationName, location);
	}
	
	public void defineGladiadores(List<String> formatGladiadores) {
		try {
			config.set("gladiadores", formatGladiadores);
			config.save(new File(instance.getDataFolder(), "gladiador.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String killsFormat(Player player, int kills) {
		return "§f" + player.getName() + "§8(§f" + kills + "§8)§r";
	}
	
	public HashSet<Gladiador.Kills> getGladiadorKills(Clan clanVencedor) {
		HashSet<Gladiador.Kills> topAll = new HashSet<Gladiador.Kills>();
		for (String name : gladiador.getpKills().keySet()) {
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
			int kills = gladiador.getpKills().get(name);
			if (gladiador.getParticipantes().get(player) == null) {
				continue;
			}
			topAll.add(gladiador.kills(name, player, kills));
		}
		return topAll;
	}
	
	public String getClans(Gladiador gladiador) {
		HashSet<String> clansTag = new HashSet<String>();
		for (Clan clan : gladiador.getClans()) {
			clansTag.add(ClansAPI.getClanTag(clan));
		}
		return StringUtils.join(clansTag, ",");
	}
	
	public String getParticipants(Gladiador gladiador) {
		HashSet<String> pk = new HashSet<String>();
		for (Player participant : gladiador.getParticipantes().keySet()) {
			String participantName = participant.getName();
			int kills = gladiador.getpKills().get(participantName.toLowerCase());
			pk.add(killsFormat(participant, kills));
		}
		return StringUtils.join(pk, ",");
	}
	
	public int getClans() {
		if (gladiador != null) {
			return gladiador.getClans().size();
		}
		return 0;
	}
	
	public int getPlayers() {
		if (gladiador != null) {
			return gladiador.getParticipantes().size();
		}
		return 0;
	}
	
	public class GladiadorSettings {
		
		private int maxMembersPerClan;
		private int minPlayers;
		private int minClans;
		private long forceDeathmatchTime;
		private int clansForStartDeathmatch;
		private Announce chamando;
		private Announce iniciando;
		private Announce deathmatch;
		
		public GladiadorSettings() {
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
	
	public GladiadorSettings getSettings() {
		return settings;
	}
	
	public HashSet<String> getGladiadores() {
		return gladiadores;
	}
	
	public Gladiador getGladiador() {
		return gladiador;
	}
	
	public GladiadorCommandManager getGladiadorCommandManager() {
		return gladiadorCommandManager;
	}
}