package dev.gamerspvp.automatictasks;

public class Thread {
	
	private int day;
	private int hour;
	private int minute;
	private String command;
	
	public Thread(int day, int hour, int minute, String command) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.command = command;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public String getCommand() {
		return command;
	}
}