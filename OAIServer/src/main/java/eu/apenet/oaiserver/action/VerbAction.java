package eu.apenet.oaiserver.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.oaiserver.util.OAISyntaxChecker;



public class VerbAction extends ActionSupport implements ServletRequestAware{
	private static final String REQUEST_SUFIX = "/request";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1614412384938099856L;
	private static Logger LOG = Logger.getLogger(VerbAction.class);
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
			String url = request.getContextPath() + REQUEST_SUFIX;;
			LOG.info(request.getUserPrincipal() + ": " + url+ request.getQueryString()) ;
			this.inputStream = OAISyntaxChecker.process(request.getParameterMap(),url);
		} catch (Exception e) {
			LOG.error("Error trying to check verb and params in OAISyntaxChecker.check.",e);
		}
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
