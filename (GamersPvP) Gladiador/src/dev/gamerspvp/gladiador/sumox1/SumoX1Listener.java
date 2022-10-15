package dev.gamerspvp.gladiador.sumox1;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.sumox1.models.SumoX1;
import dev.gamerspvp.gladiador.sumox1.models.SumoX1.statusType;
import dev.gamerspvp.gladiador.utils.Titles;

public class SumoX1Listener implements Listener {
	
	private Main instance;
	
	public SumoX1Listener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		SumoX1Manager sumoX1Manager = instance.getSumoX1Manager();
		if (sumoX1Manager.getSumox1() == null) {
			return;
		}
		SumoX1 sumox1 = sumoX1Manager.getSumox1();
		if (sumox1.getStatus() != statusType.FECHADO) {
			return;
		}
		HashSet<Player> duels = sumox1.getDueling();
		if (!duels.contains(player)) {
			return;
		}
		if (player.getLocation().getY() <= -1.0D) {
			sumox1.getParticipantes().remove(player);
			sumox1.getDueling().remove(player);
			player.teleport(sumoX1Manager.getLocation("saida"));
			Titles title = new Titles("§c§lSumoX1");
			title.setSubtitle("§cVocê perdeu!");
			title.send(player);
			/*for (Player playerWinDuel : sumox1.getDueling()) {
				
			}*/
		}
		/*if ((Spleef.getJogadores().contains(player)) && (Spleef.fechado) && (player.getLocation().getY() <= -1.0D)) {
			Spleef.removePlayer(player);
			new Titles("§cVocê perdeu!", "", 1, 5, 1);
			for (Player a : Spleef.getJogadores()) {
				a.sendMessage("§f" + player.getName() + "§c perdeu");
			}
			Spleef.checkAndFinish();
		}*/
	}
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		
	}
}