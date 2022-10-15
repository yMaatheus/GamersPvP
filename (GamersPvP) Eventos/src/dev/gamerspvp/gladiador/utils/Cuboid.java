package dev.gamerspvp.gladiador.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.google.gson.JsonObject;

public class Cuboid implements Cloneable {

	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;
	private String world;

	public Cuboid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, World world) {
		this.minX = Math.min(minX, maxX);
		this.minY = Math.min(minY, maxY);
		this.minZ = Math.min(minZ, maxZ);
		this.maxX = Math.max(minX, maxX);
		this.maxY = Math.max(minY, maxY);
		this.maxZ = Math.max(minZ, maxZ);

		this.world = world.getName();
	}

	public Cuboid(Location min, Location max) {
		this.minX = Math.min(min.getBlockX(), max.getBlockX());
		this.minY = Math.min(min.getBlockY(), max.getBlockY());
		this.minZ = Math.min(min.getBlockZ(), max.getBlockZ());
		this.maxX = Math.max(min.getBlockX(), max.getBlockX());
		this.maxY = Math.max(min.getBlockY(), max.getBlockY());
		this.maxZ = Math.max(min.getBlockZ(), max.getBlockZ());

		this.world = min.getWorld().getName();
	}

	public Cuboid(JsonObject json) {
		this(json.get("minX").getAsInt(), json.get("minY").getAsInt(), json.get("minZ").getAsInt(), json.get("maxX").getAsInt(), json.get("maxY").getAsInt(),
				json.get("maxZ").getAsInt(), Bukkit.getServer().getWorld(json.get("world").getAsString()));
	}

	@Override
	public Cuboid clone() {
		return new Cuboid(minX, minY, minZ, maxX, maxY, maxZ, Bukkit.getWorld(world));
	}

	public Cuboid expand(int x, int y, int z) {
		return new Cuboid(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z, Bukkit.getWorld(world));
	}

	public Cuboid contract(int x, int y, int z) {
		return expand(-x, -y, -z);
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public void setMinZ(int minZ) {
		this.minZ = minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	public void setWorld(World world) {
		this.world = world.getName();
	}

	public Location getCenter() {
		double X = (maxX - minX) / 2;
		double Y = (maxY - minY) / 2;
		double Z = (maxZ - minZ) / 2;

		return new Location(Bukkit.getWorld(world), minX + X, minY + Y, minZ + Z);
	}

	public List<Entity> getEntities() {
		List<Entity> entities = new ArrayList<Entity>();

		for (Chunk chunk : Bukkit.getWorld(world).getLoadedChunks()) {
			for (Entity entity : chunk.getEntities()) {
				entities.add(entity);
			}
		}

		return entities;
	}

	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<Location>();
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					locations.add(Bukkit.getWorld(world).getBlockAt(x, y, z).getLocation());
				}
			}
		}
		return locations;
	}

	public List<Block> getBlocks() {
		return getLocations().stream().map(Location::getBlock).collect(Collectors.toList());
	}

	public Set<Block> getBlocks1() {
		return getLocations().stream().map(Location::getBlock).collect(Collectors.toSet());
	}

	public List<Block> getAirBlocks() {
		return getLocations().stream().map(Location::getBlock).filter(b -> b == null || b.getType() == Material.AIR).collect(Collectors.toList());
	}

	public List<Block> getSolidBlocks() {
		return getLocations().stream().map(Location::getBlock).filter(b -> b != null && b.getType() != Material.AIR).collect(Collectors.toList());
	}

	public List<Block> getBlocks(Material material) {
		return getLocations().stream().map(Location::getBlock).filter(b -> b != null && b.getType() == material).collect(Collectors.toList());
	}

	public Set<Chunk> getChunks() {
		Set<Chunk> chunks = new HashSet<Chunk>();

		getLocations().forEach(location -> chunks.add(location.getChunk()));

		return chunks;
	}

	@SuppressWarnings("deprecation")
	public List<Block> getBlocks(Material material, byte data) {
		return getLocations().stream().map(Location::getBlock).filter(b -> b != null && b.getType() == material && b.getData() == data)
				.collect(Collectors.toList());
	}

	public List<Location> getWalls() {
		List<Location> locations = new ArrayList<Location>();

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					Location location = Bukkit.getWorld(world).getBlockAt(x, y, z).getLocation();

					if (location.getBlockX() == minX || location.getBlockX() == maxX || location.getBlockZ() == minZ || location.getBlockZ() == maxZ) {
						locations.add(location);
					}
				}
			}
		}

		return locations;
	}

	public List<Location> getWalls(int y) {
		List<Location> locations = new ArrayList<Location>();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				Location location = Bukkit.getWorld(world).getBlockAt(x, y, z).getLocation();

				if (location.getBlockX() == minX || location.getBlockX() == maxX || location.getBlockZ() == minZ || location.getBlockZ() == maxZ) {
					locations.add(location);
				}
			}
		}

		return locations;
	}

	public List<Location> getRectangle(int y) {
		List<Location> locations = new ArrayList<Location>();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				locations.add(new Location(Bukkit.getWorld(world), x, y, z));
			}
		}

		return locations;
	}

	public int getWidth() {
		return maxX - minX + 1;
	}

	public int getLength() {
		return maxZ - minZ + 1;
	}

	public int getHeigth() {
		return maxY - minY + 1;
	}

	public int getSize() {
		return getWidth() * getLength() * getHeigth();
	}

	public boolean intersect(Cuboid cuboid) {
		if (!world.equals(cuboid.getWorld().getName())) {
			return false;
		}

		int oMinX = cuboid.getMinX();
		int oMinY = cuboid.getMinY();
		int oMinZ = cuboid.getMinZ();

		int oMaxX = cuboid.getMaxX();
		int oMaxY = cuboid.getMaxY();
		int oMaxZ = cuboid.getMaxZ();

		return maxX >= oMinX && maxY >= oMinY && maxZ >= oMinZ && oMaxX >= minX && oMaxY >= minY && oMaxZ >= minZ;
	}

	public boolean contains(int X, int Y, int Z) {
		return X >= minX && X <= maxX && Y >= minY && Y <= maxY && Z >= minZ && Z <= maxZ;
	}

	public boolean contains(Location location) {
		if (location == null || !world.equals(location.getWorld().getName())) {
			return false;
		}

		int X = (int) location.getX();
		int Y = (int) location.getY();
		int Z = (int) location.getZ();

		return contains(X, Y, Z);
	}

	public Location getMinLocation() {
		return new Location(Bukkit.getWorld(world), minX, minY, minZ);
	}

	public Location getMaxLocation() {
		return new Location(Bukkit.getWorld(world), maxX, maxY, maxZ);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();

		json.addProperty("minX", minX);
		json.addProperty("minY", minY);
		json.addProperty("minZ", minZ);

		json.addProperty("maxX", maxX);
		json.addProperty("maxY", maxY);
		json.addProperty("maxZ", maxZ);

		json.addProperty("world", world);

		return json;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cuboid)) {
			return false;
		}

		Cuboid cuboid = (Cuboid) obj;

		return getMinLocation().equals(cuboid.getMinLocation()) && getMaxLocation().equals(cuboid.getMaxLocation());
	}

}
