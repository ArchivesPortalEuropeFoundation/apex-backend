package eu.apenet.persistence.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ead_saved_search")
public class EadSavedSearch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "liferay_user_id")
	private long liferayUserId;
	@Column(name = "modified_date")
	private	Date modifiedDate;
	@Column(name = "public_search")
	private	boolean publicSearch;
	private	boolean template;
	private String description;
	
	/*
	 * simple search options
	 */
	@Column(name = "search_term")
	private String searchTerm;
	private	boolean hierarchy;
	@Column(name = "only_with_daos")
	private	boolean onlyWithDaos;
	@Column(name = "method_optional")
	private	boolean methodOptional;

	/*
	 * advanced search options
	 */
	private String element;	  
	private String typedocument;	  
	private String fromdate;
	private String todate;
	@Column(name = "exact_date_search")
	private	boolean exactDateSearch;

	/*
	 * archival landscape search options
	 */
	@Column(name = "al_tree_selected_nodes")
	private	String alTreeSelectedNodes;	  
	@Column(name = "al_tree_expanded_nodes")
	private	String alTreeExpandedNodes;	 

	@Column(name = "results_per_page")
	private	Integer resultPerPage;	 
	@Column(name = "pagenumber")
	private	Integer pageNumber;
	
	private	String sorting;	 
	@Column(name = "sorting_asc")
	private	Boolean sortingAsc;

	@Column(name = "refinement_country")
	private	String refinementCountry;	
	@Column(name = "refinement_ai")
	private	String refinementAi;
	@Column(name = "refinement_fond")
	private	String refinementFond;	
	@Column(name = "refinement_type")
	private	String refinementType;
	@Column(name = "refinement_level")
	private	String refinementLevel;
	@Column(name = "refinement_dao")
	private	String refinementDao;
	@Column(name = "refinement_roledao")
	private	String refinementRoledao;

	@Column(name = "refinement_date_type")
	private	String refinementDateType;
	@Column(name = "refinement_startdate")
	private	String refinementStartdate;
	@Column(name = "refinement_enddate")
	private	String refinementEnddate;
	@Column(name = "refinement_facet_settings")
	private	String refinementFacetSettings;
	
	public boolean isContainsSimpleSearchOptions() {
		return 	hierarchy || onlyWithDaos || methodOptional;
	}

	public boolean isContainsAdvancedSearchOptions() {
		return element != null || typedocument != null || fromdate != null || todate != null || exactDateSearch == true;	  
	}

	public boolean isContainsAlSearchOptions() {
		return alTreeSelectedNodes != null || alTreeExpandedNodes != null;
	}
	public boolean isContainsRefinements() {
		return refinementCountry != null || refinementAi != null || refinementFond != null 
				|| refinementType != null || refinementLevel != null || refinementDao != null || refinementRoledao != null || refinementDateType != null || refinementStartdate != null ||
						refinementEnddate != null;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLiferayUserId() {
		return liferayUserId;
	}
	public void setLiferayUserId(long liferayUserId) {
		this.liferayUserId = liferayUserId;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public boolean isPublicSearch() {
		return publicSearch;
	}
	public void setPublicSearch(boolean publicSearch) {
		this.publicSearch = publicSearch;
	}
	public boolean isTemplate() {
		return template;
	}
	public void setTemplate(boolean template) {
		this.template = template;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public boolean isHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(boolean hierarchy) {
		this.hierarchy = hierarchy;
	}
	public boolean isOnlyWithDaos() {
		return onlyWithDaos;
	}
	public void setOnlyWithDaos(boolean onlyWithDaos) {
		this.onlyWithDaos = onlyWithDaos;
	}
	public boolean isMethodOptional() {
		return methodOptional;
	}
	public void setMethodOptional(boolean methodOptional) {
		this.methodOptional = methodOptional;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getTypedocument() {
		return typedocument;
	}
	public void setTypedocument(String typedocument) {
		this.typedocument = typedocument;
	}
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public boolean isExactDateSearch() {
		return exactDateSearch;
	}
	public void setExactDateSearch(boolean exactDateSearch) {
		this.exactDateSearch = exactDateSearch;
	}
	public String getAlTreeSelectedNodes() {
		return alTreeSelectedNodes;
	}
	public void setAlTreeSelectedNodes(String alTreeSelectedNodes) {
		this.alTreeSelectedNodes = alTreeSelectedNodes;
	}
	public String getAlTreeExpandedNodes() {
		return alTreeExpandedNodes;
	}
	public void setAlTreeExpandedNodes(String alTreeExpandedNodes) {
		this.alTreeExpandedNodes = alTreeExpandedNodes;
	}
	public Integer getResultPerPage() {
		return resultPerPage;
	}
	public void setResultPerPage(Integer resultPerPage) {
		this.resultPerPage = resultPerPage;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getSorting() {
		return sorting;
	}
	public void setSorting(String sorting) {
		this.sorting = sorting;
	}
	public Boolean isSortingAsc() {
		return sortingAsc;
	}
	public void setSortingAsc(Boolean sortingAsc) {
		this.sortingAsc = sortingAsc;
	}
	public String getRefinementCountry() {
		return refinementCountry;
	}
	public void setRefinementCountry(String refinementCountry) {
		this.refinementCountry = refinementCountry;
	}
	public String getRefinementAi() {
		return refinementAi;
	}
	public void setRefinementAi(String refinementAi) {
		this.refinementAi = refinementAi;
	}
	public String getRefinementFond() {
		return refinementFond;
	}
	public void setRefinementFond(String refinementFond) {
		this.refinementFond = refinementFond;
	}
	public String getRefinementType() {
		return refinementType;
	}
	public void setRefinementType(String refinementType) {
		this.refinementType = refinementType;
	}
	public String getRefinementDao() {
		return refinementDao;
	}
	public void setRefinementDao(String refinementDao) {
		this.refinementDao = refinementDao;
	}
	public String getRefinementRoledao() {
		return refinementRoledao;
	}
	public void setRefinementRoledao(String refinementRoledao) {
		this.refinementRoledao = refinementRoledao;
	}
	public String getRefinementDateType() {
		return refinementDateType;
	}
	public void setRefinementDateType(String refinementDateType) {
		this.refinementDateType = refinementDateType;
	}
	public String getRefinementStartdate() {
		return refinementStartdate;
	}
	public void setRefinementStartdate(String refinementStartdate) {
		this.refinementStartdate = refinementStartdate;
	}
	public String getRefinementEnddate() {
		return refinementEnddate;
	}
	public void setRefinementEnddate(String refinementEnddate) {
		this.refinementEnddate = refinementEnddate;
	}
	public String getRefinementFacetSettings() {
		return refinementFacetSettings;
	}
	public void setRefinementFacetSettings(String refinementFacetSettings) {
		this.refinementFacetSettings = refinementFacetSettings;
	}

	public String getRefinementLevel() {
		return refinementLevel;
	}

	public void setRefinementLevel(String refinementLevel) {
		this.refinementLevel = refinementLevel;
	}


}
