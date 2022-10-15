package net.gamerspvp.commons.bungee.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.netty.util.internal.ThreadLocalRandom;
import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.auth.models.UserQueue;
import net.gamerspvp.commons.network.models.GameStatus;
import net.gamerspvp.commons.network.models.GameStatus.Server;
import net.gamerspvp.commons.network.models.GameStatus.gameStatus;
import net.gamerspvp.commons.network.utils.PingServer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;

public class AuthQueueManager {
	
	private HashMap<ProxiedPlayer, UserQueue> queue;
	
	public AuthQueueManager(ProxiedCommons instance) {
		this.queue = new HashMap<ProxiedPlayer, UserQueue>();
		ProxyServer.getInstance().getScheduler().schedule(instance, new Runnable() {
			
			public void run() {
				int i = 0;
				GameStatus game = instance.getGameStatus("lobby");
				if (queue.isEmpty() || game == null || game.getStatus() != gameStatus.ONLINE) {
					return;
				}
				List<Server> values = new ArrayList<Server>();
				values.addAll(game.getServers().values());
				Collections.sort(values, new Comparator<Server>() {
				    @Override
				    public int compare(Server pt1, Server pt2) {
				        Integer f1 = pt1.getOnline();
				        Integer f2 = pt2.getOnline();
				        return f1.compareTo(f2);
				    }
				});
				
				List<ServerInfo> lobbys = new ArrayList<>(); 
				for (int a = 0; a < values.size(); a++) {
					Server server = values.get(a);
					int maxPlayers = server.getMaxPlayers() + 30; //Adicionando um maxPlayers para +30 por lobby
					ServerInfo serverInfo = instance.getProxy().getServerInfo(server.getServerName());
					if (server.getOnline() >= maxPlayers || serverInfo == null) { //Verificando se o servidor está super lotado.
						continue;
					}
					if (!PingServer.hasOnlineServer(server.getAddress(), server.getPort())) {
						continue;
					}
					lobbys.add(serverInfo); // Adicionando a lista de servidores com poucos jogadores.
				}
				if (lobbys.isEmpty()) {
					return;
				}
				List<UserQueue> users = formatQueue(queue.values());
				for (int a = 0; a < users.size(); a++) {
					if (i >= 30) {
						break;
					}
					UserQueue userQueue = users.get(a);
					//int position = a + 1;
					ProxiedPlayer proxiedPlayer = userQueue.getProxiedPlayer();
					if (proxiedPlayer == null || (!proxiedPlayer.isConnected())) {
						continue;
					}
					int n = ThreadLocalRandom.current().nextInt(0, lobbys.size());
					
					proxiedPlayer.connect(lobbys.get(n), null, false, Reason.PLUGIN, 0);
					i++;
				}
			}
		}, 2, 2, TimeUnit.SECONDS);
	}
	
	public void add(ProxiedPlayer proxiedPlayer) throws Exception {
		boolean vip = proxiedPlayer.hasPermission("gamers.staff") || proxiedPlayer.hasPermission("gamers.vip");
		queue.put(proxiedPlayer, new UserQueue(proxiedPlayer, System.currentTimeMillis(), vip));
		proxiedPlayer.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aEstamos enviando você até o §fSaguão§a do servidor!"));
	}
	
	public void remove(ProxiedPlayer proxiedPlayer) {
		if (!queue.containsKey(proxiedPlayer)) {
			return;
		}
		queue.remove(proxiedPlayer);
		proxiedPlayer.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aEnvio ao §fSaguão §aconcluido com sucesso!"));
	}
	
	public List<UserQueue> formatQueue(Collection<UserQueue> collection) {
		List<UserQueue> values = new ArrayList<>();
		values.addAll(collection);
		Collections.sort(values, new Comparator<UserQueue>() {
			
			@Override
			public int compare(UserQueue pt1, UserQueue pt2) {
				Long f1 = pt1.getFlagTime();
				Long f2 = pt2.getFlagTime();
				return f1.compareTo(f2);
			}
		});
		return values;
	}
}