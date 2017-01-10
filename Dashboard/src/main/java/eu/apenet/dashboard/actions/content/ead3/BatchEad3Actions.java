/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.ead3;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.QueueAction;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author kaisar
 */
public class BatchEad3Actions extends AbstractEad3Actions {

    public static final String ALL_ITEMS = "all";
    public static final String SELECTED_ITEMS = "only_selected";
    public static final String SEARCHED_ITEMS = "only_searched";
    private String batchItems;

    @Override
    public String validateEad3() {
        return addBatchToQueue(QueueAction.VALIDATE);
    }

    @Override
    public String publishEad3() {
        return addBatchToQueue(QueueAction.PUBLISH);
    }

    @Override
    public String unpublishEad3() {
        return addBatchToQueue(QueueAction.UNPUBLISH);
    }

    @Override
    public String deleteEad3() {
        return addBatchToQueue(QueueAction.DELETE);
    }

    @Override
    public String validatePublishEad3() {
        return addBatchToQueue(QueueAction.VALIDATE_PUBLISH);
    }

    @Override
    protected Properties getConversionParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteEseEdm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFromEuropeana() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deliverToEuropeana() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFromQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String addBatchToQueue(QueueAction queueAction) {
        return addBatchToQueue(queueAction, new Properties());
    }

    private String addBatchToQueue(QueueAction queueAction, Properties properties) {
        try {
            List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                    AjaxControllerAbstractAction.LIST_IDS);
            if (SELECTED_ITEMS.equals(batchItems) && !ids.isEmpty()) {
                if (ids != null && !ids.isEmpty()) {
                    Ead3Service.addBatchToQueue(ids, getAiId(), getXmlType(), queueAction, properties);
                    return SUCCESS;
                } else {
                    return ERROR;
                }

            } else if (SELECTED_ITEMS.equals(batchItems) && ids.isEmpty()) {
                addActionError(getText("content.message.noSelected"));
                return ERROR;
            } else if (SEARCHED_ITEMS.equals(batchItems)) {
                ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(
                        ContentManagerAction.EAD_SEARCH_OPTIONS);
                Ead3Service.addBatchToQueue(eadSearchOptions, queueAction, properties);
                return SUCCESS;
            } else {
                Ead3Service.addBatchToQueue(null, getAiId(), getXmlType(), queueAction, properties);
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(String batchItems) {
        this.batchItems = batchItems;
    }

}
