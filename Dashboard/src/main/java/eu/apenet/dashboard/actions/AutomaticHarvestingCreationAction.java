package eu.apenet.dashboard.actions;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.dashboard.listener.Duration;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.vo.Userprofile;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.apache.log4j.Logger;
import org.oclc.oai.harvester.app.RetrieveOaiPmhInformation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class AutomaticHarvestingCreationAction extends ActionSupport {
    private static final Logger LOG = Logger.getLogger(AutomaticHarvestingCreationAction.class);

    private static final Long INTERVAL_1_MONTH = 2630000000L;
    private static final Long INTERVAL_3_MONTH = 7889230000L;
    private static final Long INTERVAL_6_MONTH = 15780000000L;

    private Integer step;
    private Integer oaiprofiles;
    private List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhs;
    private String url;
    private List<String> sets;
    private List<String> metadataFormats;
    private List<Userprofile> userProfiles;
    private List<Interval> intervals;
    private String selectedSet;
    private String selectedMetadataFormat;
    private Integer selectedUserProfile;
    private String lastHarvestDate; //todo: Propose to the user or not? We could do simply a full harvest at the first time since it is currently hard to know when it was exactly done...
    private String intervalHarvest;

    public String execute() throws Exception {
        step = 0;
        int archivalInstitutionId = SecurityContext.get().getSelectedInstitution().getId();
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        archivalInstitutionOaiPmhs = archivalInstitutionOaiPmhDAO.getArchivalInstitutionOaiPmhs(archivalInstitutionId);
        LOG.info(archivalInstitutionOaiPmhs.size() + " archivalInstitutionOaiPmh for this archive");
        return SUCCESS;
    }

    public String getMetadataPrefixesAndSetsFromUrl() throws Exception {
        if(getOaiprofiles() != null) {
            step = 1;
            if(getOaiprofiles() != -1) {
                //We go for edition and not addition...
            }
            return SUCCESS;
        } else if(getUrl() != null) {
            if(getSelectedMetadataFormat() == null) {
                step = 2;
                int partnerId = SecurityContext.get().getPartnerId();
                metadataFormats = RetrieveOaiPmhInformation.retrieveMetadataFormats(getUrl());
                sets = RetrieveOaiPmhInformation.retrieveSets(getUrl());
                userProfiles = DAOFactory.instance().getUserprofileDAO().getUserprofiles(partnerId);
                intervals = new ArrayList<Interval>(3);
                intervals.add(new Interval(1, INTERVAL_1_MONTH));
                intervals.add(new Interval(3, INTERVAL_3_MONTH));
                intervals.add(new Interval(6, INTERVAL_6_MONTH));
            } else {
                step = 3;
                int archivalInstitutionId = SecurityContext.get().getSelectedInstitution().getId();
                String intervalHarvest = getIntervalHarvest();
                ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = new ArchivalInstitutionOaiPmh(archivalInstitutionId, getUrl(), getSelectedMetadataFormat(), Long.parseLong(selectedUserProfile+""), Long.parseLong(intervalHarvest));
                if(getSelectedSet() != null) {
                    archivalInstitutionOaiPmh.setSet(getSelectedSet());
                }
                JpaUtil.beginDatabaseTransaction();
                DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().store(archivalInstitutionOaiPmh);
                JpaUtil.commitDatabaseTransaction();
            }
            return SUCCESS;
        } else {
            addFieldError("url", getText("harvest.automatic.creation.wrongurl"));
            return ERROR;
        }
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs() {
        return archivalInstitutionOaiPmhs;
    }

    public void setArchivalInstitutionOaiPmhs(List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhs) {
        this.archivalInstitutionOaiPmhs = archivalInstitutionOaiPmhs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    public List<String> getMetadataFormats() {
        return metadataFormats;
    }

    public void setMetadataFormats(List<String> metadataFormats) {
        this.metadataFormats = metadataFormats;
    }

    public String getLastHarvestDate() {
        return lastHarvestDate;
    }

    public void setLastHarvestDate(String lastHarvestDate) {
        this.lastHarvestDate = lastHarvestDate;
    }

    public String getIntervalHarvest() {
        return intervalHarvest;
    }

    public void setIntervalHarvest(String intervalHarvest) {
        this.intervalHarvest = intervalHarvest;
    }

    public Integer getOaiprofiles() {
        return oaiprofiles;
    }

    public void setOaiprofiles(Integer oaiprofiles) {
        this.oaiprofiles = oaiprofiles;
    }

    public String getSelectedSet() {
        return selectedSet;
    }

    public void setSelectedSet(String selectedSet) {
        this.selectedSet = selectedSet;
    }

    public String getSelectedMetadataFormat() {
        return selectedMetadataFormat;
    }

    public void setSelectedMetadataFormat(String selectedMetadataFormat) {
        this.selectedMetadataFormat = selectedMetadataFormat;
    }

    public List<Userprofile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(List<Userprofile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public Integer getSelectedUserProfile() {
        return selectedUserProfile;
    }

    public void setSelectedUserProfile(Integer selectedUserProfile) {
        this.selectedUserProfile = selectedUserProfile;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public class Interval {
        private Integer months;
        private Long time;

        public Interval(Integer months, Long time) {
            this.months = months;
            this.time = time;
        }

        public Integer getMonths() {
            return months;
        }

        public void setMonths(Integer months) {
            this.months = months;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }
}
