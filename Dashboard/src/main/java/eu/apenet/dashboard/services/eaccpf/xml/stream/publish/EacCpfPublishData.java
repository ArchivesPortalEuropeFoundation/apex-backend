package eu.apenet.dashboard.services.eaccpf.xml.stream.publish;

import java.util.List;
import java.util.Set;

public class EacCpfPublishData {
	private String language;
	private List<String> names;
	private Set<String> places;
	private Set<String> functions;
	private Set<String> mandates;	
	private String description;
	private Set<String> occupations;
	private String dateDescription;
	private String fromDate;
	private String toDate;
	private String dateType;
	private Set<String> entityIds;
	private String recordId;
	private String other;
	private String entityType;
	private Integer numberOfArchivalMaterialRelations;
	private Integer numberOfNameRelations;
	private Integer numberOfInstitutionsRelations;
	private Integer numberOfCpfRelations;
	private Integer numberOfFunctionRelations;



	public void setLanguage(String language) {
		this.language = language;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
	public void setPlaces(Set<String> places) {
		this.places = places;
	}
	public void setFunctions(Set<String> functions) {
		this.functions = functions;
	}
	public void setMandates(Set<String> mandates) {
		this.mandates = mandates;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setOccupations(Set<String> occupations) {
		this.occupations = occupations;
	}
	public void setDateDescription(String dateDescription) {
		this.dateDescription = dateDescription;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public void setEntityIds(Set<String> entityIds) {
		this.entityIds = entityIds;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public void setNumberOfArchivalMaterialRelations(Integer numberOfArchivalMaterialRelations) {
		this.numberOfArchivalMaterialRelations = numberOfArchivalMaterialRelations;
	}
	public void setNumberOfNameRelations(Integer numberOfNameRelations) {
		this.numberOfNameRelations = numberOfNameRelations;
	}
	public void setNumberOfInstitutionsRelations(Integer numberOfInstitutionsRelatiosn) {
		this.numberOfInstitutionsRelations = numberOfInstitutionsRelatiosn;
	}
	public String getLanguage() {
		return language;
	}
	public List<String> getNames() {
		return names;
	}
	public Set<String> getMandates() {
		return mandates;
	}
	public String getDescription() {
		return description;
	}
	public Set<String> getOccupations() {
		return occupations;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getOther() {
		return other;
	}
	public Integer getNumberOfArchivalMaterialRelations() {
		return numberOfArchivalMaterialRelations;
	}
	public Integer getNumberOfNameRelations() {
		return numberOfNameRelations;
	}
	public Integer getNumberOfInstitutionsRelations() {
		return numberOfInstitutionsRelations;
	}
	public Set<String> getPlaces() {
		return places;
	}
	public String getDateDescription() {
		return dateDescription;
	}
	public String getToDate() {
		return toDate;
	}
	public Set<String> getEntityIds() {
		return entityIds;
	}
	public String getEntityType() {
		return entityType;
	}
	public Set<String> getFunctions() {
		return functions;
	}
	public String getRecordId() {
		return recordId;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public Integer getNumberOfCpfRelations() {
		return numberOfCpfRelations;
	}

	public void setNumberOfCpfRelations(Integer numberOfCpfRelations) {
		this.numberOfCpfRelations = numberOfCpfRelations;
	}

	public Integer getNumberOfFunctionRelations() {
		return numberOfFunctionRelations;
	}

	public void setNumberOfFunctionRelations(Integer numberOfFunctionRelations) {
		this.numberOfFunctionRelations = numberOfFunctionRelations;
	}



}
