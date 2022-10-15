package dev.gamerspvp.plotsaddons.transfer;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

import dev.gamerspvp.plotsaddons.Main;
import dev.gamerspvp.plotsaddons.transfer.commands.TransferCommand;
import dev.gamerspvp.plotsaddons.transfer.models.PlotTransfer;
import dev.gamerspvp.plotsaddons.utils.TimeManager;

public class TransferPlotManager {
	
	private PlotAPI plotsquared;
	private HashMap<Plot, PlotTransfer> transfer;
	
	public TransferPlotManager(Main instance) {
		this.plotsquared = new PlotAPI();
		this.transfer = new HashMap<>();
		instance.registerCommand(new TransferCommand(this), "transfer", "transferirplot");
	}
	
	public void openRequestTransference(Player owner, Player receiver, Plot plot) {
		PlotTransfer plotTransfer = new PlotTransfer(owner, receiver, plot);
		getTransfer().put(plotTransfer.getPlot(), plotTransfer);
	}
	
	public void transfer(Plot plot) {
		PlotTransfer plotTransfer = transfer.get(plot);
		Player receiver = plotTransfer.getReceiver();
		PlotPlayer plotPlayer = plotsquared.wrapPlayer(receiver);
		plot.setOwner(receiver.getUniqueId(), plotPlayer);
		transfer.remove(plot);
	}
	
	public boolean hasRequestInPlot(Plot plot) {
		if (getTransfer().containsKey(plot)) {
			long delay = getTransfer().get(plot).getTimeRequest();
			if (System.currentTimeMillis() <= delay) {
				return true;
			} else {
				getTransfer().remove(plot);
				return false;
			}
		}
		return false;
	}
	
	public String getDelayRequestPlot(Plot plot) {
		if (getTransfer().containsKey(plot)) {
			long delay = getTransfer().get(plot).getTimeRequest();
			if (System.currentTimeMillis() <= delay) {
				return TimeManager.getTimeEnd(delay);
			}
		}
		return null;
	}
	
	public PlotAPI getPlotsquared() {
		return plotsquared;
	}
	
	public HashMap<Plot, PlotTransfer> getTransfer() {
		return transfer;
	}
}