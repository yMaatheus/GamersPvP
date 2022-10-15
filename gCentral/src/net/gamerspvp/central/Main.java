package net.gamerspvp.central;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.gamerspvp.central.cargos.CargosManager;
import net.gamerspvp.central.database.DataCenterManager;
import net.gamerspvp.central.statusservers.StatusServersManager;
import net.gamerspvp.central.utils.BungeeConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import redis.clients.jedis.JedisPool;

@Getter
public class Main extends Plugin {
	
	@Getter
	private static Main instance;
	private DataCenterManager dataCenterManager;
	
	private static HashSet<Runnable> runLoopAsync;
	private JedisPool jedisPool;
	private ScheduledTask scheduledTask;
	
	private CargosManager cargosManager;
	private StatusServersManager statusServersManager;
	
	public void onEnable() {
		instance = this;
		System.out.println(instance.getDataFolder());
		this.dataCenterManager = new DataCenterManager(new BungeeConfig("database.yml", this).getConfig());
		this.jedisPool = dataCenterManager.getRedis().getJedisPool();
		runLoopAsync = new HashSet<Runnable>();
		this.scheduledTask = BungeeCord.getInstance().getScheduler().schedule(this, () -> {
			if (runLoopAsync == null) {
				return;
			}
			if (runLoopAsync.isEmpty()) {
				return;
			}
			if (jedisPool == null) {
				return;
			}
			if (jedisPool.isClosed()) {
				return;
			}
			for (Runnable runnable : runLoopAsync) {
				runnable.run();
			}
		}, 4, 1, TimeUnit.SECONDS);
		
		this.cargosManager = new CargosManager(this);
		this.statusServersManager = new StatusServersManager(this);
		
		new GlobalListeners(this);
		
		runLoopAsync(() -> {
			statusServersManager.updateStatusServers();
			Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			if ((hour == 6) && (minute == 0)) {
				System.out.println("[AutoTasks] Executando shutdown...");
				getProxy().stop("Reinicio automático!");
			}
		});
	}
	
	@Override
	public void onDisable() {
	 runLoopAsync = null;
	 //BungeeCord.getInstance().getScheduler().cancel(scheduledTask);
		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}
	
	public void runLoopAsync(Runnable runnable) {
		runLoopAsync.add(runnable);
	}
	
	public void runAsync(Runnable runnable) {
		BungeeCord.getInstance().getScheduler().runAsync(this, runnable);
	}
}