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
	public List<HoldingsGuide> getHoldingsGuides(String hgEadid, String hgTitle, String uploadMethod,
			Collection<String> fileStates, Integer aiId, String sort, boolean ascending);

	
	
	public Long countHoldingsGuideByArchivalInstitution(Integer ai, Collection<String> fileStates);
	public List<HoldingsGuide> getHoldingsGuidePage(String faEadid, String faTitle, String uploadMethod,
			Collection<String> fileStates, Integer aiId, String sort, boolean ascending, Integer pageNumber, Integer pageSize);
	public List<HoldingsGuide> getMatchHoldingsGuides(String query, Integer ai,Integer page, Integer option, String orderBy, Boolean orderDecreasing);
	public Long getMatchCountHoldingsGuides(String query, Integer ai,Integer page, Integer option);
	public List<HoldingsGuide> getHoldingsGuides(Integer aiId);
	public Long countHoldingsGuide(Integer ai, Collection<String> fileStates);

	//public void setNumberOfUnits(String eadId, Long numberOfUnits);
	//public void setNumberOfDaos(String eadId, Long numberOfDaos);
	public Long getTotalCountOfUnits();
	public Long countHoldingsGuide(List<ArchivalInstitution> archivalInstitutions, Collection<String> fileStates);
	public String getLinkedHoldingsGuideTitleByFindingAidEadid(String eadid,Integer aiId);
	public List<HoldingsGuide> getHoldingsGuidesByStateAndArchivalInstitution(Integer ai, Collection<String> fileStates);
}
