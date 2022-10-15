package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.PlayerInventory;

import net.gamers.p4free.Main;
import net.gamers.p4free.game.GameManager;
import net.gamers.p4free.kit.KitManager;
import net.gamerspvp.commons.bukkit.utils.Utils;

public class SpawnCommand extends Command {
	
	private Main instance;
	
	public SpawnCommand(Main instance) {
		super("spawn");
		this.instance = instance;
		Utils.registerCommand(this, instance, "spawn");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		GameManager gameManager = instance.getGameManager();
		World world = Bukkit.getWorld(gameManager.getSpawnWorld());
		if (world == null) {
			sender.sendMessage("§cNão foi possível completar a operação.");
			return false;
		}
		KitManager kitManager = instance.getKitManager();
		if (kitManager.hasCachePlayers(player)) {
			PlayerInventory playerInventory = player.getInventory();
			playerInventory.clear();
			playerInventory.setHelmet(null);
			playerInventory.setChestplate(null);
			playerInventory.setLeggings(null);
			playerInventory.setBoots(null);
			kitManager.removeCachePlayer(player);
		}
		player.teleport(world.getSpawnLocation(), TeleportCause.COMMAND);
		sender.sendMessage("§aTeleportado ao spawn com sucesso.");
		return false;
	}
}