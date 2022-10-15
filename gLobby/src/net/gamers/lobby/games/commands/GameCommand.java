package net.gamers.lobby.games.commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.gamers.lobby.Main;
import net.gamers.lobby.games.GamesManager;
import net.gamers.lobby.games.models.Game;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class GameCommand extends Command {
	
	private Main instance;
	
	public GameCommand(Main instance) {
		super("game");
		this.instance = instance;
		Utils.registerCommand(this, instance, "game");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamers.coordenador"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length > 1) {
			String gameName = args[1].substring(0,1).toUpperCase().concat(args[1].toLowerCase().substring(1));
			instance.getCommons().runAsync(() -> {
				try {
					if (args[0].equalsIgnoreCase("criar")) {
						Game game = new Game(gameName, null, null, null);
						if (game.exists()) {
							sender.sendMessage("§cEsse modo de jogo j§ foi criado.");
							return;
						}
						game.update();
						sender.sendMessage("§aVoc§ criou a op§§o do Modo de Jogo §f" + gameName + "§a com sucesso.");
						return;
					} else if (args.length > 2 && args[0].equalsIgnoreCase("setskin")) {
						String skinName = args[2];
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.setSkinName(skinName);
						game.update();
						sender.sendMessage("§aFoi atualizada o nome da pele do NPC representante do modo de jogo §f" + gameName + "§a.");
						return;
					} else if (args[0].equalsIgnoreCase("setitem")) {
						if (!(sender instanceof Player)) {
							return;
						}
						Player player = (Player) sender;
						ItemStack itemInHand = player.getItemInHand();
						if (itemInHand == null || player.getItemInHand().getType() == Material.AIR) {
							sender.sendMessage("§cItem inv§lido.");
							return;
						}
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.setItemStack(itemInHand);
						game.update();
						sender.sendMessage("§aFoi atualizado o item representante do modo de jogo §f" + gameName + "§a.");
						return;
					} else if (args.length > 2 && args[0].equalsIgnoreCase("setPosition")) {
						int position = 0;
						try {
							position = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							sender.sendMessage("§cValor inv§lido.");
							return;
						}
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.setPosition(position);;
						game.update();
						
						Game gameCache = GamesManager.getCache(gameName);
						if (gameCache != null) {
							gameCache.setPosition(position);
							GamesManager.put(gameCache);
							GamesManager.loadChestGames();
						}
						
						sender.sendMessage("§aFoi atualizado a posi§§o do modo de jogo §f" + gameName + "§a no seletor geral de jogos.");
						return;
					} else if (args[0].equalsIgnoreCase("setlocation")) {
						if (!(sender instanceof Player)) {
							return;
						}
						Player player = (Player) sender;
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.setLocation(player);
						game.update();
						sender.sendMessage("§aFoi atualizado a localiza§§o do NPC representante do modo de jogo §f" + gameName + "§a.");
						return;
					} else if (args.length > 2 && args[0].equalsIgnoreCase("addDesc")) {
						String description = "";
						for (int i = 2; i < args.length; i++) {
							if (i != (args.length - 1)) {
								description = description + args[i] + " ";
							} else {
								description = description + args[i];
							}
						}
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.getDescription().add(description);
						game.update();
						
						Game gameCache = GamesManager.getCache(gameName);
						if (gameCache != null) {
							gameCache.setDescription(game.getDescription());
							GamesManager.put(gameCache);
							GamesManager.loadChestGames();
						}
						sender.sendMessage("§aA descri§§o do modo de jogo §f" + gameName + "§a teve a linha §r" + description + "§a adicionada com sucesso.");
						return;
					} else if (args[0].equalsIgnoreCase("limparDesc") || args[0].equalsIgnoreCase("clearDesc")) {
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.setDescription(new ArrayList<>());
						game.update();
						
						Game gameCache = GamesManager.getCache(gameName);
						if (gameCache != null) {
							gameCache.setDescription(game.getDescription());
							GamesManager.put(gameCache);
							GamesManager.loadChestGames();
						}
						sender.sendMessage("§aA descri§§o do modo de jogo §f" + gameName + "§a foi limpa com sucesso.");
						return;
					} else if (args[0].equalsIgnoreCase("spawn")) {
						Game gameCache = GamesManager.getCache(gameName);
						if (gameCache == null) {
							return;
						}
						gameCache.despawn();
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							return;
						}
						GamesManager.put(game);
						game.spawn();
					} else if (args[0].equalsIgnoreCase("deletar")) {
						Game game = GamesManager.getGame(gameName);
						if (game == null) {
							sender.sendMessage("§cModo de jogo n§o encontrado.");
							return;
						}
						game.delete();
						sender.sendMessage("§aA op§§o de modo de jogo §f" + game.getName() + "§a foi deletada com sucesso.");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			return false;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Lobby] Comandos dispon§veis:");
		sender.sendMessage("§7/Game§a criar (nome)");
		sender.sendMessage("§7/Game§a setSkin (modo de jogo) (skin)");
		sender.sendMessage("§7/Game§a setItem (modo de jogo)");
		sender.sendMessage("§7/Game§a setPosition (modo de jogo) (posi§§o no ba§)");
		sender.sendMessage("§7/Game§a setLocation (modo de jogo)");
		sender.sendMessage("§7/Game§a addDesc (modo de jogo) (descri§§o)");
		sender.sendMessage("§7/Game§a limparDesc (modo de jogo)");
		sender.sendMessage("§7/Game§a spawn (modo de jogo)");
		sender.sendMessage("§7/Game§a deletar (modo de jogo)");
		return false;
	}
}