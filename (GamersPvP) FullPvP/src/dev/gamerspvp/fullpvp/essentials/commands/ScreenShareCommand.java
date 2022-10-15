package dev.gamerspvp.fullpvp.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.essentials.EssentialsManager;
import dev.gamerspvp.fullpvp.locations.LocationsManager;

public class ScreenShareCommand extends Command {
	
	private Main instance;
	
	public ScreenShareCommand(Main instance) {
		super("ss");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!sender.hasPermission("essentials.screenshare")) {
			return false;
		}
		if (args.length > 0) {
			LocationsManager locationsManager = instance.getLocationsManager();
			if (args[0].equalsIgnoreCase("definir")) {
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player = (Player) sender;
				locationsManager.setLocation("Essentials", "ScreenShare", player.getLocation());
				player.sendMessage("§aLocaliza§§o definida com sucesso.");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage("§cJogador n§o encontrado.");
				return false;
			}
			EssentialsManager essentialsManager = instance.getEssentialsManager();
			if (essentialsManager.hasInScreenShare(player)) {
				essentialsManager.removeScreenShare(player);
				if (sender instanceof Player) {
					Player senderPlayer = (Player) sender;
					senderPlayer.teleport(essentialsManager.getSpawnWorld().getSpawnLocation());
				}
				player.teleport(essentialsManager.getSpawnWorld().getSpawnLocation());
				player.sendMessage("");
				player.sendMessage("§c(!) Voc§ foi retirado da ScreenShare! Agora poder§ voltar a jogar no servidor.");
				player.sendMessage("§c(!) Caso tenha tido alguma problema voc§ pode contar a sua experi§ncia em um ticket em nosso discord.");
				player.sendMessage("");
				sender.sendMessage("§f" + player.getName() + " §afoi removido da screenshare com sucesso.");
				return true;
			}
			if (sender instanceof Player) {
				Player senderPlayer = (Player) sender;
				senderPlayer.teleport(locationsManager.get("Essentials", "ScreenShare"));
			}
			essentialsManager.addScreenShare(player);
			player.teleport(locationsManager.get("Essentials", "ScreenShare"));
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