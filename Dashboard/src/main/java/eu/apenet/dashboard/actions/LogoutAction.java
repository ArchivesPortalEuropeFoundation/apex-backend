package eu.apenet.dashboard.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 *
 * @author Paul
 */

public class LogoutAction extends ActionSupport {

	private static final long serialVersionUID = 8390864001027173670L;
	private String parent;
	private Logger log = Logger.getLogger(getClass());

	public String execute() throws Exception {
		log.trace("LogoutAction: execute() method is called");
		log.trace("LogoutAction: removing the EAG temp files");
		this.removeInvalidEAGAllInstitutions();
	    log.trace("LogoutAction: logout user");
		SecurityService.logout("true".equals(parent));
        return SUCCESS;
    }

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

    public void removeInvalidEAGAllInstitutions(){
    	ArchivalLandscape al = new ArchivalLandscape();
    	String alCountry = new ArchivalLandscape().getmyCountry();
    	List<ArchivalInstitution> archives = al.showArchives();
    	for(int i=0;i<archives.size();i++){
		   String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR +
					archives.get(i).getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
	       String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
	       File invalidFile=new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath);
	       if (invalidFile.exists() && invalidFile.isFile()) {
				try {
					FileUtils.forceDelete(invalidFile);
				} catch (IOException e) {
					log.error("ERROR trying to remove the file " + tempPath, e);
				}
			}  
    		
    	}
    }
	    
}
