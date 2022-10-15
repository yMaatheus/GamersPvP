package dev.gamerspvp.gladiador.guerra.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.guerra.GuerraCommandManager;
import dev.gamerspvp.gladiador.guerra.GuerraManager;
import dev.gamerspvp.gladiador.guerra.models.Guerra;
import dev.gamerspvp.gladiador.guerra.models.Guerra.statusType;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class GuerraCommand extends Command {
	
	private Main instance;
	
	public GuerraCommand(Main instance) {
		super("guerra");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		GuerraManager guerraManager = instance.getGuerraManager();
		GuerraCommandManager guerraCommandManager = guerraManager.getGuerraCommandManager();
		SimpleClans simpleClans = instance.getSimpleClans();
		boolean permission = sender.hasPermission("guerra.admin");
		if (args.length > 0) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					guerraCommandManager.start(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("parar") || args[0].equalsIgnoreCase("cancelar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					guerraCommandManager.cancel(sender);
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
					guerraManager.setLocation(locationName, location);
					sender.sendMessage("§aLocalização definida com sucesso.");
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					guerraCommandManager.info(sender);	
					return true;
				}
			}
		    if (args[0].equalsIgnoreCase("sair")) {
		    	if (!(sender instanceof Player)) {
		    		return false;
		    	}
		    	Player player = (Player) sender;
		    	Guerra guerra = guerraManager.getGuerra();
				if (guerra == null) {
					sender.sendMessage("§cO evento não está acontecendo.");
					return false;
				}
				ClanPlayer participant = guerra.getParticipantes().get(player);
				if (participant == null) {
					sender.sendMessage("§cVocê não está no evento!");
					return false;
				}
				Clan clan = participant.getClan();
				guerra.getParticipantes().remove(player);
				guerra.removeClan(clan, instance);
				player.teleport(guerraManager.getLocation("saida"));
				player.sendMessage("§aVocê saiu do evento Guerra.");
				return true;
			}
			guerraCommandManager.sendHelpCommands(sender, arg);
			return true;
		}
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		Guerra guerra = guerraManager.getGuerra();
		if (guerra == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return false;
		}
		if (guerra.getStatus() != statusType.CHAMANDO) {
			sender.sendMessage("§cO evento já encontra-se fechado.");
			return false;
		}
		ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
		if (clanPlayer == null) {
			sender.sendMessage("§cPara entrar no evento é necessário possuir um clan.");
			return false;
		}
		Clan clan = clanPlayer.getClan();
		int participantsClan = guerra.getParticipantsClan(clan);
		if (participantsClan >= guerraManager.getSettings().getMaxMembersPerClan()) {
			sender.sendMessage("§cO seu clan já chegou ao maximo de membros permitidos no evento.");
			return false;
		}
		if (guerra.getParticipantes().get(player) != null) {
			sender.sendMessage("§cVocê já está participando do evento!");
			return false;
		}
		player.teleport(guerraManager.getLocation("spawn"));
		player.setAllowFlight(false);
		clanPlayer.setFriendlyFire(false);
		guerra.getpKills().put(playerName.toLowerCase(), 0);
		guerra.getParticipantes().put(player, clanPlayer);
		if (participantsClan == 0) {
			guerra.getClans().add(clan);
		}
		sender.sendMessage("§aVocê entrou no evento Guerra!");
 		return false;
	}
}