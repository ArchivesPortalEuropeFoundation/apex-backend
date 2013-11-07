package eu.apenet.persistence.vo;

import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "queue")
public class QueueItem implements java.io.Serializable {
    public static final String XML_TYPE = "ape.xmltype";
    public static final String NO_EADID_ACTION = "ape.noeadidaction";
    public static final String EXIST_ACTION = "ape.existaction";
    public static final String DAO_TYPE = "ape.daotype";
    public static final String UPLOAD_ACTION = "ape.uploadaction";

    /**
	 * 
	 */
	private static final long serialVersionUID = -4266955613706066708L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hg_id")
	private HoldingsGuide holdingsGuide;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fa_id")
	private FindingAid findingAid;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sg_id")
	private SourceGuide sourceGuide;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uf_id")
	private UpFile upFile;
	@Column(name = "uf_id", updatable = false, insertable = false)
	private Integer upFileId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "queue_date")
	private Date queueDate;
	private String errors;
	private Integer priority;
	@Enumerated(EnumType.STRING)
	private QueueAction action;
	private String preferences;

	public QueueItem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HoldingsGuide getHoldingsGuide() {
		return this.holdingsGuide;
	}

	public void setHoldingsGuide(HoldingsGuide holdingsGuide) {
		this.holdingsGuide = holdingsGuide;
	}

	public FindingAid getFindingAid() {
		return this.findingAid;
	}

	public void setFindingAid(FindingAid findingAid) {
		this.findingAid = findingAid;
	}

	public SourceGuide getSourceGuide() {
		return sourceGuide;
	}

	public void setSourceGuide(SourceGuide sourceGuide) {
		this.sourceGuide = sourceGuide;
	}

	public Date getQueueDate() {
		return queueDate;
	}

	public void setQueueDate(Date queueDate) {
		this.queueDate = queueDate;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public QueueAction getAction() {
		return action;
	}

	public void setAction(QueueAction action) {
		this.action = action;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public Ead getEad() {
		if (findingAid != null) {
			return findingAid;
		} else if (holdingsGuide != null) {
			return holdingsGuide;
		} else {
			return sourceGuide;
		}
	}

	public void setEad(Ead ead) {
		if (ead == null) {
			findingAid = null;
			holdingsGuide = null;
			sourceGuide = null;
		} else {
			if (ead instanceof FindingAid) {
				findingAid = (FindingAid) ead;
			} else if (ead instanceof HoldingsGuide) {
				holdingsGuide = (HoldingsGuide) ead;
			} else if (ead instanceof SourceGuide) {
				sourceGuide = (SourceGuide) ead;
			}
		}
	}

	public UpFile getUpFile() {
		return upFile;
	}

	public void setUpFile(UpFile upFile) {
		this.upFile = upFile;
	}

	public Integer getUpFileId() {
		return upFileId;
	}

	public void setUpFileId(Integer upFileId) {
		this.upFileId = upFileId;
	}



}
