package eu.apenet.oaiserver.action;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.oaiserver.util.OAISyntaxChecker;

public class VerbAction extends ActionSupport implements ServletRequestAware{
	
	private InputStream inputStream;
	private HttpServletRequest request;
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * It's the unique action that must be called in server. 
	 */
	public String execute(){
		try {
			this.inputStream = OAISyntaxChecker.check(OAISyntaxChecker.fillParams(this.request),this.request);
		} catch (Exception e) {
			LOG.error("Error trying to check verb and params in OAISyntaxChecker.check.",e);
		}
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	
}
