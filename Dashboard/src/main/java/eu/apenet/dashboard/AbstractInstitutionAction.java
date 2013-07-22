package eu.apenet.dashboard;

import eu.apenet.dashboard.security.SecurityContext;


public abstract class AbstractInstitutionAction extends AbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2856463362261512454L;
	
	public final Integer getAiId(){
		SecurityContext securityContext = SecurityContext.get();
        if (securityContext == null || securityContext.getSelectedInstitution() == null){
            return null;
        }else {
            return securityContext.getSelectedInstitution().getId();
        }
	}
	public final String getAiname(){
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext == null || securityContext.getSelectedInstitution() == null){
            return null;
		}else {
            return securityContext.getSelectedInstitution().getName();
		}
	}

	protected void buildBreadcrumbs(){
		super.buildBreadcrumbs();
		addBreadcrumb( "dashboardHome.action", getAiname() );
	}
}
