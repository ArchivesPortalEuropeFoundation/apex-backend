package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;

/**
 * 
 * @author Bastiaan Verhoef
 * 
 */

public interface CLevelDAO extends GenericDAO<CLevel, Long> {

	public Long countChildCLevels(Long parentCLevelId);

	public List<CLevel> findChildCLevels(Long parentCLevelId, Integer orderId, Integer maxNumberOfItems);

	public Long countTopCLevels(Long eadContentId);

	public CLevel getTopClevelByFileId(Integer fileId, Class<? extends Ead> clazz, int orderId);

	public List<CLevel> getTopClevelsByFileId(Integer fileId, Class<? extends Ead> clazz, int firstResult, int maxResult);

	public List<CLevel> findTopCLevels(Long eadContentId, Integer orderId, Integer maxNumberOfItems);

	public List<String> findChildrenLevels(Long parentId);

	@Deprecated
	public List<CLevel> findChilds(Long parentId);

	@Deprecated
	public List<CLevel> findTopCLevels(Long eadContentId);

	@Deprecated
	public List<CLevel> findTopCLevelsOrderUnitid(Long eadContentId);

	@Deprecated
	public List<CLevel> findChildrenOrderUnitId(Long parentId);

	public CLevel findByUnitid(String unitid, Long eadContentId);

	public Long getClIdByUnitid(String unitid, Long eadContentId);

	public Long countPossibleLinkedCLevels(Integer id, Class<? extends Ead> clazz);

	public List<CLevel> getNotLinkedCLevels(Integer id, Class<? extends Ead> clazz);

	public Long countNotLinkedCLevels(Integer id, Class<? extends Ead> clazz);

	public List<CLevel> getClevelsFromSgOrHg(Integer aiId, String eadid);

	public List<CLevel> getCLevelsNodes(Long eadContentId);
	

	public Long countCLevels(Class<? extends Ead> clazz, Integer id);
	public List<CLevel> getCLevels(Class<? extends Ead> clazz, Integer id, int pageNumber, int pageSize);
}
