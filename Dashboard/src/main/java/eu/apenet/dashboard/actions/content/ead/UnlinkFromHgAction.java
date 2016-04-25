/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.ead;

import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author stefan
 */
public class UnlinkFromHgAction extends AbstractInstitutionAction {

    protected static final String UTF8 = "utf-8";
    private String id;
    private String batchItems;
    private Map<String, String> linkedUnpublishedHgs = new LinkedHashMap<String, String>();
    private String[] hgList;
    private String faIds;

    @Override
    public String input() {
        // Fetch HGs from DB; HGs must be unpublished for the unlinking operation to be successful
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        ContentSearchOptions hgSearchOptions = new ContentSearchOptions();
        hgSearchOptions.setArchivalInstitionId(getAiId());
        hgSearchOptions.setPublished(false);
        hgSearchOptions.setContentClass(HoldingsGuide.class);
        List<Ead> eligibleHgs = eadDAO.getEads(hgSearchOptions);
        
        // Fetch selected FAs
        List<Ead> selectedEADs = retrieveSelectedFindingAids();

        //Use FAs to filter ineligible HGs/SGs
        HgSgFaRelationDAO hgSgFaDAO = DAOFactory.instance().getHgSgFaRelationDAO();
        for (Ead fa : selectedEADs) {
            for (Ead hg : eligibleHgs) {
                boolean linkExists = hgSgFaDAO.existHgFaRelations(hg.getId(), fa.getId());
                if (linkExists) {
                    linkedUnpublishedHgs.putIfAbsent(hg.getEadContent().getHgId().toString(), hg.getTitle());
                }
            }
        }

        if (linkedUnpublishedHgs.isEmpty()) {
            return "errorUnlinkHgSg";
        } else {
            String[] temp = new String[linkedUnpublishedHgs.size()];
            hgList = linkedUnpublishedHgs.keySet().toArray(temp);
            return SUCCESS;
        }
    }

    @Override
    public String execute() {
        for (String hgListElement : hgList) {
            Integer ecIdLong = Integer.parseInt(hgListElement);
            ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession()
                    .getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
            if (StringUtils.isBlank(batchItems)) {
                LinkingService.removeFindingaidsFromHg(eadSearchOptions, Integer.parseInt(id), ecIdLong);
            } else {
                if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {
                    @SuppressWarnings("unchecked")
                    List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                            AjaxControllerAbstractAction.LIST_IDS);
                    if (ids != null) {
                        LinkingService.removeFindingaidsFromHg(eadSearchOptions, ids, ecIdLong);
                        return SUCCESS;
                    } else {
                        return ERROR;
                    }

                } else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
                    LinkingService.removeFindingaidsFromHg(eadSearchOptions, ecIdLong);
                    return SUCCESS;
                } else {
                    LinkingService.removeAllFindingaidsFromHg(eadSearchOptions, ecIdLong);
                    return SUCCESS;
                }
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

    public Map<String, String> getLinkedUnpublishedHgs() {
        return linkedUnpublishedHgs;
    }

    public void setLinkedUnpublishedHgs(Map<String, String> linkedUnpublishedHgs) {
        this.linkedUnpublishedHgs = linkedUnpublishedHgs;
    }

    public String[] getHgList() {
        return hgList;
    }

    public void setHgList(String[] hgList) {
        this.hgList = hgList;
    }

    public String getFaIds() {
        return faIds;
    }

    public void setFaIds(String faIds) {
        this.faIds = faIds;
    }

    private List<Ead> retrieveSelectedFindingAids() {
        List<Ead> result = new LinkedList<Ead>();
        ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession()
                .getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
        if (StringUtils.isBlank(batchItems)) {
            eadSearchOptions.setId(Integer.parseInt(id));
        } else {
            if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {
                @SuppressWarnings("unchecked")
                List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                        AjaxControllerAbstractAction.LIST_IDS);
                if (ids != null) {
                    eadSearchOptions.setIds(ids);
                } else {
                }
            } else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
            } else {
            }
        }
        return DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);
    }

}
