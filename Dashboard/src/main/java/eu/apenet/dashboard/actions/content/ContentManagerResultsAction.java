package eu.apenet.dashboard.actions.content;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FindingAid;

public class ContentManagerResultsAction extends AbstractInstitutionAction implements ServletRequestAware{

	private HttpServletRequest request;
	private Integer pageNumber = 1;
	private Integer resultPerPage = 20;
	private String orderByField = "id";
	private boolean orderByAscending = true;
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request= request;
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4513310293148562803L;

	@Override
	public String execute() throws Exception {
		fillResults();
		return SUCCESS;
	}
	private void fillResults(){
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setEadClazz(FindingAid.class);
		eadSearchOptions.setPageNumber(pageNumber);
		eadSearchOptions.setPageSize(resultPerPage);
		eadSearchOptions.setOrderByAscending(orderByAscending);
		eadSearchOptions.setOrderByField(orderByField);
		eadSearchOptions.setArchivalInstitionId(getAiId());
		ContentManagerResults results = new ContentManagerResults();
		results.setEads(DAOFactory.instance().getEadDAO().getEads(eadSearchOptions));
		results.setTotalNumberOfResults(DAOFactory.instance().getEadDAO().countEads(eadSearchOptions));
		results.setEadSearchOptions(eadSearchOptions);
		request.setAttribute("results", results);
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
	public String getOrderByField() {
		return orderByField;
	}
	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}
	public boolean isOrderByAscending() {
		return orderByAscending;
	}
	public void setOrderByAscending(boolean orderByAscending) {
		this.orderByAscending = orderByAscending;
	}

	
}
