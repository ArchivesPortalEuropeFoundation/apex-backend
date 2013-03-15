package eu.apenet.dashboard;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

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
		this.setErrorFiles(DAOFactory.instance().getQueueItemDAO().hasItemsWithErrors(getAiId()));
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

}