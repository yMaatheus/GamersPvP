package net.gamerspvp.commons.bungee.vips.commands;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.bungee.vips.ProxiedVipsManager;
import net.gamerspvp.commons.network.VipController;
import net.gamerspvp.commons.network.VipController.VipKey;
import net.gamerspvp.commons.network.models.Group;
import net.gamerspvp.commons.network.models.PlayerVip;
import net.gamerspvp.commons.network.utils.DateUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UsarKeyCommand extends Command {
	
	public UsarKeyCommand(ProxiedCommons commons) {
		super("usarkey");
		commons.getProxy().getPluginManager().registerCommand(commons, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		if (args.length > 0) {
			try {
				String key = args[0];
				VipController vipController = ProxiedVipsManager.getController();
				VipKey vipKey = vipController.searchKey(key);
				if (vipKey == null || System.currentTimeMillis() > vipKey.getTime()) {
					sender.sendMessage(new TextComponent("§cChave com tempo expirado ou inexistente."));
					return;
				}
				ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
				
				String groupName = vipKey.getGroup();
				long time = vipKey.getTime();
				Group group = ProxiedCargosManager.getGroupManager().getGroup(groupName);
				if (group == null || !(group.isVip())) {
					sender.sendMessage(new TextComponent("§cO grupo informado n§o encontrado ou n§o § §lVIP§c."));
					return;
				}
				
				PlayerVip playerVip = ProxiedVipsManager.getPlayerVip(sender.getName(), true);
				if (playerVip.getVips().get(groupName) != null && playerVip.getVips().get(groupName) > System.currentTimeMillis()) {
					sender.sendMessage(new TextComponent("§cVoc§ n§o pode usar essa chave pois j§ possui esse §lVIP §cativo."));
					return;
				}
				playerVip.getVips().put(groupName, time);
				proxiedUserManager.defineUserGroup(sender.getName(), groupName);
				
				vipController.updatePlayerVip(playerVip);
				vipController.purgeKey(key);
				sender.sendMessage(new TextComponent("§aVoc§ usou a chave§f " + key + " §ado grupo§f " + groupName + " §acom tempo de:§f" + DateUtils.formatDifference(time)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Vips] Comandos dispon§veis:"));
		sender.sendMessage(new TextComponent("§7/Usarkey §a (chave)"));
	}
}