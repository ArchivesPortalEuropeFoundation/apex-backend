package eu.apenet.dashboard.security.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import eu.apenet.dashboard.security.SecurityContextContainer;

/**
 * Checks if an user currently use the dashboard.
 * 
 * @author bastiaan
 *
 */
public class UserAvailableTag extends SimpleTagSupport {

	private String var;
	private Object userId;
	
	@Override
	public void doTag() throws JspException, IOException {
		boolean available = SecurityContextContainer.checkAvailability((Integer) userId);
		this.getJspContext().setAttribute(var, available);
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Object getUserId() {
		return userId;
	}

	public void setUserId(Object userId) {
		this.userId = userId;
	}




}
