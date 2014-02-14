package eu.apenet.dashboard.services.eaccpf.publish;

import java.util.Set;

public class EacCpfSolrObject {
	private String recordId;
	private String agencyCode;
	private String agencyName; 
	private Set<String> names;
	private Set<String> places;
	private String description;
	private Set<String> occupations;
	private String dateDescription;
	private String fromDate;
	private String toDate;
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getAgencyCode() {
		return agencyCode;
	}
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public Set<String> getNames() {
		return names;
	}
	public void setNames(Set<String> names) {
		this.names = names;
	}
	public Set<String> getPlaces() {
		return places;
	}
	public void setPlaces(Set<String> places) {
		this.places = places;
	}
	public String getDateDescription() {
		return dateDescription;
	}
	public void setDateDescription(String dateDescription) {
		this.dateDescription = dateDescription;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<String> getOccupations() {
		return occupations;
	}
	public void setOccupations(Set<String> occupations) {
		this.occupations = occupations;
	}

}
