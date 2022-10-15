package net.gamers.gladiador.mito;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.gamers.gladiador.Main;

public class SetMitoCommand extends Command {
	
	private Main instance;
	
	public SetMitoCommand(Main instance) {
		super("setmito");
		this.instance = instance;
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("mito.admin"))) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		if (args.length > 0) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage("§cJogador inv§lido.");
				return false;
			}
			MitoManager mitoManager = instance.getMitoManager();
			mitoManager.setNewMito(target);
			return true;
		}
		sender.sendMessage("§7/" + arg + " §a(player).");
		return false;
	}
}