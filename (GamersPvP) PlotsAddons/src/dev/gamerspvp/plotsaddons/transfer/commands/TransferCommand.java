package dev.gamerspvp.plotsaddons.transfer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;

import dev.gamerspvp.plotsaddons.transfer.TransferPlotManager;
import dev.gamerspvp.plotsaddons.transfer.models.PlotTransfer;

public class TransferCommand extends Command {
	
	private TransferPlotManager plotsAddonsManager;
	
	public TransferCommand(TransferPlotManager plotsAddonsManager) {
		super("transfer");
		this.plotsAddonsManager = plotsAddonsManager;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		String playerName = player.getName();
		if (args.length > 0) {
			PlotAPI plotsquared = plotsAddonsManager.getPlotsquared();
			Plot plot = plotsquared.getPlot(player.getLocation());
			if (plot == null) {
				sender.sendMessage("§cVocê não está na localização de uma plot.");
				return false;
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("confirmar")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage("§cJogador offline.");
						return false;
					}
					if (!(plotsAddonsManager.hasRequestInPlot(plot))) {
						sender.sendMessage("§cEssa plot não possui um pedido de transferência.");
						return true;
					}
					PlotTransfer plotTransfer = plotsAddonsManager.getTransfer().get(plot);
					if (!(plotTransfer.getOwner().getName().equalsIgnoreCase(target.getName()))) {
						sender.sendMessage("§cO jogador informado não abriu um pedido de transferência com essa plot.");
						return true;
					}
					if (!(plotTransfer.getReceiver().getName().equalsIgnoreCase(player.getName()))) {
						sender.sendMessage("§cO pedido de transferência não está vigorando para você.");
						return true;
					}
					int playerPlotCount = plotsquared.getPlayerPlotCount(Bukkit.getWorld("plotworld"), player);
					boolean hasSpacePlot = false;
					for (int i = 1; i < 20; i++) {
						int plotCount = playerPlotCount + i;
						if (player.hasPermission("plots.plot." + plotCount)) {
							hasSpacePlot = true;
							break;
						}
					}
					if (!(hasSpacePlot)) {
						sender.sendMessage("§cVocê não possui espaço para mais plot's em sua conta.");
						return true;
					}
					plotsAddonsManager.transfer(plot);
					sender.sendMessage("§aPedido de confirmação aceito, agora você já pode desfrutar de sua plot.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("cancelar")) {
				if (!(plot.isOwner(player.getUniqueId()))) {
					sender.sendMessage("§cPara cancelar um pedido de transferência é necessário ser dono da plot.");
					return false;
				}
				if (!(plotsAddonsManager.hasRequestInPlot(plot))) {
					sender.sendMessage("§cVocê não abriu nenhum pedido de transferência com essa plot.");
					return true;
				}
				plotsAddonsManager.getTransfer().remove(plot);
				sender.sendMessage("§aTransferência de plot cancelada com sucesso.");
			} else {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage("§cJogador offline.");
					return false;
				}
				if (target.getName().equalsIgnoreCase(playerName)) {
					sender.sendMessage("§cVocê não pode transferir sua plot para você mesmo.");
					return true;
				}
				if (!(plot.isOwner(player.getUniqueId()))) {
					sender.sendMessage("§cPara abrir um pedido de transferência você precisa ser dono da plot.");
					return false;
				}
				if (plotsAddonsManager.hasRequestInPlot(plot)) {
					sender.sendMessage("Você já abriu um pedido de transferência com essa plot.");
					return true;
				}
				plotsAddonsManager.openRequestTransference(player, target, plot);
				sender.sendMessage("§aVocê abriu uma soliciação de transferência, aguarde o outro jogador aceitar.");
				target.sendMessage("§aFoi aberto um pedido de transferência de plot de " + sender.getName() + " para você.");
			}
			return true;
		}
		sender.sendMessage("§aComandos disponíveis:");
		String command = "§7/" + arg;
		sender.sendMessage(command + " §a(player).");
		sender.sendMessage(command + " §aconfirmar (player).");
		sender.sendMessage(command + " §acancelar.");
		return false;
	}
}