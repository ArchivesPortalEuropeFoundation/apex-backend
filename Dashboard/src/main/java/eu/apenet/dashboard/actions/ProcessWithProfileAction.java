/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.dashboard.actions;

import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.UpFile;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author papp
 */
public class ProcessWithProfileAction extends AbstractInstitutionAction{
    Ingestionprofile profile = null;
    String ingestionprofile;


    public ProcessWithProfileAction() {

    }

    @Override
    public String execute() throws Exception {
        Long ingestionprofileNo = Long.parseLong((String) getServletRequest().getAttribute("ingestionprofile"));
        if (ingestionprofileNo != 0) {
            IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
            this.profile = profileDAO.findById(ingestionprofileNo);
        }
        Properties properties = retrieveProperties(profile);
        UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
        List<UpFile> upFiles = upFileDAO.getNewUpFiles(getAiId(), FileType.XML);
        for (UpFile upFile : upFiles) {
            EadService.useProfileAction(upFile, properties);
        }

        return SUCCESS;
    }

    public String getIngestionprofile() {
        return ingestionprofile;
    }

    public void setIngestionprofile(String ingestionprofile) {
        this.ingestionprofile = ingestionprofile;
    }

    private static Properties retrieveProperties(Ingestionprofile ingestionprofile) {
        Properties properties = new Properties();
        properties.setProperty(QueueItem.XML_TYPE, ingestionprofile.getFileType() + "");
        properties.setProperty(QueueItem.NO_EADID_ACTION, ingestionprofile.getNoeadidAction().getId() + "");
        properties.setProperty(QueueItem.EXIST_ACTION, ingestionprofile.getExistAction().getId() + "");
        properties.setProperty(QueueItem.DAO_TYPE, ingestionprofile.getDaoType().getId() + "");
        properties.setProperty(QueueItem.DAO_TYPE_CHECK, ingestionprofile.getDaoTypeFromFile() + "");
        properties.setProperty(QueueItem.UPLOAD_ACTION, ingestionprofile.getUploadAction().getId() + "");
        properties.setProperty(QueueItem.DATA_PROVIDER, ingestionprofile.getEuropeanaDataProvider() + "");
        properties.setProperty(QueueItem.DATA_PROVIDER_CHECK, ingestionprofile.getEuropeanaDataProviderFromFile() + "");
        properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE, ingestionprofile.getEuropeanaDaoType() + "");
        properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE_CHECK, ingestionprofile.getEuropeanaDaoTypeFromFile() + "");
        properties.setProperty(QueueItem.LANGUAGES, ingestionprofile.getEuropeanaLanguages() + "");
        properties.setProperty(QueueItem.LANGUAGE_CHECK, ingestionprofile.getEuropeanaLanguagesFromFile() + "");
        properties.setProperty(QueueItem.LICENSE, ingestionprofile.getEuropeanaLicense() + "");
        properties.setProperty(QueueItem.LICENSE_DETAILS, ingestionprofile.getEuropeanaLicenseDetails() + "");
        properties.setProperty(QueueItem.LICENSE_ADD_INFO, ingestionprofile.getEuropeanaAddRights() + "");
        properties.setProperty(QueueItem.HIERARCHY_PREFIX, ingestionprofile.getEuropeanaHierarchyPrefix() + "");
        properties.setProperty(QueueItem.INHERIT_FILE, ingestionprofile.getEuropeanaInheritElements() + "");
        properties.setProperty(QueueItem.INHERIT_ORIGINATION, ingestionprofile.getEuropeanaInheritOrigin() + "");
        return properties;
    }
}
