package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.FindingAid;

/**
 * 
 * @author paul
 *
 */

public interface FindingAidDAO extends GenericDAO<FindingAid, Integer> {
	public Long getTotalCountOfUnits();
	public Long getTotalCountOfUnits(Integer aiId);
}