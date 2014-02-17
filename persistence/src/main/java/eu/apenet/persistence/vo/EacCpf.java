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


@Entity
@Table(name = "eac_cpf")
public class EacCpf extends AbstractContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6760184769676870729L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ai_id")
	private ArchivalInstitution archivalInstitution;
	@Column(name = "ai_id", updatable = false, insertable = false)
	private Integer aiId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "um_id")
	private UploadMethod uploadMethod;
	private String title;
	private String path;
	private boolean published = false;
	private boolean converted = false;
	private ValidatedState validated = ValidatedState.NOT_VALIDATED;
	private EuropeanaState europeana = EuropeanaState.NOT_CONVERTED;
	private QueuingState queuing = QueuingState.NO;
	@Column(name = "upload_date")
	private Date uploadDate;
	@Column(name = "publish_date")	
	private Date publishDate;
	@Column(name = "identifier", nullable = false)
	private String identifier;
	
	@OneToMany(mappedBy = "eacCpf")
	private Set<QueueItem> queueItems = new HashSet<QueueItem>(0);
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public UploadMethod getUploadMethod() {
		return uploadMethod;
	}
	public void setUploadMethod(UploadMethod uploadMethod) {
		this.uploadMethod = uploadMethod;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
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
	public EuropeanaState getEuropeana() {
		return europeana;
	}
	public void setEuropeana(EuropeanaState europeana) {
		this.europeana = europeana;
	}
	public QueuingState getQueuing() {
		return queuing;
	}
	public void setQueuing(QueuingState queuing) {
		this.queuing = queuing;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public Set<QueueItem> getQueueItems() {
		return queueItems;
	}
	public void setQueueItems(Set<QueueItem> queueItems) {
		this.queueItems = queueItems;
	}






	


}
