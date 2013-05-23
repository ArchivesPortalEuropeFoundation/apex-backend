package eu.apenet.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.dashboard.security.SecurityContext;

public abstract class AbstractAction extends ActionSupport  implements Preparable, ServletRequestAware, ServletResponseAware{
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
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	protected HttpServletRequest getServletRequest() {
		return request;
	}

	protected HttpServletResponse getServletResponse() {
		return response;
	}

	@Override
	public void prepare() throws Exception {
		buildBreadcrumbs();
	}
	
}
