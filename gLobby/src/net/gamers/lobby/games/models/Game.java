package net.gamers.lobby.games.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.gamers.lobby.Main;
import net.gamers.lobby.games.GamesManager;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.utils.Hologram;
import net.gamerspvp.commons.bukkit.utils.InventoryUtils;
import net.gamerspvp.commons.bukkit.utils.LocationString;
import net.gamerspvp.commons.bukkit.utils.MakeItem;
import net.gamerspvp.commons.network.database.MySQL;
import net.gamerspvp.commons.network.models.GameStatus;
import net.gamerspvp.commons.network.models.GameStatus.Server;
import net.gamerspvp.commons.network.models.GameStatus.gameStatus;
import net.gamerspvp.commons.network.utils.PingServer;

@Getter
@Setter
public class Game {
	
	private String name;
	private String skinName;
	private ItemStack itemStack;
	private Location location;
	private List<String> description;
	private int position;
	
	private GameStatus status;
	private Hologram hologram;
	private NPC npc;
	private boolean spawn;
	private HashMap<Player, PlayerQueue> queue;
	
	private static String fancyName;
	
	public Game(String name, String skinName, ItemStack itemStack, Location location, List<String> desc, int position) {
		this.name = name;
		this.skinName = skinName;
		this.itemStack = itemStack;
		this.location = location;
		this.description = desc;
		this.position = position;
		this.status = null;
		this.hologram = null;
		this.npc = null;
		this.queue = new HashMap<>();
		
		fancyName = "§2§l" + name.toUpperCase();
	}
	
	public Game(String name, String skinName, ItemStack itemStack, Location location) {
		this.name = name;
		this.skinName = skinName;
		this.itemStack = itemStack;
		this.location = location;
		this.description = new ArrayList<>();
		this.position = 0;
		this.status = null;
		this.hologram = null;
		this.npc = null;
		this.queue = new HashMap<>();
		
		fancyName = "§2§l" + name.toUpperCase();
	}
	
	public void update() throws Exception {
		MySQL mysql = Main.getInstance().getDataCenter().getMysql();
		if (exists()) {
			String query = "UPDATE lobby_servers SET `skinName`=?, `itemStack`=?, `location`=?, `description`=?, `position`=? WHERE `name`=?";
			PreparedStatement ps = mysql.getConnection().prepareStatement(query);
			ps.setString(1, skinName);
			ps.setString(2, InventoryUtils.itemstackToString(itemStack));
			ps.setString(3, LocationString.locationToString(location));
			ps.setString(4, new Gson().toJson(description, List.class));
			ps.setInt(5, position);
			ps.setString(6, name);
			
			ps.executeUpdate();
			ps.close();
			return;
		}
		String query = "INSERT INTO lobby_servers (`name`, `skinName`, `itemStack`, `location`, `description`, `position`) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		
		ps.setString(1, name);
		ps.setString(2, skinName);
		ps.setString(3, InventoryUtils.itemstackToString(itemStack));
		ps.setString(4, LocationString.locationToString(location));
		ps.setString(5, new Gson().toJson(description, List.class));
		ps.setInt(6, position);
		
		ps.execute();
		ps.close();
	}
	
	public void update(boolean respawn) throws Exception {
		update();
		if (respawn) {
			spawn();
		}
	}
	
	public void delete() throws Exception {
		MySQL mysql = Main.getInstance().getDataCenter().getMysql();
		String query = "DELETE FROM lobby_servers WHERE name=?;";
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ps.setString(1, name);
		
		ps.execute();
		ps.close();
	}
	
	public boolean exists() throws SQLException {
		boolean value = false;
		MySQL mysql = Main.getInstance().getDataCenter().getMysql();
		PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT * FROM lobby_servers WHERE name=?;");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			value = true;
		}
		rs.close();
		ps.close();
		return value;
	}
	
	public void joinVIPGame(Player player) {
		player.sendMessage("§a[Lobby] Entrando na fila...");
		String offline = "§cDesculpe, mas o modo de jogo encontra-se offline.";
		if (status.getStatus() != gameStatus.ONLINE) {
			player.sendMessage(offline);
			return;
		}
		ArrayList<Server> serversBypass = new ArrayList<>();
		for (Server server : status.getWelcomeServers()) {
			if (!PingServer.hasOnlineServer(server.getAddress(), server.getPort())) {
				continue;
			}
			serversBypass.add(server);
		}
		if (serversBypass.isEmpty()) {
			player.sendMessage(offline);
			return;
		}
		player.sendMessage("§a[Lobby] Verifiquei aqui e voc§ § §lVIP§a, Voc§ acha mesmo que iriamos deixar voc§ em uma fila?! Me Poupe!");
		int n = ThreadLocalRandom.current().nextInt(0, serversBypass.size());
		CommonsBukkit.getInstance().getBungeeChannelAPI().connect(player, serversBypass.get(n).getServerName());
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
	}
	
	public void joinQueue(Player player) {
		PlayerQueue pQueue = queue.get(player);
		if (pQueue != null) {
			player.sendMessage("§a[Lobby] Voc§ j§ faz parte da fila do servidor, Bobinho!");
			player.sendMessage("§a[Lobby] Hmmm, voc§ § curioso(a) em! O seu lugar da fila §: §f#" + pQueue.getPosition());
			return;
		}
		player.sendMessage("§a[Lobby] Entrando na fila...");
		int position = queue.size() + 1;
		queue.put(player, new PlayerQueue(player, position));
		player.sendMessage("§a[Lobby] Voc§ § o §f#" + position + "§a na fila para conectar ao modo de jogo §f" + fancyName + "§a com sucesso.");
		player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
	}
	
	public void spawn() {
		if (!checkNull()) {
			return;
		}
		if (spawn) {
			despawn();
		}
		updateStatus();
		createHologram();
		createNPC();
		if (npc == null || hologram == null) { 
			return;
		}
		spawn = true;
		hologram.spawn();
		npc.spawn(location);
		//EntityPlayer entityPlayer = ((CraftPlayer) npc.getEntity()).getHandle();
		//Scoreboard scoreboard = TeamsManager.getScoreboard();
		//PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
		//entityPlayer.playerConnection.sendPacket(packet);
	}
	
	public void despawn() {
		if (npc == null || hologram == null) { 
			return;
		}
		hologram.despawn();
		npc.despawn();
		spawn = false;
	}
	
	public void updateStatus() {
		updateStatus(Main.getInstance().getCommons().getGameStatus(name.toLowerCase()));
	}
	
	public void updateStatus(GameStatus serverStatus) {
		this.status = serverStatus;
		if (hologram != null) {
			hologram.updateLine(2, getHologramStatus());
			hologram.updateLine(1, getOnline());
		}
		if (checkNull() || status != null || position != 0) {
			Inventory inventory = GamesManager.getInventory();
			if (inventory != null && inventory.getItem(position) != null) {
				ItemMeta itemMeta = inventory.getItem(position).getItemMeta();
				if (itemMeta.getDisplayName().equalsIgnoreCase("§a" + name)) {
					GamesManager.getInventory().setItem(position, getIconMenu());
				}
			}
		}
	}
	
	private void createNPC() {
		NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
		NPC npc = registry.createNPC(EntityType.PLAYER, "[NPC]");
		if (itemStack != null) {
			Equipment equipTrait = npc.getTrait(Equipment.class);
	        equipTrait.set(EquipmentSlot.HAND, itemStack);
	        npc.addTrait(equipTrait);
		}
		npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skinName);
        npc.data().setPersistent(NPC.PLAYER_SKIN_USE_LATEST, false);
        Entity npcEntity = npc.getEntity();
        if (npcEntity instanceof SkinnableEntity) {
            ((SkinnableEntity) npcEntity).getSkinTracker().notifySkinChange(npc.data().get(NPC.PLAYER_SKIN_USE_LATEST));
        }
        this.npc = npc;
	}
	
	private void createHologram() {
		if (status == null) {
			return;
		}
		//location.setY(location.getY() + 0.83D);
		//Location l = new Location(location.getWorld(), location.getX(), location.getY() + 0.83D, location.getZ());
		this.hologram = new Hologram(location, 0.1, fancyName, "", getHologramStatus(), getOnline());
		Location location = hologram.getLine(4).getLocation();
		hologram.editLineY(4, location.getY() + 0.5D);
	}
	
	public ItemStack getIconMenu() {
		List<String> lore = new ArrayList<>();
		for (String description : description) {
			lore.add("§7  " + description);
		}
		ItemStack value = null;
		MakeItem item = new MakeItem(itemStack.getType()).setName(fancyName);
		if (!lore.isEmpty()) {
			item.addLore("");
			item.addLore(lore);
		}
		item.addLore("", "§aClique para jogar!", getOnline(), "").addFlags(ItemFlag.HIDE_ATTRIBUTES);
		value = item.build();
		return value;
	}
	
	public String getHologramStatus() {
		String value = "§c§lOFFLINE";
		if (status != null)  {
			if (status.getStatus() == gameStatus.ONLINE) {
				value = "§a§lONLINE";
			} else if (status.getStatus() == gameStatus.MANUTENÇÃO) {
				value = "§e§lMANUTENÇÃO";
			}
		}
		return value;
	}
	
	public String getOnline() {
		String value = "§a0 §fJogando!";
		if (status != null) {
			value = "§a" + status.getOnline() + " §fJogando!";
		}
		return value;
	}
	
	public void setLocation(Player player) {
		this.location = player.getLocation();
//		location.setY(location.getY() + 0.83D);
		location.setY(location.getY() + 0.6D);
	}
	
	public boolean checkNull() {
		if (skinName == null || itemStack == null || location == null || description == null) {
			return false;
		}
		return true;
	}
}