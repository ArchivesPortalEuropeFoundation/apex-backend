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
import eu.apenet.dashboard.services.opendata.OpenDataService;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kaisar
 */
public class EnableOpenDataAction extends AbstractInstitutionAction {

    private Boolean enableOpenData = false;
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

    @Override
    public String execute() throws IOException {

        try {
            long eadTotalDoc = EadSolrServerHolder.getInstance().getTotalSolrDocsForOpenData(this.getAiName(), this.getAiId(), getEnableOpenData());
            long eacTotalDoc = EacCpfSolrServerHolder.getInstance().getTotalSolrDocsForOpenData(this.getAiName(), this.getAiId(), getEnableOpenData());
            Properties preferences = new Properties();
            preferences.setProperty(OpenDataService.ENABLE_OPEN_DATA_KEY, getEnableOpenData().toString());
            preferences.setProperty(OpenDataService.TOTAL_SOLAR_DOC_KEY, (eadTotalDoc + eacTotalDoc) + "");

            OpenDataService.openDataPublish(this.getAiId(), preferences);

//            EadSolrServerHolder.getInstance().enableOpenDataByAi(this.getAiName(), this.getAiId(), getEnableOpenData());
//            EacCpfSolrServerHolder.getInstance().enableOpenDataByAi(this.getAiName(), this.getAiId(), getEnableOpenData());
//            EagSolrServerHolder.getInstance().enableOpenDataByAi(this.getAiName(), this.getAiId(), getEnableOpenData());
//        } else {
//            
//        }
        } catch (SolrServerException ex) {
            log.error("Solr Server exception: " + ex.getMessage());
            return ERROR;
        } catch (ProcessBusyException ex) {
            log.warn("Inistitute: " + this.getAiName() + "/" + this.getAiId() + " is trying to add multiple open data operations!");
            addActionError(getText("label.ai.enableopendata.inprogress"));
            ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
            this.setEnableOpenData(archivalInstitution.isOpenDataEnabled());
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String input() throws Exception {
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
        this.setEnableOpenData(archivalInstitution.isOpenDataEnabled());
        return SUCCESS;
    }

}
