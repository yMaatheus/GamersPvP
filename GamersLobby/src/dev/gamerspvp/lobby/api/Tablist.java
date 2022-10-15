package dev.gamerspvp.lobby.api;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Tablist {
	
	public static void setForPlayer(Player p, String header, String footer){
	     
        CraftPlayer craftplayer = (CraftPlayer)p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent top = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent bottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
          Field headerField = packet.getClass().getDeclaredField("a");
          headerField.setAccessible(true);
          headerField.set(packet, top);
          headerField.setAccessible(!headerField.isAccessible());

          Field footerField = packet.getClass().getDeclaredField("b");
          footerField.setAccessible(true);
          footerField.set(packet, bottom);
          footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception ev) {
          ev.printStackTrace();
        }

        connection.sendPacket(packet);
    }

}
