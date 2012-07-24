package eu.apenet.dashboard.actions;
	

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.persistence.dao.IndexQueueDAO;

import eu.apenet.persistence.factory.DAOFactory;

import eu.apenet.persistence.vo.IndexQueue;



public class ShowFilesFailedAction extends ActionSupport{
	
	List<IndexQueue> filesFailed;
	private String idSelected;

	private final Logger log = Logger.getLogger(getClass());
	
	
	public List<IndexQueue> getFilesFailed() {
		return filesFailed;
	}
	

	public void setFilesFailed(List<IndexQueue> filesFailed) {
		this.filesFailed = filesFailed;
	}


	public String getIdSelected() {
		return idSelected;
	}


	public void setIdSelected(String idSelected) {
		this.idSelected = idSelected;
	}



	public String execute(){

		IndexQueueDAO indexqueueDAO =  DAOFactory.instance().getIndexQueueDAO();			
		filesFailed = new ArrayList<IndexQueue>(); 
		this.setFilesFailed(indexqueueDAO.getFilesWithErrors());
		
		return SUCCESS;
	}
}

