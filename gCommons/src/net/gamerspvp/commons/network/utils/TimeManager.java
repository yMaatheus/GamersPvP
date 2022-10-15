package net.gamerspvp.commons.network.utils;

import java.util.concurrent.TimeUnit;

public class TimeManager {
	
	public static long generateTime(String type, int time) {
		long Tempo = 0;
		if (type.equalsIgnoreCase("segundos") || type.contains("s")) {
			Tempo = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(time);
		}
		if (type.equalsIgnoreCase("dias") || type.contains("d")) {
			Tempo = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time);
		}
		if (type.equalsIgnoreCase("horas") || type.contains("h")) {
			Tempo = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(time);
		}
		if (type.equalsIgnoreCase("minutos") || type.contains("m")) {
			Tempo = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(time);
		}
		return Tempo;
	}
	
	public static String getTimeUntil(long epoch) {
		epoch -= System.currentTimeMillis();
		return getTime(epoch);
	}
	
	public static String getTime(long ms) {
		ms = (long) Math.ceil(ms / 1000.0);
		StringBuilder sb = new StringBuilder(40);
		if (ms / 31449600 > 0) {
			long years = ms / 31449600;
			sb.append(years + (years == 1 ? " ano " : " anos "));
			ms -= years * 31449600;
		}
		if (ms / 2620800 > 0) {
			long months = ms / 2620800;
			sb.append(months + (months == 1 ? " mês " : " meses "));
			ms -= months * 2620800;
		}
		if (ms / 604800 > 0) {
			long weeks = ms / 604800;
			sb.append(weeks + (weeks == 1 ? " semana " : " semanas "));
			ms -= weeks * 604800;
		}
		if (ms / 86400 > 0) {
			long days = ms / 86400;
			sb.append(days + (days == 1 ? " dia " : " dias "));
			ms -= days * 86400;
		}
		if (ms / 3600 > 0) {
			long hours = ms / 3600;
			sb.append(hours + (hours == 1 ? " hora " : " horas "));
			ms -= hours * 3600;
		}
		if (ms / 60 > 0) {
			long minutes = ms / 60;
			sb.append(minutes + (minutes == 1 ? " minuto " : " minutos "));
			ms -= minutes * 60;
		}
		if (ms > 0) {
			sb.append(ms + (ms == 1 ? " segundo " : " segundos "));
		}
		if (sb.length() > 1) {
			sb.replace(sb.length() - 1, sb.length(), "");
		} else {
			sb = new StringBuilder("Acabado");
		}
		return sb.toString();
	}
	
	public static String getTimeEnd(long ms) {
		return getTime(ms - System.currentTimeMillis());
	}
}