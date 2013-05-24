package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dpt.utils.util.XsltChecker;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author eloy
 *
 */

/** This class is in charge of uploading files to APEnet, unzipping them
 *  if applicable and storing them in UP FILES container
 *  
 */
public abstract class ManualUploader {

    
	private static final String MAGIC_KEY = "99999999999";
    
	private final Logger log = Logger.getLogger(getClass());
	protected String uploadingMethod;
	protected ZipManager zipManager;

	private List<String> filesNotUploaded;
	private List<String> filesUploaded;

    private List<String> warnings_eag;
    
    private List<ArchivalInstitution> archivalInstitutionsToDelete= new ArrayList<ArchivalInstitution>();
    private List<ArchivalInstitution> archivalInstitutionsToInsert= new ArrayList<ArchivalInstitution>();
    private List<AiAlternativeName> archivalInstitutionsNameNotChanged = new ArrayList<AiAlternativeName>();
    private List<AiAlternativeName> archivalInstitutionsNameChanged = new ArrayList<AiAlternativeName>();
    private List<ArchivalInstitution> archivalInstitutionsParentNotChanged = new ArrayList<ArchivalInstitution>();
	private List<ArchivalInstitution> archivalInstitutionsParentChanged = new ArrayList<ArchivalInstitution>();
    
	private List<FindingAid> fasDeleted = new ArrayList<FindingAid>();
	private List<HoldingsGuide> hgsDeleted = new ArrayList<HoldingsGuide>();
        
	public List<FindingAid> getFasDeleted() {
		return fasDeleted;
	}

	public void setFasDeleted(List<FindingAid> fasDeleted) {
		this.fasDeleted = fasDeleted;
	}

	public List<HoldingsGuide> getHgsDeleted() {
		return hgsDeleted;
	}

	public void setHgsDeleted(List<HoldingsGuide> hgsDeleted) {
		this.hgsDeleted = hgsDeleted;
	}


	public List<ArchivalInstitution> getArchivalInstitutionsToDelete() {
		return archivalInstitutionsToDelete;
	}

	public void setArchivalInstitutionsToDelete(
			List<ArchivalInstitution> archivalInstitutionsToDelete) {
		this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
	}

	public String getUploadingMethod() {
		return uploadingMethod;
	}

	public void setUploadingMethod(String uploadingMethod) {
		this.uploadingMethod = uploadingMethod;
	}

	public List<String> getFilesUploaded() {
		return filesUploaded;
	}

	public List<String> getFilesNotUploaded() {
		return filesNotUploaded;
	}

    public List<String> getWarnings_eag(){
        return warnings_eag;
    }

	public ManualUploader() {
		
	}

	public String upload(){
		return null;
	}
	
	public List<ArchivalInstitution> getArchivalInstitutionsToInsert() {
		return archivalInstitutionsToInsert;
	}

	public void setArchivalInstitutionsToInsert(
			List<ArchivalInstitution> archivalInstitutionsToInsert) {
		this.archivalInstitutionsToInsert = archivalInstitutionsToInsert;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameNotChanged() {
		return archivalInstitutionsNameNotChanged;
	}

	public void setArchivalInstitutionsNameNotChanged(
			List<AiAlternativeName> archivalInstitutionsNameNotChanged) {
		this.archivalInstitutionsNameNotChanged = archivalInstitutionsNameNotChanged;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameChanged() {
		return archivalInstitutionsNameChanged;
	}

	public void setArchivalInstitutionsNameChanged(
			List<AiAlternativeName> archivalInstitutionsNameChanged) {
		this.archivalInstitutionsNameChanged = archivalInstitutionsNameChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentNotChanged() {
		return archivalInstitutionsParentNotChanged;
	}

	public void setArchivalInstitutionsParentNotChanged(
			List<ArchivalInstitution> archivalInstitutionsParentNotChanged) {
		this.archivalInstitutionsParentNotChanged = archivalInstitutionsParentNotChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentChanged() {
		return archivalInstitutionsParentChanged;
	}

	public void setArchivalInstitutionsParentChanged(
			List<ArchivalInstitution> archivalInstitutionsParentChanged) {
		this.archivalInstitutionsParentChanged = archivalInstitutionsParentChanged;
	}

	//This method uploads only one file to the Dashboard. It stores this file in a temporal directory
	public String uploadFile(String uploadType, String fileName, File file, String contentType, Integer archivalInstitutionId, String uploadMethodString){
        
		String result = null;
        String path = null;
        if (archivalInstitutionId != null)
        	path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR;
		String fullFileName = "";
		String tempPath = path + "tmp" + fileName + new Date().getTime() + APEnetUtilities.FILESEPARATOR;	//This is the path in which the zip files are going to be unzipped    	
		File tempDir = new File(tempPath); 
    	this.filesNotUploaded = new ArrayList<String>();
    	this.filesUploaded = new ArrayList<String>();    	
		log.info("Checking eadtype: "+uploadType);
    	if (uploadType.equals("EAD")) {
			
    		// Uncomment this line if xsl and xslt files are permitted again
    		//if (contentType.equals("zip") || contentType.equals("xml") || contentType.equals("xsl") || contentType.equals("xslt")){
    		if (contentType.equals("zip") || contentType.equals("xml")) {
				if (contentType.equals("zip"))
    				fullFileName = tempPath + fileName; //The file uploaded is a ZIP file. It's necessary to store it in a temp directory to unzip it
    			else
    				fullFileName = path + fileName;

	    		File theFile = new File(fullFileName);
	    		
	    		if(theFile.exists()) {
	    			result = "error";
                } else {
                    try {
                        try {
                            FileUtils.copyFile(file, theFile);
                        } catch (IOException e) {
                            throw new APEnetException("The file " + fileName + " could not be copied from tmp directory (this file is part of a zipped file). Error: " + e.getMessage(), e);
                        }

                        if (!contentType.equals("zip")) {
                            filesUploaded.add(fileName);
                            try {
                                //Insert file uploaded into up_file table
                                JpaUtil.beginDatabaseTransaction();

                                String defaultUpDir = APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
                                UpFile upFile;
                                if (contentType.equals("xml")) {
                                    upFile = createUpFile(defaultUpDir, fileName, uploadMethodString, archivalInstitutionId, FileType.XML);
                                } else if (contentType.equals("xsl") || contentType.equals("xslt")) {
                                    upFile = createUpFile(defaultUpDir, fileName, uploadMethodString, archivalInstitutionId, FileType.XSL);
                                    if(!XsltChecker.isXsltParseable(theFile))
                                        log.error("Error! The XSLT file should NOT be kept on the server but should be removed and the partner should be advised.");
                                } else {
                                    upFile = createUpFile(defaultUpDir, fileName, uploadMethodString, archivalInstitutionId, FileType.ZIP);
                                }

                                DAOFactory.instance().getUpFileDAO().insertSimple(upFile);
                                JpaUtil.commitDatabaseTransaction();
                            } catch (Exception e) {
                            	JpaUtil.rollbackDatabaseTransaction();
                                throw new APEnetException("Error inserting the file " + fileName + " in up_table (the user was uploading this file to the Dashboard) [Database Rollback]. Error: " + e.getMessage());
                            }

                            try {
                                //Copy file into /mnt/tmp/up/ folder
                                File destFile = new File(path + fileName);
                                FileUtils.copyFile(file, destFile);
                                return ActionSupport.SUCCESS;
                            } catch (IOException e) {
                                throw new APEnetException("Error storing the file " + fileName + " in temporal up repository. Error: " + e.getMessage());
                            }
                        } else if (contentType.equals("zip")) {
                            log.info("Archival Institution which id is " + archivalInstitutionId + " is uploading the zip file " + fileName + ". Unzipping..." );
                            zipManager = new ZipManager(tempPath);
                            zipManager.unzip(fullFileName);

                            try {
                                FileUtils.forceDelete(theFile);
                            } catch (IOException e) {
                                throw new APEnetException("The file " + fileName + " could not be removed from tmp directory created to unzip a zipped file. Error: " + e.getMessage());
                            }

                            moveToTemp(tempPath, path, "xml", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString);
                            moveToTemp(tempPath, path, "xsl", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString);
                            moveToTemp(tempPath, path, "other", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString);

                            try {
                                FileUtils.deleteDirectory(tempDir);
                            } catch (IOException e) {
                                throw new APEnetException("The temporal directory " + tempPath + " could not be removed from tmp directory. Error: " + e.getMessage());
                            }

                            log.info("Files ingested succesfully. Next step: File validation (ExistingFilesChecker)");
                            return ActionSupport.SUCCESS;
                        }
                    } catch (APEnetException ape) {
                        log.error(ape.getMessage());
                        return ActionSupport.INPUT;
                    }
	    		}    		
			} else {
				//The format is not allowed
				log.error("The file " + fileName + " has a format not allowed. File not uploaded and removed automatically");
				filesNotUploaded.add(fileName);
				result = "success";
			}
    	} else if (uploadType.equals("AL")) { //Upload Archival Landscape
    		try {				
    			if ((contentType.equals("xml"))) {
    				ArchivalLandscape a = new ArchivalLandscape();
    				path = a.getmyPath(a.getmyCountry());
    				//Create a temporary file
    				fullFileName = path + a.getmyCountry() + "AL.xml"; //fileName
    				File theFile = new File(fullFileName);
    			
    				//There's already an AL uploaded for this partner. Actually, it should always be an AL because is created when check if the file to upload is well-formed 
    				if (theFile.exists() && (theFile.length()>0)) 
    				{	    			
    					String pathTemp = path + "temp" +APEnetUtilities.FILESEPARATOR;
    					File TempFile = new File(pathTemp);
    					if (!TempFile.exists())
    						TempFile.mkdir();
    					
    					fullFileName = path + "temp" + APEnetUtilities.FILESEPARATOR + fileName;					
    					File sfile = new File(fullFileName);
    					//sfile.mkdir();
    					FileUtils.copyFile(file, sfile);
    					log.info("The file: " + fileName + " has been copied to: " + fullFileName);
    					//sfile.renameTo(new File(path + "temp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml"));
    					this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
    					this.setArchivalInstitutionsNameNotChanged(a.getArchivalInstitutionsNameNotChanged());
    					this.setArchivalInstitutionsNameChanged(a.getArchivalInstitutionsNameChanged());
	    			
    					result = "error";	
    				}
    				//There's no AL for this country. It has to copy in the repository.
    				else 
    				{	 
    					FileUtils.copyFile(file, theFile);
    					result = "success";
    					this.filesUploaded.add(fileName);    					
    					log.error("There were no file in the AL repository for " + a.getmyCountry() + ".");    					
	    			}
    			}else
    			{
    				log.info("The file AL could not be uploaded because is not a xml");
    				this.filesNotUploaded.add(fileName);
    				result = "input";
    			}
	    	} catch (Exception e) {
	    		log.error("The file AL could not be uploaded. Some errors occurrs in process");
	    		result = "input";	    			
	    	}

		}else {
			//HTTP EAG upload
			path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR;
    		
    		log.info("Archival institution ID " + archivalInstitutionId + " uploads an EAG.");
    		try {       
				if (contentType.equals("xml")){
					
					//The format is allowed
					//The file is copied to /mnt/tmp/tmp/ai_id/
					fullFileName = path + fileName;
		    		File source = new File(fullFileName);
    				FileUtils.copyFile(file, source);
	    		    
    				//It is necessary to validate the file against APEnet EAG schema
                    log.debug("Beginning EAG validation");
                    APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, file.getAbsolutePath());
    				if (eag.validate()){
                        log.info("EAG is valid");
                        
                        //check the <recordId> content
                        //eag.setEagPath(fullFileName); //temp used for looking forward target tag
                        String recordIdValue = eag.lookingForwardElementContent("/eag/control/recordId");
                        if(recordIdValue!=null && recordIdValue.endsWith(MAGIC_KEY)){
                        	//replace value with a consecutive unique value
                        	ArchivalLandscape archivalLandscape = new ArchivalLandscape();
                        	int zeroes = 11-archivalInstitutionId.toString().length();
                        	String newRecordIdValue = archivalLandscape.getmyCountry()+"-";
                        	for(int x=0;x<zeroes;x++){
                        		newRecordIdValue+="0";
                        	}
                        	newRecordIdValue+=archivalInstitutionId.toString();
                        	DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                            dbfac.setNamespaceAware(true);
                    		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
                    		Document tempDoc = docBuilder.parse(fullFileName);
                    		NodeList recordsIds = tempDoc.getElementsByTagName("recordId");
                    		boolean changed = false;
                    		for(int i=0;i<recordsIds.getLength() && !changed;i++){
                    			Node currentNode = recordsIds.item(i);
                    			Node parent = currentNode.getParentNode();
                    			if(parent!=null && parent.getNodeName().equals("control")){
                    				parent = parent.getParentNode();
                    				if(parent!=null && parent.getNodeName().equals("eag")){
                    					currentNode.setTextContent(newRecordIdValue);
                    					changed = true;
                    				}
                    			}
                    		}
                    		TransformerFactory tf = TransformerFactory.newInstance(); // Save changes
                    		Transformer transformer = tf.newTransformer();
                    		transformer.transform(new DOMSource(tempDoc), new StreamResult(new File(fullFileName)));
                        }

    					//The EAG has been validated so it has to be stored in /mnt/repo/country/aiid/EAG/
    					//and it is necessary to update archival_institution table
        				result = eag.saveEAGviaHTTP(fullFileName);
       
        				if (result.equals("error_eagnotstored")) {
        					this.filesNotUploaded.add(fileName);
        					result = "error_eagnotstored";
        				}
        				else if (result.equals("error_eagalreadyuploaded")) {
        					this.filesNotUploaded.add(fileName);
        					result = "error_eagalreadyuploaded";
        				}
        				else if (result.equals("error_archivallandscape")) {
        					this.filesNotUploaded.add(fileName);
        					result = "error_archivallandscape";
        				}
        				else{
            				this.filesUploaded.add(fileName);
            				
            		        /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
            		        /////////////////////////////////////////////////////////////
            				// Remove this block and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
            				if (eag.getEagEmptyInformation()) {
            					//The EAG file should contain the default message added in case Information is empty
            					result = "success_noInformation";
            				}
            				else {
            					result = "success";	
            				}

        				}
        				    					
    				}
    				else{
                        warnings_eag = eag.showWarnings();
    					//The EAG has been neither validated nor converted 
    					log.warn("The file " + fileName + " is not valid");
    					this.filesNotUploaded.add(fileName);
    					result = "error_eagnotvalidatednotconverted";
    				}
    				
    				FileUtils.forceDelete(source);
						    				
				}
				else {
					//The format is not allowed
					log.error("The file type is not XML. Format not allowed.");
					this.filesNotUploaded.add(fileName);
					result = "error_formatnotallowed";
				}

	    	} catch (SAXException e){
                log.error(e.getMessage());
                warnings_eag = new ArrayList<String>();
                warnings_eag.add("The file uploaded is not a correct XML, please see the following exception to know the problem:<br/>" + e.getMessage());
                this.filesNotUploaded.add(fileName);
	    		result = "error_parsing";
            } catch (Exception e) {
	    	    log.error(e.getMessage(),e);
				this.filesNotUploaded.add(fileName);
	    		result = "error_eagnotstored";
	    	}
		}
    	return result;
	}
	
	//Jara: Overwrite a file of Archival Landscape. The existing file is renamed to name_old.
	public String overWriteFile(File file, String fileName, String pathFile, boolean execute){
		
		String result;
		ArchivalLandscape a = new ArchivalLandscape();
		String fullFileName = "";
		String tmpDirectory =pathFile+ "temp" + APEnetUtilities.FILESEPARATOR;
		File[] files = new File(pathFile).listFiles();
		this.filesNotUploaded = new ArrayList<String>();
    	this.filesUploaded = new ArrayList<String>();	
    		
		try {				
			File sfile = new File(tmpDirectory+fileName);
			//Change the database without commit
			String resultStore = a.storeArchives(sfile,execute);
			
			//this.setFasDeleted(a.getFasDeleted());						
			//this.setHgsDeleted(a.getHgsDeleted());			
			
			//Change the repository renaming the files and not deleted the current one
			if (!resultStore.equals("error"))
			{
				if (files.length > 1) 
				{
					/*for (int n=0;n< files.length;n++)
					{
						if (files[n].getName().contains("AL.xml"))
							fullFileName = pathFile + files[n].getName();		
					}*/
					fullFileName = pathFile + a.getmyCountry() + "AL.xml";
				}
				File theFile = new File(fullFileName);
				//theFile.delete();
				theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL_old.xml"));
								
				FileUtils.copyFile(sfile, new File(fullFileName));
				//theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml"));
				File tmpDir = new File(tmpDirectory);
				if (fileName != null)
					this.filesUploaded.add(fileName);
				FileUtils.deleteDirectory(tmpDir);
				
				//Change the global archival landscape
				a.changeAL();
				this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
				this.setArchivalInstitutionsToInsert(a.getArchivalInstitutionsToInsert());
				this.setArchivalInstitutionsNameNotChanged(a.getArchivalInstitutionsNameNotChanged());
				this.setArchivalInstitutionsNameChanged(a.getArchivalInstitutionsNameChanged());
				this.setArchivalInstitutionsParentChanged(a.getArchivalInstitutionsParentChanged());
				this.setArchivalInstitutionsParentNotChanged(a.getArchivalInstitutionsParentNotChanged());
				result= "success";
			}
			else{
				result= "error";
				if (files.length > 1) 
				{
					for (int n=0;n< files.length;n++)
					{
						if (files[n].getName().contains(".xml"))
							fullFileName = pathFile + files[n].getName();		
					}
				}
				File theFile = new File(fullFileName);
				//theFile.delete();
				theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL_old.xml"));
								
				FileUtils.copyFile(sfile, new File(fullFileName));
				theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml"));
				File tmpDir = new File(tmpDirectory);
				if (fileName != null)
					this.filesUploaded.add(fileName);
				FileUtils.deleteDirectory(tmpDir);				
			}
			
		} catch (IOException e) {
			result = "error";
			this.filesNotUploaded.add(fileName);
			log.debug("The file could not be remove. Some errors occurs in process.");
			log.error(e.getMessage());
		}				
		
		return result;
	}
	
	// This method moves all the files extracted from a Zip file to the previous directory (the root directory for the files recently uploaded)
	private void moveToTemp (String tempPath, String path, String format, List<String> filesNotUploaded, List<String> filesUploaded, Integer archivalInstitutionId, String uploadMethodString) {
		File dir = new File(tempPath);
		if (format.equals("other")) {
			String[] files = dir.list();
			for (String fileStr : files) {
				File srcFile = new File(tempPath + fileStr);
				filesNotUploaded.add(fileStr);
				try {
					FileUtils.forceDelete(srcFile);
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		} else {
			String[] files = dir.list( new SuffixFileFilter(format,IOCase.INSENSITIVE));
            final String defaultDirPath = APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
			for (String fileStr : files) {
				File srcFile = new File(tempPath + fileStr);
				File destFile = new File(path + fileStr);
			    if (destFile.exists()) {
			    	// The file already exists in the upload directory. It's necessary to notify to the user
                    log.warn("File already exists in destination folder: " + destFile.getPath());
			    	filesNotUploaded.add(fileStr);
			    	try {
						FileUtils.forceDelete(srcFile);
					} catch (IOException e) {
						log.error(e.getMessage());
					}
			    } else {
			    	try {
						// Moving the file
						// Insert file uploaded into up_file table
			    		JpaUtil.beginDatabaseTransaction();

                        UpFile upFile;
						if(format.equals("xml"))
                            upFile = createUpFile(defaultDirPath, fileStr, uploadMethodString, archivalInstitutionId, FileType.XML);
						else if(format.equals("xsl"))
                            upFile = createUpFile(defaultDirPath, fileStr, uploadMethodString, archivalInstitutionId, FileType.XSL);
						else
                            upFile = createUpFile(defaultDirPath, fileStr, uploadMethodString, archivalInstitutionId, FileType.ZIP);

                        DAOFactory.instance().getUpFileDAO().insertSimple(upFile);
						
						// The file is stored in temp repository and it is necessary to move it
						log.info("Moving file " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
						FileUtils.moveFile(srcFile, destFile);
						filesUploaded.add(fileStr);

						JpaUtil.commitDatabaseTransaction();
					} catch (Exception e) {
						log.error("Error inserting the file " + fileStr + " in up_table (the user was uploading this file to the Dashboard) or error storing the file in temporal up repository [Database and FileSystem Rollback]. Error: " + e.getMessage());
						JpaUtil.rollbackDatabaseTransaction();
						// FileSystem Rollback //todo: For Inteco: Shouldn't it be destFile to be erased if it exists?
						if (srcFile.exists()) {
							try {
								FileUtils.forceDelete(srcFile);
							} catch (IOException e1) {
								log.error("It was not possible to remove the file " + srcFile.getAbsolutePath() + ". Error: " + e1.getMessage());
							}
						}
					}
			    }
			}
		}
	}

    private UpFile createUpFile(String upDirPath, String filePath, String uploadMethodString, Integer aiId, FileType fileType){
        UpFile upFile = new UpFile();
        upFile.setFilename(filePath);
        upFile.setPath(upDirPath + filePath);

        UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(uploadMethodString);
        upFile.setUploadMethod(uploadMethod);

        upFile.setAiId(aiId);

        upFile.setFileType(fileType);

        return upFile;
    }
}
