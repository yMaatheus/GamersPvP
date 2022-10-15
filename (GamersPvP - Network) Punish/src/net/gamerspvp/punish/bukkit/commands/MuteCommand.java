package net.gamerspvp.punish.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamerspvp.punish.bukkit.Main;
import net.gamerspvp.punish.network.controllers.DatabaseController;
import net.gamerspvp.punish.network.models.Mute;
import net.gamerspvp.punish.network.utils.DateUtils;
import net.gamerspvp.punish.network.utils.TimeManager;

public class MuteCommand extends Command {

	private Main instance;

	public MuteCommand(Main instance) {
		super("mute");
		instance.registerCommand(this, "mute", "mutar");
		this.instance = instance;
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.hasPermission("punish.mute"))) {
			sender.sendMessage("§cSem permissão.");
			return false;
		}
		if (args.length >= 4) {
			String target = args[0];
			if (target.equalsIgnoreCase(sender.getName())) {
				sender.sendMessage("§cVocê não pode mutar-se.");
				return false;
			}
			Player targetPlayer = Bukkit.getPlayer(target);
			if (targetPlayer != null) {
				target = targetPlayer.getName();
			}
			String author = sender.getName();
			String date = DateUtils.fromDateAndHour();
			long time = TimeManager.generateTime(args[2], Integer.parseInt(args[1]));
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("§cDesculpe, mas deve-se usar apenas números.");
				return false;
			}
			if (time == 0) {
				sender.sendMessage("§cFormato de horário inválido.");
				return false;
			}
			String reason = "";
			for (int i = 3; i < args.length; i++) {
				reason = reason + args[i] + " ";
			}
			Mute muteTarget = instance.getMutes().get(target.toLowerCase());
			Mute mute = new Mute(target, false, reason, author, time, date);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					try {
						if (muteTarget != null) {
							if (System.currentTimeMillis() < muteTarget.getTime()) {
								sender.sendMessage("§cEsse jogador já encontra-se silenciado.");
								return;
							}
							new DatabaseController().executeUnmute(muteTarget.getName(), instance);
							instance.getMutes().remove(muteTarget.getName());
						}
						mute.execute(instance);
						sender.sendMessage("§aJogador silenciado com sucesso.");
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
		sender.sendMessage("§7/" + arg + " (nick) (tempo) (dias|horas|minutos|segundos) (motivo).");
		sender.sendMessage("");
		return false;
	}
}