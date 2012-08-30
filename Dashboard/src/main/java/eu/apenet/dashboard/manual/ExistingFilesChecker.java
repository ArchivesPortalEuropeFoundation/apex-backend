package eu.apenet.dashboard.manual;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.ctc.wstx.exc.WstxParsingException;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.vo.*;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.dpt.utils.util.Xsd_enum;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * User: Eloy García
 * Date: Sep 23d, 2010
 */

/**
 * This class is in charge of checking if the files uploaded are already stored
 * in APEnet and allowing the user to perform several actions if it happens:
 * remove the old files and store the new ones
 */
public class ExistingFilesChecker {
    public final static String STATUS_EMPTY = "empty";
    public final static String STATUS_EXISTS = "exists";
    public final static String STATUS_ERROR = "error";
    public final static String STATUS_NO_EXIST = "no exists";
    public final static String STATUS_BLOCKED = "blocked";

	// Attributes
	private static final Logger LOG = Logger.getLogger(ExistingFilesChecker.class);
	private Integer archivalInstitutionId;
	private String uploadedFilesPath;
	private String xslPath;
	private String repoPath;
	private UpFileDAO upFileDao;
	private String archivalInstitutionCountry;

    private String additionalErrors = "";

	// Getters and Setters

	// Constructor
	public ExistingFilesChecker(Integer archivalInstitutionId) {

		this.upFileDao = DAOFactory.instance().getUpFileDAO();
		this.archivalInstitutionId = archivalInstitutionId;

		// Getting Archival Institution country
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		this.archivalInstitutionCountry = archivalInstitutionDao.findById(this.archivalInstitutionId).getCountry().getIsoname();
		this.uploadedFilesPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR;
		this.repoPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;
		this.xslPath = this.repoPath + this.archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR + "XSL" + APEnetUtilities.FILESEPARATOR;
	}

    public String getAdditionalErrors(){
        return additionalErrors;
    }

	// This method retrieves all the files recently uploaded via FTP, HTTP or
	// OAI-PMH for a partner
	public void retrieveUploadedFiles(List<FileUnit> existingNewXmlFilesUploaded,
			List<FileUnit> existingNewXslFilesUploaded) throws WstxParsingException {

		String eadType = "";
		// Retrieving all XML files uploaded
		List<UpFile> listXml = upFileDao.getUpFiles(this.archivalInstitutionId, FileType.XML);
		for (UpFile aListXml : listXml) {
			FileUnit fileUnit = new FileUnit();
			fileUnit.setFileId(aListXml.getUfId());
			fileUnit.setFileName(aListXml.getFname());
			fileUnit.setFileType(aListXml.getFileType().getFtype());
			
			//It is necessary to check if the XML file uploaded is a Finding Aid or a Holdings Guide
            try {
			    eadType = this.extractAttributeFromEad(this.uploadedFilesPath + aListXml.getFname(), "archdesc", "type", true);
            } catch (WstxParsingException e){
                //We get the exception just after - so nothing to do here.
            }
			if (eadType.equals("inventory")) {
				eadType = XmlType.EAD_FA.getName();
			}
			else if (eadType.equals("holdings_guide")) {
				eadType = XmlType.EAD_HG.getName();
			}
			else {
				//The XML is not an APEnet EAD
				eadType = "Undefined";
			}
			
			fileUnit.setEadType(eadType);
			fileUnit.setEadid("");
			fileUnit.setPermId(null);
			existingNewXmlFilesUploaded.add(fileUnit);
		}

		// Retrieving all XSL files uploaded
		List<UpFile> listXsl = upFileDao.getUpFiles(this.archivalInstitutionId, FileType.XSL);
		for (UpFile aListXsl : listXsl) {
			FileUnit fileUnit = new FileUnit();
			fileUnit.setFileId(aListXsl.getUfId());
			fileUnit.setFileName(aListXsl.getFname());
			fileUnit.setFileType(aListXsl.getFileType().getFtype());
			fileUnit.setEadType("");
			fileUnit.setEadid("");
			fileUnit.setPermId(null);
			existingNewXslFilesUploaded.add(fileUnit);
		}

	}

	// This method checks if the file is already stored in the Dashboard
	// It returns "exists" if it exists, "no exists" if it doesn't exist or
	// "error" in other case
	// This method fills fileUnit with eadid and permId if it is needed
	// If the file doesn't exist, then it will store it in the System (file
	// system and database)
	public String checkFile(FileUnit fileUnit, XmlType xmlType) {
        
		Boolean dataBaseCommitError = false;
		additionalErrors = "";
		String result = "no exists";

		LOG.info("Checking file: " + fileUnit.getFileName());

		if (fileUnit.getFileType().equals("xsl")) {
			// The file has XSL format
			File file = new File(this.xslPath + fileUnit.getFileName());

			if (file.exists()) {
				LOG.info("The file " + fileUnit.getFileName() + " is an XSL file and already exists");
				result = "exists";
			} else {
				// The xsl file doesn't exist
				// It is necessary to move the file to /mnt/repo/
				// and remove it from /mnt/tmp/up/ folder
				// It is necessary to remove the entry from up_file table
				LOG.info("The file " + fileUnit.getFileName() + " is an XSL file and it will be stored in the repository");
				
				try {

					this.deleteFileFromDDBB(fileUnit.getFileId());
					
				} catch (Exception e) {
					dataBaseCommitError = true;
					
				}
				
				if (!dataBaseCommitError) {
					try {
						insertFileToTempFiles(this.uploadedFilesPath + fileUnit.getFileName(), this.repoPath
                                + this.archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR
                                + this.archivalInstitutionId + APEnetUtilities.FILESEPARATOR + "XSL"
                                + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName());
					} catch (APEnetException e) {
						LOG.error("The file " + fileUnit.getFileName() + " could not be removed from the up repository or stored in the temporal repository");
					}	
				}				
			}

		} else {
			// The file has XML format
			String eadid = "";
            try {
                eadid = this.extractAttributeFromEad(this.uploadedFilesPath + fileUnit.getFileName(), "eadheader/eadid", null, true).trim();
            } catch (WstxParsingException e){
                LOG.error("File was not correct XML.", e);
                additionalErrors += e.getMessage();
            }
            String err;
            if((err = XmlChecker.isXmlParseable(new File(this.uploadedFilesPath + fileUnit.getFileName()))) != null){
                eadid = "error";
                LOG.error("File was not correct XML.");
                additionalErrors += "File was not correct XML. Impossible to parse it. Please check if file is XML. Error: " + err;
            }

			boolean isConverted;
            try {
                isConverted = Boolean.valueOf(extractAttributeFromEad(uploadedFilesPath + fileUnit.getFileName(), "eadheader/revisiondesc/change/item", null, false));
            } catch (Exception e){
                if(e instanceof WstxParsingException){
                    eadid = "error";
                    additionalErrors = e.getMessage();
                }
				LOG.info("The file " + fileUnit.getFileName() + " was tried to be converted but failed");
            	isConverted = false;
            }

            if(xmlType == XmlType.EAC_CPF){
                LOG.info("We try to insert an EAC-CPF file");

                try {
                    LOG.info("Validating the EAC-CPF file we just uploaded");
                    List<SAXParseException> exceptions = DocumentValidation.xmlValidation(uploadedFilesPath + fileUnit.getFileName(), Xsd_enum.XSD_EAC_SCHEMA);
                    if(exceptions != null){
                        fileUnit.setErrorInformation("The file " + fileUnit.getFileName() + " is not valid with EAC-CPF");
                        throw new APEnetException("The file " + fileUnit.getFileName() + " is not valid with EAC-CPF");
                    }

                    String cpfId = extractAttributeFromEad(uploadedFilesPath + fileUnit.getFileName(), "eac-cpf/control/recordId", null, true);
                    if(StringUtils.isBlank(cpfId)){
                        throw new APEnetException("recordId is empty in the file " + fileUnit.getFileName() + ", so we remove everything");
                    }
                    CpfContentDAO cpfContentDAO = DAOFactory.instance().getCpfContentDAO();
                    if(cpfContentDAO.doesCpfExists(cpfId) != null)
                        return "exists";

                    CpfContent cpfContent = new CpfContent();
                    cpfContent.setCpfId(cpfId);

                    ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
                    ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.archivalInstitutionId);
                    cpfContent.setArchivalInstitution(archivalInstitution);

                    cpfContent.setXml(FileUtils.readFileToString(new File(uploadedFilesPath + fileUnit.getFileName()), "utf-8"));

                    HibernateUtil.beginDatabaseTransaction();
                    cpfContentDAO.insertSimple(cpfContent);

                    String dirCpf = repoPath + archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + "CPF";
                    if(!new File(dirCpf).exists())
                        new File(dirCpf).mkdir();
                    insertFileToTempFiles(uploadedFilesPath + fileUnit.getFileName(), dirCpf + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName());
                    LOG.info("The CPF file " + fileUnit.getFileName() + " has been stored in the REPO directory");
                } catch (Exception e) {
                    new File(uploadedFilesPath + fileUnit.getFileName()).delete();
                    LOG.error("The EAC-CPF could not be stored in cpfContent table [Database Rollback]. Error:" + e.getMessage(), e);
                    HibernateUtil.rollbackDatabaseTransaction();
                    return "error";
                } finally {
                    try {
                        LOG.info("Erase from temp database");
                        deleteFileFromDDBB(fileUnit.getFileId());
                    } catch (Exception ex) {
                        LOG.error("We could not erase the file from the temp database");
                    }
                }
                HibernateUtil.commitDatabaseTransaction();
                return "no exists";
            } else if(xmlType == XmlType.EAD_SG || xmlType == XmlType.EAD_FA || xmlType == XmlType.EAD_HG) {
                if(eadid.equals(STATUS_EMPTY)){ //eadid is empty
                    fileUnit.setEadType(xmlType.getName());
                    result = STATUS_EMPTY;
                } else if(eadid.equals(STATUS_ERROR) || StringUtils.isBlank(eadid)){ //No eadid or several eadid
                    LOG.info("The " + xmlType.getName() + " " + fileUnit.getFileName() + " doesn't have a proper format: it doesn't have eadid or it has several");
                    try {
                        deleteFileFromDDBB(fileUnit.getFileId()); // It is necessary to remove the entry from up_file table
                    } catch (Exception e) {
                        dataBaseCommitError = true;
                    }

                    if (!dataBaseCommitError) {
                        try {
                            File file = new File(uploadedFilesPath + fileUnit.getFileName());
                            if (file.exists())
                                FileUtils.forceDelete(file);

                            File uploadDir = new File(uploadedFilesPath);
                            if (uploadDir.listFiles().length == 0) // There aren't any file in the directory, so it should be removed
                                FileUtils.forceDelete(uploadDir);
                        } catch (IOException ex) {
                            LOG.error("The file " + fileUnit.getFileName() + " could not be removed: " + ex.getMessage(), ex);
                        }
                    }
                    result = STATUS_ERROR;
                } else {
                    // It is necessary to check if the eadid is already in the Table for the current Archival Institution
                    Integer identifier;
                    if((identifier = DAOFactory.instance().getEadDAO().isEadidUsed(eadid, archivalInstitutionId, xmlType.getClazz())) != null){
                        // The EADID already exists
                        LOG.warn("Eadid '" + eadid + "' is already existing with id '" + identifier + "' in the table of '" + xmlType.getName() + "'");
                        fileUnit.setEadid(eadid);
                        fileUnit.setPermId(identifier.intValue());
                        result = STATUS_EXISTS;
                        if (xmlType == XmlType.EAD_FA && ContentManager.isBeingHarvested() && ContentManager.eadHasEsePublished(fileUnit.getPermId())) {
                        	// The EAD is a FA, exists, has ESE files published and Europeana is performing a Harvesting process
                        	result = STATUS_BLOCKED;
                        }
                    } else {
                        Ead ead = instantiateCorrectEadType(xmlType);
                        String fileShortPath = instantiateCorrectDirPath(xmlType) + fileUnit.getFileName();
                        try {
                            HibernateUtil.beginDatabaseTransaction();
                            ead.setEadid(eadid);

                            try {
                                ead.setTitle(this.extractAttributeFromEad(this.uploadedFilesPath + fileUnit.getFileName(), "eadheader/filedesc/titlestmt/titleproper", null, true).trim());
                            } catch (WstxParsingException e) {
                                ead.setTitle("");
                            }

                            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(this.archivalInstitutionId);
                            ead.setArchivalInstitution(archivalInstitution);

                            ead.setUploadDate(new Date());
                            ead.setPathApenetead(fileShortPath);

                            FileState fileState;
                            if (isConverted) {
                                LOG.debug("File already converted in local tool");
                                fileState = DAOFactory.instance().getFileStateDAO().getFileStateByState(FileState.NOT_VALIDATED_CONVERTED);
                            } else {
                                LOG.debug("File not converted in local tool");
                                fileState = DAOFactory.instance().getFileStateDAO().getFileStateByState(FileState.NEW);
                            }
                            ead.setFileState(fileState);
                            UpFile upFile = upFileDao.findById(fileUnit.getFileId());
                            ead.setUploadMethod(upFile.getUploadMethod());

                            DAOFactory.instance().getEadDAO().store(ead);
                            HibernateUtil.commitDatabaseTransaction();
                        } catch (Exception e) {
                            LOG.error("The " + xmlType.getName() + " which eadid is " + eadid + " could not be stored in the table [Database Rollback]. Error:" + e.getMessage());
                            HibernateUtil.rollbackDatabaseTransaction();
                            dataBaseCommitError = true;
                        } finally {
                            if (!dataBaseCommitError) {
                                try {
                                    deleteFileFromDDBB(fileUnit.getFileId());
                                } catch (Exception e) {
                                    dataBaseCommitError = true;
                                    LOG.error("Error removing file which id is " + fileUnit.getFileId() + " from up_file table in Database");
                                }
                            }
                        }

                        if (!dataBaseCommitError) {
                            try {
                                insertFileToTempFiles(this.uploadedFilesPath + fileUnit.getFileName(), APEnetUtilities.getConfig().getRepoDirPath() + fileShortPath);
                                LOG.info("The EAD " + fileUnit.getFileName() + " has been stored in the REPO directory");
                                ChangeControl.logOperation(ead, ChangeControl.UPLOAD_EAD_OPERATION);
                            } catch (APEnetException ex) {
                                LOG.error("The file " + fileUnit.getFileName() + " could not be stored or removed: " + ex.getMessage(), ex);
                            }
                        }
                        result = STATUS_NO_EXIST;
                    }
                }
            }
		}
		return result;
	}

    public String instantiateCorrectDirPath(XmlType xmlType) {
        String startPath = APEnetUtilities.FILESEPARATOR + archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
        if(xmlType == XmlType.EAD_FA){
            return startPath + "FA" + APEnetUtilities.FILESEPARATOR;
        } else if(xmlType == XmlType.EAD_HG){
            return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
        } else if(xmlType == XmlType.EAD_SG){
            return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
        }
        return null;
    }

    public Ead instantiateCorrectEadType(XmlType xmlType){
        if(xmlType == XmlType.EAD_FA){
            return new FindingAid();
        } else if(xmlType == XmlType.EAD_HG){
            return new HoldingsGuide();
        } else if(xmlType == XmlType.EAD_SG){
            return new SourceGuide();
        }
        return null;
    }

	public String extractAttributeFromEad(String path, String element, String attribute, boolean isReturningFirstInstance) throws WstxParsingException {
        final String CONVERTED_FLAG = "Converted_APEnet_EAD_version_";
        XMLStreamReader2 input = null;
	    InputStream sfile = null;
        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        try {
            sfile = new FileInputStream(path);
            input = (XMLStreamReader2) xmlif.createXMLStreamReader(sfile);

            boolean abort = false;
            boolean isInsideElement = false;
            boolean isInsidePath = false;
            boolean wasInsidePath = false;
		    String importantData = "";

            //element = "revisiondesc/change/item" or "eadid"
            String[] pathElements = element.split("/");
            int lenghtPath = pathElements.length;
            int pointerPath = 0;
            
            LOG.debug("Checking EAD file, looking for element " + element + ", and attribute " + ((attribute==null)?"null":attribute) + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
                switch (input.getEventType()) {
					case XMLEvent.START_DOCUMENT:
						break;
					case XMLEvent.START_ELEMENT:
                        if(pointerPath < lenghtPath && input.getLocalName().equals(pathElements[pointerPath])){
                            LOG.debug("We arrive in " + input.getLocalName());
                            isInsidePath = true;
                            wasInsidePath = true;
                            LOG.debug("pointerPath = " + pointerPath + ", lenghtPath = " + lenghtPath);
                            if(pointerPath == lenghtPath-1){
                                isInsideElement = true;
                                if(attribute != null){
                                    for(int attributeNb = 0; attributeNb < input.getAttributeCount(); attributeNb++){
                                        if(input.getAttributeLocalName(attributeNb).equals(attribute)){
                                            LOG.debug("Returning " + input.getAttributeValue(attributeNb));
                                            return input.getAttributeValue(attributeNb);
                                        }
                                    }
                                    LOG.debug("Returning error");
                                    return "error";
                                }
                                
                                //TODO: Here add the check of empty element by Patricia and call the function with extractAttributeFromEad([path], "eadid", null, true);
                                if (input.getLocalName().equals("eadid"))
                                {
                                	if (input.isEmptyElement())
                                	{
                                		LOG.debug("Returning empty");
	                            		return("empty");
                                	}
                                }
                            }
                            pointerPath++;
                        }
						break;
					case XMLEvent.CHARACTERS:
						if (isInsideElement) {
							importantData = input.getText();
							if (importantData.startsWith(CONVERTED_FLAG)) {
                                LOG.debug("Returning true");
								return "true";
							} else if(isReturningFirstInstance){
                                LOG.debug("Returning " + importantData);
                                return importantData;
                            }
						}
						break;
					case XMLEvent.CDATA:
						break;
					case XMLEvent.END_ELEMENT:
                        if(isInsidePath && input.getLocalName().equals(pathElements[pointerPath-1])){
                            //TODO: Check added of empty element for Patricia, call the function with extractAttributeFromEad([path], "eadid", null, true);
                            if(isInsideElement && isReturningFirstInstance){
                                LOG.debug("Returning empty");
                                return "empty";
                            }
                            pointerPath--;
                            LOG.debug("We leave from " + input.getLocalName());
                            isInsideElement = false;
                            LOG.debug("pointerPath = " + pointerPath);
                            if(pointerPath == 0)
                                isInsidePath = false;
                        }
                        if(!isInsidePath && wasInsidePath)
                            abort = true;    
						break;
                }
                if (input.hasNext())
                    input.next();
            }
        } catch (Exception e) {
            if(e instanceof WstxParsingException)
                throw (WstxParsingException)e;
            LOG.error("Error parsing StAX for file " + path, e);
        } finally {
            try {
                if(input != null)
                    input.close();
                if(sfile != null)
                    sfile.close();
            } catch (Exception e) {
                LOG.error("Error closing streams" + e.getMessage(), e);
            }
        }
        LOG.debug("Returning error");
        return "error";
	}

	public String cancelAnswer(FileUnit fileUnit){
		// The user has decided not to overwrite the file in the Dashboard so it is necessary to delete the file from up directory and delete its entry in up_file table
        try {
            deleteFileFromDDBB(fileUnit.getFileId());
            try {
                ContentUtils.deleteFile(uploadedFilesPath + fileUnit.getFileName());
                File uploadDir = new File(uploadedFilesPath);
                if (uploadDir.listFiles().length == 0)
                    FileUtils.forceDelete(uploadDir);
                LOG.info("The file " + fileUnit.getFileName() + " has been deleted successfully.");
            } catch (Exception e) {
                throw new APEnetException("The file " + fileUnit.getFileName() + " or the directory " + uploadedFilesPath + " could not be removed. Error: " + e.getMessage());
            }
        } catch (APEnetException ape){
            LOG.error(ape.getMessage(), ape);
            return "error";
        }
        return "ok";
	}
	
	public String overwriteAnswer(FileUnit fileUnit){
		// The user has decided to overwrite the file
		if (fileUnit.getFileType().equals("xsl")) {
			// The file is an XSL(t) file
            try {
                deleteFileFromDDBB(fileUnit.getFileId());
                insertFileToTempFiles(uploadedFilesPath + fileUnit.getFileName(), xslPath + fileUnit.getFileName());
                LOG.info("The file " + fileUnit.getFileName() + " has been overwritten");
            } catch (APEnetException ape) {
                LOG.error(ape.getMessage(), ape);
                return "error";
            }
		} else {
			// The file is an EAD
			if (fileUnit.getEadType().equals(XmlType.EAD_FA.getName()) || fileUnit.getEadType().equals(XmlType.EAD_HG.getName()) || fileUnit.getEadType().equals(XmlType.EAD_SG.getName())) {
                try {
                    overwrite(fileUnit);
                } catch (APEnetException e) {
                    return "error";
                }
			}
		} 
		return "ok";
	}
	
	// This method overwrite (or not) a file in the Dashboard for a user
	// If everything is ok then it returns "ok" but if it was a problem, then it
	// returns "error"
	public String overwriteFile(FileUnit fileUnit, String answer,String savechangesEADIDanswer, String canceloverwriteanswer,String eadType, String neweadid) {
		String result = "ok";
		Boolean dataBaseCommitError = false;
		if (answer.equals("Cancel")) {
			result=cancelAnswer(fileUnit);
		} else if (answer.equals("Overwrite")) {
			result=overwriteAnswer(fileUnit);
		} else {
		    //Change EADID
			//Check the content of savechangesEADIDanswer OK or KO.
			if ((savechangesEADIDanswer.equals("OK")) || (canceloverwriteanswer.equals("Overwrite"))) {
	            //Change into the XML the EADID
	            //There is not any FA with this new EADID.
	        	//Edit the file and update the eadid for the new.
	        	//1st obtain the file's url.

                String eadid = changeEadidUsingDOM(fileUnit, neweadid);

	    		boolean isConverted;
	            try {
	                isConverted = Boolean.valueOf(extractAttributeFromEad(uploadedFilesPath + fileUnit.getFileName(), "eadheader/revisiondesc/change/item", null, false));
	            } catch (Exception e){
	                if(e instanceof WstxParsingException){
	                    eadid = STATUS_ERROR;
	                    additionalErrors = e.getMessage();
	                }
					LOG.info("The file " + fileUnit.getFileName() + " was tryed to be converted but failed");
	            	isConverted = false;
	            }
				/**/
	        	
                if (eadType.equals(XmlType.EAD_FA.getName()) || eadType.equals(XmlType.EAD_HG.getName()) || eadType.equals(XmlType.EAD_SG.getName())) {
                    if (eadid.equals(STATUS_ERROR) || "".equals(eadid)) {
                        // It is necessary to remove the file from /mnt/tmp/up folder
                        LOG.info("The EAD " + fileUnit.getFileName() + " doesn't have a proper format: it doesn't have eadid or it has several");
                        File file = new File(uploadedFilesPath + fileUnit.getFileName());
                        File uploadDir = new File(uploadedFilesPath);
                        try {
                            deleteFileFromDDBB(fileUnit.getFileId());
                            try {
                                if (file.exists()) {
                                    FileUtils.forceDelete(file);
                                    LOG.info("The file " + fileUnit.getFileName() + " has been removed from Dashboard");
                                }
                                if (uploadDir.listFiles().length == 0)
                                    FileUtils.forceDelete(uploadDir);
                            } catch (IOException ex) {
                                throw new APEnetException("The file " + fileUnit.getFileName() + " could not be removed: " + ex.getMessage(), ex);
                            }
                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);
                        }
                        return STATUS_ERROR;
                    } else {
                        // It is necessary to check if the eadid is already in Finding Aid Table for the current Archival Institution
                        XmlType xmlType = XmlType.getType(eadType);
                        Integer eadIdentifier = DAOFactory.instance().getEadDAO().isEadidUsed(eadid, archivalInstitutionId, xmlType.getClazz());
                        if(eadIdentifier != null) {
                            // The EAD already exists
                            fileUnit.setEadid(eadid);
                            fileUnit.setPermId(eadIdentifier);
                            if ((xmlType == XmlType.EAD_FA) && canceloverwriteanswer.equals("Overwrite")) { //todo: Why only for FA?
                                overwriteFile(fileUnit, "Overwrite", savechangesEADIDanswer, canceloverwriteanswer, eadType, neweadid);
                            } else {
                                LOG.error("eadid '" + eadid + "' is already existing in the table with id '" + eadIdentifier + "'");
                                return STATUS_EXISTS;
                            }
                        } else {
                            // The EAD doesn't exist
                            // It is necessary to move the file to /mnt/tmp/tmp/ and remove it from /mnt/tmp/up/ folder
                            // It is necessary to insert an entry in finding_aid table
                            // It is necessary to remove the entry from up_file table
                            Ead ead = instantiateCorrectEadType(xmlType);
                            String fileShortPath = instantiateCorrectDirPath(xmlType) + fileUnit.getFileName();
                            try {
                                HibernateUtil.beginDatabaseTransaction();

                                ead.setEadid(eadid);
                                try {
                                    ead.setTitle(extractAttributeFromEad(uploadedFilesPath + fileUnit.getFileName(), "eadheader/filedesc/titlestmt/titleproper", null, true).trim());
                                } catch (WstxParsingException e){
                                    ead.setTitle("");
                                }

                                ead.setUploadDate(new Date());
                                ead.setPathApenetead(fileShortPath);

                                FileStateDAO fileStateDao = DAOFactory.instance().getFileStateDAO();
                                if (isConverted)
                                    ead.setFileState(fileStateDao.getFileStateByState(FileState.NOT_VALIDATED_CONVERTED));
                                else
                                    ead.setFileState(fileStateDao.getFileStateByState(FileState.NEW));

                                UpFile upFile = upFileDao.findById(fileUnit.getFileId());
                                ead.setUploadMethod(upFile.getUploadMethod());
                                ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(archivalInstitutionId);
                                ead.setArchivalInstitution(archivalInstitution);

                                DAOFactory.instance().getEadDAO().insertSimple(ead);

                                HibernateUtil.commitDatabaseTransaction();
                            } catch (Exception e) {
                                LOG.error("The EAD which eadid is '" + eadid + "' could not be stored in table [Database Rollback]. Error:" + e.getMessage());

                                HibernateUtil.rollbackDatabaseTransaction();
                                dataBaseCommitError = true;
                            } finally {

                                if (!dataBaseCommitError) {
                                    try {
                                        deleteFileFromDDBB(fileUnit.getFileId());
                                    } catch (Exception e) {
                                        dataBaseCommitError = true;
                                        LOG.error("Error removing file which id is " + fileUnit.getFileId() + " from up_file table in Database");
                                    }
                                }
                            }

                            if (!dataBaseCommitError) {
                                try {
                                    insertFileToTempFiles(uploadedFilesPath + fileUnit.getFileName(), APEnetUtilities.getConfig().getRepoDirPath() + fileShortPath);
                                    // Register operation
                                    LOG.info("The EAD " + fileUnit.getFileName() + " has been stored in the temporal repository");
                                    ChangeControl.logOperation(ead, "Upload EAD");
                                } catch (APEnetException ex) {
                                    LOG.error("The file " + fileUnit.getFileName() + " could not be stored or removed: " + ex.getMessage(), ex);
                                }
                            }
                            return STATUS_NO_EXIST;
                        }
                    }
                }
			} else if (savechangesEADIDanswer.equals("KO")) { 
				//In this case, the change of the EADID has failed.
				//Then, the file will be accessible to Change the EADID in next access to Dashboard.
				LOG.error("The EAD " + fileUnit.getFileName() +" has not been changed correctly");
				if (canceloverwriteanswer.equals("Overwrite")){
					result= overwriteAnswer(fileUnit);
				} else if (canceloverwriteanswer.equals("Cancel")){
					result= cancelAnswer(fileUnit);
				}				
			}
		}
        return result;
	}
		
	public Boolean deleteFile(String path) throws IOException {
		File srcFile = new File(path);
        FileUtils.forceDelete(srcFile);
        return null;
	}

	// This method deletes the destination file, copies the source file to
	// temporal directory and finally deletes the source file if everything is
	// ok
	// If the source folder is empty, then the folder will be removed
	private void insertFileToTempFiles(String srcFilePath, String destFilePath) throws APEnetException {
        try {
            File srcFile = new File(srcFilePath);
            File destFile = new File(destFilePath);
            File uploadDir = new File(uploadedFilesPath);
            if (destFile.exists())
                FileUtils.forceDelete(destFile);

            FileUtils.copyFile(srcFile, destFile);
            FileUtils.forceDelete(srcFile);

            if (uploadDir.listFiles().length == 0)
                FileUtils.forceDelete(uploadDir);
        } catch (IOException e) {
            throw new APEnetException("The file '" + srcFilePath + "' could not be removed. Error: " + e.getMessage(), e);
        }
	}

	// This method removes the entry which has ufId as the primary key from up_files table
	private void deleteFileFromDDBB(Integer ufId) throws APEnetException {
		try {
			HibernateUtil.beginDatabaseTransaction();

            UpFile upFile = upFileDao.findById(ufId);
			upFileDao.deleteSimple(upFile);

			HibernateUtil.commitDatabaseTransaction();
		} catch (Exception e) {
			LOG.error("The file uploaded with ID = '" + ufId.toString() + "' couldn't be removed from up_file table [Database Rollback]. Error: " + e.getMessage());
			HibernateUtil.rollbackDatabaseTransaction();
			throw new APEnetException("Error deleting file from up_file table", e);
		}
	}
	
	private void overwrite(FileUnit fileUnit) throws APEnetException {
        LOG.info("The Archival Institution '" + archivalInstitutionId + "' has chosen to overwrite the EAD file '" + fileUnit.getFileName() + "' with eadid '" + fileUnit.getEadid() + "'");
        boolean isConverted;
        try {
            isConverted = Boolean.valueOf(extractAttributeFromEad(uploadedFilesPath + fileUnit.getFileName(), "eadheader/revisiondesc/change/item", null, false));
        } catch (Exception e){
            isConverted = false;
        }

        String title;
        try {
            title = extractAttributeFromEad(uploadedFilesPath + fileUnit.getFileName(), "eadheader/filedesc/titlestmt/titleproper", null, true).trim();
        } catch (WstxParsingException e){
            title = "";
        }

        XmlType xmlType = XmlType.getType(fileUnit.getEadType());
        if(!EadLogicAbstract.deleteOrOverwrite(fileUnit.getPermId(), xmlType, true, fileUnit, isConverted, title, archivalInstitutionId))
            throw new APEnetException();
    }

    private String changeEadidUsingDOM(FileUnit fileUnit, String neweadid) {
        UpFileDAO upFileDao = DAOFactory.instance().getUpFileDAO();
        UpFile upfile = upFileDao.getUpFile(fileUnit.getFileId());

        String path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upfile.getPath();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);

        String oldeadid = "";

        try{
            oldeadid = this.extractAttributeFromEad(this.uploadedFilesPath + fileUnit.getFileName(), "eadheader/eadid", null, true).trim();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream in = new FileInputStream(path);
            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList eadidList = doc.getElementsByTagName("eadid");
            Node eadidNode = eadidList.item(0);
            String currenteadid = eadidNode.getTextContent();
            if (!currenteadid.equals(neweadid)&&(!neweadid.isEmpty())) {
                LOG.info("Changing the eadid into the file");
                eadidNode.setTextContent(neweadid);
                String newfilepath=path.replace(".xml", "") + neweadid + ".xml";
                //fileUnit.setFileName(newfileName);
                Result result1 = new StreamResult(new java.io.File(path));
                Source source = new DOMSource(doc);
                Transformer transformer;
                transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result1);

                //Rename file
                //path = path.replace("/", "\\");
                //newfilepath = newfilepath.replace("/", "\\");
                File oldFile = new File(path);
                File newFile = new File (newfilepath);
                Boolean success= oldFile.renameTo(newFile);

                //if renaming process fails, the original file will have the original eadid identifier again.
                if (!success) {
                    LOG.info("Removing the new eadid into the original file because of fail renaming process");
                    eadidNode.setTextContent(oldeadid);
                    Result result2 = new StreamResult(new java.io.File(path));
                    Source source2 = new DOMSource(doc);
                    Transformer transformer2;
                    transformer2 = TransformerFactory.newInstance().newTransformer();
                    transformer2.transform(source2, result2);
                    return STATUS_ERROR;
                } else {
                    //Change the path into upFile table to the new path.
                    upfile.setPath(newfilepath);
                    upFileDao.update(upfile);
                    String oldfname=upfile.getFname();
                    String newfname= oldfname.replace(".xml", "") + neweadid + ".xml";
                    upfile.setFname(newfname);

                    fileUnit.setFileName(newfname);
                    fileUnit.setEadid(neweadid);
                    return neweadid;
                }
            }
        } catch (Exception ex) {
            LOG.error("overwrite: " + ex.getMessage());
        }
        return "";
    }

}
