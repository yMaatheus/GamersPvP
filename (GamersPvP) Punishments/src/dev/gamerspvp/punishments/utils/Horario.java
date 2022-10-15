package dev.gamerspvp.punishments.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Horario {
	
	public static String getHourAndDate() {
		TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
		Calendar calendar = GregorianCalendar.getInstance(tz);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		return sdf.format(calendar.getTime());
	}
}