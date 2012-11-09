package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * 
 * @author eloy
 *
 */

public interface HoldingsGuideDAO extends GenericDAO<HoldingsGuide, Integer> {

	public String getLinkedHoldingsGuideTitleByFindingAidEadid(String eadid,Integer aiId);
}
