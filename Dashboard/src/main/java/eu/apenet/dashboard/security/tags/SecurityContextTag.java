package eu.apenet.dashboard.security.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import eu.apenet.dashboard.security.SecurityContext;

public class SecurityContextTag extends SimpleTagSupport {

	private String var;
	
	@Override
	public void doTag() throws JspException, IOException {
		this.getJspContext().setAttribute(var, SecurityContext.get());
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}




}
