package net.gamers.gladiador.mito;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.gamers.gladiador.Main;
import net.gamerspvp.commons.bukkit.utils.Utils;

public class MitoManager {
	
	private Main instance;
	
	private FileConfiguration config;
	@Getter
	private String mito;
	
	public MitoManager(FileConfiguration config, Main instance) {
		this.instance = instance;
		this.config = config;
		this.mito = config.getString("mitoAtual");
		Utils.registerCommand(new SetMitoCommand(instance), instance, "setmito");
		Utils.registerCommand(new Command("mito") {
			@Override
			public boolean execute(CommandSender sender, String arg, String[] args) {
				sender.sendMessage("§aMito atual: §f" + mito);
				return false;
			}
		}, instance, "mito");
		new MitoListener(instance);
	}
	
	public void setNewMito(Player player) {
		String playerName = player.getName();
		Location location = player.getLocation();
		World world = player.getWorld();
		Bukkit.broadcastMessage("§5§l[MITO] §f" + playerName + "§7 é o novo mito do §c§lPVP!");
		world.strikeLightningEffect(location);
		mito = playerName;
		config.set("mitoAtual", playerName);
		saveConfig();
	}
	
	public boolean isMito(String playerName) {
		if (mito.equalsIgnoreCase(playerName)) {
			return true;
		}
		return false;
	}
	
	private void saveConfig() {
		try {
			config.save(new File(instance.getDataFolder(), "mito.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}