package eu.apenet.dashboard.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;

/**
 * This class is in charge of extract zip files
 */
public class ZipManager {
	private final static Logger LOGGER = Logger.getLogger(ZipManager.class);
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
			LOGGER.debug("Zip extracted SUCCESS! with normal part");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//trying apache algorithm
			extractZip(pathFile);
			LOGGER.info("Zip extracted SUCCESS! with second part");
		}
	}
	
    public static void extractZip(String archivePath) {
        File archiveFile = new File(archivePath);
        File unzipDestFolder = null;
 
        try {
            unzipDestFolder = new File(archiveFile.getPath());
            String[] zipRootFolder = new String[]{null};
            unzipFolder(archiveFile, unzipDestFolder, zipRootFolder);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    private static boolean unzipFolder(File archiveFile, File zipDestinationFolder, String[] outputZipRootFolder) {
 
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(archiveFile);
            byte[] buf = new byte[BUFFER_SIZE];
 
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entries.nextElement();
                String name = APEnetUtilities.convertToFilename(zipEntry.getName());
                name = name.replace("\\", "/");
                int i = name.indexOf("/");
                if (i > 0) {
                    outputZipRootFolder[0] = name.substring(0, i);
                }
                name = name.substring(i + 1);
 
                File destinationFile = new File(zipDestinationFolder.getParentFile().getPath(), name);
                if (name.endsWith("/")) {
                    if (!destinationFile.isDirectory() && !destinationFile.mkdirs()) {
                        LOGGER.error("Error creating temp directory:" + destinationFile.getPath());
                        return false;
                    }
                    continue;
                } else if (name.indexOf("/") != -1) {
                    File parentFolder = destinationFile.getParentFile();
                    if (!parentFolder.isDirectory()) {
                        if (!parentFolder.mkdirs()) {
                            LOGGER.error("Error creating temp directory:" + parentFolder.getPath());
                            return false;
                        }
                    }
                }
 
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(destinationFile);
                    int n;
                    InputStream entryContent = zipFile.getInputStream(zipEntry);
                    while ((n = entryContent.read(buf)) != -1) {
                        if (n > 0) {
                            fos.write(buf, 0, n);
                        }
                    }
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Unzip failed:" + e.getMessage());
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    LOGGER.error("Error closing zip file");
                }
            }
        }
        return false;
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
		FileOutputStream fos = new FileOutputStream(this.path + APEnetUtilities.convertToFilename(temp.getName()));
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
