/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard;

import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.commons.exceptions.ProcessBusyException;
import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.EadSolrServerHolder;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.dashboard.services.opendata.OpenDataService;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author kaisar
 */
public class EnableOpenDataAction extends AbstractInstitutionAction {

    private Boolean enableOpenData = false;
    private Boolean checkBoxValue = false;
    private String errorMsg = "";
    private final Logger log = Logger.getLogger(EnableOpenDataAction.class);

    public String getAiName() {
        return this.getAiname();
    }

    public Boolean getEnableOpenData() {
        return enableOpenData;
    }

    public void setEnableOpenData(Boolean enableOpenData) {
        this.enableOpenData = enableOpenData;
    }

    public Boolean getCheckBoxValue() {
        return checkBoxValue;
    }

    public void setCheckBoxValue(Boolean checkBoxValue) {
        this.checkBoxValue = checkBoxValue;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String execute() throws IOException {
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
        this.setEnableOpenData(archivalInstitution.isOpenDataEnabled());
        if (getCheckBoxValue() == true) {
            setEnableOpenData(!getEnableOpenData());
        }

        try {
            long eadTotalDoc = OpenDataService.getInstance().getTotalSolrDocsForOpenData(EadSolrServerHolder.getInstance(), archivalInstitution, getEnableOpenData());
            long eacTotalDoc = OpenDataService.getInstance().getTotalSolrDocsForOpenData(EacCpfSolrServerHolder.getInstance(), archivalInstitution, getEnableOpenData());
            long eagTotalDoc = OpenDataService.getInstance().getTotalSolrDocsForOpenData(EagSolrServerHolder.getInstance(), archivalInstitution, getEnableOpenData());
            Properties preferences = new Properties();
            preferences.setProperty(OpenDataService.ENABLE_OPEN_DATA_KEY, getEnableOpenData().toString());
            preferences.setProperty(OpenDataService.TOTAL_SOLAR_DOC_KEY, (eadTotalDoc + eacTotalDoc + eagTotalDoc) + "");

            OpenDataService.getInstance().openDataPublish(this.getAiId(), preferences);

        } catch (SolrServerException ex) {
            log.error("Solr Server exception: " + ex.getMessage());
            return ERROR;
        } catch (ProcessBusyException ex) {
            log.warn("Inistitute: " + this.getAiName() + "/" + this.getAiId() + " is trying to add multiple open data operations!");
            this.setErrorMsg(getText("label.ai.enableopendata.inprogress"));
            addActionError(getText("label.ai.enableopendata.inprogress"));
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String input() throws Exception {
        String paramValue = ServletActionContext.getRequest().getParameter("errorMsg");
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
        this.setEnableOpenData(archivalInstitution.isOpenDataEnabled());
        if (archivalInstitution.getUnprocessedSolrDocs() == 0) {
            return SUCCESS;
        } else if (paramValue == null || StringUtils.isBlank(NONE)) {
            return SUCCESS;
        } else {
            addActionError(paramValue);
            return SUCCESS;
        }
    }

}
