package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

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
		//1=The process is deleting FA from index; 
		//2=the process is deleting HG from index; 
		//3=the process is deleting database;
		//4=the process is changing EAG from file system; 
		//5=the process is changing AL from file system;
		List<String> collection = new ArrayList<String>();
		String isoname="";	
		String pathAL ="";
		String pathEAG="";
		String path_copyEAG = "";
		String path_copyAL ="";
		List<HoldingsGuide> holdingsGuideListFailed = new ArrayList<HoldingsGuide>();
		List<FindingAid> findingAidListFailed = new ArrayList<FindingAid>();	
		List<FindingAid> findingAidListdeleted = new ArrayList<FindingAid>();
		List<HoldingsGuide> holdingsGuideListdeleted=new ArrayList<HoldingsGuide>();
		boolean FAerror = false;
		boolean HGerror = false;
		boolean dataBaseCommitError = false;
		boolean EAGerror=false;
		boolean ALerror=false;
		boolean existAL =true;
		boolean existEAG = true;
		String path_EUROPEANA_copy ="";
		String path_EUROPEANA ="";
		
		FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
		HoldingsGuideDAO holdingsGuideDao = DAOFactory.instance().getHoldingsGuideDAO();
		AiAlternativeNameDAO andao = DAOFactory.instance().getAiAlternativeNameDAO();
		EseDAO esedao = DAOFactory.instance().getEseDAO();
		ArchivalInstitutionDAO aidao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalLandscape a = new ArchivalLandscape();
		
		try{
			if (this.newname.isEmpty()){
				this.setErrormessage("The name cannot be empty.");
				this.setAllok(false);
				return INPUT;
			}
			else{
			collection.add("Indexed_Not converted to ESE/EDM");
			collection.add("Indexed_Converted to ESE/EDM");
			collection.add("Indexed_Delivered to Europeana");
			collection.add("Indexed_Harvested to Europeana");
			collection.add("Indexed_Not linked");
			collection.add("Indexed_Linked");			
			HibernateUtil.beginDatabaseTransaction();
			ArchivalInstitution ai = aidao.getArchivalInstitution(aiId);			
			AiAlternativeName an = andao.findByAIId_primarykey(ai);			
			
			isoname = ai.getCountry().getIsoname();
			pathAL = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + isoname + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + isoname + "AL.xml";
			pathEAG = APEnetUtilities.getConfig().getRepoDirPath()+ ai.getEagPath();
			File  EAGfile = new File(pathEAG);
			path_copyEAG = pathEAG.replace(".xm", "");
	    	path_copyEAG = path_copyEAG + "_copy.xml";	
	    	path_copyAL = pathAL.replace(".xm", "");
        	path_copyAL = path_copyAL + "_copy.xml";
        	path_EUROPEANA = APEnetUtilities.getConfig().getRepoDirPath()+ APEnetUtilities.FILESEPARATOR + isoname + APEnetUtilities.FILESEPARATOR + ai.getAiId() + APEnetUtilities.FILESEPARATOR + "EUROPEANA";
			path_EUROPEANA_copy = path_EUROPEANA + "_copy";
			
        	
			/// UPDATE DATABASE ///
			LOG.info("Changing database ai_alternative_name table and archival_landscape table.");
			an.setAiAName(this.newname);
			ai.setAiname(this.newname);
			ai.setAutform(this.newname);
			andao.updateSimple(an);
			aidao.updateSimple(ai);
			
			validateChangeAInameProcessState = 1;
			
			/// DELETE FA FROM INDEX ///
			// Remove all FA related to this institution
			// from the Index only if it is indexed
			// Because of Solr doesn't support transactions as DDBB
			// it is necessary to make a commit immediately
			
			//Obtain the finding aids indexed from this institution.
			List<FindingAid>findingAidList = findingAidDao.getFindingAids(aiId,collection);
			//Copy EUROPEANA directory.
			File Europeanadir = new File(path_EUROPEANA);
			File Europeanadir_copy = new File(path_EUROPEANA_copy);
			if (Europeanadir.exists()){
				FileUtils.copyDirectory(Europeanadir, Europeanadir_copy);
			}
			Europeanadir = null;
			Europeanadir_copy = null;
			
			if (findingAidList.size()>0){
				for (int i = 0 ; i < findingAidList.size() ; i ++) {
					//Delete each FA of this institution.	
					FindingAid findingaid = findingAidList.get(i);
					try{
			        	LOG.info("Deleting finding aid with eadid "+ findingaid.getEadid()+" from index.");
			        	ContentManager.deleteOnlyFromIndex(findingaid.getId(), XmlType.EAD_FA, aiId, false);
						findingAidListdeleted.add(findingaid);
												
					}
					catch (Exception ex){
						FAerror=true;
						LOG.error("ERROR deleting finding aid with eadid "+findingaid.getEadid()+" from index.");
						findingAidListFailed.add(findingaid);
					}
				}
					
				validateChangeAInameProcessState = 2;
			}
			
			/// DELETE HG FROM INDEX ///
			// Remove all HG related to this institution
			// from the Index only if it is indexed
			// Because of Solr doesn't support transactions as DDBB
			// it is necessary to make a commit immediately
			
			List<HoldingsGuide> holdingsGuideList = holdingsGuideDao.getHoldingsGuides("", "", "", collection, aiId, "", false);
			//Copy the list of the holdings guides to restore the files in rollback.
			if (holdingsGuideList.size()>0){
				for (int i = 0 ; i < holdingsGuideList.size() ; i ++) {
					HoldingsGuide holdingsGuide =  holdingsGuideList.get(i);
					try{
						LOG.error("Deleting Holding guide: "+ holdingsGuide.getEadid()+" from index.");
						ContentManager.deleteOnlyFromIndex(holdingsGuide.getId(), XmlType.EAD_HG, aiId, false);
						holdingsGuideListdeleted.add(holdingsGuide);
					}
					catch (Exception ex){
						LOG.error("ERROR deleting Holding guide: "+ holdingsGuideList.get(i).getEadid()+" from index.");
						holdingsGuideListFailed.add(holdingsGuideList.get(i));
						HGerror = true;
					}
				}
				
				validateChangeAInameProcessState = 3;
			}
			
			/// CHANGE EAG ///
			File copyEAGfile = new File (path_copyEAG);
        	if (EAGfile.exists()){
        		existEAG = true;
        		FileUtils.copyFile(EAGfile, copyEAGfile);
        	}
        	else{
        		existEAG =false;
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
		    	existAL =true;
			}
		    else{
		    	existAL=false;
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
	    			
	    			//Delete EUROPEANA
	    			Europeanadir = new File (path_EUROPEANA);
	    			if (Europeanadir.exists()){
	    				FileUtils.forceDelete(Europeanadir);
	    				Europeanadir = null;
	    			}
	    			//Delete EUROPEANA_copy
					Europeanadir_copy = new File(path_EUROPEANA_copy);
					if (Europeanadir_copy.exists())
					{
						FileUtils.forceDelete(Europeanadir_copy);
						Europeanadir_copy = null;
					}
					HibernateUtil.closeDatabaseSession();
					this.setAllok(true);
					
				return SUCCESS;
			}
			
		}
		catch (Exception e){
			if (validateChangeAInameProcessState == 0) {
				//There were errors during Database Transaction
				//It is necessary to make a Database rollback
				this.setErrormessage("There were errors during deleting from Data Base.");
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.error("There were errors during Database Transaction [Database Rollback]. Error: " + e.getMessage());
			}
			
			if (validateChangeAInameProcessState == 1 && findingAidListdeleted.size()>0) {
				log.error("There were errors during deleting FA from index [Database Rollback, Index Rollback]. Error: " + e.getMessage());
				//There were errors during File system Transaction
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");
				

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during deleting FA from index.");
				//There were errors during deleting FA from index.
				//It is necessary to make a Index rollback if the file was deleted.
				for (int i=0; i< findingAidListdeleted.size();i++)
				{
						
					if ((findingAidListdeleted.get(i).getFileState().getId()>7)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_FA, findingAidListdeleted.get(i).getId());
							log.info("Index rollback of FA succeed");
						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + findingAidListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((findingAidListdeleted.get(i).getFileState().getId()>8)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(findingAidListdeleted.get(i));
								}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Finding Aid. Check Database");
								}
						}
					}							
				}
				//Restore EUROPEANA directory.
				File Europeanadir = new File(path_EUROPEANA);
				File Europeanadir_copy = new File (path_EUROPEANA_copy);
				if (Europeanadir.exists())
				{
					FileUtils.forceDelete(Europeanadir);
					Europeanadir_copy.renameTo(Europeanadir);
					log.debug("The directory EUROPEANA has been restored correctly");
				}
				Europeanadir = null;
				Europeanadir_copy=null;
				HibernateUtil.closeDatabaseSession();
				log.error("There were errors during deleting from index [Database Rollback, Index Rollback]. Error: " + e.getMessage());
			}
			
			if (validateChangeAInameProcessState == 1 && holdingsGuideListdeleted.size()>0) {
				log.error("There were errors during deleting HG from Index [Database Rollback, Index Rollback]. Error: " + e.getMessage());
				//There were errors during File system Transaction
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during deleting HG from index.");
				
				//It is necessary to make a Index rollback of the HG indexed
				//There were errors during deleting HG from index.
				//It is necessary to make a Index rollback if the file was deleted.
				//Re-index again the holdings guides deleted
				for (int i=0; i< holdingsGuideListdeleted.size();i++)
				{
					if ((holdingsGuideListdeleted.get(i).getFileState().getId()>7)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_HG, holdingsGuideListdeleted.get(i).getId());
							log.info("Index rollback of FA succeed");

						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + holdingsGuideListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((holdingsGuideListdeleted.get(i).getFileState().getId()>8)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(holdingsGuideListdeleted.get(i));
							}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Holdings Guide. Check Database");
							}
						}
					}							
				}
				
				HibernateUtil.closeDatabaseSession();
				log.error("There were errors during deleting HG from index [Database Rollback, Index Rollback]. Error: " + e.getMessage());
			}
			if (validateChangeAInameProcessState==2 && holdingsGuideListdeleted.size()>0){	
				log.error("There were errors during updating EAG file [Database Rollback, Index Rollback]. Error: " + e.getMessage());
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during EAG change.");
				
				//It is necessary to make a Index rollback if the file was deleted.
				for (int i=0; i< findingAidListdeleted.size();i++)
				{
						
					if ((findingAidListdeleted.get(i).getFileState().getId()>7)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_FA, findingAidListdeleted.get(i).getId());
							log.info("Index rollback of FA succeed");
						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + findingAidListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((findingAidListdeleted.get(i).getFileState().getId()>8)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(findingAidListdeleted.get(i));
								}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Finding Aid. Check Database");
								}
						}
					}							
				}
						
				//Restore EUROPEANA directory.
				File Europeanadir = new File(path_EUROPEANA);
				File Europeanadir_copy = new File (path_EUROPEANA_copy);
				if (Europeanadir.exists())
				{
					FileUtils.forceDelete(Europeanadir);
					Europeanadir_copy.renameTo(Europeanadir);
					log.debug("The directory EUROPEANA has been restored correctly");
				}
				Europeanadir = null;
				Europeanadir_copy=null;
				
				//It is necessary to make a Index rollback of the HG indexed
				//There were errors during deleting HG from index.
				//It is necessary to make a Index rollback if the file was deleted.
				//Re-index again the holdings guides deleted
				for (int i=0; i< holdingsGuideListdeleted.size();i++)
				{
					if ((holdingsGuideListdeleted.get(i).getFileState().getId()>7)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_HG, holdingsGuideListdeleted.get(i).getId());
							log.info("Index rollback of HG succeed");
						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + holdingsGuideListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((holdingsGuideListdeleted.get(i).getFileState().getId()>8)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(holdingsGuideListdeleted.get(i));
							}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Holdings Guide. Check Database");
							}
						}
					}							
				}
				HibernateUtil.closeDatabaseSession();
				log.error("There were errors during deleting HG from index [Database Rollback, Index Rollback]. Error: " + e.getMessage());
			}
			
			if (validateChangeAInameProcessState == 2 && holdingsGuideListdeleted.size()==0){
				log.error("There were errors during updating EAG file [Database Rollback, Index Rollback]. Error: " + e.getMessage());
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during updating EAG file.");
				
				//It is necessary to make a Index rollback if the file was deleted.
				for (int i=0; i< findingAidListdeleted.size();i++)
				{
						
					if ((findingAidListdeleted.get(i).getFileState().getId()>7)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_FA, findingAidListdeleted.get(i).getId());
							log.info("Index rollback of FA succeed");
						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + findingAidListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((findingAidListdeleted.get(i).getFileState().getId()>8)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(findingAidListdeleted.get(i));
								}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Finding Aid. Check Database");
								}
						}
					}							
				}
						
				//Restore EUROPEANA directory.
				File Europeanadir = new File(path_EUROPEANA);
				File Europeanadir_copy = new File (path_EUROPEANA_copy);
				if (Europeanadir.exists())
				{
					FileUtils.forceDelete(Europeanadir);
					Europeanadir_copy.renameTo(Europeanadir);
					log.debug("The directory EUROPEANA has been restored correctly");
				}
				Europeanadir = null;
				Europeanadir_copy=null;
				
				HibernateUtil.closeDatabaseSession();
				log.error("There were errors during deleting HG from index [Database Rollback, Index Rollback]. Error: " + e.getMessage());			
			}
			
			if (validateChangeAInameProcessState==3 && holdingsGuideListdeleted.size()>0){
				log.error("There were errors during updating EAG file [Database Rollback, Index Rollback]. Error: " + e.getMessage());
				//It is necessary to make a Database rollback
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				log.info("Database rollback succeed");

				//It is necessary to make a Index rollback of the FA indexed
				this.setErrormessage("There were errors during updating EAG file.");
				
				//It is necessary to make a Index rollback if the file was deleted.
				for (int i=0; i< findingAidListdeleted.size();i++)
				{
						
					if ((findingAidListdeleted.get(i).getFileState().getId()>7)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_FA, findingAidListdeleted.get(i).getId());
							log.info("Index rollback of FA succeed");
						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + findingAidListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((findingAidListdeleted.get(i).getFileState().getId()>8)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(findingAidListdeleted.get(i));
								}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Finding Aid. Check Database");
								}
						}
					}							
				}
				//Restore EUROPEANA directory.
				File Europeanadir = new File(path_EUROPEANA);
				File Europeanadir_copy = new File (path_EUROPEANA_copy);
				if (Europeanadir.exists())
				{
					FileUtils.forceDelete(Europeanadir);
					Europeanadir_copy.renameTo(Europeanadir);
					log.debug("The directory EUROPEANA has been restored correctly");
				}
				
				//It is necessary to make a Index rollback of the HG indexed
				//There were errors during deleting HG from index.
				//It is necessary to make a Index rollback if the file was deleted.
				//Re-index again the holdings guides deleted
				for (int i=0; i< holdingsGuideListdeleted.size();i++)
				{
					if ((holdingsGuideListdeleted.get(i).getFileState().getId()>7)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
						try {
							ContentUtils.indexRollback(XmlType.EAD_HG, holdingsGuideListdeleted.get(i).getId());
							log.info("Index rollback of HG succeed");
						} catch (Exception ex) {
							log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + holdingsGuideListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
						}
							
						//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
						if ((holdingsGuideListdeleted.get(i).getFileState().getId()>8)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.restoreOriginalStateOfEAD(holdingsGuideListdeleted.get(i));
							}
							catch (Exception ex) {
								log.error("Error restoring the original state of the Holdings Guide. Check Database");
							}
						}
					}							
				}
				
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
				if (findingAidListdeleted.size()>0){
					//It is necessary to make a Index rollback if the file was deleted.
					for (int i=0; i< findingAidListdeleted.size();i++)
					{
							
						if ((findingAidListdeleted.get(i).getFileState().getId()>7)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.indexRollback(XmlType.EAD_FA, findingAidListdeleted.get(i).getId());
								log.info("Index rollback of FA succeed");
							} catch (Exception ex) {
								log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + findingAidListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
							}
								
							//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
							if ((findingAidListdeleted.get(i).getFileState().getId()>8)&&(findingAidListdeleted.get(i).getFileState().getId()<15)) {
								try {
									ContentUtils.restoreOriginalStateOfEAD(findingAidListdeleted.get(i));
									}
								catch (Exception ex) {
									log.error("Error restoring the original state of the Finding Aid. Check Database");
									}
							}
						}							
					}
					//Restore EUROPEANA directory.
					File Europeanadir = new File(path_EUROPEANA);
					File Europeanadir_copy = new File (path_EUROPEANA_copy);
					if (Europeanadir.exists())
					{
						FileUtils.forceDelete(Europeanadir);
						Europeanadir_copy.renameTo(Europeanadir);
						log.debug("The directory EUROPEANA has been restored correctly");
					}
				}
				if (holdingsGuideListdeleted.size()>0){
					//It is necessary to make a Index rollback of the HG indexed
					//There were errors during deleting HG from index.
					//It is necessary to make a Index rollback if the file was deleted.
					//Re-index again the holdings guides deleted
					for (int i=0; i< holdingsGuideListdeleted.size();i++)
					{
						if ((holdingsGuideListdeleted.get(i).getFileState().getId()>7)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
							try {
								ContentUtils.indexRollback(XmlType.EAD_HG, holdingsGuideListdeleted.get(i).getId());
								log.info("Index rollback of HG succeed");
							} catch (Exception ex) {
								log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + holdingsGuideListdeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
							}
								
							//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
							if ((holdingsGuideListdeleted.get(i).getFileState().getId()>8)&&(holdingsGuideListdeleted.get(i).getFileState().getId()<15)) {
								try {
									ContentUtils.restoreOriginalStateOfEAD(holdingsGuideListdeleted.get(i));
								}
								catch (Exception ex) {
									log.error("Error restoring the original state of the Holdings Guide. Check Database");
								}
							}
						}							
					}
				}
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