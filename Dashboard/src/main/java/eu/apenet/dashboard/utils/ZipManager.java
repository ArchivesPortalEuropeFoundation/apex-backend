package eu.apenet.dashboard.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;

/**
 * This class is in charge of extract zip files
 */
public class ZipManager {
	private final Logger log = Logger.getLogger(getClass());
	private static final Integer BUFFER_SIZE = 2048;
	private String path;
	private String pathFile;

	/** CONSTRUCTOR */
	public ZipManager() {
		this.path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR;
	}

	public ZipManager(String destination) {
		if (destination != null) {
			this.path = destination;
		}
	}

	/**
	 * It's the first call
	 * 
	 * @param pathFile
	 */
	public void unzip(String pathFile) {
		this.pathFile = pathFile;
		try {
			navigate(null, null);
		} catch (Exception e) {
			log.error(e);
		}// It's temporally until it's managed exceptions by Struts2
			// configuration
	}

	/**
	 * This method browse into pathFile and decide follow navigating (folder) or
	 * call to extract files
	 * 
	 * @param temp
	 * @param f
	 * @throws IOException
	 */
	private void navigate(ZipEntry temp, FileInputStream f) throws IOException {
		if (temp == null) { // It only go into the first time

			f = new FileInputStream(this.pathFile);
			ZipInputStream source = new ZipInputStream(f);

			while ((temp = source.getNextEntry()) != null) {
				if (temp.isDirectory()) {
					navigate(temp, f);
				} else {
					extract(temp, source);
				}
			}
			source.close();
		} else { // It's needed make a folder to extract data

			new File(this.path + temp.getName()).mkdir();
		}
	}

	/**
	 * This method extract data
	 * 
	 * @param temp
	 * @param source
	 * @throws IOException
	 */
	private void extract(ZipEntry temp, ZipInputStream source) throws IOException {

		byte data[] = new byte[BUFFER_SIZE];
		FileOutputStream fos = new FileOutputStream(this.path + temp.getName());
		BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);

		Integer size;
		while ((size = source.read(data, 0, BUFFER_SIZE)) != -1) {
			dest.write(data, 0, size);
		}
		// Close
		dest.flush();
		dest.close();

		fos.flush();
		fos.close();
	}

	public static void zip(String path, File file, ZipOutputStream zipOutputStream) throws IOException {

		byte[] buffer = new byte[BUFFER_SIZE]; // Create a buffer for copying
		int bytesRead;
		ZipEntry entry = new ZipEntry(path);
		zipOutputStream.putNextEntry(entry);
		FileInputStream fileInputStream = new FileInputStream(file); // Stream
																		// to
																		// read
																		// file
		while ((bytesRead = fileInputStream.read(buffer)) != -1) {
			zipOutputStream.write(buffer, 0, bytesRead);
		}
		zipOutputStream.closeEntry();
		fileInputStream.close();

	}
}
