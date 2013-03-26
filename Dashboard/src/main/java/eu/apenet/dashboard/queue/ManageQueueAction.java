package eu.apenet.dashboard.queue;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.QueueDaemon;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.QueueItem;

public class ManageQueueAction  extends AbstractAction implements ServletRequestAware {
	private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); 
	private HttpServletRequest request;
	private Integer queueItemId;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7015833987047809962L;
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
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
		request.setAttribute("numberOfItemsInQueue", queueDAO.countItems());
		request.setAttribute("firstItems", queueDAO.getFirstItems());
		request.setAttribute("itemsWithErrors", queueDAO.getItemsWithErrors());
		request.setAttribute("queueActive", QueueDaemon.isActive());
		request.setAttribute("queueProcessing", QueueDaemon.isQueueProcessing());
		request.setAttribute("harvestingStarted", EadService.isHarvestingStarted());
		request.setAttribute("currentTime", DATE_TIME.format(new Date()));
		Date endDateTime = DAOFactory.instance().getResumptionTokenDAO().getPossibleEndDateTime();
		if (endDateTime != null)
			request.setAttribute("harvestingEndTime", DATE_TIME.format(endDateTime));
		return SUCCESS;
	}
	public String deleteQueueItem() throws Exception{
		QueueItemDAO queueDAO =  DAOFactory.instance().getQueueItemDAO();
		QueueItem queueItem = queueDAO.findById(queueItemId);
		EadService.deleteFromQueue(queueItem);
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
