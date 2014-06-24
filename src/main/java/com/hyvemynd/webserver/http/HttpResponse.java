package com.hyvemynd.webserver.http;

import com.hyvemynd.webserver.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates Http response
 */
public class HttpResponse {

	public static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	public static final String HTTP_VERSION = "HTTP/1.1";
	public static final String CRLF = "\r\n";
	private String status;
	private Map<String, String> headers;

	public HttpResponse(String status){
		this.status = status;
		headers = new HashMap<String, String>();
	}

	/**
	 * Returns a properly formatted HTTP 400 Bad Request
	 * @return
	 */
	public static HttpResponse get400() {
		return new HttpResponse(HttpStatusCode.BAD_REQUEST);
	}

	/**
	 * Returns a properly formatted HTTP 404 Not Found
	 * @return
	 */
	public static HttpResponse get404(){
		return new HttpResponse(HttpStatusCode.NOT_FOUND);
	}

	/**
	 * Returns a properly formatted HTTP 200 OK
	 * @return
	 */
	public static HttpResponse get200(FileManager fileManager){
		HttpResponse res = new HttpResponse(HttpStatusCode.OK);
		res.addContentType(fileManager.getContentType());
		res.addContentLength(fileManager.getContentLength());
		return res;
	}

	/**
	 * Returns a properly formatted HTTP 501 Not Implemented
	 * @return
	 */
	public static HttpResponse get501() {
		return new HttpResponse(HttpStatusCode.NOT_IMPLEMENTED);
	}

	private void addContentType(String type){
		addHeader("Content-type", type);
	}

	private void addContentLength(long length){
		addHeader("Content-length", Long.toString(length));
	}

	public void addHeader(String key, String value){
		headers.put(key.toUpperCase(), value);
	}

	public String getHeader(String key){
		return headers.get(key.toUpperCase());
	}

	public String getStatus() {
		return status;
	}

	private void addServerAndTimeHeaders(){
		addHeader("Server", "WebServer");
		addHeader("Date", new Date().toString());
	}

	public String toString() {
		String response = String.format("%s %s", HTTP_VERSION, status) + CRLF;
		addServerAndTimeHeaders();
		for (String headerKey : headers.keySet()){
			String value = headers.get(headerKey);
			response += headerKey + ": " + value + CRLF;
		}
		response += CRLF;
		log.trace("RESPONSE:\n{}", response);
		return response;
	}
}
