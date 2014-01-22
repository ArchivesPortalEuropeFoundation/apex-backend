package eu.apenet.dashboard.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.harvest.DataHarvester;
import eu.apenet.dashboard.harvest.DisplayHarvestProfileItem;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.archivesportaleurope.harvester.oaipmh.RetrieveOaiPmhInformation;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhElement;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class AutomaticHarvestingCreationAction extends AbstractInstitutionAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7566396853500559421L;

	private static final Logger LOG = Logger.getLogger(AutomaticHarvestingCreationAction.class);

    public static final Long INTERVAL_1_MONTH = 2630000000L;
    private static final Long INTERVAL_3_MONTH = 7889230000L;
    private static final Long INTERVAL_6_MONTH = 15780000000L;

    private Integer step;
    private Integer oaiprofiles;
    private List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhs;
    private String url;
    private List<SelectItem> sets = new ArrayList<SelectItem>();
    private List<SelectItem> metadataFormats = new ArrayList<SelectItem>();
    private List<Ingestionprofile> ingestionProfiles;
    private List<Interval> intervals;
    private String selectedSet;
    private String selectedMetadataFormat;
    private Integer selectedIngestionProfile;
    private String selectedActivation;
    private String selectedWeekend;
    private String lastHarvestDate;
    private String intervalHarvest;
    private boolean defaultHarvestingProcessing = false;
    
    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dashboard.menu.automaticharvestingcreation"));
    }
    public void validate() {
        defaultHarvestingProcessing = APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing();
    }

    public String execute() throws Exception {
        step = 0;
        ingestionProfiles = DAOFactory.instance().getIngestionprofileDAO().getIngestionprofiles(getAiId());
        if(ingestionProfiles.size() < 1) {
            addActionError("You need at least one user profile created before creating an automatic OAI-PMH profile");
            return ERROR;
        }
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        archivalInstitutionOaiPmhs = archivalInstitutionOaiPmhDAO.getArchivalInstitutionOaiPmhs(getAiId());
        getServletRequest().setAttribute("harvestProfileItems", DisplayHarvestProfileItem.getItems(archivalInstitutionOaiPmhs, new Date()));
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
            ingestionProfiles = DAOFactory.instance().getIngestionprofileDAO().getIngestionprofiles(getAiId());
            if(getOaiprofiles() == -1) {
	    		OaiPmhHttpClient oaiPmhHttpClient = null;
	    		List<SelectItem> setsInRepository = null;
	    		try {
	    			oaiPmhHttpClient = new OaiPmhHttpClient();
		            try {
		                metadataFormats = convert(RetrieveOaiPmhInformation.retrieveMetadataFormats(getUrl(), oaiPmhHttpClient));
		                if(metadataFormats == null || metadataFormats.isEmpty())
		                    throw new APEnetException("No metadata formats for this URL: " + getUrl());
		            } catch (Exception e) {
		                addActionError("Sorry, the URL is not a correct repository URL or the repository does not contain any metadata formats...");
		                return ERROR;
		            }
		            setsInRepository = convert(RetrieveOaiPmhInformation.retrieveSets(getUrl(), oaiPmhHttpClient));
		            sets = new ArrayList<SelectItem>(setsInRepository);
		            List<String> simpleSets = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().getSets(getUrl());
		           
		            if(setsInRepository != null) {
		                for(SelectItem set : setsInRepository) {
		                    for(String stringSet : simpleSets) {
		                        if(stringSet.equals(set.getValue())) {
		                            sets.remove(set);
		                        }
		                    }
		                }
		            }
	    		} catch (Exception e) {
	    			LOG.error("Unexcepted error occurred: " + e.getMessage());
	    		}finally {
	    			if (oaiPmhHttpClient != null){
	    				try {
	    					oaiPmhHttpClient.close();
	    				}catch(  IOException io){
	    					LOG.error("Unexcepted error occurred: " + io.getMessage(), io);
	    				}
	    			}
	    		}
	            if(setsInRepository != null && sets.size() == 0 && setsInRepository.size() != 0 && getOaiprofiles() == -1) {
	                addActionError("Sorry, all your sets are already being used in other profiles. you need to delete some profiles to continue, or edit profiles");
	                return ERROR;
	            }
            }
            intervals = new ArrayList<Interval>(3);
            intervals.add(new Interval(1, INTERVAL_1_MONTH));
            intervals.add(new Interval(3, INTERVAL_3_MONTH));
            intervals.add(new Interval(6, INTERVAL_6_MONTH));
            if(getOaiprofiles() != -1) {
                ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(getOaiprofiles().longValue());
                sets.add(new SelectItem(archivalInstitutionOaiPmh.getSet()));
                setSelectedSet(archivalInstitutionOaiPmh.getSet());
                setSelectedMetadataFormat(archivalInstitutionOaiPmh.getMetadataPrefix());
                setSelectedIngestionProfile(archivalInstitutionOaiPmh.getProfileId().intValue());
                setIntervalHarvest(archivalInstitutionOaiPmh.getIntervalHarvesting().toString());
                setSelectedActivation(Boolean.toString(archivalInstitutionOaiPmh.isEnabled()));
                setSelectedWeekend(Boolean.toString(archivalInstitutionOaiPmh.isHarvestOnlyWeekend()));
                if (archivalInstitutionOaiPmh.getFrom() != null){
                	setLastHarvestDate(new SimpleDateFormat("dd/MM/yyyy").format(DataHarvester.DATE_FORMATTER.parse(archivalInstitutionOaiPmh.getFrom())));
                }
            }
            return SUCCESS;
        }
        addActionError("Sorry, you need to input an URL.");
        return ERROR;
    }

    private List<SelectItem> convert(List<OaiPmhElement> oaiPmhElements){
    	List<SelectItem> result = new ArrayList<SelectItem>();
    	for (OaiPmhElement element: oaiPmhElements){
    		result.add(new SelectItem(element.getElement(),element.toString()));
    	}
    	return result;
    }
    public String saveProfile() throws Exception {
        try {
            step = 3;
            ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh;
            if(getOaiprofiles() != -1) {
                archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(getOaiprofiles().longValue());
//                archivalInstitutionOaiPmh.setSet(getSelectedSet());
//                archivalInstitutionOaiPmh.setUrl(getUrl());
//                archivalInstitutionOaiPmh.setMetadataPrefix(getSelectedMetadataFormat());
                archivalInstitutionOaiPmh.setProfileId(getSelectedIngestionProfile().longValue());
                archivalInstitutionOaiPmh.setIntervalHarvesting(Long.parseLong(getIntervalHarvest()));
                archivalInstitutionOaiPmh.setHarvestOnlyWeekend(Boolean.parseBoolean(getSelectedWeekend()));
                if(getSelectedActivation() != null) {
                    if(!archivalInstitutionOaiPmh.isEnabled() && Boolean.parseBoolean(getSelectedActivation())) {
                    	if (!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
                        	archivalInstitutionOaiPmh.setFrom(null);
                    	}
                        archivalInstitutionOaiPmh.setEnabled(true);
                    } else if(archivalInstitutionOaiPmh.isEnabled() && !Boolean.parseBoolean(getSelectedActivation())) {
                        archivalInstitutionOaiPmh.setEnabled(false);
                    }
                } else if(getLastHarvestDate() != null) {
                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(getLastHarvestDate());
                        archivalInstitutionOaiPmh.setFrom(DataHarvester.DATE_FORMATTER.format(date));
                    } catch (Exception e) {
                        archivalInstitutionOaiPmh.setFrom(null);
                    }
                }
            } else {
                int archivalInstitutionId = SecurityContext.get().getSelectedInstitution().getId();
                String intervalHarvest = getIntervalHarvest();
                archivalInstitutionOaiPmh = new ArchivalInstitutionOaiPmh(archivalInstitutionId, getUrl(), getSelectedMetadataFormat(), getSelectedIngestionProfile().longValue(), Long.parseLong(intervalHarvest));
                archivalInstitutionOaiPmh.setHarvestOnlyWeekend(Boolean.parseBoolean(getSelectedWeekend()));
                archivalInstitutionOaiPmh.setNewHarvesting(new Date());
                if(getSelectedSet() != null) {
                    archivalInstitutionOaiPmh.setSet(getSelectedSet());
                }
                if(getSelectedActivation() != null) {
                    archivalInstitutionOaiPmh.setEnabled(Boolean.parseBoolean(getSelectedActivation()));
                }
                if(getLastHarvestDate() != null) {
                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(getLastHarvestDate());
                        archivalInstitutionOaiPmh.setFrom(DataHarvester.DATE_FORMATTER.format(date));
                    } catch (Exception e) {
                        archivalInstitutionOaiPmh.setFrom(null);
                    }
                }
            }
            JpaUtil.beginDatabaseTransaction();
            DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().store(archivalInstitutionOaiPmh);
            JpaUtil.commitDatabaseTransaction();
            return SUCCESS;
        } catch (Exception e) {
            LOG.error("Could not save the profile...", e);
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
    	if (url != null){
    		this.url = url.trim();
    	}else {
    		this.url = url;
    	}
    }

    public List<SelectItem> getSets() {
        return sets;
    }

    public void setSets(List<SelectItem> sets) {
        this.sets = sets;
    }

    public List<SelectItem> getMetadataFormats() {
        return metadataFormats;
    }

    public void setMetadataFormats(List<SelectItem> metadataFormats) {
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

    public List<Ingestionprofile> getIngestionProfiles() {
        return ingestionProfiles;
    }

    public void setIngestionProfiles(List<Ingestionprofile> ingestionProfiles) {
        this.ingestionProfiles = ingestionProfiles;
    }

    public Integer getSelectedIngestionProfile() {
        return selectedIngestionProfile;
    }

    public void setSelectedIngestionProfile(Integer selectedIngestionProfile) {
        this.selectedIngestionProfile = selectedIngestionProfile;
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
