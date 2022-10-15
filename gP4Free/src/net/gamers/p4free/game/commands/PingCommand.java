package net.gamers.p4free.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class PingCommand extends Command {
	
	public PingCommand(Main instance) {
		super("ping");
		Utils.registerCommand(this, instance, "ping");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (args.length > 0) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				return false;
			}
			EntityPlayer targetEntityPlayer = ((CraftPlayer) target).getHandle();
			if (targetEntityPlayer == null) {
				return false;
			}
			int ping = targetEntityPlayer.ping;
			if (ping > 300) {
				sender.sendMessage("§aO ping de " + target.getName() + " é §4" + ping + "ms");
			} else if (ping > 250) {
				sender.sendMessage("§aO ping de " + target.getName() + " é §c" + ping + "ms");
			} else if (ping > 200) {
				sender.sendMessage("§aO ping de " + target.getName() + " é §e" + ping + "ms");
			} else {
				sender.sendMessage("§aO ping de " + target.getName() + " é §a" + ping + "ms");
			}
			return true;
		}
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		EntityPlayer playerEntity = ((CraftPlayer) player).getHandle();
		if (playerEntity == null) {
			return false;
		}
		int ping = playerEntity.ping;
		if (ping > 300) {
			sender.sendMessage("§aSeu ping é §4" + ping + "ms");
		} else if (ping > 250) {
			sender.sendMessage("§aSeu ping é §c" + ping + "ms");
		} else if (ping > 200) {
			sender.sendMessage("§aSeu ping é §e" + ping + "ms");
		} else {
			sender.sendMessage("§aSeu ping é " + ping + "ms");
		}
		return true;
	}
}