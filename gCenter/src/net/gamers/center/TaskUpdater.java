package net.gamers.center;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.JedisPool;

public class TaskUpdater {
	
	private HashSet<Runnable> runLoopAsync;
	private JedisPool jedisPool;
	
	public TaskUpdater() {
		this.runLoopAsync = new HashSet<Runnable>();
		this.jedisPool = Main.getInstance().getDataCenter().getRedis().getJedisPool();
	}
	
	public void init() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					if (runLoopAsync == null) {
						return;
					}
					if (runLoopAsync.isEmpty()) {
						return;
					}
					if (jedisPool == null || jedisPool.isClosed()) {
						return;
					}
					for (Runnable runnable : runLoopAsync) {
						runnable.run();
					}
					// System.out.println("Teste agendador");
					try {
						Thread.sleep(TimeUnit.SECONDS.toMillis(1));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, "Task Updater").start();
	}
	
	public void add(Runnable runnable) {
		runLoopAsync.add(runnable);
	}
	
	public void remove(Runnable runnable) {
		runLoopAsync.remove(runnable);
	}
}