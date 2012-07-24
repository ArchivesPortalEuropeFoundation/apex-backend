package eu.apenet.dashboard.security.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContextContainer;
import eu.apenet.dashboard.security.SecurityService;

public class SessionManagementAction extends AbstractAction implements ServletRequestAware {

	private String sessionId;
	private HttpServletRequest request;
	
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
		request.setAttribute("sessionInfos", SecurityContextContainer.retrieveSessionInfo());
		return SUCCESS;
	}
	public String deleteSession(){
		SecurityService.deleteSession(sessionId);
		return SUCCESS;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
}
