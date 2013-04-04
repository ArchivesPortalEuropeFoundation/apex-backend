 package eu.apenet.dashboard.actions;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import java.io.*;
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

	public String execute() {
        String result=null;
		try {
		    String path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";
		    File file = new File  (path);
	        if (!file.exists()) {
	            addActionMessage(getText("content.message.errorsmall"));
	        	result= ERROR;
            } else {
			    this.setInputStream(new FileInputStream(file));
				this.setHttpFileFileName(file.getName());
				this.setFileName("AL.xml");
	            this.setFileSize(file.length());
		    }

	    	if(this.inputStream==null){
	    	    addActionMessage(getText("content.message.errorsmall"));
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

//     public String downloadArchivalLandscapeFromDB() {
//         try {
//             StringWriter eadArchilvaLandscapeWriter = new StringWriter();
//             CreateArchivalLandscapeEad createArchivalLandscapeEad = new CreateArchivalLandscapeEad(eadArchilvaLandscapeWriter);
//             createArchivalLandscapeEad.createEadContentData("Archives Portal Europe - Archival Landscape", "GENERAL_AL_EAD", null, null);
//
//             CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
//             List<Country> countries = countryDAO.findAll();
//             ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
//
//             for(Country country : countries) {
//                 createArchivalLandscapeEad.addInsideEad(country);
//                 List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryId(country.getId()); //Get main AIs (without parents)
//                 for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
//                     if(archivalInstitution.getParentAiId() == null) {
//                         createArchivalLandscapeEad.addInsideEad(archivalInstitution);
//                         recurenceLoop(createArchivalLandscapeEad, archivalInstitution);
//                         createArchivalLandscapeEad.writeEndElement(); //close each C element for each main archival institution
//                     }
//                 }
//                 createArchivalLandscapeEad.writeEndElement(); //close each C element for each country
//             }
//
//             createArchivalLandscapeEad.closeEndFile();
//             eadArchilvaLandscapeWriter.close();
//
//             setInputStream(IOUtils.toInputStream(eadArchilvaLandscapeWriter.toString()));
//             setFileName("AL.xml");
//
//             return "download";
//         } catch (Exception e) {
//             LOG.error("Error", e);
//             return ERROR;
//         }
//     }
//
//     public void recurenceLoop(CreateArchivalLandscapeEad createArchivalLandscapeEad, ArchivalInstitution archivalInstitution) throws XMLStreamException {
//         List<ArchivalInstitution> archivalInstitutionChildren = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByParentAiId(archivalInstitution.getAiId());
//         for(ArchivalInstitution archivalInstitutionChild : archivalInstitutionChildren) {
//             createArchivalLandscapeEad.addInsideEad(archivalInstitutionChild);
//             recurenceLoop(createArchivalLandscapeEad, archivalInstitutionChild);
//             createArchivalLandscapeEad.writeEndElement();
//         }
//     }

}