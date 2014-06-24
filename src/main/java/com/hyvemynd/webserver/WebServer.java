package com.hyvemynd.webserver;

import com.hyvemynd.webserver.logging.LoggingManager;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by andresmonroy on 6/23/14.
 */
public class WebServer {
	private static final Logger log = LoggerFactory.getLogger(WebServer.class);
	private static ServerSocket serverSocket;
	private static ExecutorService threadPool;
	private static final int NUM_THREADS = 10;
	private static final int DEFAULT_PORT = 1337;
	private static final String WEB_ROOT = "/wwwroot";

	/**
	 * Default initialization for the webserver.
	 */
	private static void initDefault() {
		try {
			LoggingManager.initConsoleLogging(Level.INFO);
			threadPool = Executors.newFixedThreadPool(NUM_THREADS);
			InetAddress localhost = InetAddress.getByName("127.0.0.1");
			serverSocket = new ServerSocket(DEFAULT_PORT, 0, localhost);
		} catch (IOException e) {
			log.error("Could not open socket on port {}: {}", DEFAULT_PORT, e);
			System.exit(1);
		}
	}

	/**
	 * Initialization based on the arguments passed into the program.
	 * @param args
	 */
	private static void init(String[] args){
		try {
			parseLogLevel(args[1]);
			int port = Integer.parseInt(args[0]);
			int threads = Integer.parseInt(args[2]);
			InetAddress localhost = InetAddress.getByName("127.0.0.1");
			log.debug("Init with port {}, level {}, {} threads", args[0], args[1], args[2]);
			threadPool = Executors.newFixedThreadPool(threads);
			serverSocket = new ServerSocket(port, 0, localhost);
		} catch (ArrayIndexOutOfBoundsException e){
			log.error("Must include both a port number, logging level, and number of threads");
			System.exit(1);
		} catch (NumberFormatException e) {
			log.error("Numbers must be a positive integers");
			System.exit(1);
		} catch (IllegalArgumentException e){
			log.error("{} is an illegal port number.", args[0]);
			System.exit(1);
		} catch (IOException e) {
			log.error("An error occurred while initializing server.", e);
			System.exit(1);
		}
	}

	private static void parseLogLevel(String level) {
		if (level.equalsIgnoreCase("Error")){
			LoggingManager.initConsoleLogging(Level.ERROR);
		} else if (level.equalsIgnoreCase("Warn")){
			LoggingManager.initConsoleLogging(Level.WARN);
		} else if (level.equalsIgnoreCase("Info")){
			LoggingManager.initConsoleLogging(Level.INFO);
		} else if (level.equalsIgnoreCase("Debug")){
			LoggingManager.initConsoleLogging(Level.DEBUG);
		} else if (level.equalsIgnoreCase("Trace")){
			LoggingManager.initConsoleLogging(Level.TRACE);
		} else {
			log.error("{} is not a valid logging level.", level);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		if (args.length == 0){
			initDefault();
		} else {
			init(args);
		}

		while (true){
			try {
				log.warn("Use Ctrl-C to exit, stop, and close the server.");
				log.info("Awaiting request...");
				synchronized (threadPool){
					threadPool.execute(new RequestHandler(serverSocket.accept(), WEB_ROOT));
				}
			} catch (IOException e) {
				log.error("Could not accept request.", e);
			}
		}
	}

}
