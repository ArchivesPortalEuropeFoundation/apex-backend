package eu.apenet.persistence.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "archival_institution_oai_pmh")
public class ArchivalInstitutionOaiPmh implements Serializable {
	private static final long serialVersionUID = 3915319770716474956L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	@Column(name="last_harvesting")
	private Date lastHarvesting;
	
	@Column(name="new_harvesting")
	private Date newHarvesting;

	@Column(name="from_date")
	private String from;
	
	private String errors;
	
	@Column(name="errors_response_path")
	private String errorsResponsePath;
	
	@Column(name="oai_pmh_set")
	private String set;

	@Column(name="oai_pmh_url")
	private String url;

    @Column(name="oai_pmh_metadata")
    private String metadataPrefix;

    @Column(name="ai_id")
    private Integer aiId;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ai_id", insertable=false, updatable=false)
    private ArchivalInstitution archivalInstitution;

    @Column(name="profile_id")
    private Long profileId;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="profile_id", insertable=false, updatable=false)
    private Ingestionprofile ingestionprofile;

    @Column(name="interval_harvesting")
    private Long intervalHarvesting;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="harvest_only_weekend")
    private boolean harvestOnlyWeekend;
  

    public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


    public String getErrors() {
		return errors;
	}


	public void setErrors(String errors) {
		this.errors = errors;
	}


	public String getErrorsResponsePath() {
		return errorsResponsePath;
	}


	public void setErrorsResponsePath(String errorsResponsePath) {
		this.errorsResponsePath = errorsResponsePath;
	}


	public ArchivalInstitutionOaiPmh() {
    }

	public ArchivalInstitutionOaiPmh(Integer aiId, String url, String metadataPrefix, Long profileId, Long intervalHarvesting) {
		this.aiId = aiId;
		this.url = url;
        this.metadataPrefix = metadataPrefix;
        this.profileId = profileId;
        this.intervalHarvesting = intervalHarvesting;
        this.enabled = true;
        this.harvestOnlyWeekend = false;
	}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastHarvesting() {
        return lastHarvesting;
    }

    public void setLastHarvesting(Date lastHarvesting) {
        this.lastHarvesting = lastHarvesting;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public void setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getIntervalHarvesting() {
        return intervalHarvesting;
    }

    public void setIntervalHarvesting(Long intervalHarvesting) {
        this.intervalHarvesting = intervalHarvesting;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public Ingestionprofile getIngestionprofile() {
        return ingestionprofile;
    }

    public void setIngestionprofile(Ingestionprofile ingestionprofile) {
        this.ingestionprofile = ingestionprofile;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isHarvestOnlyWeekend() {
        return harvestOnlyWeekend;
    }

    public void setHarvestOnlyWeekend(boolean harvestOnlyWeekend) {
        this.harvestOnlyWeekend = harvestOnlyWeekend;
    }


	public Date getNewHarvesting() {
		return newHarvesting;
	}


	public void setNewHarvesting(Date newHarvesting) {
		this.newHarvesting = newHarvesting;
	}
    
}
