package dev.gamerspvp.auth.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class FilterManager {
	
	public FilterManager(String... commands) {
		((Logger) LogManager.getRootLogger()).addFilter(new Filter() {
			public Filter.Result filter(LogEvent event) {
				String message = event.getMessage().toString();
				for (String command : commands) {
					if (message.contains(command)) {
						return Filter.Result.DENY;
					}
				}
				return null;
			}
			public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object arg3, Throwable arg4) {
				return null;
			}
			public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message arg3, Throwable arg4) {
				return null;
			}
			public Filter.Result getOnMatch() {
				return null;
			}
			public Filter.Result getOnMismatch() {
				return null;
			}
			@Override
			public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object... arg4) {
				return null;
			}
		});
	}
}