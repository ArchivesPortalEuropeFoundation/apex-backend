package eu.apenet.dashboard.actions.admin;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ContentUtils;

public class ShowLogFilesAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1923466652464251319L;

	private static final Logger LOGGER = Logger.getLogger(ShowLogFilesAction.class);
	private String filename;
	private String baseDir = System.getProperty("catalina.base") + "/logs";

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String execute() throws Exception {
		File logDir = new File(baseDir);
		File[] ownfiles = logDir.listFiles(new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()){
					String name = pathname.getName();
					if (name.startsWith("dashboard") || name.startsWith("portal") || name.startsWith("oaiserver")){
						return true;
					}
				}
				return false;
			}
			
		});
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		for (File file: ownfiles){
			fileInfos.add(new FileInfo(file));
		}
		Collections.sort(fileInfos, new FileInfoComparator());
		this.getServletRequest().setAttribute("ownLogFiles", fileInfos);
		File[] otherfiles = logDir.listFiles(new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()){
					String name = pathname.getName();
					if (!(name.startsWith("dashboard") || name.startsWith("portal") || name.startsWith("oaiserver"))){
						return true;
					}
				}
				return false;
			}
			
		});
		List<FileInfo> otherFileInfos = new ArrayList<FileInfo>();
		for (File file: otherfiles){
			otherFileInfos.add(new FileInfo(file));
		}
		Collections.sort(otherFileInfos, new FileInfoComparator());
		this.getServletRequest().setAttribute("otherLogFiles", otherFileInfos);
		return SUCCESS;
	}

	public String download() throws Exception {
		if (StringUtils.isNotBlank(filename) && !filename.contains("/\\")){
			LOGGER.info(SecurityContext.get() + ": download file " + filename );
			File file = new File(baseDir, filename);
			String contentType = "text/plain";
			if (filename.endsWith(".gz")){
				contentType = "application/x-gzip";
			}
			ContentUtils.download(this.getServletRequest(), this.getServletResponse(), file, contentType);
			return null;
		}
		
		return SUCCESS;
	}
	
	static class FileInfoComparator implements Comparator<FileInfo>{

		@Override
		public int compare(FileInfo o1, FileInfo o2) {
			if (o1.getFilename().endsWith(".gz") && !o2.getFilename().endsWith(".gz")){
				return 1;
			}else if  (o2.getFilename().endsWith(".gz") && !o1.getFilename().endsWith(".gz")){
				return -1;
			}
			return o1.getFilename().compareTo(o2.getFilename());
		}
		
	}


}
