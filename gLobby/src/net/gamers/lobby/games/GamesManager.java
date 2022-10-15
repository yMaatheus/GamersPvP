package net.gamers.lobby.games;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.Getter;
import net.gamers.lobby.Main;
import net.gamers.lobby.games.commands.GameCommand;
import net.gamers.lobby.games.models.Game;
import net.gamers.lobby.games.models.PlayerQueue;
import net.gamerspvp.commons.bukkit.utils.BungeeChannel;
import net.gamerspvp.commons.bukkit.utils.InventoryUtils;
import net.gamerspvp.commons.bukkit.utils.LocationString;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.models.GameStatus.Server;
import net.gamerspvp.commons.network.models.GameStatus.gameStatus;
import net.gamerspvp.commons.network.utils.PingServer;

@Getter
public class GamesManager {
	
	private Main instance;
	@Getter
	private static HashMap<String, Game> cache;
	@Getter
	private static Inventory inventory;
	
	public GamesManager(Main instance) throws Exception {
		this.instance = instance;
		cache = new HashMap<>();
		inventory = null;
		
		loadData();
		
		new GameCommand(instance);
		new GamesListener(instance);
	}
	
	@SuppressWarnings("unchecked")
	private void loadData() throws Exception {
		//Criar a lista de Games disponiveis
		//Criar invent§rio e por cada modo de jogo em uma posi§§o
        //Conforme for atualizando o status atualizar o invent§rio por demanda
		inventory = Bukkit.createInventory(null, 3 * 9, "§7Modos de Jogo: ");
		
		MySQL mysql = Main.getInstance().getCommons().getDataCenter().getMysql();
		
		mysql.createTable("lobby_servers", "`name` TEXT,", "`skinName` TEXT,", "`itemStack` TEXT,", "`location` TEXT,", "`description` TEXT,", "`position` INTEGER");
		
		PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT * FROM lobby_servers;");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String name = rs.getString("name");
			String skinName = rs.getString("skinName");
			ItemStack itemStack = InventoryUtils.stringToItemStack(rs.getString("itemStack"));
			Location location = LocationString.stringToLocation(rs.getString("location"));
			List<String> description = new Gson().fromJson(rs.getString("description"), List.class);
			int position = rs.getInt("position");
			
			Game game = new Game(name, skinName, itemStack, location, description, position);
			if (game.checkNull()) {
				game.getLocation().getChunk().load();
				game.spawn();
			}
			
			cache.put(name.toLowerCase(), game);
		}
		rs.close();
		ps.close();
		
		loadChestGames();
		instance.runAsyncLoopTask(() -> loadQueue());
	}
	
	public static void loadChestGames() {
		if (cache.isEmpty()) {
			return;
		}
		inventory.clear();
		for (Game game : cache.values()) {
			if (!game.checkNull() || game.getPosition() == 0) {
				continue;
			}
			inventory.setItem(game.getPosition(), game.getIconMenu());
		}
	}
	
	public void loadQueue() {
		if (cache.isEmpty()) {
			return;
		}
		for (Game game : cache.values()) {
			if (!game.checkNull()) {
				continue;
			}
			//Update o status do game
			game.updateStatus();
			//Verify if server is acessible
			if (game.getQueue().isEmpty() || game.getStatus().getStatus() != gameStatus.ONLINE) {
				continue;
			}
			//Load balance de welcome Servers
			List<Server> values = new ArrayList<Server>();
			values.addAll(game.getStatus().getWelcomeServers());
			Collections.sort(values, new Comparator<Server>() {
			    @Override
			    public int compare(Server pt1, Server pt2) {
			        Integer f1 = pt1.getOnline();
			        Integer f2 = pt2.getOnline();
			        return f1.compareTo(f2);
			    }
			});
			List<Server> servers = new ArrayList<>(); 
			for (int a = 0; a < values.size(); a++) {
				Server server = values.get(a);
				if (server.getOnline() >= server.getMaxPlayers()) { //Verify if server is full.
					continue;
				}
				if (!PingServer.hasOnlineServer(server.getAddress(), server.getPort())) {
					continue;
				}
				servers.add(server); //Add the list servers don't full and acessible.
			}
			if (servers.isEmpty()) {
				continue;
			}
			//Update position users and load queue
			List<PlayerQueue> users = new ArrayList<>();
			users.addAll(game.getQueue().values());
			Collections.sort(users, new Comparator<PlayerQueue>() {
				
				@Override
				public int compare(PlayerQueue pt1, PlayerQueue pt2) {
					Long f1 = pt1.getFlagTime();
					Long f2 = pt2.getFlagTime();
					return f1.compareTo(f2);
				}
			});
			int i = 0;
			BungeeChannel bungeeAPI = instance.getCommons().getBungeeChannelAPI();
			for (int a = 0; a < users.size(); a++) {
				PlayerQueue playerQueue = users.get(a);
				int position = (a + 1);
				Player player = playerQueue.getPlayer();
				if (player == null || !player.isOnline()) {
					continue;
				}
				if (i > 3) {
					playerQueue.setPosition(position);
					game.getQueue().put(player, playerQueue);
					continue;
				}
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
				player.sendMessage("§a[Lobby] Chegou sua vez! Estou conectando voc§ agora mesmo.");
				int n = ThreadLocalRandom.current().nextInt(0, servers.size());
				bungeeAPI.connect(player, servers.get(n).getServerName());
				i++;
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Game getGame(String gameName) throws Exception {
		Game game = null;
		MySQL mysql = Main.getInstance().getCommons().getDataCenter().getMysql();
		PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT * FROM lobby_servers WHERE name=?;");
		ps.setString(1, gameName);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String name = rs.getString("name");
			String skinName = rs.getString("skinName");
			ItemStack itemStack = InventoryUtils.stringToItemStack(rs.getString("itemStack"));
			Location location = LocationString.stringToLocation(rs.getString("location"));
			List<String> description = new Gson().fromJson(rs.getString("description"), List.class);
			int position = rs.getInt("position");
			
			game = new Game(name, skinName, itemStack, location, description, position);
		}
		rs.close();
		ps.close();
		return game;
	}
	
	public static void put(Game game) {
		cache.put(game.getName().toLowerCase(), game);
	}
	
	public static Game getCache(String gameName) {
		return cache.get(gameName.toLowerCase());
	}
}