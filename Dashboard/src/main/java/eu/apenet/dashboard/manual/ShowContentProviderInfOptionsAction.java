package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * User: Eloy García
 * Date: Nov 3d, 2010
 */

/**
 * This class is in charge of redirecting the user to 
 * Content Provider Information functionality 
 */

public class ShowContentProviderInfOptionsAction extends AbstractInstitutionAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3716989273471606868L;

	private final Logger log = Logger.getLogger(getClass());
	//Attributes

	private InputStream inputStream;
	private String fileName;
	private Long fileSize;
	
	//Getters & Setters
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
	
	
	//Methods
	@Override
    public String execute() throws Exception {		
			return SUCCESS;			
    }
	
	public String download() {
		
		String value = "";
		try {
			//The ai_id is retrieved from the session

			
			ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
			ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
			File tempFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getEagPath());
			this.inputStream = new FileInputStream(tempFile);
			this.fileName = tempFile.getName();
			this.fileSize = tempFile.length();
		} catch (Exception e){
			log.error(e.getMessage());
		}
		if(inputStream==null){
			value = "error";
        } else{
			try {
				if(inputStream.available()>0){
					value = "download";
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
        }
		
		return value;
	}
}