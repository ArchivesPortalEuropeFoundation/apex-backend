package eu.apenet.dashboard.services.eag.xml.stream.publish;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EagPublishData{

	private Set<String> countries = new HashSet<String>();
	private List<String> address = new ArrayList<String>();
	private List<String> aiGroups = new ArrayList<String>();
	private List<String> aiGroupFacets = new ArrayList<String>();
	private List<Integer> aiGroupIds = new ArrayList<Integer>();
 	private Set<String> otherNames;
	private Set<String> repositories;
	private Set<String> repositoryTypes;
	private Set<String> places;
	private String other;
	private String description;
	
	public Set<String> getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(Set<String> otherNames) {
		this.otherNames = otherNames;
	}

	public Set<String> getRepositories() {
		return repositories;
	}

	public void setRepositories(Set<String> repositories) {
		this.repositories = repositories;
	}

	public Set<String> getRepositoryTypes() {
		return repositoryTypes;
	}

	public void setRepositoryTypes(Set<String> repositoryTypes) {
		this.repositoryTypes = repositoryTypes;
	}

	public String getOther() {
		return other;
	}

	public String getDescription() {
		return description;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Set<String> getCountries() {
		return countries;
	}

	public List<String> getAiGroups() {
		return aiGroups;
	}

	public void setCountries(Set<String> countries) {
		this.countries = countries;
	}

	public void setAiGroups(List<String> aiGroups) {
		this.aiGroups = aiGroups;
	}

	public List<String> getAiGroupFacets() {
		return aiGroupFacets;
	}

	public List<Integer> getAiGroupIds() {
		return aiGroupIds;
	}

	public void setAiGroupFacets(List<String> aiGroupFacets) {
		this.aiGroupFacets = aiGroupFacets;
	}

	public void setAiGroupIds(List<Integer> aiGroupIds) {
		this.aiGroupIds = aiGroupIds;
	}

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public Set<String> getPlaces() {
		return places;
	}

	public void setPlaces(Set<String> places) {
		this.places = places;
	}
	
}
