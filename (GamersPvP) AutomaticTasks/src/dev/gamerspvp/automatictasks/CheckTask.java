package dev.gamerspvp.automatictasks;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.automatictasks.shutdown.ShutdownManager;

public class CheckTask extends BukkitRunnable {
	
	@Override
	public void run() {
		Main instance = Main.getInstance();
		Calendar calendar = GregorianCalendar.getInstance(instance.getTimeZone());
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		//System.out.println("Dia: " + day + " às: " + hour + ":" + minute);
		for (Thread thread : instance.getThreads()) {
			int checkDay = thread.getDay();
			if (checkDay != 0) {
				if (day != checkDay) {
					continue;
				}
			}
			if (hour != thread.getHour()) {
				continue;
			}
			if (minute != thread.getMinute()) {
				continue;
			}
			String command = thread.getCommand();
			System.out.println("[AutoTasks] Executando o comando '" + command + "'...");
			if (command.equalsIgnoreCase("shutdown")) {
				ShutdownManager shutdownManager = instance.getShutdownManager();
				shutdownManager.executeRestart();
				return;
			}
			new BukkitRunnable() {
				
				@Override
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}.runTask(instance);
		}
	}
}