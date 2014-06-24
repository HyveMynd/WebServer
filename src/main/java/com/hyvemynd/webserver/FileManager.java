package com.hyvemynd.webserver;

import com.hyvemynd.webserver.http.MediaType;
import com.sun.xml.internal.xsom.impl.Ref;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeType;
import java.awt.*;
import java.io.File;

/**
 * Created by andresmonroy on 6/24/14.
 */
public class FileManager {

	private static final Logger log = LoggerFactory.getLogger(FileManager.class);
	private final String rootPath;
	private final String filePath;
	private final File file;
	private static final String WORKING_DIR = System.getProperty("user.dir");

	public FileManager(String webDir, String filePath){
		this.filePath = filePath;
		rootPath = WORKING_DIR + webDir + "/";
		file = new File(rootPath + filePath);
		log.debug("Full path is {}", rootPath + filePath);
	}

	public boolean fileExists(){
		return file.exists();
	}

	public String getContentType() {
		String ext = FilenameUtils.getExtension(filePath);
		log.debug("Extension is {}", ext);
		if (ext.equalsIgnoreCase("css")){
			return MediaType.CSS;
		} else if(ext.equalsIgnoreCase("html")){
			return MediaType.HTML;
		} else if (ext.equalsIgnoreCase("jpg")){
			return MediaType.JPEG;
		} else if(ext.equalsIgnoreCase("js")){
			return MediaType.JAVASCRIPT;
		} else {
			return MediaType.TEXT;
		}
	}

	public long getContentLength(){
		return file.length();
	}

	public File getFile(){
		return file;
	}
}
