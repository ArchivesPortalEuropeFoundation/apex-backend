package eu.apenet.dashboard.actions;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;

/**
 * Class created for manage Struts2-action related to index action
 * (first page after log-in).
 */
public class IndexAction extends AbstractAction {
	
	private static final long serialVersionUID = -904941635133388559L;
	/**
	 * Action mapped into struts.xml
	 * It's index for admin, country-manager and do also maintenance task.
	 * @return Structs.STATE
	 */
	@Override
    public String execute() throws Exception {
		SecurityContext securityContext = SecurityContext.get(); //security checks (role)
        if(securityContext != null){
			if (securityContext.isAdmin()) { //admin part
				return "success_admin";
			} else if (securityContext.isCountryManager()) {
				this.removeInvalidEAG(securityContext.getSelectedInstitution()); //country manager part
				return "success_country";
			}
			else { //launches old action to remove invalid old eags (apparently is a maintenance task done by some developer
				this.removeInvalidEAG(securityContext.getSelectedInstitution());
				return SUCCESS;
			}
        }
        if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()){ 
        	this.getServletRequest().setAttribute("maintenanceMode", true); //flag for maintenance mode
        }
        return ERROR;
    }
}