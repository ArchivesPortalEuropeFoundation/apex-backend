package eu.apenet.dashboard.services.eag.publish;

import java.util.Set;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;

public class EagSolrObject {
	private String language;
	private Set<String> names;
	private Set<String> places;
	private Set<String> functions;
	private Set<String> mandates;	
	private String description;
	private Set<String> occupations;
	private String dateDescription;
	private String fromDate;
	private String toDate;
	private boolean fromDateExist;
	private boolean toDateExist;
	private Set<String> entityIds;
	private String recordId;
	private String other;
	private String entityType;
	private Integer numberOfArchivalMaterialRelations;
	private Integer numberOfNameRelations;
	private Integer numberOfInstitutionsRelations;
	
	
	private ArchivalInstitution archivalInstitution;
	public EagSolrObject ( ArchivalInstitution archivalInstitution){
		this.archivalInstitution = archivalInstitution;
	}
	public ArchivalInstitution getArchivalInstitution() {
		return archivalInstitution;
	}

	


}
