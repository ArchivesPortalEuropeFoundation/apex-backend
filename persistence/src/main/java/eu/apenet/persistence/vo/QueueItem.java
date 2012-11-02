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
// Generated 10-may-2011 9:45:34 by Hibernate Tools 3.2.4.GA

/**
 * IndexQueue generated by hbm2java
 */
@Entity
@Table(name = "queue")
public class QueueItem implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4266955613706066708L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hg_id")
	private HoldingsGuide holdingsGuide;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fa_id")
	private FindingAid findingAid;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sg_id")
	private SourceGuide sourceGuide;
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
	public Ead getEad(){
		if (findingAid != null){
			return findingAid;
		}else if (holdingsGuide != null){
			return holdingsGuide;
		}else {
			return sourceGuide;
		}
	}






}
