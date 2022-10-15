package net.gamers.lobby;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import net.gamers.lobby.games.GamesManager;
import net.gamers.lobby.scoreboard.ScoreBoard;
import net.gamerspvp.commons.bukkit.CommonsBukkit;
import net.gamerspvp.commons.network.database.DataCenterManager;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main instance;
	
	private CommonsBukkit commons;
	private DataCenterManager dataCenter;
	
	private GamesManager gamesManager;
	private ScoreBoard scoreboard;
	
	private static HashSet<Runnable> tasks = new HashSet<>();
	
	@Override
	public void onEnable() {
		instance = this;
		try {
			this.commons = CommonsBukkit.getInstance();
			this.dataCenter = commons.getDataCenter();
			
			this.gamesManager = new GamesManager(this);
			this.scoreboard = new ScoreBoard(this);
			
			new GlobalListeners(this);
			task.runTaskTimerAsynchronously(instance, 9 * 20L, 3 * 20L);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
		//new HdCommand(this);
		/*Listener listener = new ListenForAll();
		RegisteredListener registeredListener = new RegisteredListener(listener, new EventExecutor() {
			@Override
			public void execute(Listener listener, Event event) throws EventException {
				if (event.getEventName().contains("MoveEvent") | event.getEventName().contains("Chunk")) {
					return;
				}
				System.out.println("" + event.getEventName());
			}
		}, EventPriority.NORMAL, this, false);
		for (HandlerList handler : HandlerList.getHandlerLists()) {
			handler.register(registeredListener);
		}*/
	}
	
	private BukkitRunnable task = new BukkitRunnable() {
		
		@Override
		public void run() {
			if (tasks.isEmpty()) {
				return;
			}
			tasks.forEach(i -> i.run());
		}
	};
	
	public void runAsyncLoopTask(Runnable runnable) {
		tasks.add(runnable);
	}
}