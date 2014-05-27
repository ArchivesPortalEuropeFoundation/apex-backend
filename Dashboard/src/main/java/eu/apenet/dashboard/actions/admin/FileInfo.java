package eu.apenet.dashboard.actions.admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileInfo {
	private static final int SIZE = 1024;
	private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); 

	private String filename;
	private String lastModified;
	private String filesize;
	
	public FileInfo(File file){
		filename = file.getName();
		long size = file.length();
		if (size > SIZE * SIZE) {
			filesize =  size / (SIZE * SIZE) + " MB";
		} else if (size > SIZE) {
			filesize = size / SIZE + " KB";
		} else {
			filesize =  size + " B";
		}
		lastModified = DATE_TIME.format(new Date(file.lastModified()));
	}
	public String getFilename() {
		return filename;
	}

	public String getLastModified() {
		return lastModified;
	}
	public String getFilesize() {
		return filesize;
	}
}
