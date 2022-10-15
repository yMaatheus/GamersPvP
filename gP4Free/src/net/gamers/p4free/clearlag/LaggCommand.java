package net.gamers.p4free.clearlag;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class LaggCommand extends Command {
	
	public LaggCommand(Main instance) {
		super("lagg");
		Utils.registerCommand(this, instance, "lagg");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.isOp())) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("killmobs")) {
				int cleared = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Entity entity : Bukkit.getWorld(world.getName()).getEntities()) {
						if (entity instanceof Villager) {
							continue;
						}
						if ((entity instanceof Creature) || (entity instanceof Animals) || (entity.isDead())) {
							entity.remove();
							cleared++;
						}
					}
				}
				sender.sendMessage("§aVocê limpou §f" + cleared + "§a mobs.");
				return true;
			}
			if (args[0].equalsIgnoreCase("unloadchunks")) {
				int chunks = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Chunk chunk : world.getLoadedChunks()) {
						if (chunk == null) {
							continue;
						}
						chunk.unload(true, true);
						chunks++;
					}
				}
				sender.sendMessage("§aVocê limpou §f" + chunks + "§a chunks.");
				return true;
			}
			if (args[0].equalsIgnoreCase("clear")) {
				int cleared = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Entity entity : world.getEntities()) {
						if (entity instanceof Player) {
							continue;
						}
						cleared++;
						entity.remove();
					}
				}
				sender.sendMessage("§aVocê limpou §f" + cleared + "§a itens do chão.");
				return true;
			}
		}
		sender.sendMessage("");
		sender.sendMessage("§a[P4Free] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a killmobs");
		sender.sendMessage("§7/" + arg + "§a unloadchunks");
		sender.sendMessage("§7/" + arg + "§a clear");
		return false;
	}
}