package eu.apenet.dashboard.actions;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.Breadcrumb;
import eu.apenet.dashboard.security.SecurityContext;

public class IndexAction extends AbstractInstitutionAction {
	private static final long serialVersionUID = -904941635133388559L;

	@Override
    public String execute() throws Exception {
		buildBreadcrumbs();
		SecurityContext securityContext = SecurityContext.get();
        if(securityContext != null){
			if (securityContext.isAdmin()) {
				return "success_admin";
			} else if (securityContext.isCountryManager()) {
				this.removeInvalidEAG(this.getAiId());
				return "success_country";
			}
			else {
				this.removeInvalidEAG(this.getAiId());
				return SUCCESS;
			}
        }
        if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()){
        	this.getServletRequest().setAttribute("maintenanceMode", true);
        }
        return ERROR;
    }
	
	private List<Breadcrumb> breadcrumbRoute;

	@Override
	protected void buildBreadcrumbs() {
		this.breadcrumbRoute = new ArrayList<Breadcrumb>();
		Breadcrumb breadcrumb = new Breadcrumb(null,getText("breadcrumb.section.home"));
		this.breadcrumbRoute.add(breadcrumb);
	}
}