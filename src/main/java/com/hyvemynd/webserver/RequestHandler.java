package com.hyvemynd.webserver;

import com.hyvemynd.webserver.http.HttpMethods;
import com.hyvemynd.webserver.http.HttpRequest;
import com.hyvemynd.webserver.http.HttpResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by andresmonroy on 6/23/14.
 */
public class RequestHandler implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private final Socket client;
	private final String webRoot;

	public RequestHandler(Socket socket, String rootPath){
		client = socket;
		webRoot = rootPath;
	}

	private boolean isMethodValid(String method){
		return method.equalsIgnoreCase(HttpMethods.HEAD) || method.equalsIgnoreCase(HttpMethods.GET);
	}

	@Override
	public void run() {
		try {

			// Process the request
			HttpRequest req = new HttpRequest(client.getInputStream());

			// Check for bad request
			if (!req.parseRequest()){
				log.info("Bad request. Sending 400");
				send(HttpResponse.get400(), null);
				return;
			}

			// Check for an invalid verb
			if (!isMethodValid(req.getVerb())){
				log.info("Method not implemented. Sending 501");
				send(HttpResponse.get501(), null);
				return;
			}

			// Find the file
			String filePath = req.getUri();
			log.debug("File path is {}", filePath);
			FileManager  fileManager= new FileManager(webRoot, filePath);

			// File not found. Return 404
			if (!fileManager.fileExists()){
				log.info("File not found. Sending 404");
				send(HttpResponse.get404(), null);
				return;
			}

			// Send the HEAD response
			if (req.getVerb().equalsIgnoreCase(HttpMethods.HEAD)){
				log.info("Sending response headers.");
				send(HttpResponse.get200(fileManager), null);
				return;
			}

			// Send the GET response
			if (req.getVerb().equalsIgnoreCase(HttpMethods.GET)){
				log.info("Sending GET response.");
				send(HttpResponse.get200(fileManager), fileManager.getFile());
				return;
			}

		} catch (IOException e) {
			log.error("An Error has occurred while handling the request.", e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				log.error("Error closing client connection.", e);
			}
		}
	}

	public void send(HttpResponse res, File file) throws IOException {
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		if (file == null){
			out.writeBytes(res.toString());
			out.flush();
		} else {
			out.writeBytes(res.toString());
			IOUtils.write(FileUtils.readFileToByteArray(file), out);
			out.flush();
		}
	}

}
