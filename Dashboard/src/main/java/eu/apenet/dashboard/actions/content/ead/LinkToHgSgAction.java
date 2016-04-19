package eu.apenet.dashboard.actions.content.ead;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class LinkToHgSgAction extends AbstractInstitutionAction {

    protected static final String UTF8 = "utf-8";
    private String id;
    private String batchItems;
    private Set<SelectItem> dynamicHgSgs = new TreeSet<SelectItem>();
    private Set<SelectItem> clevels = new TreeSet<SelectItem>();
    private Set<SelectItem> selectPrefixMethodSet = new TreeSet<SelectItem>();
    private String selectPrefixMethod;
    private Set<SelectItem> titleMethodSet = new TreeSet<SelectItem>();
    private String titleMethod;
    private String ecId;
    private String parentCLevelId;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String input() throws IOException, SAXException, ParserConfigurationException {
        selectPrefixMethodSet.add(new SelectItem("", getText("dashboard.hgcreation.prefix.nothing")));
        selectPrefixMethodSet.add(new SelectItem(LinkingService.PREFIX_UNITID, getText("dashboard.hgcreation.prefix.unitid")));
        titleMethodSet.add(new SelectItem("", getText("dashboard.hgcreation.unittitle")));
        titleMethodSet.add(new SelectItem(LinkingService.TITLE_TITLEPROPER, getText("dashboard.hgcreation.titleproper")));

        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
        eadSearchOptions.setArchivalInstitionId(getAiId());
        eadSearchOptions.setPublished(false);
        eadSearchOptions.setDynamic(true);
        eadSearchOptions.setContentClass(HoldingsGuide.class);
        List<Ead> temp = eadDAO.getEads(eadSearchOptions);
        for (Ead ead : temp) {
            dynamicHgSgs.add(new SelectItem(ead.getEadContent().getEcId() + "", ead.getTitle()));

        }
        eadSearchOptions.setContentClass(SourceGuide.class);
        temp = eadDAO.getEads(eadSearchOptions);
        for (Ead ead : temp) {
            dynamicHgSgs.add(new SelectItem(ead.getEadContent().getEcId() + "", ead.getTitle()));
        }
        if (dynamicHgSgs.size() >= 1) {
            ecId = dynamicHgSgs.iterator().next().getValue();
        }
        if (StringUtils.isNotBlank(ecId)) {
            CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
            long ecIdTemp = Long.parseLong(ecId);
            List<CLevel> clevelsTemp = clevelDAO.getCLevelsNodes(ecIdTemp);
            clevels.add(new SelectItem("", getText("dashboard.hgcreation.linktohgsg.highestlevel")));
            for (CLevel clevel : clevelsTemp) {
                clevels.add(new SelectItem(clevel.getId() + "", clevel.getUnittitle()));
            }
            fillFindingAidsInfo();
        } else {
            return "errorLinkHgSg";
        }
        return SUCCESS;
    }

    public String retrieveClevels() {
        if (StringUtils.isNotBlank(ecId)) {
            CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
            long ecIdTemp = Long.parseLong(ecId);
            List<CLevel> clevelsTemp = clevelDAO.getCLevelsNodes(ecIdTemp);
            clevels.add(new SelectItem("", getText("dashboard.hgcreation.linktohgsg.highestlevel")));
            for (CLevel clevel : clevelsTemp) {
                clevels.add(new SelectItem(clevel.getId() + "", clevel.getUnittitle()));
            }
            fillFindingAidsInfo();
        }
        return SUCCESS;

    }

    public String execute() {
        Long ecIdLong = Long.parseLong(ecId);
        Long parentCLevelIdLong = null;
        if (StringUtils.isNotBlank(parentCLevelId)) {
            parentCLevelIdLong = Long.parseLong(parentCLevelId);
        }
        ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession()
                .getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
        if (StringUtils.isBlank(batchItems)) {
            LinkingService.addFindingaidsToHgOrSg(eadSearchOptions, Integer.parseInt(id), ecIdLong, parentCLevelIdLong, selectPrefixMethod, titleMethod);
        } else {

            if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {

                @SuppressWarnings("unchecked")
                List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                        AjaxControllerAbstractAction.LIST_IDS);
                if (ids != null) {
                    LinkingService.addFindingaidsToHgOrSg(eadSearchOptions, ids, ecIdLong, parentCLevelIdLong, selectPrefixMethod, titleMethod);
                    return SUCCESS;
                } else {
                    return ERROR;
                }

            } else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
                LinkingService.addFindingaidsToHgOrSg(eadSearchOptions, ecIdLong, parentCLevelIdLong, selectPrefixMethod, titleMethod);
                return SUCCESS;
            } else {
                LinkingService.addAllFindingaidsToHgOrSg(eadSearchOptions, ecIdLong, parentCLevelIdLong, selectPrefixMethod, titleMethod);
                return SUCCESS;
            }
        }
        return SUCCESS;
    }

    public void fillFindingAidsInfo() {
        Long ecIdLong = Long.parseLong(ecId);
        List<Ead> findingAids = null;
        long totalNumberOfFindingAids = 0;
        ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession()
                .getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
        if (StringUtils.isBlank(batchItems)) {
            findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(eadSearchOptions, Integer.parseInt(id), ecIdLong);
            totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(eadSearchOptions, Integer.parseInt(id), ecIdLong);
        } else {

            if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {
                @SuppressWarnings("unchecked")
                List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                        AjaxControllerAbstractAction.LIST_IDS);
                if (ids != null) {
                    findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(eadSearchOptions, ids, ecIdLong);
                    totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(eadSearchOptions, ids, ecIdLong);
                } else {
                }

            } else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
                findingAids = LinkingService.getFindingaidsToLinkToHgOrSg(eadSearchOptions, ecIdLong);
                totalNumberOfFindingAids = LinkingService.countFindingaidsToLinkToHgOrSg(eadSearchOptions, ecIdLong);
            } else {
                findingAids = LinkingService.getAllFindingaidsToLinkToHgOrSg(eadSearchOptions, ecIdLong);
                totalNumberOfFindingAids = LinkingService.countAllFindingaidsToLinkToHgOrSg(eadSearchOptions, ecIdLong);
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

    public Set<SelectItem> getTitleMethodSet() {
        return titleMethodSet;
    }

    public void setTitleMethodSet(Set<SelectItem> titleMethodSet) {
        this.titleMethodSet = titleMethodSet;
    }

    public String getTitleMethod() {
        return titleMethod;
    }

    public void setTitleMethod(String titleMethod) {
        this.titleMethod = titleMethod;
    }

}
