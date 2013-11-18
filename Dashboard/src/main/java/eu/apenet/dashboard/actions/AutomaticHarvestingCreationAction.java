package eu.apenet.dashboard.actions;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
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
    private String selectedActivation;
    private String selectedWeekend;
    private String lastHarvestDate;
    private String intervalHarvest;
    private boolean defaultHarvestingProcessing = false;

    public void validate() {
        defaultHarvestingProcessing = APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing();
    }

    public String execute() throws Exception {
        step = 0;
        int archivalInstitutionId = SecurityContext.get().getSelectedInstitution().getId();
        userProfiles = DAOFactory.instance().getUserprofileDAO().getUserprofiles(archivalInstitutionId);
        if(userProfiles.size() < 1) {
            addActionError("You need at least one user profile created before creating an automatic OAI-PMH profile");
            return ERROR;
        }
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        archivalInstitutionOaiPmhs = archivalInstitutionOaiPmhDAO.getArchivalInstitutionOaiPmhs(archivalInstitutionId);
        return SUCCESS;
    }

    public String page2() throws Exception {
        step = 1;
        if(getOaiprofiles() != -1) {
            //We go for edition and not addition...
            Long oaiProfileId = getOaiprofiles().longValue();
            ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(oaiProfileId);
            setUrl(archivalInstitutionOaiPmh.getUrl());
        }
        return SUCCESS;
    }

    public String page3() throws Exception {
        if(getUrl() != null) {
            step = 2;
            int aiId = SecurityContext.get().getSelectedInstitution().getId();
            try {
                metadataFormats = RetrieveOaiPmhInformation.retrieveMetadataFormats(getUrl());
                if(metadataFormats == null || metadataFormats.isEmpty())
                    throw new APEnetException("No metadata formats for this URL: " + getUrl());
            } catch (Exception e) {
                addActionError("Sorry, the URL is not a correct repository URL or the repository does not contain any metadata formats...");
                return ERROR;
            }
            List<String> setsInRepository = RetrieveOaiPmhInformation.retrieveSets(getUrl());
            sets = new ArrayList<String>(setsInRepository);
            List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhList = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().getArchivalInstitutionOaiPmhs(aiId);
            userProfiles = DAOFactory.instance().getUserprofileDAO().getUserprofiles(aiId);
            if(setsInRepository != null) {
                for(String set : setsInRepository) {
                    for(ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh : archivalInstitutionOaiPmhList) {
                        if(archivalInstitutionOaiPmh.getSet().equals(set) && archivalInstitutionOaiPmh.getUrl().equals(getUrl())) {
                            sets.remove(archivalInstitutionOaiPmh.getSet());
                        }
                    }
                }
            }

            if(sets.size() == 0 && setsInRepository.size() != 0 && getOaiprofiles() == -1) {
                addActionError("Sorry, all your sets are already being used in other profiles. you need to delete some profiles to continue, or edit profiles");
                return ERROR;
            }

            intervals = new ArrayList<Interval>(3);
            intervals.add(new Interval(1, INTERVAL_1_MONTH));
            intervals.add(new Interval(3, INTERVAL_3_MONTH));
            intervals.add(new Interval(6, INTERVAL_6_MONTH));
            if(getOaiprofiles() != -1) {
                ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(getOaiprofiles().longValue());
                sets.add(archivalInstitutionOaiPmh.getSet());
                setSelectedSet(archivalInstitutionOaiPmh.getSet());
                setSelectedMetadataFormat(archivalInstitutionOaiPmh.getMetadataPrefix());
                setSelectedUserProfile(archivalInstitutionOaiPmh.getProfileId().intValue());
                setIntervalHarvest(archivalInstitutionOaiPmh.getIntervalHarvesting().toString());
                setSelectedActivation(Boolean.toString(archivalInstitutionOaiPmh.isEnabled()));
                setSelectedWeekend(Boolean.toString(archivalInstitutionOaiPmh.isHarvestOnlyWeekend()));
            }
            return SUCCESS;
        }
        addActionError("Sorry, you need to input an URL.");
        return ERROR;
    }

    public String saveProfile() throws Exception {
        try {
            step = 3;
            ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh;
            if(getOaiprofiles() != -1) {
                archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(getOaiprofiles().longValue());
                archivalInstitutionOaiPmh.setSet(getSelectedSet());
                archivalInstitutionOaiPmh.setUrl(getUrl());
                archivalInstitutionOaiPmh.setMetadataPrefix(getSelectedMetadataFormat());
                archivalInstitutionOaiPmh.setProfileId(getSelectedUserProfile().longValue());
                archivalInstitutionOaiPmh.setIntervalHarvesting(Long.parseLong(getIntervalHarvest()));
                archivalInstitutionOaiPmh.setHarvestOnlyWeekend(Boolean.parseBoolean(getSelectedWeekend()));
                if(getSelectedActivation() != null) {
                    if(!archivalInstitutionOaiPmh.isEnabled() && Boolean.parseBoolean(getSelectedActivation())) {
                        archivalInstitutionOaiPmh.setLastHarvesting(null);
                        archivalInstitutionOaiPmh.setEnabled(true);
                    } else if(archivalInstitutionOaiPmh.isEnabled() && !Boolean.parseBoolean(getSelectedActivation())) {
                        archivalInstitutionOaiPmh.setEnabled(false);
                    }
                }
            } else {
                int archivalInstitutionId = SecurityContext.get().getSelectedInstitution().getId();
                String intervalHarvest = getIntervalHarvest();
                archivalInstitutionOaiPmh = new ArchivalInstitutionOaiPmh(archivalInstitutionId, getUrl(), getSelectedMetadataFormat(), getSelectedUserProfile().longValue(), Long.parseLong(intervalHarvest));
                archivalInstitutionOaiPmh.setHarvestOnlyWeekend(Boolean.parseBoolean(getSelectedWeekend()));
                if(getSelectedSet() != null) {
                    archivalInstitutionOaiPmh.setSet(getSelectedSet());
                }
                if(getSelectedActivation() != null) {
                    archivalInstitutionOaiPmh.setEnabled(Boolean.parseBoolean(getSelectedActivation()));
                }
            }
            JpaUtil.beginDatabaseTransaction();
            DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().store(archivalInstitutionOaiPmh);
            JpaUtil.commitDatabaseTransaction();
            return SUCCESS;
        } catch (Exception e) {
            addActionError("Could not save your new profile, please contact an administrator.");
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

    public boolean isDefaultHarvestingProcessing() {
        return defaultHarvestingProcessing;
    }

    public void setDefaultHarvestingProcessing(boolean defaultHarvestingProcessing) {
        defaultHarvestingProcessing = defaultHarvestingProcessing;
    }

    public String getSelectedActivation() {
        return selectedActivation;
    }

    public void setSelectedActivation(String selectedActivation) {
        this.selectedActivation = selectedActivation;
    }

    public String getSelectedWeekend() {
        return selectedWeekend;
    }

    public void setSelectedWeekend(String selectedWeekend) {
        this.selectedWeekend = selectedWeekend;
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
