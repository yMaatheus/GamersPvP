package net.gamerspvp.lobby.server.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.gamerspvp.lobby.Main;
import net.gamerspvp.lobby.npcs.NpcManager;

public class NpcServerCommand extends Command {
	
	private Main instance;
	
	public NpcServerCommand(Main instance) {
		super("npcserver");
		this.instance = instance;
		Utils.registerCommand(this, instance, "npcserver");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (!(player.hasPermission("gamers.diretor"))) {
			player.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length > 1) {
			NpcManager npcManager = instance.getNpcManager();
			String serverName = args[1];
			if (args[0].equalsIgnoreCase("definir")) {
				if (args.length > 2) {
					String skin = args[2];
					Location location = player.getLocation();
					ItemStack hand = null;
					if ((player.getItemInHand() != null) && (player.getItemInHand().getType() != Material.AIR)) {
						hand = player.getItemInHand();
					}
					npcManager.setNPC(serverName, location, hand, skin);
					sender.sendMessage("§aNPC do servidor §f" + serverName + " §adefinido com sucesso!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("remover")) {
				if (npcManager.getCache(serverName) == null) {
					sender.sendMessage("§cNão existe nenhum NPC desse servidor.");
					return true;
				}
				npcManager.destroyNPC(serverName);
				sender.sendMessage("§aNPC do servidor §f" + serverName + " §afoi destruido com sucesso!");
				return true;
			}
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Lobby] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a definir (server) (skin).");
		sender.sendMessage("§7/" + arg + "§a remover (server).");
		sender.sendMessage("");
		return false;
	}
}