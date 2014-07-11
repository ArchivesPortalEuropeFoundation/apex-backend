package eu.apenet.dashboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscapeUtils;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.security.SecurityContext;

public abstract class AbstractAction extends ActionSupport  implements Preparable, ServletRequestAware, ServletResponseAware, ServletContextAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5482269857351940473L;
	
	public SecurityContext getSecurityContext(){
		return SecurityContext.get();
	}
	
	private List<Breadcrumb> breadcrumbRoute = new ArrayList<Breadcrumb>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;
	public final List<Breadcrumb> getBreadcrumbRoute(){
		return this.breadcrumbRoute;
	}
	
	protected final void addBreadcrumb(String value, String label){
		breadcrumbRoute.add(new Breadcrumb(value,label));
	}
	protected final void addBreadcrumb(String label){
		breadcrumbRoute.add(new Breadcrumb(null,label));
	}
	protected void buildBreadcrumbs(){
		
	}

	
	@Override 
	public final void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	@Override
	public final void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	protected HttpServletRequest getServletRequest() {
		return request;
	}

	protected HttpServletResponse getServletResponse() {
		return response;
	}

        public ServletContext getServletContext() {
            return servletContext;
        }

        @Override
        public final void setServletContext(ServletContext servletContext) {
            this.servletContext = servletContext;
        }
        
	@Override
	public void prepare() throws Exception {
		buildBreadcrumbs();
	}

	/**
	 * Method to delete temporary EAG 2012 files.
	 */
	protected String removeInvalidEAG(final Integer aiId) {
		String alCountry = new ArchivalLandscapeUtils().getmyCountry();
		String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR +
				aiId + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
		String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
		File invalidFile=new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath);
		if (invalidFile.exists() && invalidFile.isFile()) {
			try {
				FileUtils.forceDelete(invalidFile);
			} catch (IOException e) {
				return "ERROR trying to remove the file " + tempPath;
			}
		}  
		return SUCCESS;
	}
}
