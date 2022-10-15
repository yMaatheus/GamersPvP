package net.gamerspvp.commons.bukkit.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AnvilAPI {

	private static Constructor<?> containerAnvilConstructor;
	private static Constructor<?> blockPositionConstructor;
	private static Constructor<?> packetConstructor;
	private static Method nextContainerCounter;
	private static Method addSlotListener;
	private static Method getHandle;
	private static Object message;
	private static Field windowId;
	private static Field worldField;
	private static Field inventoryField;
	private static Field checkReachableField;
	private static Field activeContainerField;

	public static void openAnvil(Player p) {
		try {
			Location l = p.getLocation();
			int x = l.getBlockX();
			int y = l.getBlockY();
			int z = l.getBlockZ();
			
			Object packet;
			Object container;
			Object entityPlayer = getHandle.invoke(p);
			Object world = worldField.get(entityPlayer);
			Object inventory = inventoryField.get(entityPlayer);
			Object counter = nextContainerCounter.invoke(entityPlayer);
			
			Object blockPosition = blockPositionConstructor.newInstance(x, y, z);
			container = containerAnvilConstructor.newInstance(inventory, world, blockPosition, entityPlayer);
			packet = packetConstructor.newInstance(counter, "minecraft:anvil", message);
			
			ReflectionUtils.sendPacket(p, packet);
			
			windowId.set(container, counter);
			checkReachableField.set(container, false);
			activeContainerField.set(entityPlayer, container);
			addSlotListener.invoke(container, entityPlayer);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	static void load() {
		try {
			Class<?> craftPlayerClass = ReflectionUtils.getOBClass("entity.CraftPlayer");
			Class<?> entityPlayerClass = ReflectionUtils.getNMSClass("EntityPlayer");
			Class<?> containerAnvilClass = ReflectionUtils.getNMSClass("ContainerAnvil");

			getHandle = craftPlayerClass.getMethod("getHandle");
			nextContainerCounter = entityPlayerClass.getMethod("nextContainerCounter");

			worldField = entityPlayerClass.getField("world");
			inventoryField = entityPlayerClass.getField("inventory");
			activeContainerField = entityPlayerClass.getField("activeContainer");
			checkReachableField = containerAnvilClass.getField("checkReachable");
			windowId = containerAnvilClass.getField("windowId");

			Class<?> icraftingClass = ReflectionUtils.getNMSClass("ICrafting");
			Class<?> containerClass = ReflectionUtils.getNMSClass("Container");
			addSlotListener = containerClass.getMethod("addSlotListener", icraftingClass);

			Class<?> playerInventoryClass = ReflectionUtils.getNMSClass("PlayerInventory");
			Class<?> worldClass = ReflectionUtils.getNMSClass("World");
			Class<?> entityHumanClass = ReflectionUtils.getNMSClass("EntityHuman");
			containerAnvilConstructor = containerAnvilClass.getConstructor(playerInventoryClass, worldClass, int.class, int.class, int.class, entityHumanClass);
			Class<?> packetOpenWindowClass;
			packetOpenWindowClass = ReflectionUtils.getNMSClass("PacketPlayOutOpenWindow");
			packetConstructor = packetOpenWindowClass.getConstructor(int.class, int.class, String.class, int.class, boolean.class);
		} catch (Throwable e) {
		}
	}

}