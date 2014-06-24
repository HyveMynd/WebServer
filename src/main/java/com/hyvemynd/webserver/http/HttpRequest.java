package com.hyvemynd.webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates a valid http request based on http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
 */
public class HttpRequest {

	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class.getName());
	private static final String CRLF = "\r\n";
	private String method;
	private String URI;
	private String version;
	private Map<String, String> headers;
	private BufferedReader fromClient;

	public HttpRequest(InputStream in) {
		headers = new HashMap<String, String>();
		fromClient = new BufferedReader(new InputStreamReader(in));
	}

	/**
	 * Parse the http request. Returns true if the request is valid.
	 * @return True if the request is valid
	 */
	public boolean parseRequest(){
		try {
			log.debug("Parsing request line.");
			parseRequestLine(fromClient.readLine());
			log.debug("Parsing request headers.");
			parseHeaders(fromClient);
			return true;
		} catch (IOException e) {
			log.error("Error parsing request.", e);
			return false;
		}
	}

	/**
	 * Parse the request line of a http request. Checks for validity
	 * @param line the request line
	 * @throws IOException When any part of the request line is invalid
	 */
	private void parseRequestLine(String line) throws IOException {
		if (line == null){
			throw new IOException("Request line is null");
		}

		String[] tmp = line.split(" ");
		if (tmp.length != 3){
			log.warn("Invalid Request line: {}", line);
			throw new IOException("Invalid Request line.");
		}

		method = tmp[0]; // http method
		URI =  tmp[1]; // URI
		version = tmp[2]; // HTTP HTTP_VERSION
		log.info("Request line is {} {} {}", method, URI, version);

		if (!version.startsWith("http/") && !version.startsWith("HTTP/")){
			log.warn("Invalid Request line: {}", line);
			throw new IOException("Invalid Request line.");
		}
	}

	/**
	 * Parse the headers of a http request.
	 * @param in
	 * @throws IOException
	 */
	private void parseHeaders(BufferedReader in) throws IOException {
		String line = in.readLine();
		while(line.length() != 0){
			String[] header = line.split(":");
			headers.put(header[0].toUpperCase(), header[1]);
			line = in.readLine();
		}
		log.trace("Request Headers: {}", headers.toString());
	}

	public String getUri() {
		return URI;
	}

	public String getMethod() {
		return method;
	}

	public String getVersion() {
		return version;
	}

	public String getHeader(String headerKey){
		return headers.get(headerKey.toUpperCase());
	}

	@Override
	public String toString() {
		String request = String.format("%s %s %s\r\n", method, URI, version);
		for (String headerKey : headers.keySet()){
			String value = getHeader(headerKey);
			request += headerKey + ": " + value + CRLF;
		}
		request += CRLF;
		log.trace("HTTPRequest:\n{}", request);
		return request;
	}
}