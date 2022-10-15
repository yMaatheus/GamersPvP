package net.gamerspvp.commons.bungee.vips.commands;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.vips.ProxiedVipsManager;
import net.gamerspvp.commons.network.models.PlayerVip;
import net.gamerspvp.commons.network.utils.DateUtils;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class VipsCommand extends Command {
	
	public VipsCommand(ProxiedCommons commons) {
		super("vips");
		commons.getProxy().getPluginManager().registerCommand(commons, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		String playerName = proxiedPlayer.getName();
		try {
			PlayerVip playerVip = ProxiedVipsManager.getPlayerVip(playerName, false);
			if (playerVip == null) {
				sender.sendMessage(new TextComponent(MessageUtils.PLAYER_NOT_BANCO.getMessage()));
				return;
			}
			if (playerVip.getVips().isEmpty()) {
				sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhum §lVIP §cnessa conta."));
				return;
			}
			StringBuilder builder = new StringBuilder();
			int i = 0;
			for (String key : playerVip.getVips().keySet()) {
				long value = playerVip.getVips().get(key);
				if (System.currentTimeMillis() > value) {
					continue;
				}
				i++;
				builder.append("§7* §a" + key + " §7- §fTermina em: §a" + DateUtils.formatDifference(value) + " §8(§f"+ DateUtils.longToDate(value) + "§8)\n");
			}
			if (i == 0) {
				sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhum §lVIP §cativo nessa conta."));
				return;
			}
			sender.sendMessage(new TextComponent(""));
			sender.sendMessage(new TextComponent("§a[Vips] Informa§§es de seus §lVIP(s)§a:"));
			sender.sendMessage(new TextComponent(""));
			sender.sendMessage(new TextComponent(builder.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}