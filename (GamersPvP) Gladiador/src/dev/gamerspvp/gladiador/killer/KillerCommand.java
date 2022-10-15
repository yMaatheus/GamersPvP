package dev.gamerspvp.gladiador.killer;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.killer.Killer.statusType;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class KillerCommand extends Command {
	
	private Main instance;
	
	public KillerCommand(Main instance) {
		super("killer");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		KillerManager killerManager = instance.getKillerManager();
		KillerCommandManager killerCommand = killerManager.getKillerCommand();
		boolean permission = sender.hasPermission("killer.admin");
		if (args.length > 0) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					killerCommand.start(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("parar") || args[0].equalsIgnoreCase("cancelar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					killerCommand.cancel(sender);
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
					List<String> locations = Arrays.asList(new String[] {"spawn", "saida"});
					if (!(locations.contains(locationName))) {
						sender.sendMessage("§cVocê so pode definir as seguintes localizações: " + locations);
						return false;
					}
					Location location = player.getLocation();
					killerManager.setLocation(locationName, location);
					sender.sendMessage("§aLocalização definida com sucesso.");
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permissão.");
						return false;
					}
					killerCommand.info(sender);	
					return true;
				}
			}
		    if (args[0].equalsIgnoreCase("sair")) {
		    	if (!(sender instanceof Player)) {
		    		return false;
		    	}
		    	Player player = (Player) sender;
		    	Killer killer = killerManager.getKiller();
				if (killer == null) {
					sender.sendMessage("§cO evento não está acontecendo.");
					return false;
				}
				String participant = killer.getParticipantes().get(player);
				if (participant == null) {
					sender.sendMessage("§cVocê não está no evento!");
					return false;
				}
				killer.getParticipantes().remove(player);
				killerManager.executeCheck();
				player.teleport(killerManager.getLocation("saida"));
				player.sendMessage("§aVocê saiu do evento Gladiador.");
				return true;
			}
		    killerCommand.sendHelpCommands(sender, arg);
			return true;
		}
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		Killer killer = killerManager.getKiller();
		if (killer == null) {
			sender.sendMessage("§cO evento não está acontecendo.");
			return false;
		}
		if (killer.getStatus() != statusType.CHAMANDO) {
			sender.sendMessage("§cO evento já encontra-se fechado.");
			return false;
		}
		SimpleClans simpleClans = instance.getSimpleClans();
		ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
		if (clanPlayer != null) {
			clanPlayer.setFriendlyFire(true);
		}
		if (killer.getParticipantes().get(player) != null) {
			sender.sendMessage("§cVocê já está participando do evento!");
			return false;
		}
		player.setAllowFlight(false);
		player.teleport(killerManager.getLocation("spawn"));
		killer.getParticipantes().put(player, playerName);
		sender.sendMessage("§aVocê entrou no evento Killer!");
 		return false;
	}
}