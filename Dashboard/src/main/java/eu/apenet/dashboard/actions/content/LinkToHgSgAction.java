package eu.apenet.dashboard.actions.content;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.SourceGuide;

public class LinkToHgSgAction  extends AbstractInstitutionAction{
    protected static final String UTF8 = "utf-8";
	private String id;
	private String batchItems;
    private Set<SelectItem> dynamicHgSgs = new TreeSet<SelectItem>();
    private Set<SelectItem> clevels = new TreeSet<SelectItem>();
    private String ecId;
    private String parentCLevelId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String input() throws IOException, SAXException, ParserConfigurationException{
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPublished(false);
		eadSearchOptions.setDynamic(true);
		eadSearchOptions.setEadClazz(HoldingsGuide.class);
		List<Ead> temp = eadDAO.getEads(eadSearchOptions);
		for (Ead ead :temp){
			dynamicHgSgs.add(new SelectItem(ead.getEadContent().getEcId() +"", ead.getTitle()));
			
		}
		eadSearchOptions.setEadClazz(SourceGuide.class);
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
			LinkingService.addFindingaidToHgOrSg(Integer.parseInt(id), getAiId(), ecIdLong, parentCLevelIdLong);
		}else {
			if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {

				List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
						AjaxControllerAbstractAction.LIST_IDS);
				if (ids != null) {
					LinkingService.addFindingaidsToHgOrSg(ids, getAiId(), ecIdLong, parentCLevelIdLong);
					return SUCCESS;
				} else {
					return ERROR;
				}

			} else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
				EadSearchOptions eadSearchOptions = (EadSearchOptions)getServletRequest().getSession()
						.getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
				LinkingService.addFindingaidsToHgOrSg(eadSearchOptions, ecIdLong, parentCLevelIdLong);
				return SUCCESS;
			} else {
				LinkingService.addFindingaidsToHgOrSg(null, ecIdLong, parentCLevelIdLong);
				return SUCCESS;
			}
		}
   		return SUCCESS;
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

    
}
