package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ChangeAInameAction extends AbstractInstitutionAction {


	protected Logger log = Logger.getLogger(getClass());
	
    //Attributes
	
	private String name;
	private String newname;
	private Integer numberOfFindingAids; //Number of Finding Aids which belong to an archival institution
	private Boolean allok;
	private String errormessage;
	private String id;
	private String currentAction;	//This variable stores the action from 
									//ReadEAGAction was called. It is useful 
									//for knowing where to come back, because 
									//ReadEAGAction can be called from 
									//different points within the 
									//application
    
    //Getters and Setters
	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public Boolean getAllok() {
		return allok;
	}

	public void setAllok(Boolean allok) {
		this.allok = allok;
	}
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.changeAIname"));
	}
	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNewname() {
		return newname;
	}

	public void setNewname(String newname) {
		this.newname = newname;
	}
	
	public Integer getNumberOfFindingAids() {
		return numberOfFindingAids;
	}

	public void setNumberOfFindingAids(Integer numberOfFindingAids) {
		this.numberOfFindingAids = numberOfFindingAids;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	public String getCurrentAction() {
		return currentAction;
	}
	
    //Constructor
    public ChangeAInameAction(){
    }
	
    //Methods
	public String execute() throws Exception{

		//The ai_id is retrieved from the session
		//This code has been moved to the prepare method
		
		//The action from where ChangeAInameAction was invoked is retrieved
		this.setCurrentAction(ServletActionContext.getActionMapping().getName());		
		
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
		this.name = archivalInstitution.getAiname();
				
		return SUCCESS;
	}
	
	
	public String validateChangeAIname() throws Exception{
		Integer aiId = this.getAiId();
		
		Integer validateChangeAInameProcessState = 0;	//This variable is in charge of storing the changing process state: 
		//3=the process is deleting database;
		//4=the process is changing EAG from file system; 
		//5=the process is changing AL from file system;

		String isoname="";	
		String pathAL ="";
		String pathEAG="";
		String path_copyEAG = "";
		String path_copyAL ="";
		
		AiAlternativeNameDAO andao = DAOFactory.instance().getAiAlternativeNameDAO();
		ArchivalInstitutionDAO aidao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalLandscape a = new ArchivalLandscape();
		
		try{
			if (this.newname.isEmpty()){
				this.setErrormessage("The name cannot be empty.");
				this.setAllok(false);
				return INPUT;
			}
			else{
			
			HibernateUtil.beginDatabaseTransaction();
			ArchivalInstitution ai = aidao.getArchivalInstitution(aiId);
			if (ContentUtils.containsPublishedFiles(ai)){
				throw new APEnetException("Could not delete an archival institution with published EAD files");
			}
			AiAlternativeName an = andao.findByAIId_primarykey(ai);			
			
			isoname = ai.getCountry().getIsoname();
			pathAL = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + isoname + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + isoname + "AL.xml";
			pathEAG = APEnetUtilities.getConfig().getRepoDirPath()+ ai.getEagPath();
			File  EAGfile = new File(pathEAG);
			path_copyEAG = pathEAG.replace(".xm", "");
	    	path_copyEAG = path_copyEAG + "_copy.xml";	
	    	path_copyAL = pathAL.replace(".xm", "");
        	path_copyAL = path_copyAL + "_copy.xml";
			
        	
			/// UPDATE DATABASE ///
			LOG.info("Changing database ai_alternative_name table and archival_landscape table.");
			an.setAiAName(this.newname);
			ai.setAiname(this.newname);
			ai.setAutform(this.newname);
			andao.updateSimple(an);
			aidao.updateSimple(ai);
			
			validateChangeAInameProcessState = 3;
			

			
			/// CHANGE EAG ///
			File copyEAGfile = new File (path_copyEAG);
        	if (EAGfile.exists()){
        		FileUtils.copyFile(EAGfile, copyEAGfile);
        	}

        		
        	boolean changeEAG = false;
			NodeList nodeAutList = null;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();   	 
	        InputStream sfile = new FileInputStream(pathEAG);
	        Document doc = dBuilder.parse(sfile);
	       
	        doc.getDocumentElement().normalize();
	        LOG.info("Changing EAG, changing autform tag's content.");
	        nodeAutList = doc.getElementsByTagNameNS("http://www.archivesportaleurope.eu/profiles/APEnet_EAG/", "autform");
	        for (int j=0;j<nodeAutList.getLength();j++){
	        	Node nodeAutform = nodeAutList.item(j);
	        	if (!nodeAutform.getTextContent().equals(this.newname)){
	        		nodeAutform.setTextContent(this.newname);
	        		changeEAG=true;
	        		LOG.info("EAG has been modified correctly");
	        	}
	        }
	        TransformerFactory tf = TransformerFactory.newInstance();
    		Transformer transformer = tf.newTransformer();
    		transformer.transform(new DOMSource(doc), new StreamResult(new File(pathEAG))); // Stored
    			
        	validateChangeAInameProcessState=4;
			EAGfile = null;
			copyEAGfile = null;
			
			/// CHANGE AL FILE ///				
			
			//change the Archival Landscape
			
			File  ALfile = new File(pathAL);
		    File copyALfile = new File (path_copyAL);
		    if (ALfile.exists()){
		    	FileUtils.copyFile(ALfile, copyALfile);
			}
	        	
					Boolean changeAL = false;
					log.info("Modifying Archival Landscape because the change of the name of the Archival Institution with id: " + aiId);
					//First, looking for the node c with the attribute id internal_al_id from ddbb.								
					NodeList nodeCList = null;
						
						
		        	InputStream sfile2 = new FileInputStream(pathAL);
		        	doc = dBuilder.parse(sfile2);
		        	doc.getDocumentElement().normalize();
			        	
		        	nodeCList = doc.getElementsByTagName("c");
		        	for (int i =0;i<nodeCList.getLength();i++){
		        		Node nodeC = nodeCList.item(i);
		        		if (nodeC.hasAttributes()){
		        			NamedNodeMap attributes = nodeC.getAttributes();
		        			Node attributeId = attributes.getNamedItem("id");
		        			if (attributeId != null){
		        				//Check this value with internal_al_id ddbb
		        				if (attributeId.getTextContent().equals(ai.getInternalAlId())){		        					
		        					if (nodeC.hasChildNodes()){
		        						NodeList nodeDidList = nodeC.getChildNodes();
		        						for (int k=0;k<nodeDidList.getLength();k++){	        							
		        							Node nodedid = nodeDidList.item(k);
		        							if (nodedid.getNodeName().trim().equals("did") && nodedid.hasChildNodes()){
		        								if (nodedid.hasChildNodes()){
		        									NodeList unittitlelist = nodedid.getChildNodes();
		        									for (int t=0;t<unittitlelist.getLength();t++){
		        										if (unittitlelist.item(t).getNodeName().trim().equals("unittitle")){
		        											Node unittitlenode = unittitlelist.item(t);
		        											LOG.info("Changing <unititle> in AL from " + unittitlenode.getTextContent() + " to " + this.newname);
		        											unittitlenode.setTextContent(this.newname);
		        											changeAL= true;
		        											LOG.info("The AL has been changed.");
		        										}
		        									}
		        								}
		        							}
		        						}
		        					}
		        				}
		        			}
		        		}
		        	}
	    			transformer.transform(new DOMSource(doc), new StreamResult(new File(pathAL))); // Stored
	    			ALfile =null;
					copyALfile =null;
	    			//--- 6th CHANGE GENERAL AL  ----------------------------------
	    			a.changeAL();
		    			
		        	validateChangeAInameProcessState = 5;
					
					/// FINAL COMMITS ///
					//Final commit in Database
					HibernateUtil.commitDatabaseTransaction();
					
					//Delete EAG_copy
					ContentUtils.deleteFile(path_copyEAG);
					LOG.info("The EAG_copy has been deleted");
					
					//Delete AL_copy
					LOG.info("Deleting temporal file with path " +path_copyAL);
	    			ContentUtils.deleteFile(path_copyAL);
	    			LOG.info("The file " + path_copyAL + "has been deleted correctly.");
	    			
					HibernateUtil.closeDatabaseSession();
					this.setAllok(true);
					
				return SUCCESS;
			}
			
		}
		catch (Exception e){
			
			if (validateChangeAInameProcessState==3 ){
				log.error("There were errors during updating EAG file [Database Rollback, Index Rollback]. Error: " + e.getMessage());
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during updating EAG file.");
				


				
				//There were errors during EAG modify.
				//It is necessary to make EAG rollback
				File EAGfile = new File(pathEAG);
				File copyEAGfile = new File (path_copyEAG);
				ContentUtils.deleteFile(pathEAG);
				copyEAGfile.renameTo(EAGfile);
				log.info("EAG rollback succeed");
				EAGfile = null;
				copyEAGfile = null;
				
				HibernateUtil.closeDatabaseSession();
				
			}
			
			if (validateChangeAInameProcessState == 4){
				log.error("There were errors during updating AL file [Database Rollback, Index Rollback, EAG Rollback]. Error: " + e.getMessage());
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during updating AL file.");

				//There were errors during EAG modify.
				//It is necessary to make EAG rollback			
				File EAGfile = new File(pathEAG);
				File copyEAGfile = new File (path_copyEAG);
				ContentUtils.deleteFile(pathEAG);
				copyEAGfile.renameTo(EAGfile);
				log.info("EAG rollback succeed");
				EAGfile = null;
				copyEAGfile = null;
				
				//There were errors during AL modify.
				//It is necessary to make AL rollback
				File ALfile = new File (pathAL);
				File copyALfile = new File (path_copyAL);
				ContentUtils.deleteFile(pathAL);
				copyALfile.renameTo(ALfile);
				log.info("AL of the country rollback succeed");
				ALfile =null;
				copyALfile =null;
				//--- 6th CHANGE GENERAL AL  ----------------------------------
    			a.changeAL();
    			HibernateUtil.closeDatabaseSession();
				
			}
			
			if (validateChangeAInameProcessState == 5){
				this.setErrormessage("There were errors during final commits");
				//There were errors during Database, Index or File system commit
				HibernateUtil.closeDatabaseSession();
				log.error("FATAL ERROR. Error during Database, Index or File System commits. Please, check inconsistencies in Database, Index and File system " + e);
			}
			this.setAllok(false);
			return ERROR;
		}
	}			
	
}