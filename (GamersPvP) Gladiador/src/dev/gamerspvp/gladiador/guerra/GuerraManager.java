package dev.gamerspvp.gladiador.guerra;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.guerra.models.Guerra;
import dev.gamerspvp.gladiador.guerra.models.Guerra.statusType;
import dev.gamerspvp.gladiador.locations.LocationsManager;
import dev.gamerspvp.gladiador.mito.MitoManager;
import dev.gamerspvp.gladiador.topclans.ClanTop;
import dev.gamerspvp.gladiador.topclans.ClanTopManager;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class GuerraManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private GuerraSettings settings;
	private Guerra guerra;
	private GuerraCommandManager guerraCommandManager;
	
	private double premio;
	private HashSet<String> ganhadores;
	public String prefix = "§a[Guerra] §r";
	
	public GuerraManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.settings = new GuerraSettings();
		this.guerra = null;
		this.guerraCommandManager = new GuerraCommandManager(instance);
		this.premio = config.getDouble("premio");
		this.ganhadores = new HashSet<String>(config.getStringList("ganhadores"));
		new GuerraListener(instance);
	}
	
	public void executeStart() {
		executeClearDrops();
		this.guerra = new Guerra();
		int alerts = settings.chamando.alerts;
		int time = settings.chamando.time;
		guerra.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (guerra.getAlerts() >= alerts) {
					if ((getClans() < settings.minClans) || (getPlayers() < settings.minPlayers)) {
						Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado por insuficiência de participantes.");
						guerra.setStatus(statusType.CANCELADO);
						guerra.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
						guerra.reset();
						guerra = null;
						return;
					}
					executeClose();
				} else {
					String tempo = String.valueOf((alerts - guerra.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fEvento §a§lGUERRA §fsendo iniciado!");
					Bukkit.broadcastMessage(prefix + "§fTempo restante: §a" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fPara participar digite: §a/Guerra");
					Bukkit.broadcastMessage(prefix + "§fPremios: §2$§a" + premio + " + §fTags: §a[Guerra] §fe §5[Mito]");
					Bukkit.broadcastMessage(prefix + "§fClans: §a" + getClans() + " §7- §fJogadores: §a" + getPlayers());
					Bukkit.broadcastMessage("");
					guerra.setAlerts(guerra.getAlerts() + 1);
				}
			}
		}.runTaskTimer(instance, 20L, time * 20L));
	}
	
	private void executeClose() {
		guerra.setStatus(statusType.FECHADO);
		guerra.reset();
		int alerts = settings.iniciando.alerts;
		int time = settings.iniciando.time;
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §a§lGUERRA §ffechado!");
		Bukkit.broadcastMessage(prefix + "§fClans: §a" + getClans() + " §7- §fJogadores: §a" + getPlayers());
		Bukkit.broadcastMessage("");
		guerra.setTask(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (guerra.getAlerts() >= alerts) {
					guerra.setStatus(statusType.PVP);
					guerra.reset();
					logReportClansMembers();
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fO PvP foi do Evento §a§lGUERRA §ffoi iniciado!");
					Bukkit.broadcastMessage(prefix + "§fClans: §a" + getClans() + " §7- §fJogadores: §a" + getPlayers());
					Bukkit.broadcastMessage("");
				} else {
					String tempo = String.valueOf((alerts - guerra.getAlerts()) * time);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(prefix + "§fIniciando PvP em: §a" + tempo + " §fsegundos");
					Bukkit.broadcastMessage(prefix + "§fClans: §a" + getClans() + " §7- §fJogadores: §a" + getPlayers());
					Bukkit.broadcastMessage("");
					guerra.setAlerts(guerra.getAlerts() + 1);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, time * 20L));
	}
	
	public void executeCheck() {
		if (guerra == null) {
			return;
		}
		int clans = getClans();
		if (clans == 1) {
			executeEnd();
		}
	}
	
	private void executeEnd() {
		guerra.reset();
		ganhadores.clear();
		Clan clanWinner = guerra.getClans().stream().findFirst().get();
		String clanWinnerTag = clanWinner.getTag();
		String clanTag = ClansAPI.getClanTag(clanWinner);
		HashSet<Guerra.Kills> allValues = kills(clanWinner);
		HashSet<Guerra.Kills> topList = new HashSet<Guerra.Kills>();
		Guerra.Kills mito = guerra.top(1, allValues).get(0);
		Player mitoPlayer = mito.getPlayer();
		String formatMitoKills = killsFormat(mitoPlayer, mito.getKills());
		List<String> formatWinnersKills = new ArrayList<String>();
		
		topList.addAll(guerra.top(3, allValues));
		MitoManager mitoManager = instance.getMitoManager();
		if (!(mitoManager.isMito(mito.getName()))) {
			mitoManager.setNewMito(mitoPlayer);
		}
		topList.forEach(winner -> {
			Player player = winner.getPlayer();
			String playerName = player.getName();
			depositPremio(player);
			ganhadores.add(playerName);
			formatWinnersKills.add(killsFormat(player, winner.getKills()));
		});
		defineGanhadores(new ArrayList<String>(ganhadores));
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(prefix + "§fEvento §a§lGUERRA §ffoi finalizado");
		Bukkit.broadcastMessage(prefix + "§fClan ganhador: §a" + clanTag);
		Bukkit.broadcastMessage(prefix + "§fMito: §a" + formatMitoKills);
		Bukkit.broadcastMessage(prefix + "§fGanhadores: " + StringUtils.join(formatWinnersKills, "§a, "));
		Bukkit.broadcastMessage("");
		addWinToClanTop(clanWinnerTag);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				guerra.getParticipantes().keySet().forEach(player -> {
					player.teleport(getLocation("saida"));
					player.sendMessage("§aVocê foi teleportado até a saida.");
				});
				executeClearDrops();
				guerra = null;
			}
		}.runTaskLater(instance, 600);
	}
	
	public void executeCancel() {
		Bukkit.broadcastMessage(prefix + "§cO Evento foi cancelado.");
		guerra.setStatus(statusType.CANCELADO);
		guerra.reset();
		guerra.teleportAll("saida", "§aVocê foi teleportado até a saida.", 0, instance);
		guerra = null;
	}
	
	public boolean hasLocations() {
		LocationsManager locationsManager = instance.getLocationsManager();
		String category = "Guerra";
		if (locationsManager.get(category, "spawn") == null) {
			return false;
		}
		if (locationsManager.get(category, "saida") == null) {
			return false;
		}
		return true;
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
	
	public void logReportClansMembers() {
		HashMap<Player, ClanPlayer> participants = new HashMap<Player, ClanPlayer>();
		participants.putAll(guerra.getParticipantes());
		Set<String> list = new HashSet<String>();
		for (Clan clan : guerra.getClans()) {
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
	}
	
	public void addWinToClanTop(String clanWinnerTag) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ClanTopManager clanTopManager = instance.getClanTopManager();
				try {
					ClanTop clanTop = clanTopManager.getClan(clanWinnerTag);
					clanTop.setWins(clanTop.getWins() + 1);
					clanTop.update(clanTopManager, instance);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				clanTopManager.updateTop();
			}
		}.runTaskAsynchronously(instance);
	}
	
	public Location getLocation(String locationName) {
		LocationsManager locationsManager = instance.getLocationsManager();
		return locationsManager.get("Guerra", locationName);
	}
	
	public void setLocation(String locationName, Location location) {
		LocationsManager locationsManager = instance.getLocationsManager();
		locationsManager.setLocation("Guerra", locationName, location);
	}
	
	public void defineGanhadores(List<String> ganhadores) {
		try {
			config.set("ganhadores", ganhadores);
			config.save(new File(instance.getDataFolder(), "guerra.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void depositPremio(Player player) {
		player.sendMessage(prefix + "§eVocê recebeu §2$§f" + premio + " §ecomo prêmio.");
		instance.getEconomy().depositPlayer(player, premio);
	}
	
	public String killsFormat(Player player, int kills) {
		return "§f" + player.getName() + "§8(§f" + kills + "§8)§r";
	}
	
	public HashSet<Guerra.Kills> kills(Clan clanVencedor) {
		HashSet<Guerra.Kills> topAll = new HashSet<Guerra.Kills>();
		for (String name : guerra.getpKills().keySet()) {
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
			int kills = guerra.getpKills().get(name);
			if (guerra.getParticipantes().get(player) == null) {
				continue;
			}
			topAll.add(guerra.kills(name, player, kills));
		}
		return topAll;
	}
	
	public String getClans(Guerra guerra) {
		HashSet<String> clansTag = new HashSet<String>();
		for (Clan clan : guerra.getClans()) {
			clansTag.add(ClansAPI.getClanTag(clan));
		}
		return StringUtils.join(clansTag, ", ");
	}
	
	public String getParticipants(Guerra guerra) {
		HashSet<String> pk = new HashSet<String>();
		for (Player participant : guerra.getParticipantes().keySet()) {
			String participantName = participant.getName();
			int kills = guerra.getpKills().get(participantName.toLowerCase());
			pk.add(killsFormat(participant, kills));
		}
		return StringUtils.join(pk, ", ");
	}
	
	public int getClans() {
		if (guerra != null) {
			return guerra.getClans().size();
		}
		return 0;
	}
	
	public int getPlayers() {
		if (guerra != null) {
			return guerra.getParticipantes().size();
		}
		return 0;
	}
	
	public class GuerraSettings {
		
		private int maxMembersPerClan;
		private int minPlayers;
		private int minClans;
		private Announce chamando;
		private Announce iniciando;
		
		public GuerraSettings() {
			this.maxMembersPerClan = config.getInt("membrosMaximosPorClan");
			this.minPlayers = config.getInt("minJogadores");
			this.minClans = config.getInt("minClans");
			this.chamando = new Announce(config.getInt("chamando.avisos"), config.getInt("chamando.tempoEntreAvisos"));
			this.iniciando = new Announce(config.getInt("iniciando.avisos"), config.getInt("iniciando.tempoEntreAvisos"));
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
	
	public GuerraSettings getSettings() {
		return settings;
	}
	
	public GuerraCommandManager getGuerraCommandManager() {
		return guerraCommandManager;
	}
	
	public Guerra getGuerra() {
		return guerra;
	}
	
	public HashSet<String> getGanhadores() {
		return ganhadores;
	}
}