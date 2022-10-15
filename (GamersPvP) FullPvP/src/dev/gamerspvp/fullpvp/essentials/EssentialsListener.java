package dev.gamerspvp.fullpvp.essentials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;

import br.com.devpaulo.legendchat.api.events.PrivateMessageEvent;
import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.utils.Title;

public class EssentialsListener implements Listener {
	
	private Main instance;
	
	public EssentialsListener(Main instance) {
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		player.teleport(essentialsManager.getSpawnWorld().getSpawnLocation());
		player.sendMessage(essentialsManager.getMessageJoin().replace("%player%", player.getName()).replace("%online%", "" + Bukkit.getOnlinePlayers().size()));
		player.setAllowFlight(false);
		String header = essentialsManager.getTablistHeader();
		String footer = essentialsManager.getTablistFooter();
		Title.sendTabTitle(player, header, footer);
		if ((player.hasPermission("fly.vip")) || (player.hasPermission("fly.admin"))) {
			if (!(player.getAllowFlight())) {
				if (!(essentialsManager.isVipWorld(player.getWorld().getName()))) {
					return;
				}
				player.sendMessage("§a[Fly] Fly ativado automaticamente por possuir acesso §a§lVIP§a!");
				player.setAllowFlight(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		if (!essentialsManager.hasInScreenShare(player)) {
			return;
		}
		Bukkit.broadcast("§c" + player.getName()  + " deslogou na screenshare!", "essentials.screenshare");
	}
	
	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		boolean adminPermission = player.hasPermission("fly.admin");
		if ((player.hasPermission("fly.vip")) || (adminPermission)) {
			EssentialsManager essentialsManager = instance.getEssentialsManager();
			if (!(essentialsManager.isVipWorld(player.getWorld().getName()))) {
				if (adminPermission) {
					return;
				}
				player.setAllowFlight(false);
				player.sendMessage("§a[Fly] Fly desativado automaticamente neste mundo por ser de PvP.");
				return;
			}
			if (player.getAllowFlight()) {
				return;
			}
			player.sendMessage("§a[Fly] Fly ativado automaticamente neste mundo por possuir §a§lVIP§a!");
			player.setAllowFlight(true);
			return;
		}
		player.setAllowFlight(false);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!player.getAllowFlight()) {
			return;
		}
		if (player.hasPermission("fly.admin") || (player.isOp())) {
			return;
		}
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		if (!(essentialsManager.isDisableFlyRegion(player.getLocation()))) {
			return;
		}
		player.setAllowFlight(false);
		player.sendMessage("§a[Fly] Fly desativado automaticamente por voc§ entrar em uma §rea de pvp.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.isOp()) {
			return;
		}
		Material material = event.getMaterial();
		if ((material == null) || (material == Material.AIR)) {
			return;
		}
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		if (essentialsManager.isDenyPlaceBlock(material.getId(), player.getWorld().getName())) {
			event.setCancelled(true);
			player.sendMessage("§cN§o § permitido colocar esse bloco neste mundo!");
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		if (!(player.getWorld().getName().equalsIgnoreCase("plotworld"))) {
			String[] blockedCommands = new String[] { "/sethome" };
			for (String cmd : blockedCommands) {
				if ((message.startsWith(cmd + " ")) || (message.equalsIgnoreCase(cmd))) {
					event.setCancelled(true);
					player.sendMessage("§cVoc§ n§o pode definir homes nesse mundo.");
					return;
				}
			}
		}
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		if (!essentialsManager.hasInScreenShare(player)) {
			return;
		}
		event.setCancelled(true);
		player.sendMessage("§cVoc§ n§o pode executar comandos na ScreenShare!");
	}
	
	@EventHandler
	public void onServerListPingEvent(ServerListPingEvent event) {
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		if (Bukkit.hasWhitelist()) {
			event.setMotd(essentialsManager.getMaintanceMotd());
			return;
		}
		event.setMotd(essentialsManager.getMotd());
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		Player player = event.getEntity();
		Player playerKiller = player.getKiller();
		if (playerKiller != null) {
			event.setDeathMessage("§7" + player.getName() + " foi morto por " + playerKiller.getName());
		}
	}
	
	@EventHandler
	public void onPrivateMessageEvent(PrivateMessageEvent event) {
		CommandSender sender = event.getSender();
		String senderName = sender.getName();
		CommandSender receiver = event.getReceiver();
		String receiverName = receiver.getName();
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		if (essentialsManager.getCloseTell(senderName.toLowerCase()) != null) {
			event.setCancelled(true);
			sender.sendMessage("§cVoc§ precisa ativar suas mensagens privadas para enviar mensagem para algu§m.");
			return;
		}
		String receiverReason = essentialsManager.getCloseTell(receiverName.toLowerCase());
		if (receiverReason == null) {
			return;
		}
		event.setCancelled(true);
		sender.sendMessage("§cO jogador encontra-se com o recebimento de suas mensagens privadas desativadas pelo motivo: §f" + receiverReason);
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		EssentialsManager essentialsManager = instance.getEssentialsManager();
		event.setRespawnLocation(essentialsManager.getSpawnWorld().getSpawnLocation());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		event.setCancelled(true);
	}
}