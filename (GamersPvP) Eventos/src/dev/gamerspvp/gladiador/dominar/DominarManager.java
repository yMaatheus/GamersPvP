package dev.gamerspvp.gladiador.dominar;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import dev.gamerspvp.gladiador.Main;
import dev.gamerspvp.gladiador.dominar.commands.DominarCommand;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class DominarManager {
	
	private Main instance;
	private FileConfiguration config;
	
	private boolean active;
	private Location location;
	private HashSet<String> chestSet;
	
	private Clan clan;
	private HashSet<Player> players;
	private long invencible;
	
	public DominarManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.active = true;
		this.location = null;
		this.chestSet = new HashSet<String>();
		this.clan = null;
		this.players = new HashSet<Player>();
		this.invencible = System.currentTimeMillis();
		new DominarListener(instance);
		instance.registerCommand(new DominarCommand(instance), "dominar");
		new BukkitRunnable() {

			@Override
			public void run() {
				clearBuildArea(getProtectedRegion(location));
			}
		}.runTaskLater(instance, 30L);
	}

	public void updatePlayersClan() {
		if (!(active)) {
			return;
		}
		List<ClanPlayer> onlineMembers = clan.getOnlineMembers();
		if (onlineMembers.isEmpty()) {
			return;
		}
		players.clear();
		for (ClanPlayer member : onlineMembers) {
			players.add(member.toPlayer());
		}
	}

	public void setActive(boolean active) {
		this.active = active;
		if (location == null) {
			return;
		}
		clearBuildArea(getProtectedRegion(location));
		if (!(active)) {
			reset();
		}
	}

	public void clearBuildArea(ProtectedRegion protectedRegion) {
		if (protectedRegion == null) {
			return;
		}
		if (location == null) {
			return;
		}
		location.getChunk().load();
		World world = location.getWorld();
		BlockVector minPoint = protectedRegion.getMinimumPoint();
		BlockVector maxPoint = protectedRegion.getMaximumPoint();
		int minX = minPoint.getBlockX();
		int minY = minPoint.getBlockY();
		int minZ = minPoint.getBlockZ();
		int maxX = maxPoint.getBlockX();
		int maxY = maxPoint.getBlockY();
		int maxZ = maxPoint.getBlockZ();
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					// if ((x == location.getBlockX()) && (y == location.getBlockY()) && (z ==
					// location.getBlockZ())) {
					// continue;
					// }
					setBlockFast(world, x, y, z, 0, (byte) 0);
				}
			}
		}
	}

	public boolean isInRegion(Location location, String regionMame) {
		RegionManager regionManager = instance.getWorldGuard().getRegionManager(location.getWorld());
		ApplicableRegionSet regionSet = regionManager.getApplicableRegions(location);
		for (ProtectedRegion each : regionSet) {
			if (each.getId().equalsIgnoreCase(regionMame)) {
				return true;
			}
		}
		return false;
	}

	public ProtectedRegion getProtectedRegion(Location location) {
		if (location == null) {
			return null;
		}
		RegionManager manager = instance.getWorldGuard().getRegionManager(location.getWorld());
		ApplicableRegionSet regionSet = manager.getApplicableRegions(location);
		for (ProtectedRegion each : regionSet) {
			if (each.getId().contains("minapvp-dominar")) {
				return each;
			}
		}
		return null;
	}

	public void setBlock(Location location) {
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		double yaw = location.getYaw();
		double pitch = location.getPitch();
		String world = location.getWorld().getName();

		this.location = location;

		config.set("location.x", Double.valueOf(x));
		config.set("location.y", Double.valueOf(y));
		config.set("location.z", Double.valueOf(z));
		config.set("location.yaw", Double.valueOf(yaw));
		config.set("location.pitch", Double.valueOf(pitch));
		config.set("location.world", world);
		saveConfig();
	}

	private Location getBlockLocation() {
		if (config.getConfigurationSection("location") == null) {
			return null;
		}
		double x = config.getDouble("location.x");
		double y = config.getDouble("location.y");
		double z = config.getDouble("location.z");
		double yaw = config.getDouble("location.yaw");
		double pitch = config.getDouble("location.pitch");
		World world = Bukkit.getWorld(config.getString("location.world"));
		if ((x == 0.0D) && (y == 0.0D) && (z == 0.0D)) {
			return null;
		}
		return new Location(world, x, y, z, (short) (int) yaw, (short) (int) pitch);
	}

	@SuppressWarnings("deprecation")
	private void setBlockFast(World world, int x, int y, int z, int blockId, byte data) {
		net.minecraft.server.v1_8_R3.World w = ((CraftWorld) world).getHandle();
		net.minecraft.server.v1_8_R3.Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
		BlockPosition bp = new BlockPosition(x, y, z);
		int combined = blockId + (data << 12);
		IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
		chunk.a(bp, ibd);
		world.refreshChunk(chunk.locX, chunk.locZ);
	}

	public void reset() {
		this.clan = null;
		this.players = new HashSet<Player>();
		this.invencible = 0;
	}

	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "dominar.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean containsPlayer(Player player) {
		if (clan == null) {
			return false;
		}
		if (players.contains(player)) {
			return true;
		}
		return false;
	}

	public HashSet<String> getChestSet() {
		return chestSet;
	}

	public boolean isActive() {
		return active;
	}

	public Location getLocation() {
		if (location == null) {
			Location loc = getBlockLocation();
			if (loc == null) {
				return null;
			}
			location = loc;
			return loc;
		}
		return location;
	}

	public Clan getClan() {
		return clan;
	}

	public void setClan(Clan clan) {
		this.clan = clan;
	}

	public HashSet<Player> getPlayers() {
		return players;
	}

	public long getInvencible() {
		return invencible;
	}

	public void setInvencible(long invencible) {
		this.invencible = invencible;
	}
}