package eu.apenet.persistence.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "archival_institution")
public class ArchivalInstitution implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5793664005280731903L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int aiId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_ai_id")
	private ArchivalInstitution parent;
	@Column(name = "parent_ai_id", insertable = false, updatable = false)
	private Integer parentAiId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", insertable = false, updatable = false)
	private User partner;
	
	@Column(name = "user_id")
	private Integer partnerId;
	@Column(name = "country_id")
	private Integer countryId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="country_id", insertable = false, updatable = false)
	private Country country;
	private String ainame;
	@Temporal(TemporalType.DATE)
	@Column(name = "registration_date")
	private Date registrationDate;
	@Column(name = "eag_path")
	private String eagPath;
	private String repositorycode;
	private String autform;
	@Column(name = "isgroup")
	private boolean group;
	@Column(name = "internal_al_id")
	private String internalAlId;
	private int alorder;
	@Column(name = "contain_searchable_items")
	private boolean containSearchableItems;

	@OneToMany(mappedBy="archivalInstitution")
	private Set<AiAlternativeName> aiAlternativeNames = new HashSet<AiAlternativeName>(0);

	@OneToMany(mappedBy="parent")
	private Set<ArchivalInstitution> childArchivalInstitutions = new HashSet<ArchivalInstitution>(0);

	@OneToMany(mappedBy="archivalInstitution")
	private Set<FindingAid> findingAids = new HashSet<FindingAid>(0);
	@OneToMany(mappedBy="archivalInstitution")
	private Set<HoldingsGuide> holdingsGuides = new HashSet<HoldingsGuide>(0);
	@OneToMany(mappedBy="archivalInstitution")
	private Set<SourceGuide> sourceGuides = new HashSet<SourceGuide>(0);
	public int getAiId() {
		return this.aiId;
	}

	public void setAiId(int aiId) {
		this.aiId = aiId;
	}
	public ArchivalInstitution getParent() {
		return this.parent;
	}
	public void setParent(ArchivalInstitution archivalInstitution) {
		this.parent = archivalInstitution;
	}
	
	public Integer getParentAiId() {
		return parentAiId;
	}

	public void setParentAiId(Integer parentAiId) {
		this.parentAiId = parentAiId;
	}

	public User getPartner() {
		return this.partner;
	}

	public void setPartner(User partner) {
		this.partner = partner;
	}

	public String getAiname() {
		return this.ainame;
	}

	public void setAiname(String ainame) {
		this.ainame = ainame;
	}

	public Date getRegistrationDate() {
		return this.registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getEagPath() {
		return this.eagPath;
	}

	public void setEagPath(String eagPath) {
		this.eagPath = eagPath;
	}

	public String getRepositorycode() {
		return this.repositorycode;
	}

	public void setRepositorycode(String repositorycode) {
		this.repositorycode = repositorycode;
	}

	public boolean isContainSearchableItems() {
		return containSearchableItems;
	}

	public void setContainSearchableItems(boolean containSearchableItems) {
		this.containSearchableItems = containSearchableItems;
	}

	public String getAutform() {
		return this.autform;
	}

	public void setAutform(String autform) {
		this.autform = autform;
	}

	public boolean isGroup() {
		return this.group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}

//	public Set<UpFile> getUpFiles() {
//		return this.upFiles;
//	}
//
//	public void setUpFiles(Set<UpFile> upFiles) {
//		this.upFiles = upFiles;
//	}

	public Set<HoldingsGuide> getHoldingsGuides() {
		return this.holdingsGuides;
	}

	public void setHoldingsGuides(Set<HoldingsGuide> holdingsGuides) {
		this.holdingsGuides = holdingsGuides;
	}

    public Set<SourceGuide> getSourceGuides() {
        return sourceGuides;
    }

    public void setSourceGuides(Set<SourceGuide> sourceGuides) {
        this.sourceGuides = sourceGuides;
    }

	public Set<AiAlternativeName> getAiAlternativeNames() {
		return this.aiAlternativeNames;
	}

	public void setAiAlternativeNames(Set<AiAlternativeName> aiAlternativeNames) {
		this.aiAlternativeNames = aiAlternativeNames;
	}

	public Set<FindingAid> getFindingAids() {
		return this.findingAids;
	}

	public void setFindingAids(Set<FindingAid> findingAids) {
		this.findingAids = findingAids;
	}


	public Set<ArchivalInstitution> getChildArchivalInstitutions() {
		return childArchivalInstitutions;
	}

	public void setChildArchivalInstitutions(Set<ArchivalInstitution> childArchivalInstitutions) {
		this.childArchivalInstitutions = childArchivalInstitutions;
	}

	@Override
        public String toString() {
        return "ArchivalInstitution{" +
                
        		"aiId=" + aiId +
                ", ainame='" + ainame + '\'' +
                ", eagPath='" + eagPath + '\'' +
                ", repositorycode='" + repositorycode + '\'' +
                ", autform='" + autform + '\'' +
                '}';
    }

	public void setAlorder(int alorder) {
		this.alorder = alorder;
	}

	public int getAlorder() {
		return alorder;
	}

	public String getInternalAlId() {
		return internalAlId;
	}

	public void setInternalAlId(String internalAlId) {
		this.internalAlId = internalAlId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

}
