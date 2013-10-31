package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * 
 * @author eloy
 *
 */

public interface HoldingsGuideDAO extends GenericDAO<HoldingsGuide, Integer> {
	
	public List<HoldingsGuide> getHoldingsGuidesByArchivalInstitutionId(Integer aiId);
	public List<HoldingsGuide> getHoldingsGuidesByArchivalInstitutionId(Integer aiId,Boolean indexed);
}
