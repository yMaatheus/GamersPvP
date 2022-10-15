package dev.gamerspvp.gladiador.eventochat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.gladiador.Main;

public class EventoChatManager {
	
	private Main instance;
	
	private FileConfiguration config;
	private EventoChat eventoChat;
	
	private double premio;
	private String winner;
	
	public EventoChatManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.eventoChat = new EventoChat();
		this.premio = config.getDouble("premio");
		this.winner = config.getString("winner");
		eventTask();
		new EventoChatListener(instance);
	}
	
	public void executeStart() {
		eventoChat.setAcontecendo(true);
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§8-=-=-=-=-=-=-=- §eEvento Chat§8 -=-=-=-=-=-=-=-");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§eEscreva o resultado da conta: §f" + getMathCalculation());
		Bukkit.broadcastMessage("§ePremio: §2$§f" + premio);
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§8-=-=-=-=-=-=-=- §eEvento Chat§8 -=-=-=-=-=-=-=-");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				eventoChat.reset();
			}
		}.runTaskLaterAsynchronously(instance, 10 * 60 * 20);
	}
	
	public void eventTask() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				executeStart();
			}
		}.runTaskTimerAsynchronously(instance, 0, 60 * 60 * 20L);
	}
	
	public void defineWinner(String playerName) {
		winner = playerName;
		config.set("winner", playerName);
		saveConfig();
	}
	
	public boolean isWinner(String playerName) {
		if (winner.equalsIgnoreCase(playerName)){
			return true;
		}
		return false;
	}
	
	public String getMathCalculation() {
		Random r2 = new Random();int r22 = r2.nextInt(500);
		Random r3 = new Random();int r33 = r3.nextInt(500);
		Random r4 = new Random();int r44 = r4.nextInt(20);
		Random r5 = new Random();int r55 = r5.nextInt(200);
		Random r6 = new Random();int r66 = r6.nextInt(800);
		Random r7 = new Random();int r77 = r7.nextInt(100);
		
		int resultado = r22 + r33 - r44 + r55 + r66 - r77;
		eventoChat.setResultado(resultado);
		String conta = r22 + " + " + r33 + " - " + r44 + " + " + r55 + " + " + r66 + " - " + r77;
		return conta;
	}
	
	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "eventochat.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public EventoChat getEventoChat() {
		return eventoChat;
	}
	
	public double getPremio() {
		return premio;
	}
}