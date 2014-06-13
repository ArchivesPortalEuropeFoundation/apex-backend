package eu.apenet.dashboard.services.eag.xml.stream.publish;

import java.util.Set;

public class EagPublishData{

	private Set<String> otherNames;
	private Set<String> repositories;
	private Set<String> repositoryTypes;
	private String other;
	private String description;
	private String language;
	
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}




	
}
