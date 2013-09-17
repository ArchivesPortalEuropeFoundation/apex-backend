package eu.apenet.dashboard.actions.content;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class LinkToHgSgAction  extends AbstractInstitutionAction{

	protected static final String UTF8 = "utf-8";
	private String id;
	private String batchItems;
    private Set<SelectItem> dynamicHgSgs = new TreeSet<SelectItem>();
    private Set<SelectItem> clevels = new TreeSet<SelectItem>();
    private Set<SelectItem> selectPrefixMethodSet = new TreeSet<SelectItem>();
    private String selectPrefixMethod;
    private String ecId;
    private String parentCLevelId;

    private static final Logger LOGGER = Logger.getLogger(LinkToHgSgAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String input() throws IOException, SAXException, ParserConfigurationException{
		selectPrefixMethodSet.add(new SelectItem("", getText("dashboard.hgcreation.prefix.nothing")));
		selectPrefixMethodSet.add(new SelectItem(LinkingService.PREFIX_UNITID, getText("dashboard.hgcreation.prefix.unitid")));
		selectPrefixMethodSet.add(new SelectItem(LinkingService.PREFIX_EADID, getText("dashboard.hgcreation.prefix.eadid")));
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
        eadSearchOptions.setArchivalInstitionId(getAiId());
		eadSearchOptions.setPublished(false);
		eadSearchOptions.setDynamic(true);
		eadSearchOptions.setEadClass(HoldingsGuide.class);
		List<Ead> temp = eadDAO.getEads(eadSearchOptions);
		for (Ead ead :temp){
			dynamicHgSgs.add(new SelectItem(ead.getEadContent().getEcId() +"", ead.getTitle()));
			
		}
		eadSearchOptions.setEadClass(SourceGuide.class);
		temp = eadDAO.getEads(eadSearchOptions);
		for (Ead ead :temp){
			dynamicHgSgs.add(new SelectItem(ead.getEadContent().getEcId() +"", ead.getTitle()));
		}
		if (dynamicHgSgs.size() >= 1){
			ecId = dynamicHgSgs.iterator().next().getValue();
		}
		if (StringUtils.isNotBlank(ecId)){
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			long ecIdTemp = Long.parseLong(ecId);
			List<CLevel> clevelsTemp = clevelDAO.getParentCLevels(ecIdTemp);
			clevels.add(new SelectItem("", getText("dashboard.hgcreation.linktohgsg.highestlevel")));
			for (CLevel clevel :clevelsTemp){
				clevels.add(new SelectItem(clevel.getClId() +"", clevel.getUnittitle()));
			}
			fillFindingAidsInfo();
		}
		return SUCCESS;
	}
	
	public String retrieveClevels(){
		if (StringUtils.isNotBlank(ecId)){
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			long ecIdTemp = Long.parseLong(ecId);
			List<CLevel> clevelsTemp = clevelDAO.getParentCLevels(ecIdTemp);
			clevels.add(new SelectItem("", getText("dashboard.hgcreation.linktohgsg.highestlevel")));
			for (CLevel clevel :clevelsTemp){
				clevels.add(new SelectItem(clevel.getClId() +"", clevel.getUnittitle()));
			}
			fillFindingAidsInfo();
		}
		return SUCCESS;
		
	}
	public String execute(){
		Long ecIdLong = Long.parseLong(ecId);
		Long parentCLevelIdLong = null;
		if (StringUtils.isNotBlank(parentCLevelId)){
			parentCLevelIdLong = Long.parseLong(parentCLevelId);
		}
		if (StringUtils.isBlank(batchItems)){
			LinkingService.addFindingaidToHgOrSg(Integer.parseInt(id), getAiId(), ecIdLong, parentCLevelIdLong, selectPrefixMethod);
		}else {
			if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {

				@SuppressWarnings("unchecked")
				List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
						AjaxControllerAbstractAction.LIST_IDS);
				if (ids != null) {
					LinkingService.addFindingaidsToHgOrSg(ids, getAiId(), ecIdLong, parentCLevelIdLong, selectPrefixMethod);
					return SUCCESS;
				} else {
					return ERROR;
				}

			} else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
				EadSearchOptions eadSearchOptions = (EadSearchOptions)getServletRequest().getSession()
						.getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
				LinkingService.addFindingaidsToHgOrSg(eadSearchOptions, ecIdLong, parentCLevelIdLong, selectPrefixMethod);
				return SUCCESS;
			} else {
				LinkingService.addFindingaidsToHgOrSg(ecIdLong, parentCLevelIdLong, selectPrefixMethod);
				return SUCCESS;
			}
		}
   		return SUCCESS;
	}
	public void fillFindingAidsInfo(){
		Long ecIdLong = Long.parseLong(ecId);
		List<Ead> findingAids = null;
		long totalNumberOfFindingAids = 0;
        LOGGER.info("The ID of my selected FA is " + id);
        LOGGER.info("The ecId is " + ecId);
        LOGGER.info("The ai_id I have is " + getAiId());
		if (StringUtils.isBlank(batchItems)){
			findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(Integer.parseInt(id), getAiId(), ecIdLong);
			totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(Integer.parseInt(id), getAiId(), ecIdLong);
		}else {
			if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {
				@SuppressWarnings("unchecked")
				List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
						AjaxControllerAbstractAction.LIST_IDS);
				if (ids != null) {
					findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(ids, getAiId(), ecIdLong);
					totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(ids, getAiId(), ecIdLong);
				} else {
				}

			} else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
				EadSearchOptions eadSearchOptions = (EadSearchOptions)getServletRequest().getSession()
						.getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
				findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(eadSearchOptions, ecIdLong);
				totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(eadSearchOptions, ecIdLong);
			} else {
				findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(ecIdLong);
				totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(ecIdLong);
			}
		}
		getServletRequest().setAttribute("findingAids", findingAids);
		getServletRequest().setAttribute("totalNumberOfFindingAids", totalNumberOfFindingAids);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBatchItems() {
		return batchItems;
	}

	public void setBatchItems(String batchItems) {
		this.batchItems = batchItems;
	}
    protected Writer openOutputWriter() throws IOException {
        getServletRequest().setCharacterEncoding(UTF8);
        getServletResponse().setCharacterEncoding(UTF8);
        getServletResponse().setContentType("application/json");
        return new OutputStreamWriter(getServletResponse().getOutputStream(), UTF8);
    }

	public Set<SelectItem> getDynamicHgSgs() {
		return dynamicHgSgs;
	}

	public void setDynamicHgSgs(Set<SelectItem> dynamicHgSgs) {
		this.dynamicHgSgs = dynamicHgSgs;
	}

	public String getEcId() {
		return ecId;
	}

	public void setEcId(String ecId) {
		this.ecId = ecId;
	}

	public Set<SelectItem> getClevels() {
		return clevels;
	}

	public void setClevels(Set<SelectItem> clevels) {
		this.clevels = clevels;
	}

	public String getParentCLevelId() {
		return parentCLevelId;
	}

	public void setParentCLevelId(String parentCLevelId) {
		this.parentCLevelId = parentCLevelId;
	}

	public Set<SelectItem> getSelectPrefixMethodSet() {
		return selectPrefixMethodSet;
	}

	public void setSelectPrefixMethodSet(Set<SelectItem> selectPrefixMethodSet) {
		this.selectPrefixMethodSet = selectPrefixMethodSet;
	}

	public String getSelectPrefixMethod() {
		return selectPrefixMethod;
	}

	public void setSelectPrefixMethod(String selectPrefixMethod) {
		this.selectPrefixMethod = selectPrefixMethod;
	}

    
}
