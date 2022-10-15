package net.gamers.center.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;

import net.gamers.center.Main;
import net.gamers.center.MessageEvent;
import net.gamers.center.database.MySQL;

public class LogManager {
	
	public static void onMessageEvent(MessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("log_report")) {
			new Thread(() -> {
				LogReport logReport = new Gson().fromJson(event.getMessage(), LogReport.class);
				try {
					String date = convertToFileName(logReport.getDate());
					File fileLog = new File(new File(".").getCanonicalPath(), date + "-" + logReport.getAuthor() + "-log.txt");
					if (!fileLog.exists()) {
						fileLog.createNewFile();
					}
					FileWriter fileWriter = new FileWriter(fileLog, true);
					PrintWriter printWriter = new PrintWriter(fileWriter);
					List<Log> logs = getReport(getQuery(logReport));
					for (int a = 0; a < logs.size(); a++) {
						Log log = logs.get(a);
						printWriter.println(convertToLog(log.getFlagTime()) + " (" + log.getKey() + ") " + log.getText() + " Reason: " + log.getReason());
					}
					printWriter.flush();
					printWriter.close();
					executeZipFile(date + "-" + logReport.getAuthor(), fileLog);
					Main.getInstance().getDiscordManager().sendLogReportFile(date + "-" + logReport.getAuthor() + ".zip");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).run();
		}
	}
	
	public static List<Log> getReport(String query) throws Exception {
		List<Log> logs = new ArrayList<Log>();
		MySQL mysql = Main.getInstance().getDataCenter().getMysql();
		PreparedStatement ps = mysql.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String date = rs.getString("date");
			String key = rs.getString("key");
			String playerName = rs.getString("playerName");
			String text = rs.getString("log");
			String reason = rs.getString("reason");
			long flagTime = rs.getLong("flagTime");
			
			logs.add(new Log(date, key, playerName, text, reason, flagTime));
		}
		rs.close();
		ps.close();
		Collections.sort(logs, new Comparator<Log>() {
		    @Override
		    public int compare(Log pt1, Log pt2) {
		        Long f1 = pt1.getFlagTime();
		        Long f2 = pt2.getFlagTime();
		        return f1.compareTo(f2);
		    }
		});
		return logs;
	}
	
	public static void executeZipFile(String zipName, File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(zipName + ".zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		zipOut.close();
		fis.close();
		fos.close();
	}
	
	public static String convertToLog(long arg) {
		Date dt = new Date(arg);
		DateFormat df = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
		df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		return df.format(dt);
	}
	
	public static String convertToFileName(long arg) {
		Date dt = new Date(arg);
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		return df.format(dt);
	}
	
	public static String getQuery(LogReport logReport) {
		String type = logReport.getType();
		long currentMillis = System.currentTimeMillis();
		long lastDays = currentMillis - TimeUnit.DAYS.toMillis(logReport.getDays());
		if (type.equalsIgnoreCase("2")) {
			return "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "' AND `key`='" + logReport.getKey() + "';";
		} else if (type.equalsIgnoreCase("3")) {
			return "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "' AND `playerName`='" + logReport.getPlayerName() + "';";
		} else if (type.equalsIgnoreCase("4")) {
			String key = logReport.getKey();
			String playerName = logReport.getPlayerName();;
			return "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "' AND `key`='" + key + "' AND `playerName`='" + playerName + "';";
		}
		return "SELECT * FROM `logs` WHERE `flagTime` BETWEEN '" + lastDays + "' AND '" + currentMillis + "';";
	}
}