package eu.apenet.dashboard.queue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.EadSolrServerHolder;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.dashboard.listener.QueueDaemon;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.eag.xml.XmlEagParser;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagSolrPublisher;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.UpFile;

public class ManageQueueAction extends AbstractAction {
	private static final Logger LOGGER = Logger.getLogger(ManageQueueAction.class);
	private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	private Integer queueItemId;
	private String selectedAction;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7015833987047809962L;

	public Integer getQueueItemId() {
		return queueItemId;
	}

	public void setQueueItemId(Integer queueItemId) {
		this.queueItemId = queueItemId;
	}
	

	public String getSelectedAction() {
		return selectedAction;
	}

	public void setSelectedAction(String selectedAction) {
		this.selectedAction = selectedAction;
	}

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("admin.queuemanagement.title"));
	}

	public String execute() throws Exception {
		QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
		getServletRequest().setAttribute("numberOfItemsInQueue", queueDAO.countItems());
		getServletRequest().setAttribute("firstItems", convert(queueDAO.getFirstItems()));
		getServletRequest().setAttribute("disabledItems", convert(queueDAO.getDisabledItems()));
		getServletRequest().setAttribute("itemsWithErrors", convert(queueDAO.getItemsWithErrors()));
		getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
		getServletRequest().setAttribute("queueStatus", QueueDaemon.getQueueStatus());
		getServletRequest().setAttribute("queueStatusCss", QueueDaemon.getQueueStatusCss());
		
		getServletRequest().setAttribute("queueProcessing", QueueDaemon.isQueueProcessing());
		getServletRequest().setAttribute("europeanaHarvestingStarted", EadService.isHarvestingStarted());
		getServletRequest().setAttribute("dashboardHarvestingStarted", HarvesterDaemon.isHarvesterProcessing());
		getServletRequest().setAttribute("currentTime", DATE_TIME.format(new Date()));
		Date endDateTime = DAOFactory.instance().getResumptionTokenDAO().getPossibleEndDateTime();
		if (endDateTime != null)
			getServletRequest().setAttribute("europeanaHarvestingEndTime", DATE_TIME.format(endDateTime));
		return SUCCESS;
	}

	private List<DisplayQueueItem> convert(List<QueueItem> queueItems) {
		List<DisplayQueueItem> results = new ArrayList<DisplayQueueItem>();
		for (QueueItem queueItem : queueItems) {
			DisplayQueueItem displayItem = new DisplayQueueItem();
			displayItem.setId(queueItem.getId());
			displayItem.setAction(queueItem.getAction().toString());
			displayItem.setPriority(queueItem.getPriority());
			displayItem.setErrors(queueItem.getErrors());
			try {
				if (queueItem.getAbstractContent() != null) {
					AbstractContent content = queueItem.getAbstractContent();
					displayItem.setEadidOrFilename(content.getIdentifier());
					displayItem.setArchivalInstitution(content.getArchivalInstitution().getAiname());
				} else if (queueItem.getUpFile() != null) {
					UpFile upFile = queueItem.getUpFile();
					displayItem.setEadidOrFilename(upFile.getPath() + upFile.getFilename());
					displayItem.setArchivalInstitution(upFile.getArchivalInstitution().getAiname());
				}
				if (QueueAction.USE_PROFILE.equals(queueItem.getAction())) {
					Properties preferences = EadService.readProperties(queueItem.getPreferences());
					IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction
							.getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
					displayItem.setAction(displayItem.getAction() + "("
							+ getText(ingestionprofileDefaultUploadAction.getResourceName()) + ")");
				}
			} catch (Exception e) {

			}
			results.add(displayItem);
		}
		return results;
	}

	public String manageQueueItem() throws Exception {
		QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
		QueueItem queueItem = queueDAO.findById(queueItemId);
		if ("DELETE".equals(selectedAction)) {
			EadService.deleteFromQueue(queueItem);
		} else {
			queueItem.setErrors(null);
			if ("DISABLE".equals(selectedAction)) {
				queueItem.setPriority(0);
			} else if ("ENABLE".equals(selectedAction)) {
				queueItem.setPriority(1000);
			}  else if ("HIGHEST".equals(selectedAction)) {
				queueItem.setPriority(5000);
			}else if ("LOWEST".equals(selectedAction)) {
				queueItem.setPriority(1);
			}
			queueDAO.store(queueItem);
		}
		return SUCCESS;
	}

	public String deleteAllQueueItemsWithErrors() throws Exception {
		QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
		List<QueueItem> queueItems = queueDAO.getItemsWithErrors();
		for (QueueItem queueItem : queueItems) {
			try {
				EadService.deleteFromQueue(queueItem);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return SUCCESS;
	}

	public String deleteAllUnusedUploadFiles() throws Exception {
		EadService.deleteAllUnusedUploadFiles();
		return SUCCESS;
	}

	public String forceSolrCommit() throws Exception {
		try {
			LOGGER.info("Start hard commit solr cores");
			EacCpfSolrServerHolder.getInstance().hardCommit();
			LOGGER.info("EAC-CPF hard commit finished");
			EagSolrServerHolder.getInstance().hardCommit();
			LOGGER.info("EAG hard commit finished");
			EadSolrServerHolder.getInstance().hardCommit();
			LOGGER.info("EAD hard commit finished");
		} catch (Exception de) {
			LOGGER.error(de.getMessage(), de);
		}
		return SUCCESS;
	}

	public String republishAllEagFiles(){
		
			EagSolrPublisher publisher = new EagSolrPublisher();
			try {
				publisher.deleteEverything();
			}catch(Exception e){
				LOGGER.error(e.getMessage(), e);
			}	
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsWithRepositoryCode();
			for (ArchivalInstitution archivalInstitution: archivalInstitutions){
				try {
				System.out.println("Publish : " + archivalInstitution.getAiId() + " " + archivalInstitution.getAiname());
				XmlEagParser.parseAndPublish(archivalInstitution);
				}catch(Exception e){
					LOGGER.error(e.getMessage(), e);
				}	
			}

		return SUCCESS;
	}
	public String startStopQueue() {
		if (QueueDaemon.isActive()) {
			QueueDaemon.stop();
		} else {
			QueueDaemon.start();
		}
		return SUCCESS;
	}

}
