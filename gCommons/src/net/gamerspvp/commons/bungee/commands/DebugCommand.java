package net.gamerspvp.commons.bungee.commands;

import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class DebugCommand extends Command {
	
	private String permission;
	
	public DebugCommand(String name, String permission) {
		super(name);
		this.permission = permission;
		ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("�a* " + name + " - " + permission));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		double total = Runtime.getRuntime().totalMemory();
		double free = Runtime.getRuntime().freeMemory();
		double used = total - free;
		
		double percentage = (total - free) / total * 100.0D;
		StringBuilder builder = new StringBuilder();
		builder.append("§a[Commons] Informações da memória:\n");
		builder.append(" §a* §7Total - §f" + ((int) total / 1048576) + "MB\n");
		builder.append(" §a* §7Disponivel - §f" + ((int) free / 1048576) + "MB\n");
		builder.append(" §a* §7Em uso - §f" + ((int) used / 1048576) + "MB\n");
		builder.append(" §a* §7Consumindo - §f" + (int) percentage + "%\n");
		builder.append(" §a* §7Jogadores conectados no proxy: §f" + BungeeCord.getInstance().getPlayers().size());
		sender.sendMessage(new TextComponent(builder.toString()));
	}
}