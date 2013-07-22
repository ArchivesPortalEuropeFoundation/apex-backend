package eu.apenet.dashboard;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.security.SecurityService;
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

		ArchivalLandscape al = new ArchivalLandscape();
		String pathCountry = al.getmyPath(al.getmyCountry());	
		
		File[] files = new File(pathCountry).listFiles();    	
    	if (files != null && files.length > 0) //The directory is not empty
    	{ 
    		pathCountry = pathCountry + files[0].getName();
            log.debug("Read XML file: " + pathCountry);
            this.archives = al.showArchives();            
            if (this.archives.size()==0) {
                log.error("The list of archives is empty for this country: " + pathCountry);
    			return ERROR;
            }else if(this.archives.size() == 1){
            	SecurityService.selectArchivalInstitution(archives.get(0).getAiId());
            	return "one-archive";
            }
            else {
            	addActionMessage(getText("al.message.instselection"));
            }
            return SUCCESS;
    	}
    	log.info("GoDashboardAction: execute() There is no archival landscape definition in ('" + pathCountry + "'). User can't go to the Dashboard");
    	return ERROR;
	}
	public String selectArchive(){
		SecurityService.selectArchivalInstitution(ai_selected);		
		return SUCCESS;
	}

}