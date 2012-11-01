package eu.apenet.dashboard.actions;
	

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.persistence.dao.QueueItemDAO;

import eu.apenet.persistence.factory.DAOFactory;

import eu.apenet.persistence.vo.QueueItem;



public class ShowFilesFailedAction extends ActionSupport{
	
	List<QueueItem> filesFailed;
	private String idSelected;

	private final Logger log = Logger.getLogger(getClass());
	
	
	public List<QueueItem> getFilesFailed() {
		return filesFailed;
	}
	

	public void setFilesFailed(List<QueueItem> filesFailed) {
		this.filesFailed = filesFailed;
	}


	public String getIdSelected() {
		return idSelected;
	}


	public void setIdSelected(String idSelected) {
		this.idSelected = idSelected;
	}



	public String execute(){

		QueueItemDAO indexqueueDAO =  DAOFactory.instance().getQueueItemDAO();			
		filesFailed = new ArrayList<QueueItem>(); 
		this.setFilesFailed(indexqueueDAO.getFilesWithErrors());
		
		return SUCCESS;
	}
}

