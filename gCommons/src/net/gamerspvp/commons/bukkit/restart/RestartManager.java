package net.gamerspvp.commons.bukkit.restart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.Getter;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.restart.commands.ReiniciarCommand;
import net.gamerspvp.commons.bukkit.restart.commands.RestartCommand;
import net.gamerspvp.commons.bukkit.restart.listeners.RestartListener;
import net.gamerspvp.commons.bukkit.restart.listeners.custom.PreServerShutdownEvent;
import net.gamerspvp.commons.bukkit.utils.BungeeChannel;
import net.gamerspvp.commons.bukkit.utils.Titles;
import net.gamerspvp.commons.network.models.GameStatus;
import net.gamerspvp.commons.network.models.GameStatus.Server;
import net.gamerspvp.commons.network.models.GameStatus.gameStatus;
import net.gamerspvp.commons.network.utils.PingServer;

public class RestartManager {
	
	private static CommonsBukkit instance;
	@Getter
	private static boolean closed;
	
	public RestartManager(CommonsBukkit instance) {
		RestartManager.instance = instance;
		closed = false;
		
		new ReiniciarCommand(instance);
		new RestartCommand(instance);
		
		new RestartListener(instance);
	}
	
	public static void executeRestart() {
		//Executar o restart
		// * Servidor enviará todos os jogadores ao lobby e depois reiniciará
		// 
		closed = true;
		Bukkit.getPluginManager().callEvent(new PreServerShutdownEvent());
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§c[AVISO] O Servidor irá reiniciar em §f§l60 §csegundos!");
		Bukkit.broadcastMessage("");
		
		Titles title = new Titles();
		title.setTitle("§c§lSERVER REINICIANDO");
		title.setSubtitle("§cReiniciando em §f60 §csegundos!");
		title.broadcast();
		
		new BukkitRunnable() {
			public void run() {
				executeSendPlayersLobby();
				Bukkit.shutdown();
			}
		}.runTaskLater(instance, 1200L);
	}
	
	public static void executeFastRestart() {
		closed = true;
		Bukkit.getPluginManager().callEvent(new PreServerShutdownEvent());
		Bukkit.broadcastMessage("§cServidor reiniciando..");
		executeSendPlayersLobby();
		Bukkit.shutdown();
	}
	
	private static void executeSendPlayersLobby() {
		GameStatus game = instance.getGameStatus("lobby");
		if (game == null || game.getStatus() != gameStatus.ONLINE) {
			executeKickAll();
			return;
		}
		List<Server> values = new ArrayList<Server>();
		if (game.getWelcomeServers().isEmpty()) {
			values.addAll(game.getServers().values());
		} else {
			values.addAll(game.getWelcomeServers());
		}
		Collections.sort(values, new Comparator<Server>() {
		    @Override
		    public int compare(Server pt1, Server pt2) {
		        Integer f1 = pt1.getOnline();
		        Integer f2 = pt2.getOnline();
		        return f1.compareTo(f2);
		    }
		});
		
		List<Server> lobbys = new ArrayList<>(); 
		for (int a = 0; a < values.size(); a++) {
			Server server = values.get(a);
			if (server.getOnline() >= server.getMaxPlayers()) { //Verify if server is full.
				continue;
			}
			if (!PingServer.hasOnlineServer(server.getAddress(), server.getPort())) {
				continue;
			}
			lobbys.add(server); //Add the list servers don't full and acessible.
		}
		if (lobbys.isEmpty()) {
			executeKickAll();
			return;
		}
		BungeeChannel bungeeAPI = instance.getBungeeChannelAPI();
		HashSet<Player> players = new HashSet<>();
		players.addAll(Bukkit.getOnlinePlayers());
		if (players.isEmpty()) {
			return;
		}
		for (Player player : players) {
			if (player == null || !player.isOnline()) {
				continue;
			}
			int n = ThreadLocalRandom.current().nextInt(0, lobbys.size());
			bungeeAPI.connect(player, lobbys.get(n).getServerName());
		}
	}
	
	private static void executeKickAll() {
		HashSet<Player> players = new HashSet<>();
		players.addAll(Bukkit.getOnlinePlayers());
		if (players.isEmpty()) {
			return;
		}
		for (Player player : players) {
			if (player == null || !player.isOnline()) {
				continue;
			}
			player.kickPlayer("§cServidor reiniciando...");
		}
	}
}