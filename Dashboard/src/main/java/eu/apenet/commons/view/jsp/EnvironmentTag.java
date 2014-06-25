package eu.apenet.commons.view.jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class EnvironmentTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();  
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();  
		String serverName = request.getServerName();
		String result = "Local development";
		if ("dashboard.archivesportaleurope.net".equalsIgnoreCase(serverName)){
			result = "Production";
		}else if ("acc-dashboard.archivesportaleurope.net".equalsIgnoreCase(serverName)){
			result = "Acceptance";
		}else if ("test.archivesportaleurope.net".equalsIgnoreCase(serverName)){
			result = "Test";
		}else if ("development.archivesportaleurope.net".equalsIgnoreCase(serverName)){
			result = "Development";
		}else if ("contentchecker.archivesportaleurope.net".equalsIgnoreCase(serverName)){
			result = "Content checker";
		}
		this.getJspContext().getOut().write(result);
	}

}
