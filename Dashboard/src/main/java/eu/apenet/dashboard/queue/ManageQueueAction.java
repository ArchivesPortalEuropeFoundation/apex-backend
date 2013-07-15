package eu.apenet.dashboard.queue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.QueueDaemon;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.QueueItem;

public class ManageQueueAction  extends AbstractAction{
	private static final Logger LOGGER = Logger.getLogger(ManageQueueAction.class);
	private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); 
	private Integer queueItemId;
	

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

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("admin.queuemanagement.title"));
	}

	public String execute() throws Exception{
		QueueItemDAO queueDAO =  DAOFactory.instance().getQueueItemDAO();	
		getServletRequest().setAttribute("numberOfItemsInQueue", queueDAO.countItems());
		getServletRequest().setAttribute("firstItems", queueDAO.getFirstItems());
		getServletRequest().setAttribute("itemsWithErrors", queueDAO.getItemsWithErrors());
		getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
		getServletRequest().setAttribute("queueProcessing", QueueDaemon.isQueueProcessing());
		getServletRequest().setAttribute("harvestingStarted", EadService.isHarvestingStarted());
		getServletRequest().setAttribute("currentTime", DATE_TIME.format(new Date()));
		Date endDateTime = DAOFactory.instance().getResumptionTokenDAO().getPossibleEndDateTime();
		if (endDateTime != null)
			getServletRequest().setAttribute("harvestingEndTime", DATE_TIME.format(endDateTime));
		return SUCCESS;
	}
	public String deleteQueueItem() throws Exception{
		QueueItemDAO queueDAO =  DAOFactory.instance().getQueueItemDAO();
		QueueItem queueItem = queueDAO.findById(queueItemId);
		EadService.deleteFromQueue(queueItem);
		return SUCCESS;
	}
	
	public String deleteAllQueueItemsWithErrors() throws Exception{
		QueueItemDAO queueDAO =  DAOFactory.instance().getQueueItemDAO();
		List<QueueItem> queueItems = queueDAO.getItemsWithErrors();
		for (QueueItem queueItem: queueItems){
			try {
				EadService.deleteFromQueue(queueItem);
			}catch (Exception e){
				LOGGER.error(e.getMessage(), e);
			}
		}
		return SUCCESS;
	}
	
	public String deleteAllUnusedUploadFiles() throws Exception{
		EadService.deleteAllUnusedUploadFiles();
		return SUCCESS;
	}
	
	public String startStopQueue(){
		if (QueueDaemon.isActive()){
			QueueDaemon.stop();
		}else {
			QueueDaemon.start();
		}
		return SUCCESS;
	}

}	
