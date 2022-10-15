package net.gamers.p4free.reportes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.gamers.p4free.Main;
import net.gamers.p4free.reportes.commands.ReportCommand;
import net.gamers.p4free.reportes.commands.ReportsCommand;
import net.gamerspvp.commons.bukkit.utils.MakeItem;
import net.gamerspvp.commons.network.utils.TimeManager;

public class ReportManager {
	
	private HashMap<Player, Report> cache;
	private HashMap<String, Long> cooldown;
	private Inventory inventory;
	
	public ReportManager(Main instance) {
		//this.instance = instance;
		this.cache = new HashMap<Player, Report>();
		this.cooldown = new HashMap<String, Long>();
		this.inventory = Bukkit.createInventory(null, 6 * 9, "§7Reports");
		new ReportCommand(instance);
		new ReportsCommand(instance);
		
		new ReportListener(instance);
	}
	
	public void executeReport(Player player, Player reporter, String reason) {
		String reporterName = reporter.getName().toLowerCase();
		if (cooldown.get(reporterName) != null) {
			if (cooldown.get(reporterName) > System.currentTimeMillis()) {
				reporter.sendMessage("§cAcalme-se rapaz, aguarde §f" + TimeManager.getTimeEnd(cooldown.get(reporterName))  + " §cpara efetuar uma nova den§ncia.");
				return;
			}
		}
		Report report = null;
		if (cache.get(player) == null) {
			report = new Report(player);
			cache.put(player, report);
		}
		report = cache.get(player);
		if (report.getReporters().get(reporter.getName()) != null) {
			reporter.sendMessage("§cVoc§ j§ reportou esse jogador.");
			return;
		}
		report.getReporters().put(reporter.getName(), reason);
		report.setReports(report.getReports() + 1);
		if (report.getReports() >= 3) {
			warnReport(report);
		}
		cache.put(player, report);
		cooldown.put(reporterName, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(120));
		updateInventory();
		reporter.sendMessage("§aDen§ncia enviada com §xito!");
	}
	
	public void warnReport(Report report) {
		String reporters = report.getReporters().toString().replace("{", "").replace("}", "");
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.hasPermission("reportes.moderador")) {
				onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 20, 1);
				onlinePlayer.sendMessage("");
				onlinePlayer.sendMessage("§c§ Um jogador est§ recebendo muitas den§ncias!");
				onlinePlayer.sendMessage("§c§ Jogador acusado: §f" + report.getPlayerName());
				onlinePlayer.sendMessage("§c§ Den§ncias: §f" + reporters);
				onlinePlayer.sendMessage("");
			}
		}
	}
	
	public void updateInventory() {
		// Get all report's
		// Adicionar reports em ordem de mairo quantidade de denuncias
		if (inventory == null) {
			inventory = Bukkit.createInventory(null, 6 * 9, "§7Reports");
		}
		inventory.clear();
		if (cache.isEmpty()) {
			return;
		}
		List<Report> convert = new ArrayList<>();
		convert.addAll(cache.values());
		Collections.sort(convert, new Comparator<Report>() {
			@Override
			public int compare(Report pt1, Report pt2) {
				Integer f1 = pt1.getReports();
				Integer f2 = pt2.getReports();
				return f2.compareTo(f1);
			}
		});
		if (convert.size() > 54) {
			convert = convert.subList(0, 54);
		}
		for (int a = 0; a < convert.size(); a++) {
			Report report = convert.get(a);
			String reporters = report.getReporters().toString().replace("{", "").replace("}", "");
			int totalReports = report.getReports();
			String[] lore = new String[] {"","§a§ §fDen§ncias: " + reporters, "§a§ §eTotal De Den§ncias: " + totalReports, "", "§e§ §fClique com o esquerdo para teleportar-se", "§e§ §fClique com o direito para deletar"};
			ItemStack skull = new MakeItem(Material.SKULL_ITEM).setName("§a" + report.getPlayerName()).setSkullOwner(report.getPlayerName()).addLore(lore).buildhead();
			inventory.addItem(skull);
		}
		if (!(inventory.getViewers().isEmpty())) {
			for (HumanEntity viewers : inventory.getViewers()) {
				viewers.openInventory(inventory);
			}
		}
	}
	
	public void removeReport(Player player) {
		cache.remove(player);
		updateInventory();
	}
	
	public Report getCache(Player player) {
		return cache.get(player);
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}