package eu.apenet.dashboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ViewALAction extends ActionSupport implements ServletRequestAware {

	//Attributes
	
	private static final long serialVersionUID = -846464987187314333L;
	private Integer couId;			// Identifier for the country which belongs to the partner
	private Integer ai_id;
	private HttpServletRequest request;
	private List<Breadcrumb> breadcrumbRoute;
	
	public List<Breadcrumb> getBreadcrumbRoute(){
		return this.breadcrumbRoute;
	}

	public Integer getCouId() {
		return couId;
	}

	public void setCouId(Integer couId) {
		this.couId = couId;
	}

	public Integer getAi_id() {
		return ai_id;
	}

	public void setAi_id(Integer ai_id) {
		this.ai_id = ai_id;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	//Validator
	@Override
	public void validate() {
		super.validate();
		buildBreadcrumb();
	}

	private void buildBreadcrumb() {
		this.breadcrumbRoute = new ArrayList<Breadcrumb>();
		Breadcrumb breadcrumb = new Breadcrumb(null,getText("breadcrumb.section.previewal"));
		this.breadcrumbRoute.add(breadcrumb);
	}
	
	//Constructor
	
	//Methods
	public String execute() throws Exception{
		this.couId = SecurityContext.get().getCountryId();
			return SUCCESS;
	}
	
	public String showEAG(){
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution institution = aiDao.findById(ai_id);
		String eagPath = institution.getEagPath();
		if (eagPath != null && !eagPath.isEmpty()) {
			String prefixPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
			eagPath = prefixPath + eagPath;
			if (!new File(eagPath).exists()) {
				prefixPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
				eagPath = prefixPath + institution.getEagPath();
			}
			File eagFile = new File(eagPath);
			request.setAttribute("eagUrl", eagFile.getAbsoluteFile());
		}
		return SUCCESS;
		
	}

	
}