package eu.apenet.dashboard.actions.admin;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.XslUploadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.XslUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoannmoranville on 18/12/14.
 */
public class UploadXslAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(UploadXslAction.class);
    private Integer countryId;
    private List<Country> countries;
    private Integer institutionId;
    private List<UploadXslInstitution> institutions;
    private File xslFile;
    private String xslFilename;

    public String execute() throws Exception {
        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
        setCountries(countryDAO.getCountriesWithArchivalInstitutionsWithEAG());
        return SUCCESS;
    }

    public String chooseInstitution() throws Exception {
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryId(countryId);
        List<UploadXslInstitution> uploadXslInstitutions = new ArrayList<UploadXslInstitution>(archivalInstitutions.size());
        XslUploadDAO xslUploadDAO = DAOFactory.instance().getXslUploadDAO();
        for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
            uploadXslInstitutions.add(new UploadXslInstitution(archivalInstitution, xslUploadDAO.hasXslUpload(archivalInstitution.getAiId())));
        }
        setInstitutions(uploadXslInstitutions);
        setCountryId(getCountryId());
        return SUCCESS;
    }

    public String uploadXsl() {
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        XslUploadDAO xslUploadDAO = DAOFactory.instance().getXslUploadDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitution(getInstitutionId());
        String filenameSaved = archivalInstitution.getEncodedRepositorycode() + '-' + System.currentTimeMillis() +".xsl";
        String xslFilePath = APEnetUtilities.getDashboardConfig().getXslDirPath() + APEnetUtilities.FILESEPARATOR + filenameSaved;
        File dstFile = new File(xslFilePath);
        try {
            try {
                FileUtils.moveFile(xslFile, dstFile);
            } catch (IOException ioe) {
                throw new DashboardAPEnetException(ioe);
            }
            XslUpload xslUpload = new XslUpload();
            xslUpload.setArchivalInstitutionId(getInstitutionId());
            xslUpload.setArchivalInstitution(archivalInstitution);
            xslUpload.setName(filenameSaved);
            xslUpload.setReadableName(xslFilename);
            xslUploadDAO.store(xslUpload);
        } catch (DashboardAPEnetException dae) {
            LOGGER.error("Error in upload of XSL file", dae);
            if(!dstFile.delete()) {
                LOGGER.error("And couldn't delete the file: " + dstFile.getAbsolutePath());
            }
            return ERROR;
        }
        return SUCCESS;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public File getXslFile() {
        return xslFile;
    }

    public void setXslFile(File xslFile) {
        this.xslFile = xslFile;
    }

    public String getXslFilename() {
        return xslFilename;
    }

    public void setXslFilename(String xslFilename) {
        this.xslFilename = xslFilename;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<UploadXslInstitution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(List<UploadXslInstitution> institutions) {
        this.institutions = institutions;
    }

    public class UploadXslInstitution {
        public ArchivalInstitution archivalInstitution;
        public boolean hasXsl;

        public UploadXslInstitution(ArchivalInstitution archivalInstitution, boolean hasXsl) {
            this.archivalInstitution = archivalInstitution;
            this.hasXsl = hasXsl;
        }

        public ArchivalInstitution getArchivalInstitution() {
            return archivalInstitution;
        }

        public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
            this.archivalInstitution = archivalInstitution;
        }

        public boolean isHasXsl() {
            return hasXsl;
        }

        public void setHasXsl(boolean hasXsl) {
            this.hasXsl = hasXsl;
        }
    }
}
