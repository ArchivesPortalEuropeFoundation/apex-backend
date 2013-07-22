package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;

/**
 * 
 * @author Bastiaan Verhoef
 *
 */

public interface EadContentDAO extends GenericDAO<EadContent, Long> {
	public EadContent getEadContentByFindingAidId(Integer findingAidId);
	
	public EadContent getEadContentByHoldingsGuideId(Integer holdingsGuideId);
    public EadContent getEadContentBySourceGuideId(Integer sgId);

    public EadContent getEadContentByEadid(String eadid);

    public EadContent getEadContentByFileId(Integer fileId, Class<? extends Ead> clazz);
    public List<EadContent> getEadContentsByFileId(Integer fileId, Class<? extends Ead> clazz);
    
}

