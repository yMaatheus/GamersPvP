package net.gamerspvp.commons.bungee.vips.commands;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.group.ProxiedGroupManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.bungee.vips.ProxiedVipsManager;
import net.gamerspvp.commons.network.models.Group;
import net.gamerspvp.commons.network.models.PlayerVip;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TrocarVipCommand extends Command {
	
	public TrocarVipCommand(ProxiedCommons commons) {
		super("trocarvip");
		commons.getProxy().getPluginManager().registerCommand(commons, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		String playerName = proxiedPlayer.getName();
		
		if (args.length > 0) {
			try {
				String groupName = args[0].substring(0,1).toUpperCase().concat(args[0].toLowerCase().substring(1));
				ProxiedGroupManager proxiedGroupManager = ProxiedCargosManager.getGroupManager();
				ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
				Group group = proxiedGroupManager.getGroup(groupName);
				if (group == null || !(group.isVip())) {
					sender.sendMessage(new TextComponent("§cO grupo informado não encontrado ou não é §lVIP§c."));
					return;
				}
				PlayerVip playerVip = ProxiedVipsManager.getPlayerVip(playerName, false);
				if (playerVip == null || System.currentTimeMillis() > playerVip.getVips().get(groupName)) {
					sender.sendMessage(new TextComponent("§cVocê não possui §lVIP §cou o tempo foi expirado."));
					return;
				}
				proxiedUserManager.defineUserGroup(playerName, groupName);
				sender.sendMessage(new TextComponent("§aVocê trocou seu grupo para §f" + groupName + " §acom sucesso."));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Vips] Comandos disponíveis:"));
		sender.sendMessage(new TextComponent("§7/Trocarvip §a (vip)"));
	}
}