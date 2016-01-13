/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard;

import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 *
 * @author kaisar
 */
public class EnableOpenDataAction extends AbstractInstitutionAction {

    private Boolean enableOpenData = false;
    private int aiId;
    private String aiName;

    public void setAiId() {
        this.aiId = this.getAiId();
    }

    public void setAiName() {
        this.aiName = this.getAiname();
    }

    public String getAiName() {
        return aiName;
    }

    public Boolean getEnableOpenData() {
        return enableOpenData;
    }

    public void setEnableOpenData(Boolean enableOpenData) {
        this.enableOpenData = enableOpenData;
    }

    @Override
    public String execute() throws Exception {
        this.setAiName();

        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
        archivalInstitution.getAiname();
        if (ContentUtils.containsPublishedFiles(archivalInstitution)) {
            addActionError(getText("label.ai.changeainame.published.eads"));
        }
        return SUCCESS;
    }

}
