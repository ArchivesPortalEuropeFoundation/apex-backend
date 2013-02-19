package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * 
 * @author paul
 *
 */

public interface FindingAidDAO extends GenericDAO<FindingAid, Integer> {
//	public Long countFindingAids(Integer ai, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide);
//	public List<FindingAid> searchFindingAids(String query, Integer ai, Integer pageNumber, Integer pageSize, int option, String sortValue, boolean ascending, Collection<String> fileStates, List<String> statuses, Boolean isLinkedToHoldingsGuide);
//	public Long countSearchFindingAids(String queries, Integer ai, int option, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide);
//	public List<FindingAid> getFindingAids(Integer aiId, Collection<String> fileStates);
//	public List<FindingAid> getFindingAids(Integer aiId, List<Integer> selectedIds, Collection<String> fileStates);
	public List<FindingAid> getFindingAidsNotLinked(Integer aiId);
	public List<FindingAid> getAllFindingAidByHgId(HoldingsGuide holdingsGuide);
	//public void setNumberOfUnits(String eadId, Long numberOfUnits);
	//public void setNumberOfDaos(String eadId, Long numberOfDaos);
	public Long getTotalCountOfUnits();
	public Long getTotalCountOfUnits(Integer aiId);
//	public Long countFindingAidsNotLinkedByArchivalInstitution(Integer aiId, Collection<String> fileStates);
//	public boolean existFindingAidsNotLinkedByArchivalInstitution(Integer aiId);
	public Long getFindingAidsLinkedByHoldingsGuide(HoldingsGuide hgvo);
//	public Long countFindingAids(List<ArchivalInstitution> archivalInstitutions, Collection<String> fileStates);
//	public Long countSearchFindingAidsUnits(String queries, Integer ai, int option, Collection<String> fileStates, List<String> statuses,Boolean isLinkedToHoldingsGuide);
	public List<FindingAid> getFindingAidsByHoldingsGuideId(Integer hgId,boolean published, Integer pageSize, Integer pageNumber);
	public Long countFindingAidsByHoldingsGuideId(Integer hgId,boolean published);
	public Long countFindingAidsIndexedByInternalArchivalInstitutionId(String identifier);
//	List<FindingAid> getFindingAidsNotLinkedByArchivalInstitution(Integer aiId, Integer start, Integer maxResults);
}