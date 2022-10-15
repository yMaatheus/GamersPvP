package dev.gamerspvp.plotsaddons.transfer.models;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;

public class PlotTransfer {
	
	private Player owner;
	private Player receiver;
	private Plot plot;
	private long timeRequest;
	
	public PlotTransfer(Player owner, Player receiver, Plot plot) {
		this.owner = owner;
		this.receiver = receiver;
		this.plot = plot;
		this.timeRequest = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(300));
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public Player getReceiver() {
		return receiver;
	}
	
	public void setReceiver(Player recivier) {
		this.receiver = recivier;
	}
	
	public Plot getPlot() {
		return plot;
	}
	
	public void setPlot(Plot plot) {
		this.plot = plot;
	}
	
	public long getTimeRequest() {
		return timeRequest;
	}
	
	public void setTimeRequest(long timeRequest) {
		this.timeRequest = timeRequest;
	}
}