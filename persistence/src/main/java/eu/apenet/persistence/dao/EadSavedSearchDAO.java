package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.EadSavedSearch;

/**
 * Interface for EadSavedSearchDAO
 * 
 * @author Bastiaan Verhoef
 * 
 */

public interface EadSavedSearchDAO extends GenericDAO<EadSavedSearch, Long> {

	/***
	 * This function gets a savedSearch object by an id
	 * 
	 * @param id {@link Long} savedSearch object id
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return savedSearch {@link EadSavedSearch}, null if error
	 */
	public EadSavedSearch getEadSavedSearch(Long id, Long liferayUserId);
	
	/***
	* This function gets the number of savedSearches of the user by user id
	*
	* @param liferayUserId {@link Long} current user id
	* 
	* @return query.getSingleResult() {@link Long} the number of the collections the user has
	*/
	public Long countEadSavedSearches(Long liferayUserId);
	
	/***
	 * This functyion gets the saved searches by user id ordered by date descending
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return query.getResultList() List {@link EadSavedSearch}
	 */
	public List<EadSavedSearch> getEadSavedSearches(Long liferayUserId, int pageNumber, int pageSize);
	
	/***
	 * This function requests for a criteria and gets the searches there are out of the collection
	 * 
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 *  
	 * @return searches List {@link EadSavedSearch} the number of savedSearches<br/>
	 * there are out of the collection by collection and user
	 */
	public List<EadSavedSearch> getEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long collectionId,long liferayUserId, int pageNumber, int pageSize);
	
	/***
	 * This function gets a list of the savedSearches the user has
	 * 
	 * @param eadSavedSearchesIds List {@link Long} savedSearches ids
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return searches List {@link EadSavedSearch} criteria.list()
	 */
	public List<EadSavedSearch> getEadSavedSearchByIdsAndUserid(List<Long> eadSavedSearchesIds, Long liferayUserId);
	
	/***
	 * This function requests for a criteria and gets the number of<br/>
	 * searches there are out of the collection
	 * 
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 * 
	 * @return (Long)criteria.uniqueResult() {@link Long} the number of savedSearches<br/>
	 * there are out of the collection by collection and user
	 */
	public Long countEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId);
}
