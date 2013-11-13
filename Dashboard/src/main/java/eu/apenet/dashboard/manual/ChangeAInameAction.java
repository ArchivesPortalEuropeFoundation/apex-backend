package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class ChangeAInameAction extends AbstractInstitutionAction {
	private static final long serialVersionUID = -2075500483635921910L;

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
		if (ContentUtils.containsPublishedFiles(archivalInstitution)){
			addActionError(getText("label.ai.changeainame.published.eads"));
		}
		return SUCCESS;
	}

	public String validateChangeAIname() throws Exception{
		Integer aiId = this.getAiId();

		Integer validateChangeAInameProcessState = 0;	//This variable is in charge of storing the changing process state:
		//0=initial state before start any process;
		//1=the process starts;
		//3=the process is deleting database;
		//4=the process is changing EAG from file system;

		String pathEAG="";
		String path_copyEAG = "";

		AiAlternativeNameDAO andao = DAOFactory.instance().getAiAlternativeNameDAO();
		ArchivalInstitutionDAO aidao = DAOFactory.instance().getArchivalInstitutionDAO();

		try{
			if (this.newname.isEmpty()){
				addActionError(getText("changeAIname.noEmptyName"));
				this.setAllok(false);
				return INPUT;
			}
			else{
				validateChangeAInameProcessState = 1;
				JpaUtil.beginDatabaseTransaction();
				ArchivalInstitution ai = aidao.getArchivalInstitution(aiId);
				if (ContentUtils.containsPublishedFiles(ai)){
					addActionError(getText("label.ai.changeainame.published.eads"));
					throw new APEnetException(getText("changeAIname.noDeletePublishedAI"));
				}
				AiAlternativeName an = andao.findByAIId_primarykey(ai);


				pathEAG = APEnetUtilities.getConfig().getRepoDirPath()+ ai.getEagPath();
				File  EAGfile = new File(pathEAG);
				path_copyEAG = pathEAG.replace(".xm", "");
				path_copyEAG = path_copyEAG + "_copy.xml";

				/// UPDATE DATABASE ///
				an.setAiAName(this.newname);
				ai.setAiname(this.newname);
				ai.setAutform(this.newname);
				andao.updateSimple(an);
				aidao.updateSimple(ai);

				log.info("The process has updated the values in database (transaction not committed yet).");

				validateChangeAInameProcessState = 3;

				/// CHANGE EAG ///
				File copyEAGfile = new File (path_copyEAG);
				if (EAGfile.exists()){
					FileUtils.copyFile(EAGfile, copyEAGfile);
				}

				NodeList nodeAutList = null;
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setNamespaceAware(true);
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				InputStream sfile = new FileInputStream(pathEAG);
				Document doc = dBuilder.parse(sfile);

				doc.getDocumentElement().normalize();
				nodeAutList = doc.getElementsByTagNameNS("http://www.archivesportaleurope.net/Portal/profiles/eag_2012/", "autform");
				boolean autformChanged = false;
				for (int j=0;j<nodeAutList.getLength() && !autformChanged;j++){
					Node nodeAutform = nodeAutList.item(j);
					if (nodeAutform.getTextContent().equals(this.name)
							&& !autformChanged){
						nodeAutform.setTextContent(this.newname);
						autformChanged = true;
						LOG.debug("<autform> in EAG has been modified correctly");
					}
				}

				// Check if the autform is changed.
				if (!autformChanged) {
					addActionError(getText("label.ai.changeainame.error.noCurrentNameInAutform"));
					log.error("There were errors during updating EAG file [Database Rollback]. Error: EAG file hasn't any autform value with the curernt name.");

					// Rollback all the changes.
					rollbackAllChanges(pathEAG, path_copyEAG, true);

					this.setAllok(false);

					return ERROR;
				}

				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(new DOMSource(doc), new StreamResult(new File(pathEAG))); // Stored

				log.info("The process has saved the new EAG file.");

				validateChangeAInameProcessState=4;
				EAGfile = null;
				copyEAGfile = null;

				/// FINAL COMMITS ///
				//Final commit in Database
				JpaUtil.commitDatabaseTransaction();

				//Delete EAG_copy
				ContentUtils.deleteFile(path_copyEAG);

				JpaUtil.closeDatabaseSession();
				this.setAllok(true);

				// Refresh institution in session.
				SecurityService.selectArchivalInstitution(aiId);

				return SUCCESS;
			}
		}
		catch (Exception e){
			addActionError(getText("label.ai.changeainame.error"));
			log.error("There were errors during updating AL file. Error: " + e.getMessage(),e);
			if (validateChangeAInameProcessState == 1) {
				//There were errors during Database Transaction
				//It is necessary to make a Database rollback

				this.setErrormessage(getText("changeAIname.errDeletingFromDb"));
				JpaUtil.rollbackDatabaseTransaction();
				JpaUtil.closeDatabaseSession();
				log.error("There were errors during Database Transaction [Database Rollback]. Error: " + e.getMessage());
			}
			if (validateChangeAInameProcessState==3 ){
				log.error("There were errors during updating EAG file [Database Rollback]. Error: " + e.getMessage(),e);
				rollbackAllChanges(pathEAG, path_copyEAG, false);
			}

			if (validateChangeAInameProcessState == 4){
				this.setErrormessage("There were errors during final commits");
				//There were errors during Database, Index or File system commit
				JpaUtil.closeDatabaseSession();
				log.error("FATAL ERROR. Error during Database, Index or File System commits. Please, check inconsistencies in Database, Index and File system " + e,e);
			}
			this.setAllok(false);
			return ERROR;
		}
	}

	/**
	 * Method to rollback all the changes due to an error in updating EAG file.
	 *
	 * @param pathEAG path to the current EAG file.
	 * @param path_copyEAG path to the copy of the current EAG file.
	 * @param existsInAutform if the current name exists in autform element.
	 *
	 * @throws IOException IOException
	 */
	private void rollbackAllChanges(final String pathEAG, final String path_copyEAG, final boolean existsInAutform) throws IOException {
		//It is necessary to make a Database rollback
		JpaUtil.rollbackDatabaseTransaction();
		JpaUtil.closeDatabaseSession();
		log.info("Database rollback succeed");

		if (existsInAutform) {
			//It is necessary to make a Index rollback of the FA indexed
			this.setErrormessage(getText("changeAIname.errUpdatingEAG"));
		} else {
			this.setErrormessage(getText("label.ai.changeainame.error.noCurrentNameInAutform"));
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

		JpaUtil.closeDatabaseSession();
	}
}