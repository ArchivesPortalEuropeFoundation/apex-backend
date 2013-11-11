package eu.apenet.dashboard;

import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;


/**
 * User: Jara Alvarez & Paul
 * Date: Sep 27th, 2010
 *
 */

public class GoDashboardAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5637650587384475582L;

	private Logger log = Logger.getLogger(getClass());

	private List<ArchivalInstitution> archives;
	private Integer ai_id;
	private Integer ai_selected;
	


	
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();		
		addBreadcrumb(getText("breadcrumb.section.selectArchive"));
	}

	public Integer getAi_selected() {
		return ai_selected;
	}

	public void setAi_selected(Integer ai_selected) {
		this.ai_selected = ai_selected;
	}

	public Integer getAi_id() {
		return ai_id;
	}

	public void setAi_id(Integer ai_id) {
		this.ai_id = ai_id;
	}


	public List<ArchivalInstitution> getArchives() {
		return archives;
	}

	public String execute() throws Exception{
		Integer countryId = SecurityContext.get().getCountryId();
		if(countryId!=null){
			log.debug("Reading archival institutions from DDBB");
			this.archives = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsNoGroups(countryId);
			return SUCCESS;
		}
    	return ERROR;
	}
	public String selectArchive(){
		SecurityService.selectArchivalInstitution(ai_selected);		
		return SUCCESS;
	}

}