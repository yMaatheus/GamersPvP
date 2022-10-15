package dev.gamerspvp.gladiador.gladiador.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.gladiador.GladiadorCommandManager;
import dev.gamerspvp.gladiador.gladiador.GladiadorManager;
import dev.gamerspvp.gladiador.gladiador.models.Gladiador;
import dev.gamerspvp.gladiador.gladiador.models.Gladiador.statusType;
import dev.gamerspvp.gladiador.topclans.ClanTop;
import dev.gamerspvp.gladiador.topclans.ClanTopManager;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class GladiadorCommand extends Command {
	
	private Main instance;
	
	public GladiadorCommand(Main instance) {
		super("gladiador");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		GladiadorManager gladiadorManager = instance.getGladiadorManager();
		GladiadorCommandManager gladiadorCommandManager = gladiadorManager.getGladiadorCommandManager();
		SimpleClans simpleClans = instance.getSimpleClans();
		boolean permission = sender.hasPermission("gladiador.admin");
		if (args.length > 0) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					gladiadorCommandManager.start(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("forcedeathmatch")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					gladiadorCommandManager.forceDeathmatch(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("parar") || args[0].equalsIgnoreCase("cancelar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					gladiadorCommandManager.cancel(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("set")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					if (args.length < 2) {
						sender.sendMessage("§cDefina a localização.");
						return false;
					}
					if (!(sender instanceof Player)) {
			    		return false;
			    	}
			    	Player player = (Player) sender;
					String locationName = args[1].toLowerCase();
					List<String> locations = Arrays.asList(new String[] {"deathmatch","spawn", "saida"});
					if (!(locations.contains(locationName))) {
						sender.sendMessage("§cVocê so pode definir as seguintes localizações: " + locations);
						return false;
					}
					Location location = player.getLocation();
					gladiadorManager.setLocation(locationName, location);
					sender.sendMessage("§aLocalização definida com sucesso.");
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					gladiadorCommandManager.info(sender);	
					return true;
				}
			}
		    if (args[0].equalsIgnoreCase("sair")) {
		    	if (!(sender instanceof Player)) {
		    		return false;
		    	}
		    	Player player = (Player) sender;
		    	Gladiador gladiador = gladiadorManager.getGladiador();
				if (gladiador == null) {
					sender.sendMessage("§cO evento não está acontecendo.");
					return false;
				}
				ClanPlayer participant = gladiador.getParticipantes().get(player);
				if (participant == null) {
					sender.sendMessage("§cVocê não está no evento!");
					return false;
				}
				Clan clan = participant.getClan();
				gladiador.getParticipantes().remove(player);
				gladiador.removeClan(clan, instance);
				player.teleport(gladiadorManager.getLocation("saida"));
				player.sendMessage("§aVocê saiu do evento Gladiador.");
				return true;
			} else if (args[0].equalsIgnoreCase("top")) {
				ClanTopManager clanTopManager = instance.getClanTopManager();
				sender.sendMessage("");
				sender.sendMessage("           §2§lCLAN TOP           ");
				sender.sendMessage("");
				List<ClanTop> clanTopList = clanTopManager.getTopList();
				for (int a = 0; a < clanTopList.size(); a++) {
					ClanTop clanTop = clanTopList.get(a);
					int position = a + 1;
					String clanTag = clanTop.getClan();
					Clan clan = simpleClans.getClanManager().getClan(clanTop.getClan());
					if (clan != null) {
						clanTag = ClansAPI.getClanTag(clan);
					}
					sender.sendMessage(" §f" + position + "§7. §a" + clanTag + " §7- §f" + clanTop.getWins() + " vitórias");
				}
				sender.sendMessage("");
				return true;
			}
			gladiadorCommandManager.sendHelpCommands(sender, arg);
			return true;
		}
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		Gladiador gladiador = gladiadorManager.getGladiador();
		if (gladiador == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return false;
		}
		if (gladiador.getStatus() != statusType.CHAMANDO) {
			sender.sendMessage("§cO evento já encontra-se fechado.");
			return false;
		}
		ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
		if (clanPlayer == null) {
			sender.sendMessage("§cPara entrar no evento é necessário possuir um clan.");
			return false;
		}
		Clan clan = clanPlayer.getClan();
		int participantsClan = gladiador.getParticipantsClan(clan);
		if (participantsClan >= gladiadorManager.getSettings().getMaxMembersPerClan()) {
			sender.sendMessage("§cO seu clan já chegou ao maximo de membros permitidos no evento.");
			return false;
		}
		if (gladiador.getParticipantes().get(player) != null) {
			sender.sendMessage("§cVocê já está participando do evento!");
			return false;
		}
		player.teleport(gladiadorManager.getLocation("spawn"));
		player.setAllowFlight(false);
		clanPlayer.setFriendlyFire(false);
		gladiador.getpKills().put(playerName.toLowerCase(), 0);
		gladiador.getParticipantes().put(player, clanPlayer);
		if (participantsClan == 0) {
			gladiador.getClans().add(clan);
		}
		sender.sendMessage("§aVocê entrou no evento gladiador!");
 		return false;
	}
}