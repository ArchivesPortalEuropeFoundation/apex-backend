package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * 
 * @author paul
 *
 */

public interface FindingAidDAO extends GenericDAO<FindingAid, Integer> {
	public List<FindingAid> getFindingAidsNotLinked(Integer aiId);
	public List<FindingAid> getAllFindingAidByHgId(HoldingsGuide holdingsGuide);
	public Long getTotalCountOfUnits();
	public Long getTotalCountOfUnits(Integer aiId);
	public List<FindingAid> getFindingAidsByHoldingsGuideId(Integer hgId,boolean published, Integer pageSize, Integer pageNumber);
}