package net.gamers.lobby;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamerspvp.commons.bukkit.utils.Hologram;
import net.gamerspvp.commons.bukkit.utils.Utils;

public class HdCommand extends Command {
	
	public HdCommand(Main instance) {
		super("hd");
		Utils.registerCommand(this, instance, "hd");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		Player player = (Player) sender;
		if (args.length > 0) {
			Hologram hologram = new Hologram(player.getLocation());
			if (args[0].equalsIgnoreCase("colado")) {
				hologram.addLineColado("§aCOLADO 1");
				hologram.addLineColado("§eCOLADO 2");
				StringBuilder builder = new StringBuilder();
				for (int key : hologram.getLines2().keySet()) {
					String value = hologram.getLines2().get(key).getText();
					builder.append(key + "=" + value + ",");
				}
				System.out.println(builder.toString());
			} else if (args[0].equalsIgnoreCase("default")) {
				hologram.addLineDefalt("§aDEFAULT 1");
				hologram.addLineDefalt("§eDEFAULT 2");
				hologram.reverseLines();
				StringBuilder builder = new StringBuilder();
				for (int key : hologram.getLines2().keySet()) {
					String value = hologram.getLines2().get(key).getText();
					builder.append(key + "=" + value + ",");
				}
				System.out.println(builder.toString());
			} else if (args[0].equalsIgnoreCase("ordenado")) {
				hologram.addLineOrdenado("§aORDENADO 1");
				hologram.addLineOrdenado("§eORDENADO 2");
				StringBuilder builder = new StringBuilder();
				for (int key : hologram.getLines2().keySet()) {
					String value = hologram.getLines2().get(key).getText();
					builder.append(key + "=" + value + ",");
				}
				System.out.println(builder.toString());
			} else if (args[0].equalsIgnoreCase("clans")) {
				Hologram clans = new Hologram(player.getLocation(), 0.25, "§fO top atualiza em §1§l60 SEGUNDOS§f.", "2", "TOP-1", "TOP-2", "TOP-3","TOP-4", "TOP-5", "TOP-6", "TOP-7", "TOP-8", "TOP-9", "TOP-10", "13", "§fcom os clans com mais §9kills§f.","§fO top clans § ordenado de acordo", "§1§lTOP 10 CLAN");
				clans.spawn();
				StringBuilder builder = new StringBuilder();
				for (int key : clans.getLines2().keySet()) {
					String value = clans.getLines2().get(key).getText();
					builder.append(key + "=" + value + ",");
				}
				System.out.println(builder.toString());
				return true;
			}
			hologram.spawn();
		}
		return false;
	}
}