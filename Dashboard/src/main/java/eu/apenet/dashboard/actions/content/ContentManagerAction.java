package eu.apenet.dashboard.actions.content;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.ValidatedState;

public class ContentManagerAction extends AbstractInstitutionAction implements ServletRequestAware{

	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String EAD_SEARCH_OPTIONS = "eadSearchOptions";
	protected static final String CONTENT_MESSAGE_NO = "content.message.no";
	protected static final String CONTENT_MESSAGE_YES = "content.message.yes";
	protected static final String CONTENT_MESSAGE_QUEUE = "content.message.queue";
	protected static final String CONTENT_MESSAGE_ERROR = "content.message.fatalerror";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4513310293148562803L;
    private Map<String, String> typeList = new LinkedHashMap<String, String>();	
    private String type = XmlType.EAD_FA.getSolrPrefix();
    private Map<String, String> convertedStatusList = new LinkedHashMap<String, String>();
    private String[] convertedStatus = new String[] {FALSE, TRUE};
    private Map<String, String> validatedStatusList = new LinkedHashMap<String, String>();
    private String[] validatedStatus = new String[] {ValidatedState.NOT_VALIDATED.toString(), ValidatedState.VALIDATED.toString(), ValidatedState.FATAL_ERROR.toString()};
    private Map<String, String> publishedStatusList = new LinkedHashMap<String, String>();
    private String[] publishedStatus = new String[] {FALSE, TRUE};
    private Map<String, String> europeanaStatusList = new LinkedHashMap<String, String>();
    private String[] europeanaStatus = new String[] {EuropeanaState.NOT_CONVERTED.toString(), EuropeanaState.CONVERTED.toString(),EuropeanaState.DELIVERED.toString(), EuropeanaState.HARVESTED.toString()};
    private Map<String, String> searchTermsFieldList = new LinkedHashMap<String, String>();
    private HttpServletRequest request;
	private Integer pageNumber = 1;
	private Integer resultPerPage = 20;
	private String orderByField = "id";
	private boolean orderByAscending = false;
	private String searchTerms;
	private String searchTermsField;
	private boolean refreshFromSession = true;
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request= request;
		
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

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.contentmanager"));
	}
	
	
	@Override
	public void prepare() throws Exception {
		fillFields();
		super.prepare();
	}
	private void fillFields(){
		convertedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
		convertedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
		validatedStatusList.put(ValidatedState.VALIDATED.toString(), getText(CONTENT_MESSAGE_YES));
		validatedStatusList.put(ValidatedState.NOT_VALIDATED.toString(), getText(CONTENT_MESSAGE_NO));
		validatedStatusList.put(ValidatedState.FATAL_ERROR.toString(), getText(CONTENT_MESSAGE_ERROR));
		publishedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
		publishedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
		europeanaStatusList.put(EuropeanaState.CONVERTED.toString(), getText("content.message.eseedm"));
		europeanaStatusList.put(EuropeanaState.NOT_CONVERTED.toString(), getText(CONTENT_MESSAGE_NO));
		europeanaStatusList.put(EuropeanaState.DELIVERED.toString(), getText("content.message.europeana.delivered"));
		europeanaStatusList.put(EuropeanaState.HARVESTED.toString(), getText("content.message.europeana.harvested"));
		typeList.put(XmlType.EAD_FA.getSolrPrefix(), getText("content.message." + XmlType.EAD_FA.getResourceName()));
		typeList.put(XmlType.EAD_HG.getSolrPrefix(), getText("content.message." + XmlType.EAD_HG.getResourceName()));
		typeList.put(XmlType.EAD_SG.getSolrPrefix(), getText("content.message." + XmlType.EAD_SG.getResourceName()));
		searchTermsFieldList.put("", getText("content.message.all"));
		searchTermsFieldList.put("eadid", getText("content.message.id"));
		searchTermsFieldList.put("title", getText("content.message.title"));
		if (refreshFromSession){
			EadSearchOptions eadSearchOptions = (EadSearchOptions) request.getSession()
					.getAttribute(EAD_SEARCH_OPTIONS);
			if (eadSearchOptions != null) {
				if (eadSearchOptions.getConverted() != null) {
					convertedStatus = new String[] { eadSearchOptions.getConverted().toString() };
				}
				if (eadSearchOptions.getValidated().size() > 0) {
					validatedStatus = new String[eadSearchOptions.getValidated().size()];
					for (int i = 0; i < eadSearchOptions.getValidated().size(); i++) {
						validatedStatus[i] = eadSearchOptions.getValidated().get(i).toString();
					}
				}
				if (eadSearchOptions.getPublished() != null) {
					publishedStatus = new String[] { eadSearchOptions.getPublished().toString() };
				}
				if (eadSearchOptions.getEuropeana().size() > 0) {
					europeanaStatus = new String[eadSearchOptions.getEuropeana().size()];
					for (int i = 0; i < eadSearchOptions.getEuropeana().size(); i++) {
						europeanaStatus[i] = eadSearchOptions.getEuropeana().get(i).toString();
					}
				}
				type = XmlType.getType(eadSearchOptions.getEadClazz()).getSolrPrefix();
				searchTerms = eadSearchOptions.getSearchTerms();
				//orderByField = eadSearchOptions.getOrderByField();
				//orderByAscending = eadSearchOptions.isOrderByAscending();
				searchTermsField = eadSearchOptions.getSearchTermsField();
			}
		}
	}

	@Override
	public String execute() throws Exception {
		fillResults();
		return SUCCESS;
	}
	private void fillResults(){
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageNumber(pageNumber);
		eadSearchOptions.setPageSize(resultPerPage);
		eadSearchOptions.setOrderByAscending(orderByAscending);
		eadSearchOptions.setOrderByField(orderByField);
		eadSearchOptions.setArchivalInstitionId(getAiId());
		if (convertedStatus != null && convertedStatus.length == 1){
			eadSearchOptions.setConverted(Boolean.valueOf(convertedStatus[0]));
		}
		if (validatedStatus != null && validatedStatus.length >= 1 && validatedStatus.length <= 2){
			for (String validatedStatusItem: validatedStatus){
				eadSearchOptions.getValidated().add(ValidatedState.getValidatedState(validatedStatusItem));
			}
		}
		if (publishedStatus != null && publishedStatus.length == 1){
			eadSearchOptions.setPublished(Boolean.valueOf(publishedStatus[0]));
		}
		if (europeanaStatus != null && europeanaStatus.length >= 1 && europeanaStatus.length <= 3){
			for (String europeanaStatusItem: europeanaStatus){
				eadSearchOptions.getEuropeana().add(EuropeanaState.getEuropeanaState(europeanaStatusItem));
			}
		}
		eadSearchOptions.setSearchTerms(searchTerms);
		eadSearchOptions.setSearchTermsField(searchTermsField);
		eadSearchOptions.setEadClazz(XmlType.getTypeBySolrPrefix(type).getClazz());
		ContentManagerResults results = new ContentManagerResults(eadSearchOptions);
		results.setEads(DAOFactory.instance().getEadDAO().getEads(eadSearchOptions));
		results.setTotalNumberOfResults(DAOFactory.instance().getEadDAO().countEads(eadSearchOptions));
		request.getSession().setAttribute(EAD_SEARCH_OPTIONS, eadSearchOptions);
		request.setAttribute("results", results);
	}




	public Map<String, String> getConvertedStatusList() {
		return convertedStatusList;
	}




	public void setConvertedStatusList(Map<String, String> convertedStatusList) {
		this.convertedStatusList = convertedStatusList;
	}




	public String[] getConvertedStatus() {
		return convertedStatus;
	}




	public void setConvertedStatus(String[] convertedStatus) {
		this.convertedStatus = convertedStatus;
	}




	public Map<String, String> getValidatedStatusList() {
		return validatedStatusList;
	}




	public void setValidatedStatusList(Map<String, String> validatedStatusList) {
		this.validatedStatusList = validatedStatusList;
	}




	public String[] getValidatedStatus() {
		return validatedStatus;
	}




	public void setValidatedStatus(String[] validatedStatus) {
		this.validatedStatus = validatedStatus;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public Map<String, String> getPublishedStatusList() {
		return publishedStatusList;
	}

	public void setPublishedStatusList(Map<String, String> publishedStatusList) {
		this.publishedStatusList = publishedStatusList;
	}

	public String[] getPublishedStatus() {
		return publishedStatus;
	}

	public void setPublishedStatus(String[] publishedStatus) {
		this.publishedStatus = publishedStatus;
	}

	public Map<String, String> getEuropeanaStatusList() {
		return europeanaStatusList;
	}

	public void setEuropeanaStatusList(Map<String, String> europeanaStatusList) {
		this.europeanaStatusList = europeanaStatusList;
	}

	public String[] getEuropeanaStatus() {
		return europeanaStatus;
	}

	public void setEuropeanaStatus(String[] europeanaStatus) {
		this.europeanaStatus = europeanaStatus;
	}

	public Map<String, String> getTypeList() {
		return typeList;
	}

	public void setTypeList(Map<String, String> typeList) {
		this.typeList = typeList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRefreshFromSession() {
		return refreshFromSession;
	}

	public void setRefreshFromSession(boolean refreshFromSession) {
		this.refreshFromSession = refreshFromSession;
	}

	public Map<String, String> getSearchTermsFieldList() {
		return searchTermsFieldList;
	}

	public void setSearchTermsFieldList(Map<String, String> searchTermsFieldList) {
		this.searchTermsFieldList = searchTermsFieldList;
	}

	public String getSearchTermsField() {
		return searchTermsField;
	}

	public void setSearchTermsField(String searchTermsField) {
		this.searchTermsField = searchTermsField;
	}





	
}
