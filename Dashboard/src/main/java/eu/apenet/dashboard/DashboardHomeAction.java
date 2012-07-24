package eu.apenet.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.IndexQueueDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.IndexQueue;

/**
 * Paul
 */

public class DashboardHomeAction extends AbstractInstitutionAction {

	private static final long serialVersionUID = -7244833981839205973L;

	
	
	private Boolean errorFiles = false;
	
	public Boolean getErrorFiles() {
		return errorFiles;
	}
	public void setErrorFiles(Boolean errorFiles) {
		this.errorFiles = errorFiles;
	}
	


	public String execute() throws Exception  {
		
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution  archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
		this.setErrorFiles(isThereErrorFiles());
        if (archivalInstitution.getEagPath() == null){
			//This is the first time that partner enters to the Dashboard
			//It is necessary to upload or create an EAG file			
			addActionMessage(getText("label.eag.firstcreation"));
			return INPUT;
		}
		else {
			return SUCCESS;			
		}
    }

	
	/**
	 * This method checks if this institution has files which could not be indexed during indexing night process 
	 * and then a message is showed in content manager to the user.
	 * @return false or true
	 * 
	 * */
	public Boolean isThereErrorFiles(){    	
   	 		
		IndexQueueDAO iqDao = DAOFactory.instance().getIndexQueueDAO();
		List<IndexQueue> iqList = new ArrayList<IndexQueue>(); 
		iqList = iqDao.getFilesWithErrors();
		
		if (iqList.size()==0)
			return false;
		else
		{
			for (int i=0;i<iqList.size();i++)
			{
				if ((iqList.get(i).getFindingAid()!=null) && (iqList.get(i).getFindingAid().getArchivalInstitution().getAiId()== this.getAiId()))
                    return true;
				if ((iqList.get(i).getHoldingsGuide()!=null) && (iqList.get(i).getHoldingsGuide().getArchivalInstitution().getAiId()== this.getAiId()))
                    return true;
			}
		}
		return false;
    }
}