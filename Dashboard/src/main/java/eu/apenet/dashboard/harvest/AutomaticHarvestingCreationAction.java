package eu.apenet.dashboard.harvest;

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
import eu.apenet.dashboard.listener.HarvesterDaemon;
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



    private Integer step;
    private Integer oaiprofiles;
    private List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhs;
    private String url;
    private List<SelectItem> sets = new ArrayList<SelectItem>();
    private List<SelectItem> metadataFormats = new ArrayList<SelectItem>();
    private List<SelectItem> harvesterMethods = new ArrayList<SelectItem>();
    private List<Ingestionprofile> ingestionProfiles;
    private List<Interval> intervals;
    private String selectedSet;
    private String selectedMetadataFormat;
    private Integer selectedIngestionProfile;
    private String selectedActivation;
    private String selectedWeekend;
    private String lastHarvestDate;
    private String intervalHarvest;
    private boolean harvesterMethod = true;
    private boolean defaultHarvestingProcessing = false;
    private boolean locked;
    
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
            addActionError(getText("label.harvesting.error.needprofile"));
            return ERROR;
        }
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        archivalInstitutionOaiPmhs = archivalInstitutionOaiPmhDAO.getArchivalInstitutionOaiPmhs(getAiId());
        getServletRequest().setAttribute("harvestProfileItems", DisplayHarvestProfileItem.getItems(archivalInstitutionOaiPmhs, new Date()));
        getServletRequest().setAttribute("harvestingStarted", HarvesterDaemon.isHarvesterProcessing());
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
		                addActionError(getText("label.harvesting.error.url"));
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
	                addActionError(getText("label.harvesting.error.allsetsused"));
	                return ERROR;
	            }
            }
            intervals = new ArrayList<Interval>(3);
            intervals.add(new Interval(getText("label.harvesting.interval.weeks", new String[] {"2"}), ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS));
            intervals.add(new Interval(getText("label.harvesting.interval.months", new String[] {"1"}), ArchivalInstitutionOaiPmh.INTERVAL_1_MONTH));
            intervals.add(new Interval(getText("label.harvesting.interval.months", new String[] {"3"}), ArchivalInstitutionOaiPmh.INTERVAL_3_MONTH));
            intervals.add(new Interval(getText("label.harvesting.interval.months", new String[] {"6"}), ArchivalInstitutionOaiPmh.INTERVAL_6_MONTH));
            harvesterMethods = new ArrayList<SelectItem>();
            harvesterMethods.add(new SelectItem("true", getText("label.harvesting.method.listidentifiers")));
            harvesterMethods.add(new SelectItem("false", getText("label.harvesting.method.listrecords")));
            if(getOaiprofiles() != -1) {
                ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(getOaiprofiles().longValue());
                sets.add(new SelectItem(archivalInstitutionOaiPmh.getSet()));
                setSelectedSet(archivalInstitutionOaiPmh.getSet());
                setSelectedMetadataFormat(archivalInstitutionOaiPmh.getMetadataPrefix());
                setSelectedIngestionProfile(archivalInstitutionOaiPmh.getProfileId().intValue());
                setIntervalHarvest(archivalInstitutionOaiPmh.getIntervalHarvesting().toString());
                this.locked = archivalInstitutionOaiPmh.isLocked();
                setSelectedActivation(Boolean.toString(archivalInstitutionOaiPmh.isEnabled()));
                setSelectedWeekend(Boolean.toString(archivalInstitutionOaiPmh.isHarvestOnlyWeekend()));
                setHarvesterMethod(archivalInstitutionOaiPmh.isHarvestMethodListByIdentifiers());
                if (archivalInstitutionOaiPmh.getFrom() != null){
                	setLastHarvestDate(new SimpleDateFormat("dd/MM/yyyy").format(DataHarvester.DATE_FORMATTER.parse(archivalInstitutionOaiPmh.getFrom())));
                }
            }
            return SUCCESS;
        }
        addActionError(getText("label.harvesting.error.nourl"));
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
                archivalInstitutionOaiPmh.setProfileId(getSelectedIngestionProfile().longValue());
                if (!archivalInstitutionOaiPmh.isLocked()){
                	archivalInstitutionOaiPmh.setIntervalHarvesting(getInterval());
                }
                archivalInstitutionOaiPmh.setHarvestOnlyWeekend(Boolean.parseBoolean(getSelectedWeekend()));
                archivalInstitutionOaiPmh.setHarvestMethodListByIdentifiers(isHarvesterMethod());
                if(getSelectedActivation() != null) {
                    if(!archivalInstitutionOaiPmh.isEnabled() && Boolean.parseBoolean(getSelectedActivation())) {
                    	if (!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
                        	archivalInstitutionOaiPmh.setFrom(null);
                        	archivalInstitutionOaiPmh.setNewHarvesting(new Date());
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
                Long intervalHarvest = getInterval();
                archivalInstitutionOaiPmh = new ArchivalInstitutionOaiPmh(archivalInstitutionId, getUrl(), getSelectedMetadataFormat(), getSelectedIngestionProfile().longValue(), intervalHarvest);
                archivalInstitutionOaiPmh.setHarvestMethodListByIdentifiers(isHarvesterMethod());
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
            addActionError(getText("label.harvesting.error.savefailed"));
            return ERROR;
        }
    }

    private Long getInterval(){
        Long interval = Long.parseLong(getIntervalHarvest());
        
        if (interval >= ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS){
        	return interval;
        }else {
        	return ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS;
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

    public List<SelectItem> getHarvesterMethods() {
		return harvesterMethods;
	}
	public void setHarvesterMethods(List<SelectItem> harvesterMethods) {
		this.harvesterMethods = harvesterMethods;
	}
	public boolean isHarvesterMethod() {
		return harvesterMethod;
	}
	public void setHarvesterMethod(boolean harvesterMethod) {
		this.harvesterMethod = harvesterMethod;
	}

	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public class Interval {
        private String description;
        private Long time;

        public Interval(String description, Long time) {
            this.description = description;
            this.time = time;
        }


        public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }
}
