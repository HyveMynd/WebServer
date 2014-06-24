package com.hyvemynd.webserver.logging;

import org.apache.log4j.*;

import java.io.IOException;

/**
 * Created by andresmonroy on 6/23/14.
 */
public class LoggingManager {

	private static Logger init(Level level){
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(level);
		return rootLogger;
	}

	public static void initConsoleLogging(Level level){
		Logger rootLogger = init(level);
		PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");
		rootLogger.addAppender(new ConsoleAppender(layout));
	}

	public static void initFileLogging(Level level, String logFilename) {
		Logger rootLogger = init(level);
		PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");

		try {
			RollingFileAppender fileAppender = new RollingFileAppender(layout, logFilename);
			rootLogger.addAppender(fileAppender);
		} catch (IOException e) {
			System.out.println("Failed to add appender!");
		}
	}

}