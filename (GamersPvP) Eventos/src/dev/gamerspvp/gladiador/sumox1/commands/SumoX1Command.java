package dev.gamerspvp.gladiador.sumox1.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.sumox1.SumoX1Manager;
import dev.gamerspvp.gladiador.sumox1.models.SumoX1;

public class SumoX1Command extends Command {
	
	private Main instance;
	
	public SumoX1Command(Main instance) {
		super("sumox1");
		this.instance = instance;
		instance.registerCommand(this, "sumox1");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		boolean permission = sender.hasPermission("sumox1.admin");
		if (args.length > 0) {
			SumoX1Manager sumox1Manager = instance.getSumoX1Manager();
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (!(permission)) {
					sender.sendMessage("§cSem permiss§o.");
					return false;
				}
				SumoX1 sumox1 = sumox1Manager.getSumox1();
				if (!(sumox1Manager.hasLocations())) {
					sender.sendMessage("§cDefina as localiza§§es para iniciar o evento.");
					return false;
				}
				if (sumox1 != null) {
					sender.sendMessage("§cO evento j§ est§ acontencendo.");
					return false;
				}
				sumox1Manager.executeStart();
				sender.sendMessage("§7Iniciando evento SumoX1.");
			} else if (args[0].equalsIgnoreCase("cancelar") || args[0].equalsIgnoreCase("parar")) {
				if (!(permission)) {
					sender.sendMessage("§cSem permiss§o.");
					return false;
				}
				SumoX1 sumox1 = sumox1Manager.getSumox1();
				if (sumox1 == null) {
					sender.sendMessage("§cO evento n§o est§ acontecendo.");
					return false;
				}
				sumox1Manager.executeCancel();
				return true;
			} else if (args[0].equalsIgnoreCase("info")) {
				if (!(permission)) {
					sender.sendMessage("§cSem permiss§o.");
					return false;
				}
				
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
				List<String> locations = Arrays.asList(new String[] {"spawn", "saida", "pos1", "pos2"});
				if (!(locations.contains(locationName))) {
					sender.sendMessage("§cVoc§ so pode definir as seguintes localiza§§es: " + locations);
					return false;
				}
				Location location = player.getLocation();
				sumox1Manager.setLocation(locationName, location);
				sender.sendMessage("§aLocaliza§§o §f" + locationName + " §adefinida com sucesso.");
			} else if (args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("");
				sender.sendMessage("§a[SumoX1] Comandos dispon§veis:");
				sender.sendMessage("§7/" + arg + "§a sair");
				sender.sendMessage("§7/" + arg + "§a ajuda");
				if (permission) {
					sender.sendMessage("");
					sender.sendMessage("§7/" + arg + "§a iniciar");
					sender.sendMessage("§7/" + arg + "§a parar");
					sender.sendMessage("§7/" + arg + "§a set (spawn | saida | pos1 | pos2)");
					sender.sendMessage("§7/" + arg + "§a info");
				}
				sender.sendMessage("");
			}
			return false;
		}
		return false;
	}
}