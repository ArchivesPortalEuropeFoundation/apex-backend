package eu.apenet.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;

/**
 * 
 * @author Bastiaan Verhoef
 *
 */

public interface CLevelDAO extends GenericDAO<CLevel, Long> {
	

	public Long countChildCLevels(Long parentCLevelId);
	
	public List<CLevel> findChildCLevels(Long parentCLevelId, Integer orderId, Integer maxNumberOfItems);

	public Long countTopCLevels(Long eadContentId);
	
	public List<CLevel> findTopCLevels(Long eadContentId, Integer orderId, Integer maxNumberOfItems);

	public List<String> findChildrenLevels(Long parentId);
	
	@Deprecated
	public List<CLevel> findChilds(Long parentId);

	@Deprecated
	public  List<CLevel> findTopCLevels(Long eadContentId);

	public CLevel findByHrefEadid(FindingAid findingAid);

    @Deprecated
    public List<CLevel> findTopCLevelsOrderUnitid(Long eadContentId);

    @Deprecated
	public List<CLevel> findChildrenOrderUnitId(Long parentId);

    public CLevel findByUnitid(String unitid, Long eadContentId);

    public Long getClIdByUnitid(String unitid, Long eadContentId);

	public List<CLevel> findByHrefEadid(String eadid);
	
	public List<CLevel> getCLevelsWithinSystemByHoldingsGuideId(Integer hgId);
	
	public List<CLevel> getCLevelsOutOfSystemByHoldingsGuideId(Integer hgId, Integer aiId);
	
	public Long countCLevelsByEadId(Integer hgId,Class<? extends Ead> clazz);
	
	public Long countTotalCLevelsByHoldingsGuideId(Integer hgId);

    public void setEcIdHql(String unitid, Long eadContentId, Long oldEadContentId);
    public void setEcIdHql(Long clId, Long eadContentId, Long oldEadContentId);
    public void setEcIdHql(List<Long> clIds, Long eadContentId, Long oldEadContentId);
}

