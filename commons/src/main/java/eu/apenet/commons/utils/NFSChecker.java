package eu.apenet.commons.utils;

import java.io.File;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;

public class NFSChecker implements Runnable {
	
	private final Logger log = Logger.getLogger(getClass());
	private String currentStateNFS;

	// Getters & Setters
	public void setCurrentStateNFS(String currentStateNFS) {
		this.currentStateNFS = currentStateNFS;
	}

	public String getCurrentStateNFS() {
		return currentStateNFS;
	}
	
	// Methods
	public void run()  {
		
		this.setCurrentStateNFS("ERROR");
		
		try{			
			String pathAL = APEnetUtilities.getApePortalAndDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";
			
			if (new File(pathAL).exists()){
				this.setCurrentStateNFS("SUCCESS");
			}else{
				this.setCurrentStateNFS("ERROR");
			}
		}catch(Exception e){
			log.error("NFS error: " + e);
			this.setCurrentStateNFS("ERROR");
		}

	}

}
