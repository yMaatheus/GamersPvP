package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamers.p4free.game.GameManager;
import net.gamers.p4free.utils.LocationsManager;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class SSCommand extends Command {
	
	private Main instance;
	
	public SSCommand(Main instance) {
		super("ss");
		this.instance = instance;
		Utils.registerCommand(this, instance, "ss", "screenshare");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("gamers.moderador"))) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length > 0) {
			LocationsManager locationsManager = instance.getLocationsManager();
			if (args[0].equalsIgnoreCase("definir") || (args[0].equalsIgnoreCase("set"))) {
				if (!(sender instanceof Player)) {
					return false;
				}
				if (!(sender.hasPermission("gamers.admin"))) {
					sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
					return false;
				}
				Player player = (Player) sender;
				locationsManager.setLocation("Game", "ScreenShare", player.getLocation(), instance);
				player.sendMessage("§aLocaliza§§o definida com sucesso.");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(MessageUtils.PLAYER_NOT_FOUND.getMessage());
				return false;
			}
			GameManager gameManager = instance.getGameManager();
			if (gameManager.hasInScreenShare(player)) {
				gameManager.removeScreenShare(player);
				World world = Bukkit.getWorld(gameManager.getSpawnWorld());
				if (sender instanceof Player) {
					Player senderPlayer = (Player) sender;
					senderPlayer.teleport(world.getSpawnLocation());
				}
				player.teleport(world.getSpawnLocation());
				player.sendMessage("");
				player.sendMessage("§c(!) Voc§ foi retirado da ScreenShare! Agora poder§ voltar a jogar no servidor.");
				player.sendMessage("§c(!) Caso tenha tido alguma problema voc§ pode contar a sua experi§ncia em um ticket em nosso discord.");
				player.sendMessage("");
				sender.sendMessage("§f" + player.getName() + " §afoi removido da screenshare com sucesso.");
				return true;
			}
			if (sender instanceof Player) {
				Player senderPlayer = (Player) sender;
				senderPlayer.teleport(locationsManager.get("Game", "ScreenShare"));
			}
			gameManager.addScreenShare(player);
			player.teleport(locationsManager.get("Game", "ScreenShare"));
			player.sendMessage("");
			player.sendMessage("§c(!) Voc§ foi teleportado at§ a ScreenShare, a staff do servidor ir§ verificar se voc§ est§ com algo ilegal, siga as instru§§es.");
			player.sendMessage("§c(!) Voc§ tem direito de pedir para ficar em chamada de voz com a equipe do servidor no discord.");
			player.sendMessage("");
			player.playSound(player.getLocation(), Sound.EXPLODE, 20F, 20F);
			sender.sendMessage("§f" + player.getName() + " §afoi adicionado da screenshare com sucesso.");
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Essentials] Comandos dispon§veis:");
		sender.sendMessage("§7/" + arg + "§a (nick)");
		sender.sendMessage("§7/" + arg + "§a definir");
		return false;
	}
}