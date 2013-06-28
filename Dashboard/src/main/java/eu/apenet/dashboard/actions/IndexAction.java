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
			if (securityContext.isAdmin()) {
				return "success_admin";
			} else if (securityContext.isCountryManager()) {
				return "success_country";
			}
			else {
				return SUCCESS;
			}
        }
        if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()){
        	addActionError(getText("user.maintenancemode"));
        }
        return ERROR;
    }
}