package net.gamerspvp.punish.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.controllers.DatabaseController;
import net.gamerspvp.punish.network.models.Mute;

public class UnMuteCommand extends Command {

	private Main instance;

	public UnMuteCommand(Main instance) {
		super("unmute");
		instance.registerCommand(this, "unmute", "desmutar");
		this.instance = instance;
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.mute"))) {
			sender.sendMessage("§cSem permissão.");
			return false;
		}
		if (args.length > 0) {
			String target = args[0];
			Mute mute = instance.getMutes().get(target.toLowerCase());
			if (mute == null) {
				sender.sendMessage("§cEsse jogador não está silenciado.");
				return false;
			}
			new BukkitRunnable() {
				
				@Override
				public void run() {
					try {
						new DatabaseController().executeUnmute(target.toLowerCase(), instance);
						instance.getMutes().remove(target.toLowerCase());
						sender.sendMessage("§aPunição revogada com sucesso.");
					} catch (Exception e) {
						sender.sendMessage("Não foi possivel completar a operação.");
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(instance);
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Punish] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + " (nick).");
		sender.sendMessage("");
		return true;
	}
}