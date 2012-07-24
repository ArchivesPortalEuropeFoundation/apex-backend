package eu.apenet.dashboard.manual;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import eu.apenet.persistence.vo.Warnings;

public class EADCMUnit {
	private Integer eadCMUnitState;
	private Integer eadCMUnitId;
	private String eadCMUnitEadId;
	private String eadCMUnitTitle;
	private String eadCMUnitUpMeth;
	private String eadCMUnitHolding;
	private String eadCMUnitUpDate;
	private Set<Warnings> eadCMUnitListWarnings;
	private Boolean eadCMUnitIsCaching;
	private Integer eadCMUnitNumberThumbnails;
	private Long countEadCMUnitDocs;
	private Long possibleFindingAidsLinked;
	private Long findingAidsLinked;
	private Integer queuePosition = null;
	private Long numberOfDAOs = 0l;
	private String indexError = null;
	
	public Integer getQueuePosition() {
		return queuePosition;
	}

	public void setQueuePosition(Integer queuePosition) {
		this.queuePosition = queuePosition;
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public EADCMUnit() {}
	
	public Integer getEadCMUnitState() {
		return eadCMUnitState;
	}

	public void setEadCMUnitState(Integer eadCMUnitState) {
		this.eadCMUnitState = eadCMUnitState;
	}

	public Integer getEadCMUnitId() {
		return eadCMUnitId;
	}

	public void setEadCMUnitId(Integer eadCMUnitId) {
		this.eadCMUnitId = eadCMUnitId;
	}

	public String getEadCMUnitUpMeth() {
		return eadCMUnitUpMeth;
	}

	public void setEadCMUnitUpMeth(String eadCMUnitUpMeth) {
		this.eadCMUnitUpMeth = eadCMUnitUpMeth;
	}

	public String getEadCMUnitUpDate() {
		return eadCMUnitUpDate;
	}

	public void setEadCMUnitUpDate(Date eadCMUnitUpDate) {
		this.eadCMUnitUpDate = formatter.format(eadCMUnitUpDate);
	}

	public void setEadCMUnitHolding(String eadCMUnitHolding) {
		this.eadCMUnitHolding = eadCMUnitHolding;
	}

	public String getEadCMUnitHolding() {
		return eadCMUnitHolding;
	}

	public void setEadCMUnitTitle(String eadCMUnitTitle) {
		this.eadCMUnitTitle = eadCMUnitTitle;
	}

	public String getEadCMUnitTitle() {
		return eadCMUnitTitle;
	}

	public void setEadCMUnitEadId(String eadCMUnitEadId) {
		this.eadCMUnitEadId = eadCMUnitEadId;
	}

	public String getEadCMUnitEadId() {
		return eadCMUnitEadId;
	}

	public void setEadCMUnitListWarnings(Set<Warnings> eadCMUnitListWarnings) {
		this.eadCMUnitListWarnings = eadCMUnitListWarnings;
	}

	public Set<Warnings> getEadCMUnitListWarnings() {
		return eadCMUnitListWarnings;
	}

	public void setEadCMUnitIsCaching(Boolean eadCMUnitIsCaching) {
		this.eadCMUnitIsCaching = eadCMUnitIsCaching;
	}

	public Boolean getEadCMUnitIsCaching() {
		return eadCMUnitIsCaching;
	}

	public void setEadCMUnitNumberThumbnails(Integer eadCMUnitNumberThumbnails) {
		this.eadCMUnitNumberThumbnails = eadCMUnitNumberThumbnails;
	}

	public Integer getEadCMUnitNumberThumbnails() {
		return eadCMUnitNumberThumbnails;
	}

	public void setCountEadCMUnitDocs(Long countEadCMUnitDocs) {
		this.countEadCMUnitDocs = countEadCMUnitDocs;
	}

	public Long getCountEadCMUnitDocs() {
		return countEadCMUnitDocs;
	}

	public Long getNumberOfDAOs() {
		return numberOfDAOs;
	}

	public void setNumberOfDAOs(Long numberOfDAOs) {
		this.numberOfDAOs = numberOfDAOs;
	}

	public void setFindingAidsLinked(Long findingAidsLinked) {
		this.findingAidsLinked = findingAidsLinked;
	}

	public Long getFindingAidsLinked() {
		return findingAidsLinked;
	}

	public void setPossibleFindingAidsLinked(Long possibleFindingAidsLinked) {
		this.possibleFindingAidsLinked = possibleFindingAidsLinked;
	}

	public Long getPossibleFindingAidsLinked() {
		return possibleFindingAidsLinked;
	}

	public String getIndexError() {
		return indexError;
	}

	public void setIndexError(String indexError) {
		this.indexError = indexError;
	}
	
}
