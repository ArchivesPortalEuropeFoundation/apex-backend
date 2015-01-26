package eu.apenet.dashboard.security.actions;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityContextContainer;
import eu.apenet.dashboard.security.SecurityService;

public class SessionManagementAction extends AbstractAction {

	private String sessionId;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7015833987047809962L;

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("dashboard.sessionmanagement.title"));
	}
	@Override
	public String execute(){
		getServletRequest().setAttribute("sessionInfos", SecurityContextContainer.retrieveSessionInfo());
		return SUCCESS;
	}
	public String deleteSession(){
		if(SecurityContext.get().isAdmin())
			SecurityService.deleteSession(sessionId);
		return SUCCESS;
	}
	
}
