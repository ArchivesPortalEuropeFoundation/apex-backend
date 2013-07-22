package eu.apenet.dashboard.infraestructure;

import eu.apenet.persistence.vo.ArchivalInstitution;

public class ArchivalInstitutionNode {

	public ArchivalInstitution ai;
	private Long numFindingAids;
	private Long numHoldingGuides;
	private Long numSourceGuides;
	
	
	public ArchivalInstitution getAi() {
		return ai;
	}


	public void setAi(ArchivalInstitution ai) {
		this.ai = ai;
	}


	public Long getNumFindingAids() {
		return numFindingAids;
	}


	public void setNumFindingAids(Long numFindingAids) {
		this.numFindingAids = numFindingAids;
	}


	public Long getNumHoldingGuides() {
		return numHoldingGuides;
	}


	public void setNumHoldingGuides(Long numHoldingGuides) {
		this.numHoldingGuides = numHoldingGuides;
	}


	public Long getNumSourceGuides() {
		return numSourceGuides;
	}


	public void setNumSourceGuides(Long numSourceGuides) {
		this.numSourceGuides = numSourceGuides;
	}


	public ArchivalInstitutionNode(ArchivalInstitution ai, Long numFindingAids,
			Long numHoldingGuides, Long numSourceGuides) {
		super();
		this.ai = ai;
		this.numFindingAids = numFindingAids;
		this.numHoldingGuides = numHoldingGuides;
		this.numSourceGuides = numSourceGuides;

	}


	
	
	
	
	
}
