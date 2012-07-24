package eu.apenet.dashboard.actions;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dashboard.security.SecurityService;

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
		SecurityService.logout("true".equals(parent));
        return SUCCESS;
    }

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}


	    
}
