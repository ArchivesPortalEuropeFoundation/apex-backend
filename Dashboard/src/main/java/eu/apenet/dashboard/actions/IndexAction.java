package eu.apenet.dashboard.actions;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;

public class IndexAction extends AbstractAction {
	private static final long serialVersionUID = -904941635133388559L;

	@Override
    public String execute() throws Exception {
		SecurityContext securityContext = SecurityContext.get();
        if(securityContext != null){
			if (securityContext.isAdminOrCoordinator()) {
				return "success_admin";
			} else if (securityContext.isCountryManager()) {
				this.removeInvalidEAG(securityContext.getSelectedInstitution());
				return "success_country";
			}
			else {
				this.removeInvalidEAG(securityContext.getSelectedInstitution());
				return SUCCESS;
			}
        }
        if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()){
        	this.getServletRequest().setAttribute("maintenanceMode", true);
        }
        return ERROR;
    }

}