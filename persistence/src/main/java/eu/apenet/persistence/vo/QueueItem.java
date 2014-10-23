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
	@JoinColumn(name = "eac_cpf_id")
	private EacCpf eacCpf;
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

	public EacCpf getEacCpf() {
		return eacCpf;
	}

	public void setEacCpf(EacCpf eacCpf) {
		this.eacCpf = eacCpf;
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

	public AbstractContent getAbstractContent(){
		if (this.findingAid != null) {
			return this.findingAid;
		} else if (this.holdingsGuide != null) {
			return this.holdingsGuide;
		} else if (this.sourceGuide != null){
			return this.sourceGuide;
		}
		return this.eacCpf;		
	}
	public Ead getEad(){
		if (this.findingAid != null) {
			return this.findingAid;
		} else if (this.holdingsGuide != null) {
			return this.holdingsGuide;
		} else if (this.sourceGuide != null){
			return this.sourceGuide;
		}
		return null;
	}
	public void setAbstractContent(AbstractContent content) {
		if (content == null) {
			this.findingAid = null;
			this.holdingsGuide = null;
			this.sourceGuide = null;
			this.eacCpf = null;
		} else {
			if (content instanceof FindingAid) {
				this.findingAid = (FindingAid) content;
			} else if (content instanceof HoldingsGuide) {
				this.holdingsGuide = (HoldingsGuide) content;
			} else if (content instanceof SourceGuide) {
				this.sourceGuide = (SourceGuide)content;
			} 
			this.eacCpf = (EacCpf)content;
		}
	}	
	public void setEad(Ead ead) {
		if (ead == null) {
			this.findingAid = null;
			this.holdingsGuide = null;
			this.sourceGuide = null;
		} else {
			if (ead instanceof FindingAid) {
				this.findingAid = (FindingAid) ead;
			} else if (ead instanceof HoldingsGuide) {
				this.holdingsGuide = (HoldingsGuide) ead;
			} else if (ead instanceof SourceGuide) {
				this.sourceGuide = (SourceGuide)ead;
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
