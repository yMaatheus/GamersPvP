package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class GamemodeCommand extends Command {

	public GamemodeCommand(Main instance) {
		super("gamemode");
		Utils.registerCommand(this, instance, "gamemode", "gm");
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		boolean moderador = player.hasPermission("gamers.moderador");
		boolean admin = player.hasPermission("gamers.admin");
		boolean isOp = player.isOp();
		if (!(moderador)) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length > 0) {
			if (args.length > 1) {
				if (!(admin)) {
					sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
					return false;
				}
				Player target = Bukkit.getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
					return false;
				}
				String targetName = target.getName();
				StringBuilder sb = new StringBuilder("§aVoc§ atualizou o gamemode de §f");
				sb.append(targetName + "§a para §f" + target.getGameMode() + "§a com sucesso.");
				if (args[0].equalsIgnoreCase("0")) {
					if (target.getGameMode() == GameMode.SURVIVAL) {
						player.sendMessage("§cO jogador j§ est§ no " + target.getGameMode().toString());
						return false;
					}
					target.setGameMode(GameMode.SURVIVAL);
					player.sendMessage(sb.toString());
				} else if (args[0].equalsIgnoreCase("1")) {
					if (!(isOp)) {
						sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
						return false;
					}
					if (target.getGameMode() == GameMode.CREATIVE) {
						player.sendMessage("§cO jogador j§ est§ no " + target.getGameMode().toString());
						return false;
					}
					target.setGameMode(GameMode.CREATIVE);
					player.sendMessage(sb.toString());
				} else if (args[0].equalsIgnoreCase("2")) {
					if (target.getGameMode() == GameMode.ADVENTURE) {
						player.sendMessage("§cO jogador j§ est§ no " + target.getGameMode().toString());
						return false;
					}
					target.setGameMode(GameMode.ADVENTURE);
					player.sendMessage(sb.toString());
				} else if (args[0].equalsIgnoreCase("3")) {
					if (target.getGameMode() == GameMode.SPECTATOR) {
						player.sendMessage("§cO jogador j§ est§ no " + target.getGameMode().toString());
						return false;
					}
					target.setGameMode(GameMode.SPECTATOR);
					player.sendMessage(sb.toString());
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("0")) {
				if (player.getGameMode() == GameMode.SURVIVAL) {
					player.sendMessage("§cVoc§ j§ est§ no " + player.getGameMode().toString());
					return false;
				}
				player.setGameMode(GameMode.SURVIVAL);
				player.sendMessage("§aSeu modo de jogo foi atualizado para: §2" + player.getGameMode());
			}
			if (args[0].equalsIgnoreCase("1")) {
				if (!(player.isOp())) {
					sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
					return false;
				}
				if (player.getGameMode() == GameMode.CREATIVE) {
					player.sendMessage("§cVoc§ j§ est§ no " + player.getGameMode().toString());
					return false;
				}
				player.setGameMode(GameMode.CREATIVE);
				player.sendMessage("§aSeu modo de jogo foi atualizado para: §2" + player.getGameMode());
			}
			if (args[0].equalsIgnoreCase("2")) {
				if (player.getGameMode() == GameMode.ADVENTURE) {
					player.sendMessage("§cVoc§ j§ est§ no " + player.getGameMode().toString());
					return false;
				}
				player.setGameMode(GameMode.ADVENTURE);
				player.sendMessage("§aSeu modo de jogo foi atualizado para: §2" + player.getGameMode());
			}
			if (args[0].equalsIgnoreCase("3")) {
				if (player.getGameMode() == GameMode.SPECTATOR) {
					player.sendMessage("§cVoc§ j§ est§ no " + player.getGameMode().toString());
					return false;
				}
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage("§aSeu modo de jogo foi atualizado para: §2" + player.getGameMode());
			}
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[P4Free] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a (0,1,2,3)");
		if (isOp)
			sender.sendMessage("§7/" + arg + "§a (0,1,2,3) (nick)");
		return false;
	}
}