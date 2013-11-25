package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.EadSavedSearch;

/**
 * 
 * @author Bastiaan Verhoef
 * 
 */

public interface EadSavedSearchDAO extends GenericDAO<EadSavedSearch, Long> {

	public EadSavedSearch getEadSavedSearch(Long id, Long liferayUserId);
	public Long countEadSavedSearches(Long liferayUserId);
	public List<EadSavedSearch> getEadSavedSearches(Long liferayUserId, int pageNumber, int pageSize);
}
