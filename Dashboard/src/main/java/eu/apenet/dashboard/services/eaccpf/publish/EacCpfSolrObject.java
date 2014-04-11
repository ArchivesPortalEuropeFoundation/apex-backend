package eu.apenet.dashboard.services.eaccpf.publish;

import java.util.Set;

import eu.apenet.persistence.vo.EacCpf;

public class EacCpfSolrObject {
	private Long id;
	private String language;
	private String agencyCode;
	private String agencyName; 
	private String country;
	private Set<String> names;
	private Set<String> places;
	private Set<String> functions;
	private Set<String> mandates;	
	private String description;
	private Set<String> occupations;
	private String dateDescription;
	private String fromDate;
	private String toDate;
	private Set<String> entityIds;
	private String recordId;
	private String other;
	private String entityType;
	
	
	
	private EacCpf eacCpf;
	public EacCpfSolrObject ( EacCpf eacCpf){
		this.eacCpf = eacCpf;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public EacCpf getEacCpf() {
		return eacCpf;
	}
	public void setEacCpf(EacCpf eacCpf) {
		this.eacCpf = eacCpf;
	}


	public Set<String> getEntityIds() {
		return entityIds;
	}
	public void setEntityIds(Set<String> entityIds) {
		this.entityIds = entityIds;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public Set<String> getFunctions() {
		return functions;
	}
	public void setFunctions(Set<String> functions) {
		this.functions = functions;
	}
	public Set<String> getMandates() {
		return mandates;
	}
	public void setMandates(Set<String> mandates) {
		this.mandates = mandates;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}

}
