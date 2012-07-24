 package eu.apenet.dashboard.actions;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;


/**
 * User: Jara Alvarez
 * Date: Sep 20th, 2010
 *
 */

public class DownloadGeneralALAction extends ActionSupport {


	private final Logger log = Logger.getLogger(getClass());
	private InputStream inputStream; //It's necesary to download and preview a file
	private String fileName; //It's used to manage the file download name
	private Long fileSize; //It's used to store the file download size
	 private String httpFileFileName; 		//The uploaded file name
	   
	   public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getHttpFileFileName() {
		return httpFileFileName;
	}

	public void setHttpFileFileName(String httpFileFileName) {
		this.httpFileFileName = httpFileFileName;
	}

	public String execute(){
		   
		   String result=null;
   		   		   
			try {
				String path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";		        	
		    	File file = new File  (path);
	            
	            //if the xml does not exist, error
	        	if (!file.exists()) 
	        	{ 
	        		addActionMessage("Error");
	        		result= ERROR;
	        	}
				else
				{
					this.setInputStream(new FileInputStream(file));
					this.setHttpFileFileName(file.getName());
					this.setFileName("AL.xml");
	                this.setFileSize(file.length());
				}
	        	
	    		if(this.inputStream==null){
	    			addActionMessage("Error");
	    			result= ERROR;
	            } else{
	    			try {
	    				if(this.inputStream.available()>0){
	    					result= "download";
	    				}
	    			} catch (IOException e) {
	    				log.error("The general archival landscape could not be downloaded");
	    			}
	            }
	        	
			} catch (Exception e){
				log.error(e.getMessage());
			}
			return result;		   
	   }
}