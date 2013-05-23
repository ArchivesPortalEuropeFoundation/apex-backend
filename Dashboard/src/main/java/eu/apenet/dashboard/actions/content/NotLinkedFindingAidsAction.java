package eu.apenet.dashboard.actions.content;

import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;

public class NotLinkedFindingAidsAction extends AbstractInstitutionAction{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4513310293148562803L;
    private Integer id;
	private Integer pageNumber = 1;
	private Integer resultPerPage = 100;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getResultPerPage() {
		return resultPerPage;
	}
	public void setResultPerPage(Integer resultPerPage) {
		this.resultPerPage = resultPerPage;
	}


	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.contentmanager"));
	}
	
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	



	@Override
	public String execute() throws Exception {
		FindingAidDAO faDAO = DAOFactory.instance().getFindingAidDAO();
		CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
		getServletRequest().setAttribute("notUploadedFindingAids", cLevelDAO.getCLevelsOutOfSystemByHoldingsGuideId(getId(), resultPerPage, pageNumber));
		getServletRequest().setAttribute("countNotUploadedFindingAids", cLevelDAO.countCLevelsOutOfSystemByHoldingsGuideId(getId()));
		getServletRequest().setAttribute("notIndexedFindingAids", faDAO.getFindingAidsByHoldingsGuideId(getId(),false, resultPerPage, pageNumber));
		getServletRequest().setAttribute("countNotIndexedFindingAids", faDAO.countFindingAidsByHoldingsGuideId(getId(),false));
		return SUCCESS;
	}







	
}
