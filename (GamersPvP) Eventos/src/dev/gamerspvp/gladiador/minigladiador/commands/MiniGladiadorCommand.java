package dev.gamerspvp.gladiador.minigladiador.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.minigladiador.MiniGladiadorCommandManager;
import dev.gamerspvp.gladiador.minigladiador.MiniGladiadorManager;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador;
import dev.gamerspvp.gladiador.minigladiador.models.MiniGladiador.statusType;
import dev.gamerspvp.gladiador.utils.InventoryUtils;
import dev.gamerspvp.gladiador.utils.Item;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class MiniGladiadorCommand extends Command {
	
	private Main instance;
	
	public MiniGladiadorCommand(Main instance) {
		super("minigladiador");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		MiniGladiadorManager miniGladiadorManager = instance.getMiniGladiadorManager();
		MiniGladiadorCommandManager miniGladiadorCommand = miniGladiadorManager.getMiniGladiadorCommand();
		SimpleClans simpleClans = instance.getSimpleClans();
		boolean permission = sender.hasPermission("minigladiador.admin");
		if (args.length > 0) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permiss§o.");
						return false;
					}
					miniGladiadorCommand.start(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("forcedeathmatch")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permiss§o.");
						return false;
					}
					miniGladiadorCommand.forceDeathmatch(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("parar") || args[0].equalsIgnoreCase("cancelar")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permiss§o.");
						return false;
					}
					miniGladiadorCommand.cancel(sender);
					return true;
				} else if (args[0].equalsIgnoreCase("set")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permiss§o.");
						return false;
					}
					if (args.length < 2) {
						sender.sendMessage("§cDefina a localiza§§o.");
						return false;
					}
					if (!(sender instanceof Player)) {
			    		return false;
			    	}
			    	Player player = (Player) sender;
					String locationName = args[1].toLowerCase();
					List<String> locations = Arrays.asList(new String[] {"deathmatch","spawn", "saida"});
					if (!(locations.contains(locationName))) {
						sender.sendMessage("§cVoc§ so pode definir as seguintes localiza§§es: " + locations);
						return false;
					}
					Location location = player.getLocation();
					miniGladiadorManager.setLocation(locationName, location);
					sender.sendMessage("§aLocaliza§§o definida com sucesso.");
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					if (!(permission)) {
						sender.sendMessage("§cSem permiss§o.");
						return false;
					}
					miniGladiadorCommand.info(sender);	
					return true;
				}
			}
		    if (args[0].equalsIgnoreCase("sair")) {
		    	if (!(sender instanceof Player)) {
		    		return false;
		    	}
		    	Player player = (Player) sender;
		    	MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
				if (miniGladiador == null) {
					sender.sendMessage("§cO evento n§o est§ acontecendo.");
					return false;
				}
				ClanPlayer participant = miniGladiador.getParticipantes().get(player);
				if (participant == null) {
					sender.sendMessage("§cVoc§ n§o est§ no evento!");
					return false;
				}
				Clan clan = participant.getClan();
				miniGladiador.getParticipantes().remove(player);
				miniGladiador.removeClan(clan, instance);
				PlayerInventory playerInventory = player.getInventory();
				playerInventory.clear();
				playerInventory.setArmorContents(null);
				player.teleport(miniGladiadorManager.getLocation("saida"));
				player.sendMessage("§aVoc§ saiu do evento MiniGladiador.");
				return true;
			} else if (args[0].equalsIgnoreCase("setkit")) {
				if (!(sender instanceof Player)) {
		    		return false;
		    	}
				if (!(permission)) {
					sender.sendMessage("§cSem permiss§o.");
					return false;
				}
		    	Player player = (Player) sender;
		    	PlayerInventory playerInventory = player.getInventory();
		    	List<String> itens = new ArrayList<String>();
				for (ItemStack i : playerInventory.getContents()) {
					itens.add(InventoryUtils.itemstackToString(miniGladiadorManager.setItem(new Item(i))));
				}
				ItemStack helmet = miniGladiadorManager.setItem(new Item(playerInventory.getHelmet())).clone();
				ItemStack chestplate = miniGladiadorManager.setItem(new Item(playerInventory.getChestplate())).clone();
				ItemStack leggings = miniGladiadorManager.setItem(new Item(playerInventory.getLeggings())).clone();
				ItemStack boots = miniGladiadorManager.setItem(new Item(playerInventory.getBoots())).clone();
				Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER, "SetKit");
				for (String itemString : itens) {
					inventory.addItem(InventoryUtils.stringToItemStack(itemString));
				}
				miniGladiadorManager.setMiniGladiadorKit(inventory, helmet, chestplate, leggings, boots);
				playerInventory.clear();
				playerInventory.setArmorContents(null);
				sender.sendMessage("§aKit do MiniGladiador definido com sucesso.");
				return true;
			}
			miniGladiadorCommand.sendHelpCommands(sender, permission, arg);
			return true;
		}
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		PlayerInventory playerInventory = player.getInventory();
		MiniGladiador miniGladiador = miniGladiadorManager.getMiniGladiador();
		if (miniGladiador == null) {
			sender.sendMessage("§cO evento n§o est§ acontecendo.");
			return false;
		}
		if (miniGladiador.getStatus() != statusType.CHAMANDO) {
			sender.sendMessage("§cO evento j§ encontra-se fechado.");
			return false;
		}
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null) {
				continue;
			}
			player.sendMessage("§cLimpe seu invent§rio antes de entrar no evento.");
			return true;
		}
		ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player);
		if (clanPlayer == null) {
			sender.sendMessage("§cPara entrar no evento § necess§rio possuir um clan.");
			return false;
		}
		Clan clan = clanPlayer.getClan();
		int participantsClan = miniGladiador.getParticipantsClan(clan);
		if (participantsClan >= miniGladiadorManager.getSettings().getMaxMembersPerClan()) {
			sender.sendMessage("§cO seu clan j§ chegou ao maximo de membros permitidos no evento.");
			return false;
		}
		if (miniGladiador.getParticipantes().get(player) != null) {
			sender.sendMessage("§cVoc§ j§ est§ participando do evento!");
			return false;
		}
		playerInventory.setArmorContents(null);
		player.setAllowFlight(false);
		player.teleport(miniGladiadorManager.getLocation("spawn"));
		clanPlayer.setFriendlyFire(false);
		miniGladiador.getpKills().put(playerName.toLowerCase(), 0);
		miniGladiador.getParticipantes().put(player, clanPlayer);
		if (participantsClan == 0) {
			miniGladiador.getClans().add(clan);
		}
		miniGladiadorManager.setPlayerKit(player);
		sender.sendMessage("§aVoc§ entrou no evento MiniGladiador!");
		return false;
	}
}