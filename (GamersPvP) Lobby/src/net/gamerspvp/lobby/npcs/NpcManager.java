package net.gamerspvp.lobby.npcs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.gamerspvp.commons.bukkit.utils.InventoryUtils;
import net.gamerspvp.lobby.Main;

public class NpcManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private NPCRegistry registry;
	
	private HashMap<String, NPC> cache;
	
	public NpcManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
		this.cache = new HashMap<String, NPC>();
		if (config.getConfigurationSection("Npcs") == null) {
			return;
		}
		for (String name : config.getConfigurationSection("Npcs").getKeys(false)) {
			String worldName = config.getString("Npcs." + name + ".location.world");
			if (worldName == null) {
				continue;
			}
			World world = Bukkit.getWorld(worldName);
			if (world == null) {
				continue;
			}
			double x = config.getDouble("Npcs." + name + ".location.x");
			double y = config.getDouble("Npcs." + name + ".location.y");
			double z = config.getDouble("Npcs." + name + ".location.z");
			float yaw = config.getFloat("Npcs." + name + ".location.yaw");
			float pitch = config.getFloat("Npcs." + name + ".location.pitch");
			String skin = config.getString("Npcs." + name + ".skin");
			ItemStack hand = null;
			if (config.getString("Npcs." + name + ".hand") != null) {
				hand = InventoryUtils.stringToItemStack(config.getString("Npcs." + name + ".hand"));
			}
			Location location = new Location(world, x, y, z, yaw, pitch);
			spawnNPC(name, location, hand, skin);
		}
	}
	
	public void setNPC(String name, Location location, ItemStack hand, String skin) {
		NPC cachedNPC = cache.get(name);
		if (cachedNPC != null) {
			cachedNPC.destroy();
		}
		spawnNPC(name, location, hand, skin);
		config.set("Npcs." + name + ".location.world", location.getWorld().getName());
		config.set("Npcs." + name + ".location.x", location.getX());
		config.set("Npcs." + name + ".location.y", location.getY());
		config.set("Npcs." + name + ".location.z", location.getZ());
		config.set("Npcs." + name + ".location.yaw", location.getYaw());
		config.set("Npcs." + name + ".location.pitch", location.getPitch());
		config.set("Npcs." + name + ".skin", skin);
		if (hand != null) {
			config.set("Npcs." + name + ".hand", InventoryUtils.itemstackToString(hand));
		}
		saveConfig();
	}
	
	public void destroyNPC(String name) {
		NPC npc = cache.get(name);
		if (npc != null) {
			npc.destroy();
			cache.remove(name);
		}
		//config.set("Npcs." + name + ".location.world", null);
		//config.set("Npcs." + name + ".location.x", null);
		//config.set("Npcs." + name + ".location.y", null);
		//config.set("Npcs." + name + ".location.z", null);
		//config.set("Npcs." + name + ".location.yaw", null);
		//config.set("Npcs." + name + ".location.pitch", null);
		//config.set("Npcs." + name + ".skin", null);
		config.set("Npcs." + name, null);
		saveConfig();
	}
	
	private void spawnNPC(String name, Location location, ItemStack hand, String skin) {
		NPC npc = registry.createNPC(EntityType.PLAYER, name);
		if (hand != null) {
			Equipment equipTrait = npc.getTrait(Equipment.class);
	        equipTrait.set(EquipmentSlot.HAND, hand);
	        npc.addTrait(equipTrait);
		}
		npc.spawn(location);
        npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skin);
        npc.data().setPersistent(NPC.PLAYER_SKIN_USE_LATEST, false);
        Entity npcEntity = npc.getEntity();
        if (npcEntity instanceof SkinnableEntity) {
            ((SkinnableEntity) npcEntity).getSkinTracker().notifySkinChange(npc.data().get(NPC.PLAYER_SKIN_USE_LATEST));
        }
		cache.put(name, npc);
	}
	
	public NPC getCache(String name) {
		return cache.get(name);
	}
	
	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "npcs.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}