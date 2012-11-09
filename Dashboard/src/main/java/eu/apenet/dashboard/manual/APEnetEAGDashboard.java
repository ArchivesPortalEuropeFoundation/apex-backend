package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.infraestructure.APEnetEAG;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.HoldingsGuide;


/**
 * User: Eloy García
 * Date: Sep 23d, 2010
 */
/**This class is in charge of managing each EAG file uploaded or edited by a partner. 
 * This class gathers all the operations a partner can do within the Dashboard 
 * Content Provider Information option
 */
public class APEnetEAGDashboard extends APEnetEAG {
		
	//Attributes
	private final Logger log = Logger.getLogger(getClass());
	
	private String handicapped;
	private Boolean hasChangedHandicapped;
	private String workingPlaces;
	private Boolean hasChangedWorkingPlaces;
	private String laboratory;
	private Boolean hasChangedLaboratory;
	private String reproduction;
	private Boolean hasChangedReproduction;
	private String archivalHoldings;
	private Boolean hasChangedArchivalHoldings;
	
    private static final String EAG_XMLNS = "http://www.archivesportaleurope.eu/profiles/APEnet_EAG/";
    private static final String LOCAL_HG_ERROR = "HG_Error";
    private static final String PORTAL_URL = "http://www.archivesportaleurope.eu";
    //private static final String EAG_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    //private static final String EAG_SCHEMA = "http://www.ministryculture.es/";

	//Getters and Setters
	public void setHandicapped(String handicapped) {

		if (!this.handicapped.equals(handicapped)){
			this.hasChangedHandicapped = true;
		}
		
		this.handicapped = handicapped;	
	}

	public String getHandicapped() {
		return handicapped;
	}
	
	public void setWorkingPlaces(String workingPlaces) {

		if (!this.workingPlaces.equals(workingPlaces)){
			this.hasChangedWorkingPlaces = true;
		}

		this.workingPlaces = workingPlaces;	
	}

	public String getWorkingPlaces() {
		return workingPlaces;
	}
	
	public void setLaboratory(String laboratory) {

		if (!this.laboratory.equals(laboratory)){
			this.hasChangedLaboratory = true;
		}

		this.laboratory = laboratory;	
	}

	public String getLaboratory() {
		return laboratory;
	}
	
	public void setReproduction(String reproduction) {

		if (!this.reproduction.equals(reproduction)){
			this.hasChangedReproduction = true;
		}
	
		this.reproduction = reproduction;
	}

	public String getReproduction() {
		return reproduction;
	}
	
	public void setArchivalHoldings(String archivalHoldings) {
		
		if (!this.archivalHoldings.equals(archivalHoldings)){
			this.hasChangedArchivalHoldings = true;
		}
	
		this.archivalHoldings = archivalHoldings;

	}

	public String getArchivalHoldings() {
		return archivalHoldings;
	}
	
	//Constructor
	public APEnetEAGDashboard(Integer aiId, String tempEagPath) {
		super(new StrutsResourceBundleSource(), aiId, tempEagPath);
				
		if (this.eagPath == null){
					
			//respondiblePersonSurname and responsiblePersonName have been removed from EAG Web Form for the moment
			this.responsiblePersonSurname = "";
			this.responsiblePersonName = "";
			this.access = "yes";
			this.handicapped = "yes";
			this.workingPlaces = "";
			this.library = "yes";
			this.laboratory = "yes";
			this.reproduction = "yes";
			this.automation = "yes";
			this.archivalHoldings = "";
			
		}
		else {
			
			//respondiblePersonSurname and responsiblePersonName have been removed from EAG Web Form for the moment
			this.responsiblePersonSurname = "";
			this.responsiblePersonName = "";
			this.handicapped = this.extractAttributeFromEag("archguide/desc/buildinginfo/handicapped", "question", true);
			if (this.handicapped.equals("error"))
				this.handicapped="yes";
			this.workingPlaces = this.extractAttributeFromEag("archguide/desc/searchroom/num", null, true);
			if (this.workingPlaces.equals("error"))
				this.workingPlaces="";
			this.library = this.extractAttributeFromEag("archguide/desc/techservices/library", "question", true);
			if (this.library.equals("error"))
				this.library="yes";
			this.laboratory = this.extractAttributeFromEag("archguide/desc/techservices/restorationlab", "question", true);
			if (this.laboratory.equals("error"))
				this.laboratory="yes";
			this.reproduction = this.extractAttributeFromEag("archguide/desc/techservices/reproductionser", "question", true);
			if (this.reproduction.equals("error"))
				this.reproduction="yes";
			this.automation = this.extractAttributeFromEag("archguide/desc/automation", "question", true);
			if (this.automation.equals("error"))
				this.automation="yes";
			this.archivalHoldings = this.extractAttributeFromEag("archguide/desc/extent/num", null, true);
			if (this.archivalHoldings.equals("error"))
				this.archivalHoldings="";
		}
		
		this.hasChangedHandicapped = false;
		this.hasChangedWorkingPlaces = false;
		this.hasChangedLaboratory = false;
		this.hasChangedReproduction = false;
		this.hasChangedArchivalHoldings = false;
			
	}

	//Methods
	public Boolean APEnetEAGValidate (String filename) throws APEnetException, SAXException {
        //EAG file is stored temporally in the location defined in eagPath attribute
        log.debug("Path of EAG: " + eagPath);
        log.debug("Filename of EAG: " + filename);
        File file = new File(eagPath + filename);
        Xsd_enum schema = Xsd_enum.XSD_APE_EAG_SCHEMA;
        try {
            InputStream in = new FileInputStream(file);
            List<SAXParseException> exceptions = DocumentValidation.xmlValidation(in, schema);
            if(exceptions!=null){
                StringBuilder warn;
                warnings_ead = new ArrayList<String>();
                for(SAXParseException exception : exceptions){
                    warn = new StringBuilder();
                    warn.append("l.").append(exception.getLineNumber()).append(" c.").append(exception.getColumnNumber()).append(": ").append(exception.getMessage()).append("<br/>");
                    warnings_ead.add(warn.toString());
                }
                return false;
            }
        } catch (SAXException e){
            throw e;
        } catch (Exception e){
            throw new APEnetException("Exception while validating an EAG file", e);
        }
		return true;
	}

	public Boolean convertToAPEnetEAG (String filename) throws APEnetException {
        //EAG file is stored temporally in the location defined in eagPath attribute
        File file = new File(eagPath + filename);
        try {
            InputStream in;
            final String xslfilename = "changeNS.xsl";
            String outputFilePath = eagPath + "converted_" + filename;
            File outputfile = new File(outputFilePath);
            String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + xslfilename;
            in = new FileInputStream(file);
            TransformationTool.createTransformation(in, outputfile, FileUtils.openInputStream(new File(xslFilePath)), null, true, true, null, true, null);
            in.close();
            FileUtils.copyFile(outputfile, file);
        } catch (Exception e){
            throw new APEnetException("Exception while converting in APEnet EAG", e);
        }
		return true;
	}

	public List<String> showWarnings () {
		return warnings_ead;
	}

	//This method creates a new EAG file via Web Form
	public String saveEAGModified () {
		
		ActionSupport actionSupport = new ActionSupport();
		String value = "";
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);
		Boolean isNew = true;
		Boolean someRepositorguideInformationEmpty = false;

        log.debug(archivalInstitution.toString());
        
		//First, it is necessary to check if global Archival Landscape and local Archival Landscape are ready for using
		//and check if the Archival Institution is within the local and global Archival Landscape files
		File gArchivalLandscape = new File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml");
		File lArchivalLandscape = new File(APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml");

		if (gArchivalLandscape.exists() && lArchivalLandscape.exists() && this.isArchivalInstitutionInArchivalLandscape()){
			
			//It is necessary to check if this EAG has been uploaded before for another archival institution			
			if (this.isEagAlreadyUploaded()){
				value = "error_eagalreadyuploaded";
			}
			else {
				//EAG file will be always created from scratch
				//If there is an old EAG file, it has to be deleted first
//				if (!(this.eagPath == null)){
//					isNew = false;
//					File oldEAG = new File(this.getEagPath());
//					if (oldEAG.exists()) {
//						log.info("Removing old EAG " + this.eagPath);
//						try {
//							FileUtils.forceDelete(oldEAG);													
//						}
//						catch (IOException e) {
//							log.error("The EAG with path: " + this.eagPath + "couldn't be removed from the filesystem");
//						}
//					}
//
//					oldEAG = null;
//				}

				if (this.eagPath == null) {
					
					//It is necessary to create a new EAG file from scratch
					//First, it is necessary to build the path
					this.eagPath = APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml";
					File eagDir = new File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG");
		            if(!eagDir.exists())
		                eagDir.mkdirs();

		            String storagePath = APEnetUtilities.getConfig().getRepoDirPath() + this.eagPath;
					File eagFile = new File(storagePath);
		            
		            try {
			            //Create XML document
			            DocumentBuilder documentBuilder;
			            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			            Document xmlDocument = documentBuilder.newDocument();
			            Result result = new StreamResult(eagFile);
			            Source source;
		                Element root = xmlDocument.createElement("eag");
		                root.setAttribute("audience", "external");
		                root.setAttribute("xmlns", EAG_XMLNS);
		                //root.setAttribute("xmlns:xsi", EAG_XSI);//
		                //root.setAttribute("xsi:schemaLocation", EAG_SCHEMA);//
		                xmlDocument.appendChild(root);
		                	Element eagheaderNode = xmlDocument.createElementNS(EAG_XMLNS, "eagheader");
		                	eagheaderNode.setAttribute("countryencoding", "iso3166-1");
		                	eagheaderNode.setAttribute("dateencoding", "iso8601");
		                	eagheaderNode.setAttribute("langencoding", "iso639-2b");
		                	eagheaderNode.setAttribute("repositoryencoding", "iso15511");
		                	eagheaderNode.setAttribute("scriptencoding", "iso15924");
		                	eagheaderNode.setAttribute("status", "edited");
		                	root.appendChild(eagheaderNode);
		                    Element finalNode = xmlDocument.createElementNS(EAG_XMLNS, "eagid");
		                    finalNode.setTextContent(this.getId());
		                    eagheaderNode.appendChild(finalNode);
		                    Element mainhistNode = xmlDocument.createElementNS(EAG_XMLNS, "mainhist");
		                    eagheaderNode.appendChild(mainhistNode);
		                    Element maineventNode = xmlDocument.createElementNS(EAG_XMLNS, "mainevent");
		                    maineventNode.setAttribute("maintype", "create");
		                    mainhistNode.appendChild(maineventNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "date");
		                    finalNode.setAttribute("calendar", "gregorian");
		                    finalNode.setAttribute("era", "ce");

	                    	//The EAG is new so the date must be the current date
	                        Calendar date = new GregorianCalendar();
	                        Integer month = date.get(Calendar.MONTH) + 1;
	                        String monthString = "";
	                        
	                        if (month < 10) {
	                        	monthString = "0" + month.toString(); 
	                        }
	                        else {
	                        	monthString = month.toString();
	                        }
	                        
	                        Integer day = date.get(Calendar.DAY_OF_MONTH);
	                        String dayString = "";
	                        
	                        if (day < 10) {
	                        	dayString = "0" + day.toString();
	                        }
	                        else {
	                        	dayString = day.toString();
	                        }
	                        
	                        finalNode.setAttribute("normal", Integer.toString(date.get(Calendar.YEAR)) + "-" + monthString + "-" + dayString);
	                        
	                        date = null;
	                        month = null;
	                        monthString = null;
	                        day = null;
	                        dayString = null;
		                    	
		                    maineventNode.appendChild(finalNode);

		                    Element respeventNode = xmlDocument.createElementNS(EAG_XMLNS, "respevent");
		                    maineventNode.appendChild(respeventNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "surnames");
		                    finalNode.setTextContent(this.getResponsiblePersonSurname());
		                    respeventNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "firstname");
		                    finalNode.setTextContent(this.getResponsiblePersonName());
		                    respeventNode.appendChild(finalNode);
		                    
		                	Element archguideNode = xmlDocument.createElementNS(EAG_XMLNS, "archguide");
		                	root.appendChild(archguideNode);
		                    Element identityNode = xmlDocument.createElementNS(EAG_XMLNS, "identity");
		                    archguideNode.appendChild(identityNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "repositorid");
		                    finalNode.setAttribute("countrycode", archivalInstitution.getCountry().getIsoname());
		                    finalNode.setAttribute("repositorycode", this.getId());
		                    identityNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "autform");
		                    finalNode.setTextContent(this.getName());
		                    identityNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "parform");
		                    finalNode.setTextContent(this.getEnglishName());
		                    identityNode.appendChild(finalNode);
		                    Element descNode = xmlDocument.createElementNS(EAG_XMLNS, "desc");
		                    archguideNode.appendChild(descNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "geogarea");
		                    finalNode.setTextContent("Europe");
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "country");
		                    finalNode.setTextContent(this.getCountry());
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "municipality");
		                    finalNode.setTextContent(this.getCityTown());
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "street");
		                    finalNode.setTextContent(this.getStreet());
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "postalcode");
		                    finalNode.setTextContent(this.getPostalCode());
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "telephone");
		                    finalNode.setTextContent(this.getTelephone());
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "email");
		                    finalNode.setAttribute("href", this.getEmailAddress());
		                    descNode.appendChild(finalNode);
		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "webpage");
		                    finalNode.setAttribute("href", this.getWebPage());
		                    descNode.appendChild(finalNode);

		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "access");
		                    finalNode.setAttribute("question", this.getAccess());
		                    descNode.appendChild(finalNode);

		                	if (this.repositorguideInformation != null) {
		                		Element includeNode = null;
		                		finalNode = xmlDocument.createElementNS(EAG_XMLNS, "repositorguides");
		                		for (int i = 0; i < this.repositorguideInformation.size(); i ++ ) {
	                				// It is necessary to add a new tag <repositorguide> with the information
	    			                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "repositorguide");
	    			                
	    			                /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
	    			                /////////////////////////////////////////////////////////////
	    			                // Remove this block and uncomment the other block if 'Link to holdings guide' default text is wanted again within EAG web form
	    			                if (this.repositorguideInformation.get(i).equals("")){
	    			                	//The information is empty, so it will be necessary to check the URL
	    			                	if (!this.repositorguideURL.get(i).equals("")) {
		    			                	someRepositorguideInformationEmpty = true;
		    			                	includeNode.setTextContent(actionSupport.getText("label.ai.hg.information.content.default"));
	    			                	}
	    			                	else {
			    			                includeNode.setTextContent(this.repositorguideInformation.get(i));
	    			                	}
	    			                }
	    			                else {
		    			                includeNode.setTextContent(this.repositorguideInformation.get(i));
	    			                }
	    			                /*
	    			                if (this.repositorguideInformation.get(i).equals("")){
	    			                	//The information is empty, so it will be necessary to add some Information
	    			                	someRepositorguideInformationEmpty = true;
	    			                	includeNode.setTextContent(actionSupport.getText("label.ai.hg.information.content.default"));
	    			                }
	    			                else {
		    			                includeNode.setTextContent(this.repositorguideInformation.get(i));
	    			                }
	    			                */

	    			                // It is necessary to add a new attribute href with the URL if it is not empty
	    			                // Remove this block and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
		                			if (this.repositorguideURL.get(i) != "") {
		    			                
		    			                if (this.repositorguideResource.get(i).equals(REPOSITORGUIDE_LOCAL_RESOURCE)) {
		    			                	
		    			                	HoldingsGuide holdingsGuideSelected = this.findHoldingsGuideSelected(this.repositorguidePossibleHGTitle.get(i));
		    			                	if (holdingsGuideSelected.getTitle().equals(LOCAL_HG_ERROR)) {
			    			                	includeNode.setAttribute("href", PORTAL_URL);	    			                		
		    			                	}
		    			                	else {
			    			                	includeNode.setAttribute("href", PORTAL_CONTEXT + SECOND_DISPLAY_PREVIEW_ACTION + "?id=" + holdingsGuideSelected.getId() + "&xmlTypeId=" + XmlType.EAD_HG.getIdentifier());		    			                	
		    			                	}
		    			                	
		    			                }
		    			                else {
		    			                	includeNode.setAttribute("href", this.repositorguideURL.get(i));
		    			                }
		    			                
		                			}
		                			else {
		                				includeNode.setAttribute("href", "");
		                			}
		                			/*
		                			if (this.repositorguideURL.get(i) != "") {
		    			                
		    			                if (this.repositorguideResource.get(i).equals(REPOSITORGUIDE_LOCAL_RESOURCE)) {
		    			                	
		    			                	HoldingsGuide holdingsGuideSelected = this.findHoldingsGuideSelected(this.repositorguidePossibleHGTitle.get(i));
		    			                	if (holdingsGuideSelected.getTitle().equals(LOCAL_HG_ERROR)) {
			    			                	includeNode.setAttribute("href", PORTAL_URL);	    			                		
		    			                	}
		    			                	else {
			    			                	includeNode.setAttribute("href", PORTAL_CONTEXT + SECOND_DISPLAY_PREVIEW_ACTION + "?id=" + holdingsGuideSelected.getId() + "&xmlTypeId=" + XmlType.EAD_HG.getIdentifier());		    			                	
		    			                	}
		    			                	
		    			                }
		    			                else {
		    			                	includeNode.setAttribute("href", this.repositorguideURL.get(i));
		    			                }
		    			                
		                			}
									*/
	    			                /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
	    			                /////////////////////////////////////////////////////////////
		                			
		                			finalNode.appendChild(includeNode);
				                }
		                		includeNode = null;
				                descNode.appendChild(finalNode);
		                		
		                	}

		                    finalNode = xmlDocument.createElementNS(EAG_XMLNS, "buildinginfo");
				                Element includeNode = xmlDocument.createElementNS(EAG_XMLNS, "searchroom");
				                Element includeNode2 = xmlDocument.createElementNS(EAG_XMLNS, "num");
				                includeNode2.setAttribute("unit", "site");
				                includeNode2.setTextContent(this.getWorkingPlaces());
				                includeNode.appendChild(includeNode2);
				                finalNode.appendChild(includeNode);

				                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "handicapped");
				                includeNode.setAttribute("question", this.getHandicapped());
				                finalNode.appendChild(includeNode);
				                descNode.appendChild(finalNode);

				            finalNode = xmlDocument.createElementNS(EAG_XMLNS, "extent");
				                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "num");
				                includeNode.setAttribute("unit", "linearmetre");
				                includeNode.setTextContent(this.getArchivalHoldings());
				                finalNode.appendChild(includeNode);
				                descNode.appendChild(finalNode);

				            finalNode = xmlDocument.createElementNS(EAG_XMLNS, "organization");
				                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "descunit");
				                includeNode.setAttribute("classcode", "linearmetre");
				                includeNode.setAttribute("fathercode", "");
				                includeNode.setAttribute("level", "fonds");

					                includeNode2 = xmlDocument.createElementNS(EAG_XMLNS, "unitid");
					                includeNode2.setAttribute("href", "http://www.apenet.eu");
					                includeNode.appendChild(includeNode2);

					                includeNode2 = xmlDocument.createElementNS(EAG_XMLNS, "unittitle");
					                includeNode.appendChild(includeNode2);

					                includeNode2 = xmlDocument.createElementNS(EAG_XMLNS, "date");
					                includeNode2.setAttribute("calendar", "gregorian");
					                includeNode2.setAttribute("era", "ce");
					                includeNode2.setAttribute("normal", "2010");
					                includeNode.appendChild(includeNode2);

					                includeNode2 = xmlDocument.createElementNS(EAG_XMLNS, "extent");
						                Element includeNode3 = xmlDocument.createElementNS(EAG_XMLNS, "num");
						                includeNode3.setAttribute("unit", "linearmetre");
						                includeNode2.appendChild(includeNode3);
						            includeNode.appendChild(includeNode2);
						    finalNode.appendChild(includeNode);
					        descNode.appendChild(finalNode);

			                finalNode = xmlDocument.createElementNS(EAG_XMLNS, "techservices");
				                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "restorationlab");
				                includeNode.setAttribute("question", this.getLaboratory());
				                finalNode.appendChild(includeNode);

				                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "reproductionser");
				                includeNode.setAttribute("question", this.getReproduction());
				                finalNode.appendChild(includeNode);
			
				                includeNode = xmlDocument.createElementNS(EAG_XMLNS, "library");
				                includeNode.setAttribute("question", this.getLibrary());
				                finalNode.appendChild(includeNode);
				            descNode.appendChild(finalNode);

			                finalNode = xmlDocument.createElementNS(EAG_XMLNS, "automation");
			                finalNode.setAttribute("question", this.getAutomation());
			                descNode.appendChild(finalNode);

			            //Create the new file 
			            source = new DOMSource(xmlDocument);

			            //Write the XML
			            Transformer transformer;
			            try {
			                transformer = TransformerFactory.newInstance().newTransformer();
			                transformer.transform(source, result);
			                
			                //Finally, the new path, autform and repositorycode are stored in archival_institution table
			                if (isNew) {
			                	//If the EAG is new, the registration date has to be stored in Data Base
				                Date dateNow = new Date();
				                archivalInstitution.setRegistrationDate(dateNow);	                	
			                }

			                archivalInstitution.setEagPath(eagPath);
			                archivalInstitution.setAutform(this.getName());
			                archivalInstitution.setRepositorycode(this.getId());
			                archivalInstitutionDao.store(archivalInstitution);
			                

							log.info("The EAG " + this.eagPath + " has been created and stored in repository");
			                ChangeControl.logOperation("Upload eag");
			                
			                if (someRepositorguideInformationEmpty) {
			                	value = "correct_withoutRepositorguideInformation";
			                }
			                else {
			                	value = "correct";
			                }
			                
			            } 
			            catch (TransformerConfigurationException e) {
							log.error("Error configuring Transformer during the creation of " + storagePath + " file. " +  e.getMessage());
			                value = "error";
			            } 
			            catch (TransformerFactoryConfigurationError e) {
							log.error("Error configuring Transformer Factory during the creation of " + storagePath + " file. " +  e.getMessage());
			                value = "error";
			            } 
			            catch (TransformerException e) {
							log.error("Transformer error during the creation of " + storagePath + " file. " +  e.getMessage());
			                value = "error";
			            }
			            finally {
			            	root = null;
			            	finalNode = null;
			            	eagheaderNode = null;
			            	mainhistNode = null;
			            	maineventNode = null;
			            	respeventNode = null;
			            	archguideNode = null;
			            	identityNode = null;
			            	descNode = null;
		                    includeNode = null;
		                    includeNode2 = null;
		                    includeNode3 = null;
			            	transformer = null;
			            	actionSupport = null;
			            }
			        } 
			       	catch (ParserConfigurationException e1) {
						log.error("Error building " + storagePath + " file: " + e1.getMessage());
			            value = "error";
			        }

				}
				else {

					Integer eagModifyingProcessState = 0;
					String oldEAGpath = null;
					
					//EAG file already exists. It will be necessary to modify it
					try {
			        	
						//First, it is necessary to backup the original EAG
						File srcFile = new File(this.eagPath);
						File destFile = new File(this.eagPath + "_old");
						FileUtils.copyFile(srcFile, destFile);

						eagModifyingProcessState = 1;
						
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			        	//For those nodes modified, the name space won't be taken into account
						dbFactory.setNamespaceAware(true);
			        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			        	InputStream sfile = new FileInputStream(this.eagPath);
			        	Document doc = dBuilder.parse(sfile);
			        	doc.getDocumentElement().normalize();
			        	
			        	//The EAG is being modified, so it is necessary to add a new updating date
			        	//First, it is necessary to remove the older updating date if exists
			        	NamedNodeMap attributes = null;
			        	NodeList maineventNodeList = doc.getElementsByTagName("mainevent");
			        	for(int i = 0; i < maineventNodeList.getLength(); i ++){
			        		if(maineventNodeList.item(i).hasAttributes()){
			        			attributes = maineventNodeList.item(i).getAttributes();
			        			for(int j = 0; j < attributes.getLength(); j ++){
			        				if(attributes.item(j).getNodeName().equals("maintype") && attributes.item(j).getNodeValue().equals("update")){
			        					maineventNodeList.item(i).getParentNode().removeChild(maineventNodeList.item(i));
			        					break;
			        				}
			        			}
			        		}
			        	}
			        	
			        	//Second, a new mainevent is created for inserting the updating date
			        	maineventNodeList = doc.getElementsByTagName("mainhist");
			        	Node mainhistNode = maineventNodeList.item(0);
			        	
                        Element maineventNode = doc.createElementNS(EAG_XMLNS, "mainevent");
                        maineventNode.setAttribute("maintype", "update");
                        mainhistNode.appendChild(maineventNode);
                        Element finalNode = doc.createElementNS(EAG_XMLNS, "date");
                        finalNode.setAttribute("calendar", "gregorian");
                        finalNode.setAttribute("era", "ce");
                    	
                        Calendar date = new GregorianCalendar();
                        Integer month = date.get(Calendar.MONTH) + 1;
                        String monthString = "";
                        
                        if (month < 10) {
                        	monthString = "0" + month.toString(); 
                        }
                        else {
                        	monthString = month.toString();
                        }
                        
                        Integer day = date.get(Calendar.DAY_OF_MONTH);
                        String dayString = "";
                        
                        if (day < 10) {
                        	dayString = "0" + day.toString();
                        }
                        else {
                        	dayString = day.toString();
                        }
                        
                        finalNode.setAttribute("normal", Integer.toString(date.get(Calendar.YEAR)) + "-" + monthString + "-" + dayString);

                        maineventNode.appendChild(finalNode);

	                    Element respeventNode = doc.createElementNS(EAG_XMLNS, "respevent");
	                    maineventNode.appendChild(respeventNode);
	                    finalNode = doc.createElementNS(EAG_XMLNS, "surnames");
	                    finalNode.setTextContent(this.getResponsiblePersonSurname());
	                    respeventNode.appendChild(finalNode);
	                    finalNode = doc.createElementNS(EAG_XMLNS, "firstname");
	                    finalNode.setTextContent(this.getResponsiblePersonName());
	                    respeventNode.appendChild(finalNode);

                        date = null;
                        month = null;
                        monthString = null;
                        day = null;
                        dayString = null;
                        maineventNodeList = null;
                        attributes = null;
                        mainhistNode = null;
                        maineventNode = null;
                        finalNode = null;

			        	if (hasChangedName) {
				        	NodeList nodeList = doc.getElementsByTagName("autform");
				        	nodeList.item(0).setTextContent(this.getName());

				        	nodeList = null;
						}
						
						if (hasChangedEnglishName) {
				        	NodeList nodeList = doc.getElementsByTagName("parform");
				        	nodeList.item(0).setTextContent(this.getEnglishName());

				        	nodeList = null;
						}

						if (hasChangedId) {
							NodeList nodeList = doc.getElementsByTagName("eagid");
				        	nodeList.item(0).setTextContent(this.getId());

				        	nodeList = null;
						}
						
						/*
						if (hasChangedResponsiblePersonSurname) {
				        	NodeList nodeList = doc.getElementsByTagName("surnames");
				        	nodeList.item(0).setTextContent(this.getResponsiblePersonSurname());

				        	nodeList = null;
						}
						
						if (hasChangedResponsiblePersonName) {
				        	NodeList nodeList = doc.getElementsByTagName("firstname");
				        	nodeList.item(0).setTextContent(this.getResponsiblePersonSurname());

				        	nodeList = null;
						}
						*/
						
						if (hasChangedCountry) {
				        	NodeList nodeList = doc.getElementsByTagName("country");
				        	nodeList.item(0).setTextContent(this.getCountry());

				        	nodeList = null;
						}
						
						if (hasChangedCityTown) {
				        	NodeList nodeList = doc.getElementsByTagName("municipality");
				        	nodeList.item(0).setTextContent(this.getCityTown());

				        	nodeList = null;
						}
							
						if (hasChangedPostalCode) {
				        	NodeList nodeList = doc.getElementsByTagName("postalcode");
				        	nodeList.item(0).setTextContent(this.getPostalCode());

				        	nodeList = null;
						}

						if (hasChangedStreet) {
				        	NodeList nodeList = doc.getElementsByTagName("street");
				        	nodeList.item(0).setTextContent(this.getStreet());

				        	nodeList = null;
						}

						if (hasChangedTelephone) {
				        	NodeList nodeList = doc.getElementsByTagName("telephone");
				        	nodeList.item(0).setTextContent(this.getTelephone());

				        	nodeList = null;
						}
						
						if (hasChangedEmailAddress) {
				        	NodeList nodeList = doc.getElementsByTagName("email");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("href");
			                attributeNode.setTextContent(this.getEmailAddress());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}
						
						if (hasChangedWebPage) {
				        	NodeList nodeList = doc.getElementsByTagName("webpage");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("href");
			                attributeNode.setTextContent(this.getWebPage());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}
						
						if (hasChangedAccess) {
				        	NodeList nodeList = doc.getElementsByTagName("access");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("question");
			                attributeNode.setTextContent(this.getAccess());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}
						
	                	//Inserting repositorguides if it is necessary
						//First, it is necessary to remove the old repositorguides 
						NodeList nodeListTemp = doc.getElementsByTagName("repositorguides");
                		for(int i=0;i<nodeListTemp.getLength();i++){ //It should be just one
                			nodeListTemp.item(i).getParentNode().removeChild(nodeListTemp.item(i));
                		}
						
                		//Second, it is necessary to create and insert a new repositorguides if the user has added someone.
                		if (this.repositorguideInformation != null) {
	                		
	                		Element includeNode = null;
	                		finalNode = doc.createElementNS(EAG_XMLNS, "repositorguides");
	                		
	                		for (int i = 0; i < this.repositorguideInformation.size(); i ++ ) {
                				// It is necessary to add a new tag <repositorguide> with the information
    			                includeNode = doc.createElementNS(EAG_XMLNS, "repositorguide");

    			                /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
    			                /////////////////////////////////////////////////////////////
    			                // Remove this block and uncomment the other block if 'Link to holdings guide' default text is wanted again within EAG web form
    			                if (this.repositorguideInformation.get(i).equals("")){
    			                	//The information is empty, so it will be necessary to check the URL
    			                	if (!this.repositorguideURL.get(i).equals("")) {
	    			                	someRepositorguideInformationEmpty = true;
	    			                	includeNode.setTextContent(actionSupport.getText("label.ai.hg.information.content.default"));
    			                	}
    			                	else {
		    			                includeNode.setTextContent(this.repositorguideInformation.get(i));
    			                	}
    			                }
    			                else {
	    			                includeNode.setTextContent(this.repositorguideInformation.get(i));
    			                }
    			                /*
    			                if (this.repositorguideInformation.get(i).equals("")){
    			                	//The information is empty, so it will be necessary to add some Information
    			                	someRepositorguideInformationEmpty = true;
    			                	includeNode.setTextContent(actionSupport.getText("label.ai.hg.information.content.default"));
    			                }
    			                else {
	    			                includeNode.setTextContent(this.repositorguideInformation.get(i));
    			                }

    			                 */
    			                
    			                // It is necessary to add a new attribute href with the URL if it is not empty
    			                // Remove this block and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
	                			if (this.repositorguideURL.get(i) != "") {
	    			                
	    			                if (this.repositorguideResource.get(i).equals(REPOSITORGUIDE_LOCAL_RESOURCE)) {
	    			                	
	    			                	HoldingsGuide holdingsGuideSelected = this.findHoldingsGuideSelected(this.repositorguidePossibleHGTitle.get(i));
	    			                	if (holdingsGuideSelected.getTitle().equals(LOCAL_HG_ERROR)) {
		    			                	includeNode.setAttribute("href", PORTAL_URL);	    			                		
	    			                	}
	    			                	else {
		    			                	includeNode.setAttribute("href", PORTAL_CONTEXT + SECOND_DISPLAY_PREVIEW_ACTION + "?id=" + holdingsGuideSelected.getId() + "&xmlTypeId=" + XmlType.EAD_HG.getIdentifier());		    			                	
	    			                	}
	    			                	
	    			                }
	    			                else {
	    			                	includeNode.setAttribute("href", this.repositorguideURL.get(i));
	    			                }
	    			                
	                			}
	                			else {
	                				includeNode.setAttribute("href", "");
	                			}

	                			/*
    			                // It is necessary to add a new attribute href with the URL if it is not empty
	                			if (this.repositorguideURL.get(i) != "") {
	    			                
	    			                if (this.repositorguideResource.get(i).equals(REPOSITORGUIDE_LOCAL_RESOURCE)) {
	    			                	
	    			                	HoldingsGuide holdingsGuideSelected = this.findHoldingsGuideSelected(this.repositorguidePossibleHGTitle.get(i));
	    			                	if (holdingsGuideSelected.getTitle().equals(LOCAL_HG_ERROR)) {
		    			                	includeNode.setAttribute("href", PORTAL_URL);	    			                		
	    			                	}
	    			                	else {
		    			                	includeNode.setAttribute("href", PORTAL_CONTEXT + SECOND_DISPLAY_PREVIEW_ACTION + "?id=" + holdingsGuideSelected.getId() + "&xmlTypeId=" + XmlType.EAD_HG.getIdentifier());		    			                	
	    			                	}
	    			                	
	    			                }
	    			                else {
	    			                	includeNode.setAttribute("href", this.repositorguideURL.get(i));
	    			                }
	    			                
	                			}
	                			 */
    			                /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
    			                /////////////////////////////////////////////////////////////
	                			
	                			finalNode.appendChild(includeNode);
			                }

							if (doc.getElementsByTagName("buildinginfo").getLength()==1) {
								doc.getElementsByTagName("buildinginfo").item(0).getParentNode().insertBefore(finalNode, doc.getElementsByTagName("buildinginfo").item(0) );
							}else{
								doc.getElementsByTagName("desc").item(0).appendChild(finalNode);
							}

	                		includeNode = null;
	                		finalNode = null;
	                	}

						if (hasChangedWorkingPlaces) {
							Boolean numFound = false;
							int i = 0;
				        	NodeList nodeList = doc.getElementsByTagName("searchroom");
				        	NodeList childNodeList = nodeList.item(0).getChildNodes();
				        	while (!numFound && i < childNodeList.getLength()) {

					        	if ((childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) && (childNodeList.item(i).getNodeName().equals("num"))) {

						        	childNodeList.item(i).setTextContent(this.getWorkingPlaces());

						        	nodeList = null;
						        	numFound = true;
					        	}
					        	
				        		i = i + 1;
				        		
				        	}
				        	
				        	nodeList = null;
				        	childNodeList = null;
						}

						if (hasChangedHandicapped) {
				        	NodeList nodeList = doc.getElementsByTagName("handicapped");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("question");
			                attributeNode.setTextContent(this.getHandicapped());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}

						if (hasChangedLibrary) {
				        	NodeList nodeList = doc.getElementsByTagName("library");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("question");
			                attributeNode.setTextContent(this.getLibrary());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}

						if (hasChangedLaboratory) {
							NodeList nodeList = doc.getElementsByTagName("restorationlab");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("question");
			                attributeNode.setTextContent(this.getLaboratory());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}

						if (hasChangedReproduction) {
				        	NodeList nodeList = doc.getElementsByTagName("reproductionser");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("question");
			                attributeNode.setTextContent(this.getReproduction());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}

						if (hasChangedAutomation) {
				        	NodeList nodeList = doc.getElementsByTagName("automation");
			                attributes = nodeList.item(0).getAttributes();    
			                Node attributeNode = attributes.getNamedItem("question");
			                attributeNode.setTextContent(this.getAutomation());

				        	nodeList = null;
				        	attributes = null;
				        	attributeNode = null;
						}

						if (hasChangedArchivalHoldings) {
							Boolean numFound = false;
							int i = 0;
				        	NodeList nodeList = doc.getElementsByTagName("extent");
				        	NodeList childNodeList = nodeList.item(0).getChildNodes();
				        	while (!numFound && i < childNodeList.getLength()) {

					        	if ((childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) && (childNodeList.item(i).getNodeName().equals("num"))) {

						        	childNodeList.item(i).setTextContent(this.getArchivalHoldings());

						        	nodeList = null;
						        	numFound = true;
					        	}
					        	
				        		i = i + 1;
				        		
				        	}
				        	
				        	nodeList = null;
				        	childNodeList = null;
						}
						
						//Write the XML
			            if (hasChangedId) {
			            	//If eadid has changed, it is necessary to: 
			    			//Remove the old file
			            	oldEAGpath = this.getEagPath();
							File oldEAG = new File(oldEAGpath);
							if (oldEAG.exists()) {
								log.info("Removing EAG " + this.eagPath);
								FileUtils.forceDelete(oldEAG);							
							}
							
							oldEAG = null;

			            	//Create a new path with the new name
			    			this.eagPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml";
			    			
			            }

			            Result result = new StreamResult(new java.io.File(this.eagPath));;
			            Source source = new DOMSource(doc);
			            Transformer transformer;
		                transformer = TransformerFactory.newInstance().newTransformer();
		                transformer.transform(source, result);
		                
		                eagModifyingProcessState = 2;
		                
		                //Second, the new path, autform and repositorycode are updated in archival_institution table
		                HibernateUtil.beginDatabaseTransaction();
		                
		                Date dateNow = new Date();
		                archivalInstitution.setRegistrationDate(dateNow);
		                archivalInstitution.setEagPath(APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");
		                archivalInstitution.setAutform(this.getName());
		                archivalInstitution.setRepositorycode(this.getId());
		                archivalInstitutionDao.updateSimple(archivalInstitution);
		                
		                eagModifyingProcessState = 3;
		                
		                //FINAL COMMITS

						//Final commit in the File System
		                File backupFile = null;
		                if (hasChangedId) {
							backupFile = new File(oldEAGpath + "_old");		                	
		                }
		                else {
							backupFile = new File(this.eagPath + "_old");
		                }
						FileUtils.forceDelete(backupFile);

						//Final commit in Database
						HibernateUtil.commitDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						
		    			result = null;
		    			source = null;
		    			transformer = null;
						dbFactory = null;
						dBuilder = null;
						sfile = null;
						doc = null;
						srcFile = null;
						destFile = null;
						backupFile = null;

		    			//Register operation
		                ChangeControl.logOperation("Upload eag");
						log.info("EAG " + this.eagPath + " has been successfuly modified");
						
		                if (someRepositorguideInformationEmpty) {
		                	value = "correct_withoutRepositorguideInformation";
		                }
		                else {
		                	value = "correct";
		                }
							        		        	
			        }
			        catch (Exception e) {

						if (eagModifyingProcessState == 0) {
							//There were errors during backup the file
							File backupEAG = new File(this.getEagPath() + "_old");
							if (backupEAG.exists()) {
								try {
									FileUtils.forceDelete(backupEAG);
								}
								catch (Exception ex) {
									log.error("Error removing backup EAG file. Error:" + e.getMessage());
								}
							}
							log.error("There were errors during backup the eag file " + this.eagPath + ". Error: " + e.getMessage());
						}
						
						if (eagModifyingProcessState == 1) {
							//There were errors during the modification of the EAG
							//It is necessary to make a file system rollback
							//First, the eag file is removed
							File currentEAG = new File(this.getEagPath());
							try {
								FileUtils.forceDelete(currentEAG);								
							}
							catch (Exception ex) {
								log.error("Error removing current EAG. Error:" + e.getMessage());
							}
							
							//Second, the backup file is restored
							File backupEAG = null;
							if (hasChangedId) {
								backupEAG = new File(oldEAGpath + "_old");
								currentEAG = new File(oldEAGpath);
							}
							else {
								backupEAG = new File(this.getEagPath() + "_old");
							}
							
							try {
								FileUtils.copyFile(backupEAG, currentEAG);
							}
							catch (Exception ex) {
								log.error("Error restoring backup EAG file. Error:" + e.getMessage());
							}

							
							//Third, the backup file is removed
							try {
								FileUtils.forceDelete(backupEAG);
							}
							catch (Exception ex) {
								log.error("Error removing backup EAG file. Error:" + e.getMessage());
							}
							
							currentEAG = null;
							backupEAG = null;
							
							log.error("There were errors during the modification of the EAG file " + this.eagPath + " [File System Rollback]. Error: " + e.getMessage());
						}

						if (eagModifyingProcessState == 2) {
							//There were errors during Database transaction
							//It is necessary to make a file system and a database rollback
							//File System rollback
							//First, the eag file is removed
							File currentEAG = new File(this.getEagPath());
							try {
								FileUtils.forceDelete(currentEAG);								
							}
							catch (Exception ex) {
								log.error("Error removing current EAG. Error:" + e.getMessage());
							}
							
							//Second, the backup file is restored
							File backupEAG = null;
							if (hasChangedId) {
								backupEAG = new File(oldEAGpath + "_old");
								currentEAG = new File(oldEAGpath);
							}
							else {
								backupEAG = new File(this.getEagPath() + "_old");
							}
							
							try {
								FileUtils.copyFile(backupEAG, currentEAG);
							}
							catch (Exception ex) {
								log.error("Error restoring backup EAG file. Error:" + e.getMessage());
							}

							
							//Third, the backup file is removed
							try {
								FileUtils.forceDelete(backupEAG);
							}
							catch (Exception ex) {
								log.error("Error removing backup EAG file. Error:" + e.getMessage());
							}
							
							currentEAG = null;
							backupEAG = null;

							//Database rollback
							HibernateUtil.rollbackDatabaseTransaction();
							HibernateUtil.closeDatabaseSession();
							
							log.error("There were errors during Database Transaction and the modification of the EAG file " + this.eagPath + " [File System and Database Rollback]. Error: " + e.getMessage());
						}
						
						if (eagModifyingProcessState == 3) {
							//There were errors during Database or File system commit
							HibernateUtil.closeDatabaseSession();
							log.error("FATAL ERROR. Error during Database or File System commits while modifying the EAG " + this.eagPath + ". Please, check inconsistencies in Database and File system", e);
						}

			        	oldEAGpath = null;
						value = "error";
			        }

				}
				
				if (value.equals("correct") || value.equals("correct_withoutRepositorguideInformation")){
					//It is necessary to insert eagPath in the local Archival Landscape for the current country
					//and rebuild the global Archival Landscape
					String valuetmp = this.modifyArchivalLandscape();
					if (valuetmp.equals("correct")) {
						if (value.equals("correct_withoutRepositorguideInformation")) {
							value = "correct_withoutRepositorguideInformation";
						}
					}
					else {
						value = valuetmp;
					}
				}
				
			}
			
		}
		else {
			value = "error";
		}
		
		archivalInstitutionDao = null;
		archivalInstitution = null;
		gArchivalLandscape = null;
		lArchivalLandscape = null;
		isNew = null;
		
		return value;
		
	}

	//This method stores a new EAG file or overwrites an existing one which has been uploaded via HTTP
	public String saveEAGviaHTTP (String sourcePath) {
		
		String value = "";
		Integer overwrittingEAGProcess = 0;
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);
		
		//First, it is necessary to check if global Archival Landscape and local Archival Landscape are ready for using
		//and check if the Archival Institution is within the local and global Archival Landscape files
		File gArchivalLandscape = new File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml");
		File lArchivalLandscape = new File(APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml");
		
		if (gArchivalLandscape.exists() && lArchivalLandscape.exists() && this.isArchivalInstitutionInArchivalLandscape()){
			//It is necessary to build the path
			this.eagPath = sourcePath;
			this.setId(this.extractAttributeFromEag("eagheader/eagid", null, true));
			this.setName(this.extractAttributeFromEag("archguide/identity/autform", null,true));
			
			//It is necessary to check if this EAG has been updated before for another archival institution			
			if (this.isEagAlreadyUploaded()){
				value = "error_eagalreadyuploaded";
			} else {
				this.eagPath = APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + this.aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml";
				String storagePath = APEnetUtilities.getConfig().getRepoDirPath() + this.eagPath;
				String oldEAGPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + this.aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + "_remove" + this.getId() + ".xml";
				File source = new File(sourcePath);
				File destination = new File(storagePath);
				        
				try {
					
					log.info("Starting EAG file storage process via HTTP upload");
					
					/// UPDATE DATABASE ///
					log.debug("Updating archival_institution table.");

					//Begin transaction with Database
					HibernateUtil.beginDatabaseTransaction();

					//The new path, autform and repositorycode are stored in archival_institution table
			        Date dateNow = new Date();
			        archivalInstitution.setRegistrationDate(dateNow);
			        archivalInstitution.setEagPath(this.eagPath);
	                archivalInstitution.setAutform(this.getName());
	                archivalInstitution.setRepositorycode(this.getId());
			        archivalInstitutionDao.insertSimple(archivalInstitution);
			        
			        overwrittingEAGProcess = 1;
			        
			        /// UPDATE FILE SYSTEM ///
			        
					// Rename the old EAG to _remove...
					log.debug("Renaming the EAG to _remove for the archival institution with id: " + this.getAiId());
					ContentUtils.renameFileToRemove(storagePath);
			        
					overwrittingEAGProcess = 2;
					
					// Copy the new EAG
					FileUtils.copyFile(source, destination);
					
					overwrittingEAGProcess = 3;
			        
			        /// FINAL COMMITS ///
			        
					//Final commit in the File system
					//It is necessary to remove the old EAG
					ContentUtils.deleteFile(oldEAGPath);

					//Final commit in Database
					HibernateUtil.commitDatabaseTransaction();
						
			        ChangeControl.logOperation("Upload eag");						
					log.info("The EAG " + this.eagPath + " has been created and stored in repository");

			        value = "correct";	                			

				} catch (Exception e) {
					
					value = "error_eagnotstored";
					
					if (overwrittingEAGProcess == 0) {
						//There were errors during Database Transaction
						//It is necessary to make a Database rollback
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						log.error("There were errors during Database Transaction while uploading a new EAG via HTTP protocol for archival institution " + this.getAiId(), e);
					}

					if (overwrittingEAGProcess == 1) {
						//There were errors during File System updating
						//It is necessary to make a Database rollback
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						
						//It is necessary to make a File System rollback
						ContentUtils.rollbackRenameFileFromRemove(oldEAGPath);
						
						log.error("There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution " + this.getAiId(), e);
					}

					if (overwrittingEAGProcess == 2) {
						//There were errors during File System updating
						//It is necessary to make a Database rollback
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						
						//It is necessary to make a File System rollback
						//Removing the new EAG for restoring the old one
						try {
							ContentUtils.deleteFile(destination.getAbsolutePath());
						}
						catch (Exception ex) {
							log.error("There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution " + this.getAiId() + ". Error removing the new EAG " + destination.getAbsolutePath(), e);
						}
						
						ContentUtils.rollbackRenameFileFromRemove(oldEAGPath);

						log.error("There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution " + this.getAiId());
					}

					if (overwrittingEAGProcess == 3) {
						//There were errors during Database, Index or File system commit
						HibernateUtil.closeDatabaseSession();
						log.error("FATAL ERROR. Error during Database or File System commits when an EAG file was uploading via HTTP protocol. Please, check inconsistencies in Database and File system for archival institution which id is: " + this.getAiId(), e);
					}
				}
			}
			if (value.equals("correct")){
				//It is necessary to insert eagPath in the local Archival Landscape for the current country and rebuild the global Archival Landscape
				value = this.modifyArchivalLandscape();
			}
		} else {
			if (!gArchivalLandscape.exists()){
				log.error("gArchivalLandscape does not exist: " + gArchivalLandscape.getAbsolutePath());
			}
			if (!lArchivalLandscape.exists()){
				log.error("lArchivalLandscape does not exist: " + lArchivalLandscape.getAbsolutePath());
			}
			if (!isArchivalInstitutionInArchivalLandscape()){
				log.error("No Archival institution in Archival Landscape." );
			}
			value = "error_archivallandscape";
		}
		return value;
	}
	
	//This method inserts the APEnet EAG reference into the local archival landscape for
	//a specific country (country related to the archival institution) and rebuilds the 
	//global archival landscape
	private String modifyArchivalLandscape() {
		String value = "";
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);
		log.info("Modifying Archival Landscape because of EAG file modification for Archival Institution id: " + this.aiId);

		//It is necessary to insert eagPath in the local Archival Landscape for the current country
		//and rebuild the global Archival Landscape
		try {
        	
        	Boolean found = false;
        	Boolean repositoryTagFound = false;
        	int i;
        	int j;
        	Node parentNode = null;
        	NodeList nodeList = null;
        	NodeList parentNodeList = null;
        	i = 0;
        	j = 0;
        	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();     	
        	InputStream sfile = new FileInputStream(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml");
        	Document doc = dBuilder.parse(sfile);
        	doc.getDocumentElement().normalize();
        	
        	nodeList = doc.getElementsByTagName("unittitle");
        	while ((!found) && (i < nodeList.getLength())){        	
                if (nodeList.item(i).getTextContent().trim().equals(archivalInstitution.getAiname())){
                	parentNode = nodeList.item(i).getParentNode();
                	parentNodeList = parentNode.getChildNodes();
                	j = 0;
                	while ( (!repositoryTagFound) && (j < parentNodeList.getLength()) ){
                		if ( (parentNodeList.item(j).getNodeName().trim().equals("repository")) && (parentNodeList.item(j).getNodeType() == Node.ELEMENT_NODE)){
                			repositoryTagFound = true;
                		}
                		else {
	                		j = j + 1;	                			
                		}
                	}		                	
                	found = true;
                }
                else {
	                i = i + 1;	                	
                }
        		
        	}
        	
        	if (found){
        		log.info("<repository> tag found within the AL file");
        		//Everything is OK and the archival institution has been found within the archival landscape
	        	if (repositoryTagFound){
            		//The local archival landscape already has a repository tag
            		//so it is necessary to change its value
        			Element extrefNode = null;
        			Node repository = parentNodeList.item(j);
        			NodeList repositoryChildren = repository.getChildNodes();
        			Boolean extrefFound = false;
        			for (int k = 0 ; k < repositoryChildren.getLength() ; k ++) {
        				
        				if (repositoryChildren.item(k).getNodeName().equals("extref")){
        					//There is already a extref tag and it is only needed to change the values
        	        		log.info("<extref> tag found within the AL file");
        					extrefFound = true;
        					if (repositoryChildren.item(k).hasAttributes()){
        						//extref has children
	        					NamedNodeMap attributes = repositoryChildren.item(k).getAttributes();
	                            Node attributeHref = attributes.getNamedItem("xlink:href");
	                            Node attributeTitle = attributes.getNamedItem("xlink:title");
	                
	                            if (attributeHref!=null && attributeTitle!=null){
	                            	//xlink:href attribute exits and xlink:title attribute exits 
	            	        		log.info("xlink:href and xlink:title attributes exist in <repository> tag");	                            	
	                            	attributeHref.setTextContent(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");
		                            attributeTitle.setTextContent(archivalInstitution.getAiname() + " EAG");
	            	        		log.info("Changing xlink:href content to " + APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");	                            	
	            	        		log.info("Changing xlink:title content to " + archivalInstitution.getAiname() + " EAG");	                            	
	            					
	                            }
	                            else {
	                            	//xlink:href attribute exits or xlink:title attribute exits
	                            	//Removing old repository and adding a new one
	            	        		log.info("xlink:href or xlink:title attributes don't exist. Removing old <repository> tag and adding a new one");	                            	
	            	        		Node parent = repositoryChildren.item(k).getParentNode();
	                            	Node extrefOld = repositoryChildren.item(k);
	                    			extrefNode = doc.createElement("extref");
	                    			extrefNode.setAttribute("xlink:href", APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");
	                    			extrefNode.setAttribute("xlink:title", archivalInstitution.getAiname() + " EAG");
	            	        		parent.replaceChild(extrefNode, extrefOld);
	            	        		log.info("xlink:href attribute added with content " + APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");	                            	
	            	        		log.info("xlink:title attribute added with content " + archivalInstitution.getAiname() + " EAG");	                            	
	                            }
        					}
        				}
        			}
        			
        			if (!extrefFound){
        				//If there is not a extref tag, it is needed to create a new one
            			extrefNode = doc.createElement("extref");
            			extrefNode.setAttribute("xlink:href", APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");
            			extrefNode.setAttribute("xlink:title", archivalInstitution.getAiname() + " EAG");
            			repository.appendChild(extrefNode);	            		        				
        			}
        			
            	}
            	else {
            		log.info("<repository> tag not found within the AL file. A new <repository> tag will be inserted");
            		//It is necessary to add a new repository tag within did tag
        			Element repositoryNode = null;
            		Element extrefNode = null;
        			Node did = nodeList.item(i).getParentNode();
        			
        			repositoryNode = doc.createElement("repository");
        			did.appendChild(repositoryNode);
        				extrefNode = doc.createElement("extref");
        				extrefNode.setAttribute("xlink:href", APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR + this.getId() + ".xml");
        				extrefNode.setAttribute("xlink:title", archivalInstitution.getAiname() + " EAG");
        				repositoryNode.appendChild(extrefNode);
            		
            	}
	        	
	            Result result = new StreamResult(new java.io.File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml"));;
	            Source source = new DOMSource(doc);
	            Transformer transformer;
                transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
        		
                //Finally, it is necessary to rebuild the global Archival Landscape
        		log.info("Rebuilding the whole AL");
                ArchivalLandscape archivalLandscape = new ArchivalLandscape();
                archivalLandscape.changeAL();
                
    			result = null;
    			source = null;
                transformer = null;
                
                value = "correct";
        	}
        	else {
        		//The archival institution doesn't exist in the archival landscape
				log.error("The archival institution doesn't exist within the archical landscape");
        		value = "error";
        	}
        	
        	nodeList = null;
        	parentNode = null;
        	parentNodeList = null;
			dbFactory = null;
			dBuilder = null;
			sfile = null;
			doc = null;

        	
		}
        catch (TransformerConfigurationException e) {
			log.error("Error configuring Transformer during the creation of " + this.eagPath + " file. " +  e.getMessage());
            value = "error";
        } 
        catch (TransformerFactoryConfigurationError e) {
			log.error("Error configuring Transformer Factory during the creation of " + this.eagPath + " file. " +  e.getMessage());
            value = "error";
        } 
        catch (TransformerException e) {
			log.error("Transformer error during the creation of " + this.eagPath + " file. " +  e.getMessage());
            value = "error";
        }
		catch (IOException e) {
			log.error("The file " + this.getEagPath() + " could not be removed. " +  e.getMessage());
			value = "error";
		}
        catch (Exception e) {
        	log.error(e.getMessage());
        	value = "error";
        }
        
		archivalInstitutionDao = null;
		archivalInstitution = null;

		return value;
	}
	
	//This method check the existence of the current Archival Institution within 
	//the Global and Local (for its country) Archival Landscape
	private Boolean isArchivalInstitutionInArchivalLandscape(){
		
		Boolean value = false;
		Boolean found = false;
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);
    	int i = 0;
    	NodeList nodeList = null;

        log.debug("Archival Institution AI NAME: '" + archivalInstitution.getAiname() + "'");

		try {
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            String alPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml";
	    	InputStream sfile = new FileInputStream(alPath);
	    	Document doc = dBuilder.parse(sfile);
	    	doc.getDocumentElement().normalize();
	    	
	    	nodeList = doc.getElementsByTagName("unittitle");
	    	while ((!found) && (i < nodeList.getLength())){
                log.debug("unittitle: '" + nodeList.item(i).getTextContent() + "'");
	            if (nodeList.item(i).getTextContent().trim().equals(archivalInstitution.getAiname())){
	            	found = true;
	            }
	            else {
	                i = i + 1;	                	
	            }
	    		
	    	}
	    	
	    	if (found){
                String archivalLandscapePath = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";
	    		sfile = new FileInputStream(archivalLandscapePath);
		    	doc = dBuilder.parse(sfile);
		    	doc.getDocumentElement().normalize();
		    	found = false;
		    	i = 0;

		    	nodeList = doc.getElementsByTagName("unittitle");
		    	while ((!found) && (i < nodeList.getLength())){
                    log.debug("unittitle: '" + nodeList.item(i).getTextContent() + "'");
		            if (nodeList.item(i).getTextContent().trim().equals(archivalInstitution.getAiname())){
		            	found = true;
		            }
		            else {
		                i = i + 1;	                	
		            }
		    		
		    	}
		    	
		    	if (found) {
		    		value = true;
		    	} else {
                    log.info("Archival institution '" + archivalInstitution.getAiname() + "' was not found in archival landscape ('"+ archivalLandscapePath +"')");
		    		value = false;
		    	}
	    	} else {
                log.info("Archival institution '" + archivalInstitution.getAiname() + "' was not found in archival landscape ('"+ alPath +"')");
	    		value = false;
	    	}
		} catch (Exception e) {
			log.error("The Archival Landscape (local, global or both) doesn't include this Archival Institution " + archivalInstitution.getAiname(), e);
        	value = false;
        }

		return value;
	}
	
	private Boolean isEagAlreadyUploaded() {
		//It will be necessary to check eagid and autform values in archival_institution table
		Boolean eagAlreadyUploaded = false;
		
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao.getArchivalInstitutionsByRepositorycode(this.getId());
		
		if (archivalInstitutionList.size() > 0) {
			//There is at least one institution with the same EAG uploaded
			for (int i = 0; i < archivalInstitutionList.size(); i ++){
				//If the institution found is the same that the instituion which wants to change the EAG file, then the operation is permitted
				if (archivalInstitutionList.get(i).getAiId() != this.getAiId()){
					eagAlreadyUploaded = true;					
				}
			}
		}
		else {
			archivalInstitutionList = archivalInstitutionDao.getArchivalInstitutionsByAutform(this.getName());
			if (archivalInstitutionList.size() > 0) {
				//There is at least one institution with the same EAG uploaded
				for (int i = 0; i < archivalInstitutionList.size(); i ++){
					//If the institution found is the same that the institution which wants to change the EAG file, then the operation is permitted
					if (archivalInstitutionList.get(i).getAiId() != this.getAiId()){
						eagAlreadyUploaded = true;					
					}
				}
			}
		}
		
		archivalInstitutionDao = null;
		archivalInstitutionList = null;
		
		return eagAlreadyUploaded;
	}
	
	// This method extracts the holdingsGuide object selected for the user
	private HoldingsGuide findHoldingsGuideSelected(String hgTitle) {
		
		Boolean found = false;
		HoldingsGuide hgResult = new HoldingsGuide();
		int i = 0;
		
		while (!found && i < this.holdingsGuideIndexed.size()){

			if (this.holdingsGuideIndexed.get(i).getTitle().equals(hgTitle)) {
				hgResult = (HoldingsGuide) this.holdingsGuideIndexed.get(i);
				found = true;
			}
			else {
				i = i + 1;
			}

		}
		
		if (!found) {
			hgResult.setTitle(LOCAL_HG_ERROR);
		}
				
		return hgResult;
	}

	@Override
    public String toString() {
        return "APEnetEAG{" +
                "aiId=" + this.aiId +
                ", eagPath='" + this.eagPath + '\'' +
                ", name='" + this.name + '\'' +
                ", englishName='" + this.englishName + '\'' +
                ", id='" + this.id + '\'' +
                ", responsiblePersonSurname='" + this.responsiblePersonSurname + '\'' +
                ", responsiblePersonName='" + this.responsiblePersonName + '\'' +
                ", country='" + this.country + '\'' +
                ", cityTown='" + this.cityTown + '\'' +
                ", postalCode='" + this.postalCode + '\'' +
                ", street='" + this.street + '\'' +
                ", telephone='" + this.telephone + '\'' +
                ", emailAddress='" + this.emailAddress + '\'' +
                ", webPage='" + this.webPage + '\'' +
                ", access='" + this.access + '\'' +                
                ", handicapped='" + this.handicapped + '\'' +                
                ", searchroom='" + this.workingPlaces + '\'' +                
                ", library='" + this.library + '\'' +                
                ", restorationlab='" + this.laboratory + '\'' +                
                ", reproductionser='" + this.reproduction + '\'' +                
                ", automation='" + this.automation + '\'' +                
                ", archivalHoldings='" + this.archivalHoldings + '\'' +                
                '}';
    }

}
