package dev.gamerspvp.gladiador.dominar.commands;

import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.dominar.DominarManager;
import dev.gamerspvp.gladiador.utils.ClansAPI;
import dev.gamerspvp.gladiador.utils.TimeFormater;

public class DominarCommand extends Command {

	private Main instance;

	public DominarCommand(Main instance) {
		super("dominar");
		this.instance = instance;
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.isOp())) {
			sender.sendMessage("§cSem permiss§o.");
			return false;
		}
		DominarManager dominarManager = instance.getDominarManager();
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("ativar")) {
				if (dominarManager.isActive()) {
					sender.sendMessage("§cDominio j§ est§ ativado.");
					return false;
				}
				dominarManager.setActive(true);
				sender.sendMessage("§aDominio ativado com sucesso.");
			} else if (args[0].equalsIgnoreCase("desativar")) {
				if (!(dominarManager.isActive())) {
					sender.sendMessage("§cDominio j§ est§ desativado.");
					return false;
				}
				dominarManager.setActive(false);
				sender.sendMessage("§aDominio desativado com sucesso.");
			} else if (args[0].equalsIgnoreCase("setbloco")) {
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player = (Player) sender;
				String playerName = player.getName();
				if (dominarManager.getChestSet().contains(playerName.toLowerCase())) {
					sender.sendMessage("§cQuebre o bloco que dever§ ser o lugar.");
					return false;
				}
				dominarManager.getChestSet().add(playerName.toLowerCase());
				sender.sendMessage("§aAgora defina a localiza§§o de por o bloco quebrando uma bedrock.");
			} else if (args[0].equalsIgnoreCase("info")) {
				HashSet<String> playerList = new HashSet<String>();
				for (Player dominador : dominarManager.getPlayers()) {
					playerList.add(dominador.getName());
				}
				String players = StringUtils.join(playerList, ", ");
				sender.sendMessage("");
				sender.sendMessage("§7Clan dominando§8: §f" + ClansAPI.getClanTag(dominarManager.getClan()));
				if (System.currentTimeMillis() > dominarManager.getInvencible()) {
					sender.sendMessage("§7Invencibilidade§8: §fSem invencibilidade");
				} else {
					String timeFormat = TimeFormater.formatOfEnd(dominarManager.getInvencible());
					sender.sendMessage("§7Invencibilidade§8: §f" + timeFormat);
				}
				if (playerList.isEmpty()) {
					sender.sendMessage("§7Players Dominantes§8: §fNingu§m");
				} else {
					sender.sendMessage("§7Players Dominantes§8: §f" + players);
				}
				sender.sendMessage("");
			}
			return true;
		}
		sender.sendMessage("§aComandos dispon§veis:");
		String command = "§7/" + arg;
		sender.sendMessage(command + " §aativar.");
		sender.sendMessage(command + " §adesativar.");
		sender.sendMessage(command + " §asetbloco.");
		return false;
	}
}