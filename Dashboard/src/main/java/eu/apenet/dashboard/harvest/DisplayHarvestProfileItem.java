package eu.apenet.dashboard.harvest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.vo.OaiPmhStatus;

public class DisplayHarvestProfileItem {
    private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    private static final SimpleDateFormat DATE = new SimpleDateFormat("dd-MM-yyyy");
	private long id;
	private String country;
	private String ainame;
	private String url;
	private String set;
    private String metadataPrefix;
    
	private String lastHarvesting;
	private String newHarvesting;
	private String from;
	private String intervalHarvesting;
	private String ingestionProfile;
	private OaiPmhStatus status;
	private String harvestingStatus;
	private String harvestingDetails;
	private String errorResponsePath;
    private boolean enabled;
    private boolean harvestOnlyWeekend;
    private boolean readyForHarvesting;
    private boolean harvestMethodListByIdentifiers;

	public DisplayHarvestProfileItem(ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh, Date now){
		this.id = archivalInstitutionOaiPmh.getId();
		this.ainame = archivalInstitutionOaiPmh.getArchivalInstitution().getAiname();
		this.country = archivalInstitutionOaiPmh.getArchivalInstitution().getCountry().getCname();
		this.url = archivalInstitutionOaiPmh.getUrl();
		this.set = archivalInstitutionOaiPmh.getSet();
		this.metadataPrefix = archivalInstitutionOaiPmh.getMetadataPrefix();
		if (archivalInstitutionOaiPmh.getLastHarvesting() != null){
			this.lastHarvesting = DATE_TIME.format(archivalInstitutionOaiPmh.getLastHarvesting());
		}
		if (archivalInstitutionOaiPmh.getNewHarvesting() != null){
			this.newHarvesting = DATE_TIME.format(archivalInstitutionOaiPmh.getNewHarvesting());
			readyForHarvesting = now.after(archivalInstitutionOaiPmh.getNewHarvesting());

		}else {
			readyForHarvesting = true;
		}
		this.from = archivalInstitutionOaiPmh.getFrom();
		this.intervalHarvesting = ContentUtils.getDaysFromMilliseconds(archivalInstitutionOaiPmh.getIntervalHarvesting());
		this.ingestionProfile = archivalInstitutionOaiPmh.getIngestionprofile().getNameProfile();
		if (archivalInstitutionOaiPmh.getHarvestingStatus() != null){
			this.harvestingStatus = archivalInstitutionOaiPmh.getHarvestingStatus().getResourceName();
		}
		this.harvestMethodListByIdentifiers = archivalInstitutionOaiPmh.isHarvestMethodListByIdentifiers();
		this.status = archivalInstitutionOaiPmh.getHarvestingStatus();
		this.harvestingDetails = archivalInstitutionOaiPmh.getHarvestingDetails();
		this.errorResponsePath = archivalInstitutionOaiPmh.getErrorsResponsePath();
		this.enabled = archivalInstitutionOaiPmh.isEnabled();
		this.harvestOnlyWeekend = archivalInstitutionOaiPmh.isHarvestOnlyWeekend();

	}
	
	public static List<DisplayHarvestProfileItem> getItems(List<ArchivalInstitutionOaiPmh> list, Date now){
		List<DisplayHarvestProfileItem> result = new ArrayList<DisplayHarvestProfileItem>();
		for (ArchivalInstitutionOaiPmh item : list){
			result.add(new DisplayHarvestProfileItem(item, now));
		}
		return result;
	}

	public long getId() {
		return id;
	}

	public String getCountry() {
		return country;
	}

	public String getAiname() {
		return ainame;
	}

	public String getUrl() {
		return url;
	}

	public String getSet() {
		return set;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public String getLastHarvesting() {
		return lastHarvesting;
	}

	public String getNewHarvesting() {
		return newHarvesting;
	}

	public String getFrom() {
		return from;
	}

	public String getIntervalHarvesting() {
		return intervalHarvesting;
	}

	public String getIngestionProfile() {
		return ingestionProfile;
	}

	public boolean isErrors() {
		return OaiPmhStatus.FAILED.equals(status) || OaiPmhStatus.SUCCEED_WITH_ERRORS.equals(status) || OaiPmhStatus.SUCCEED_WITH_WARNINGS.equals(status);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isHarvestOnlyWeekend() {
		return harvestOnlyWeekend;
	}

	public boolean isReadyForHarvesting() {
		return readyForHarvesting;
	}
	public String getGlobalCss(){
		if (isEnabled()){
			if (isReadyForHarvesting()){
				return "harvestProfileReady";
			}
		}else {
			return "harvestProfileDisabled";
		}
		return "";
	}
	public String getErrorCss(){
		if (isErrors()){
			return "harvestProfileErrors";
		}
		return "";
	}



	public static SimpleDateFormat getDateTime() {
		return DATE_TIME;
	}

	public static SimpleDateFormat getDate() {
		return DATE;
	}

	public String getHarvestingStatus() {
		return harvestingStatus;
	}

	public String getHarvestingDetails() {
		return harvestingDetails;
	}

	public String getErrorResponsePath() {
		return errorResponsePath;
	}

	public boolean isHarvestMethodListByIdentifiers() {
		return harvestMethodListByIdentifiers;
	}


	
}
