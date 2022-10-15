package dev.gamerspvp.reportes;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import dev.gamerspvp.reportes.commands.ReportCommand;
import dev.gamerspvp.reportes.commands.ReportsCommand;
import dev.gamerspvp.reportes.models.Report;
import dev.gamerspvp.reportes.utils.MakeItem;
import dev.gamerspvp.reportes.utils.TimeFormater;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ReportManager {
	
	private Main instance;
	private JDA jda;
	
	private HashMap<String, Report> cache;
	private HashMap<String, Long> cooldown;
	private Inventory inventory;
	
	public ReportManager(Main instance) {
		this.instance = instance;
		try {
			JDABuilder jdaBuilder = JDABuilder.createDefault("");
			jdaBuilder.setActivity(Activity.of(ActivityType.STREAMING, "O yMatheus § um lindo!", "https://loja.gamerspvp.net/"));
			this.jda = jdaBuilder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		this.cache = new HashMap<String, Report>();
		this.cooldown = new HashMap<String, Long>();
		this.inventory = Bukkit.createInventory(null, 6 * 9, "§7Reports");
		new ReportListener(instance);
		instance.registerCommand(new ReportCommand(instance), "report");
		instance.registerCommand(new ReportsCommand(instance), "reports");
	}
	
	public void executeReport(Player player, Player reporter, String reason) {
		String reporterName = reporter.getName().toLowerCase();
		if (cooldown.get(reporterName) != null) {
			if (cooldown.get(reporterName) < System.currentTimeMillis()) {
				reporter.sendMessage("§cAcalme-se rapaz, aguarde §f" + TimeFormater.formatOfEnd(cooldown.get(reporterName))  + " §cpara efetuar uma nova den§ncia.");
				return;
			}
		}
		String name = player.getName().toLowerCase();
		Report report = null;
		if (cache.get(name) == null) {
			report = new Report(player);
			cache.put(name, report);
		}
		report = cache.get(name);
		if (report.getReporters().get(reporter.getName()) != null) {
			reporter.sendMessage("§cVoc§ j§ reportou esse jogador.");
			return;
		}
		report.getReporters().put(reporter.getName(), reason);
		report.setReports(report.getReports() + 1);
		if (report.getReports() >= 3) {
			warnReport(report);
		}
		cache.put(name, report);
		cooldown.put(reporterName, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(120));
		updateInventory();
		reporter.sendMessage("§aDen§ncia enviada com exito!");
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
		if (jda == null) {
			return;
		}
		Guild guild = jda.getGuildById("368594399862587392");
		if (guild == null) {
			return;
		}
		TextChannel textChannel = guild.getTextChannelById("768437233954193438");
		if (textChannel == null) {
			return;
		}
		EmbedBuilder embedbuilder = new EmbedBuilder();
		embedbuilder.setTitle("**NOVO REPORT**");
		embedbuilder.addField("§ Um jogador est§ recebendo muitas den§ncias!", "Jogador acusado: " + report.getPlayerName(), false);
		//embedbuilder.setFooter("Por: " + jda.getSelfUser().getName(), jda.getSelfUser().getAvatarUrl());
		embedbuilder.setAuthor("GamersPvP", "https://loja.gamerspvp.net/", jda.getSelfUser().getAvatarUrl());
		embedbuilder.setColor(Color.GREEN);
		textChannel.sendMessage(embedbuilder.build()).queue();
		textChannel.sendFile(new File("plugins/" + instance.getDescription().getName() + "/audios/audio1.wav")).queue();
		User lucasK1ller = guild.getMemberById("322038284803112960").getUser();
		if (lucasK1ller == null) {
			return;
		}
		((PrivateChannel)lucasK1ller.openPrivateChannel().complete()).sendMessage(lucasK1ller.getAsMention() + " Um jogador est§ recebendo muitas den§ncias, vai ver o Discord seu corno!").queue();
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
	
	public void removeReport(String targetName) {
		cache.remove(targetName.toLowerCase());
		updateInventory();
	}
	
	public Report getCache(String playerName) {
		return cache.get(playerName.toLowerCase());
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}