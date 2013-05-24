package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * 
 * @author eloy
 *
 */

public interface HoldingsGuideDAO extends GenericDAO<HoldingsGuide, Integer> {

	public String getLinkedHoldingsGuideTitleByFindingAidEadid(String eadid,Integer aiId);
}
