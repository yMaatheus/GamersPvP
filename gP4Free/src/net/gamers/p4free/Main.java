package net.gamers.p4free;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import lombok.Getter;
import net.gamers.p4free.clearlag.ClearLagManager;
import net.gamers.p4free.essentials.EssentialsManager;
import net.gamers.p4free.game.GameManager;
import net.gamers.p4free.kit.KitManager;
import net.gamers.p4free.reportes.ReportManager;
import net.gamers.p4free.scoreboard.ScoreboardManager;
import net.gamers.p4free.utils.LocationsManager;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.bukkit.utils.BukkitConfig;
import net.gamerspvp.commons.network.database.DataCenterManager;

@Getter
public class Main extends JavaPlugin {
	
	@Getter
	private static Main instance;
	private CommonsBukkit commons;
	private DataCenterManager dataCenterManager;
	private WorldGuardPlugin worldGuard;
	private LocationsManager locationsManager;
	
	private ClearLagManager clearLagManager;
	private EssentialsManager essentialsManager;
	private GameManager gameManager;
	private KitManager kitManager;
	private ReportManager reportManager;
	private ScoreboardManager scoreboardManager;
	
	@Override
	public void onEnable() {
		instance = this;
		try {
			this.commons = CommonsBukkit.getInstance();
			this.dataCenterManager = commons.getDataCenter();
			this.worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
			this.locationsManager = new LocationsManager(BukkitConfig.loadConfig("locations.yml", this), this);
			
			this.clearLagManager = new ClearLagManager(this);
			this.essentialsManager = new EssentialsManager(BukkitConfig.loadConfig("essentials.yml", this), this);
			this.gameManager = new GameManager(BukkitConfig.loadConfig("game.yml", this), this);
			this.reportManager = new ReportManager(this);
			this.kitManager = new KitManager(this);
			
			this.scoreboardManager = new ScoreboardManager(this);
			new GlobalListeners(this);
		} catch (Exception e) {
			System.out.println("[P4Free] Não foi possivel carregar os sistemas do plugin.");
			e.printStackTrace();
			Bukkit.shutdown();
			return;
		}
	}
	
	@Override
	public void onLoad() {
		instance = this;
	}
}