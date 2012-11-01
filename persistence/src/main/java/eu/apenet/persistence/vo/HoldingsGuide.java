package eu.apenet.persistence.vo;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Table;

@Entity
@Table(name = "holdings_guide")
public class HoldingsGuide extends Ead {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2297086401601854142L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String eadid;
	private String title;
	@Column(name = "path_apenetead")
	private String pathApenetead;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fs_id")
	private FileState fileState;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upload_date")
	private Date uploadDate;
	/*
	 * states
	 */
	private boolean converted;
	private ValidatedState validated;
	private boolean searchable;
	private QueingState queuing;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="um_id")
	private UploadMethod uploadMethod;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ai_id")
	private ArchivalInstitution archivalInstitution;
	@Column(name="ai_id", updatable = false, insertable = false)
	private Integer aiId;
	private Long totalNumberOfDaos;
	private Long totalNumberOfUnits;
	private Long totalNumberOfUnitsWithDao;
	@OneToMany(mappedBy="holdingsGuide")
	private Set<QueueItem> indexQueues = new HashSet<QueueItem>(0);
	@OneToMany(mappedBy="holdingsGuide")
	private Set<Warnings> warningses = new HashSet<Warnings>(0);
	@OneToMany(mappedBy="holdingsGuide")
	private Set<EadContent> eadContents = new HashSet<EadContent>(0);
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEadid() {
		return eadid;
	}

	public void setEadid(String eadid) {
		this.eadid = eadid;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public String getPathApenetead() {
		return pathApenetead;
	}

	public void setPathApenetead(String pathApenetead) {
		this.pathApenetead = pathApenetead;
	}

	public FileState getFileState() {
		return fileState;
	}

	public void setFileState(FileState fileState) {
		this.fileState = fileState;
	}

	public UploadMethod getUploadMethod() {
		return uploadMethod;
	}

	public void setUploadMethod(UploadMethod uploadMethod) {
		this.uploadMethod = uploadMethod;
	}

	public ArchivalInstitution getArchivalInstitution() {
		return archivalInstitution;
	}

	public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
		this.archivalInstitution = archivalInstitution;
	}

	public Integer getAiId() {
		return aiId;
	}

	public void setAiId(Integer aiId) {
		this.aiId = aiId;
	}

	public Long getTotalNumberOfDaos() {
		return totalNumberOfDaos;
	}

	public void setTotalNumberOfDaos(Long totalNumberOfDaos) {
		this.totalNumberOfDaos = totalNumberOfDaos;
	}

	public Long getTotalNumberOfUnits() {
		return totalNumberOfUnits;
	}

	public void setTotalNumberOfUnits(Long totalNumberOfUnits) {
		this.totalNumberOfUnits = totalNumberOfUnits;
	}

	public Long getTotalNumberOfUnitsWithDao() {
		return totalNumberOfUnitsWithDao;
	}

	public void setTotalNumberOfUnitsWithDao(Long totalNumberOfUnitsWithDao) {
		this.totalNumberOfUnitsWithDao = totalNumberOfUnitsWithDao;
	}

	
	
	public Set<QueueItem> getIndexQueues() {
		return indexQueues;
	}

	public void setIndexQueues(Set<QueueItem> indexQueues) {
		this.indexQueues = indexQueues;
	}

	public Set<Warnings> getWarningses() {
		return warningses;
	}

	public void setWarningses(Set<Warnings> warningses) {
		this.warningses = warningses;
	}

	public Set<EadContent> getEadContents() {
		return eadContents;
	}

	public void setEadContents(Set<EadContent> eadContents) {
		this.eadContents = eadContents;
	}


	public HoldingsGuide() {
	}
	@Deprecated
	public HoldingsGuide(int hgId, FileState fileState,
			UploadMethod uploadMethod, ArchivalInstitution archivalInstitution,
			String hgTittle, Date uploadDate, String pathApenetead) {
		this.setId(hgId);
		this.setFileState(fileState);
		this.setUploadMethod(uploadMethod);
		this.setArchivalInstitution(archivalInstitution);
		this.setTitle(hgTittle);
		this.setUploadDate(uploadDate);
		this.setPathApenetead(pathApenetead);
	}

	public boolean isConverted() {
		return converted;
	}

	public void setConverted(boolean converted) {
		this.converted = converted;
	}

	public ValidatedState getValidated() {
		return validated;
	}

	public void setValidated(ValidatedState validated) {
		this.validated = validated;
	}

	public QueingState getQueuing() {
		return queuing;
	}

	public void setQueuing(QueingState queuing) {
		this.queuing = queuing;
	}



}
