package me.smith_61;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VOMLogger {

	public static final Logger VOM_LOGGER = Logger.getLogger("VOMLogger");
	
	public static void logError(Throwable error, String format, Object... args) {
		VOMLogger.VOM_LOGGER.log(Level.SEVERE, String.format(format, args), error);
	}
	
	public static void logInfo(String format, Object... args) {
		VOMLogger.logFormat(Level.INFO, format, args);
	}
	
	public static void logFormat(Level level, String format, Object... args) {
		VOMLogger.VOM_LOGGER.log(level, String.format(format, args));
	}
}
