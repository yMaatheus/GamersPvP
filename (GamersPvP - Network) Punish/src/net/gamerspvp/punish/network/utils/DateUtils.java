package net.gamerspvp.punish.network.utils;

import java.text.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.regex.*;

public class DateUtils {
 public static String formatCurrency(double n, int iteration) {
	if (n <= 999.0) {
	 return String.valueOf((int) n);
	}
	char[] c = { 'k', 'm', 'b', 't' };
	double d = (long) n / 100L / 10.0;
	boolean isRound = d * 10.0 % 10.0 == 0.0;
	return (d < 1000.0) ? new StringBuilder().append((d > 99.9 || isRound || (!isRound && d > 9.99)) ? Integer.valueOf((int) d * 10 / 10) : new StringBuilder(String.valueOf(d)).toString()).append(c[iteration]).toString() : formatCurrency(d, iteration + 1);
 }

 public static String fromInt(int time) {
	if (time >= 3600) {
	 int hours = time / 3600;
	 int minutes = time % 3600 / 60;
	 int seconds = time % 3600 % 60;
	 return String.valueOf((hours < 10) ? "0" : "") + hours + ":" + ((minutes < 10) ? "0" : "") + minutes + ":"
		 + ((seconds < 10) ? "0" : "") + seconds;
	}
	int minutes2 = time / 60;
	int seconds2 = time % 60;
	return String.valueOf(minutes2) + ":" + ((seconds2 < 10) ? "0" : "") + seconds2;
 }

 public static String fromHourAndDate() {
	TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
	Calendar calendar = Calendar.getInstance(tz);
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	return sdf.format(calendar.getTime());
 }

 public static String fromDateAndHour() {
	TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
	Calendar calendar = Calendar.getInstance(tz);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	return sdf.format(calendar.getTime());
 }

 public static String fromDate() {
	TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
	Calendar calendar = Calendar.getInstance(tz);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	return sdf.format(calendar.getTime());
 }

 public static String fromHour() {
	TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
	Calendar calendar = Calendar.getInstance(tz);
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	return sdf.format(calendar.getTime());
 }

 private static String fromLong(String restingTime, long lenth) {
	int days = (int) TimeUnit.SECONDS.toDays(lenth);
	long hours = TimeUnit.SECONDS.toHours(lenth) - days * 24;
	long minutes = TimeUnit.SECONDS.toMinutes(lenth) - TimeUnit.SECONDS.toHours(lenth) * 60L;
	long seconds = TimeUnit.SECONDS.toSeconds(lenth) - TimeUnit.SECONDS.toMinutes(lenth) * 60L;
	String totalDay = String.valueOf(days) + ((days > 1) ? " dias " : " dia ");
	String totalHours = String.valueOf(hours) + ((hours > 1L) ? " horas " : " hora ");
	String totalMinutes = String.valueOf(minutes) + ((minutes > 1L) ? " minutos " : " minuto ");
	String totalSeconds = String.valueOf(seconds) + ((seconds > 1L) ? " segundos." : " segundo");
	if (days == 0) {
	 totalDay = "";
	}
	if (hours == 0L) {
	 totalHours = "";
	}
	if (minutes == 0L) {
	 totalMinutes = "";
	}
	if (seconds == 0L) {
	 totalSeconds = "";
	}
	restingTime = String.valueOf(totalDay) + totalHours + totalMinutes + totalSeconds;
	restingTime = restingTime.trim();
	if (restingTime.equals("")) {
	 restingTime = "0 segundos";
	}
	return restingTime;
 }

 public static String formatDifference(long time) {
	String t = "";
	long totalLenth = time;
	long timeLefting = totalLenth - System.currentTimeMillis();
	long seconds = timeLefting / 1000L;
	t = fromLong(t, seconds);
	return t;
 }

 public static long parseDateDiff(String time, boolean future) throws Exception {
	Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",2);
	Matcher m = timePattern.matcher(time);
	int years = 0;
	int months = 0;
	int weeks = 0;
	int days = 0;
	int hours = 0;
	int minutes = 0;
	int seconds = 0;
	boolean found = false;
	while (m.find()) {
	 if (m.group() != null) {
		if (m.group().isEmpty()) {
		 continue;
		}
		for (int i = 0; i < m.groupCount(); ++i) {
		 if (m.group(i) != null && !m.group(i).isEmpty()) {
			found = true;
			break;
		 }
		}
		if (!found) {
		 continue;
		}
		if (m.group(1) != null && !m.group(1).isEmpty()) {
		 years = Integer.parseInt(m.group(1));
		}
		if (m.group(2) != null && !m.group(2).isEmpty()) {
		 months = Integer.parseInt(m.group(2));
		}
		if (m.group(3) != null && !m.group(3).isEmpty()) {
		 weeks = Integer.parseInt(m.group(3));
		}
		if (m.group(4) != null && !m.group(4).isEmpty()) {
		 days = Integer.parseInt(m.group(4));
		}
		if (m.group(5) != null && !m.group(5).isEmpty()) {
		 hours = Integer.parseInt(m.group(5));
		}
		if (m.group(6) != null && !m.group(6).isEmpty()) {
		 minutes = Integer.parseInt(m.group(6));
		}
		if (m.group(7) != null && !m.group(7).isEmpty()) {
		 seconds = Integer.parseInt(m.group(7));
		 break;
		}
		break;
	 }
	}
	if (!found) {
	 throw new Exception("Illegal Date");
	}
	if (years > 20) {
	 throw new Exception("Illegal Date");
	}
	Calendar c = new GregorianCalendar();
	if (years > 0) {
	 c.add(1, years * (future ? 1 : -1));
	}
	if (months > 0) {
	 c.add(2, months * (future ? 1 : -1));
	}
	if (weeks > 0) {
	 c.add(3, weeks * (future ? 1 : -1));
	}
	if (days > 0) {
	 c.add(5, days * (future ? 1 : -1));
	}
	if (hours > 0) {
	 c.add(11, hours * (future ? 1 : -1));
	}
	if (minutes > 0) {
	 c.add(12, minutes * (future ? 1 : -1));
	}
	if (seconds > 0) {
	 c.add(13, seconds * (future ? 1 : -1));
	}
	return c.getTimeInMillis();
 }
}
