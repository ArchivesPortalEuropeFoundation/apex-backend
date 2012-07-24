package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * 
 * @author Bastiaan Verhoef
 *
 */

public interface EadContentDAO extends GenericDAO<EadContent, Long> {
	public EadContent getEadContentByFindingAidId(Integer findingAidId);
	
	public EadContent getEadContentByHoldingsGuideId(Integer holdingsGuideId);
    public EadContent getEadContentBySourceGuideId(Integer sgId);

	public HoldingsGuide getHoldingsGuideByFindingAid(FindingAid findingAid);
    
    public EadContent getEadContentByEadid(String eadid);

    public EadContent getEadContentByFileId(Integer fileId, Class<? extends Ead> clazz);
}

