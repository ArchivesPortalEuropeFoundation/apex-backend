package eu.apenet.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.dashboard.security.SecurityContext;

public abstract class AbstractAction extends ActionSupport  implements Preparable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5482269857351940473L;
	
	public SecurityContext getSecurityContext(){
		return SecurityContext.get();
	}
	
	private List<Breadcrumb> breadcrumbRoute = new ArrayList<Breadcrumb>();
	
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
	public void prepare() throws Exception {
		buildBreadcrumbs();
	}
	
}
