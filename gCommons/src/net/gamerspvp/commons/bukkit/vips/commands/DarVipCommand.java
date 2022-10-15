package net.gamerspvp.commons.bukkit.vips.commands;

import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.cargos.CargosManager;
import net.gamerspvp.commons.bukkit.cargos.group.GroupBukkit;
import net.gamerspvp.commons.bukkit.cargos.group.GroupManager;
import net.gamerspvp.commons.bukkit.cargos.user.UserManager;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.bukkit.vips.VipsManager;
import net.gamerspvp.commons.network.VipController;
import net.gamerspvp.commons.network.models.PlayerVip;

public class DarVipCommand extends Command {
	
	private CommonsBukkit commons;
	
	public DarVipCommand(CommonsBukkit commons) {
		super("darvip");
		this.commons = commons;
		Utils.registerCommand(this, commons, "darvip");
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof ConsoleCommandSender)) {
			return false;
		}
		if (args.length > 0) {
			String groupName = args[1].substring(0,1).toUpperCase().concat(args[1].toLowerCase().substring(1));
			GroupManager groupManager = CargosManager.getGroupManager();
			GroupBukkit group = groupManager.getGroup(groupName);
			if (group == null || !(group.isVipGroup())) {
				sender.sendMessage("§cO grupo informado n§o encontrado ou n§o § §lVIP§c.");
				return false;
			}
			new BukkitRunnable() {
				
				@Override
				public void run() {
					try {
						String playerName = args[0];
						int days = Integer.parseInt(args[2]);
						long time = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days);
						VipController vipController = VipsManager.getController();
						UserManager userManager = CargosManager.getUserManager();
						
						PlayerVip playerVip = VipsManager.getPlayerVip(playerName, true);
						if (playerVip.getVips().get(groupName) > System.currentTimeMillis()) {
							sender.sendMessage("§cVoc§ n§o pode usar essa chave pois j§ possui esse §lVIP §cativo.");
							return;
						}
						playerVip.getVips().put(groupName, time);
						
						vipController.updatePlayerVip(playerVip);
						userManager.defineUserGroup(playerName, groupName);
						sender.sendMessage("§aO §lVIP§f " + groupName + " §ade§f " + playerName + " §acom dura§§o de§f " + days + " dias §afoi definido com sucesso.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(commons);
			return true;
		}
		sender.sendMessage("");
		sender.sendMessage("§a[Cargos] Comandos dispon§veis:");
		sender.sendMessage("§7/DarVip §a (nick) (grupo) (dias)");
		return true;
	}
}