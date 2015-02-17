package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Collection;
import eu.apenet.persistence.vo.CollectionContent;

/***
 * interface CollectionDAO
 *
 */
public interface CollectionDAO extends GenericDAO<Collection, Long>{
	/***
	 * Function that gets a list with the user collections by user id
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param pageNumber {@link Integer} the current number of page in the pagination results
	 * @param pageSize {@link Integer} the number of results results per page
	 * @param sortValue {@link String} By default order descendant by date (none), if it has values it is the number of the field to sort
	 * @param ascending {@link boolean} true for ascending or false for descending
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	public List<Collection> getCollectionsByUserId(Long liferayUserId,Integer pageNumber, Integer pagesize, String field, boolean showDesc);
	
	/***
	 * Function gets a single collection by collection id
	 * 
	 * @param id {@link Long} collection id
	 * 
	 * @return collection {@linkCollection} single collection
	 */
	public Collection getCollectionById(Long id);
	
	/***
	 * Function gets a list of collections by a pattern in the name
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param title {@link String} the pattern to search in the title of the collection
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	public List<Collection>  getCollectionByName (Long liferayUserId, String name, int pageSize);
	
	/***
	 * Function counts the number of the collections by user
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return results {@link Long} number of collections, 0 if there is no results
	 */
	public Long countCollectionsByUserId(Long liferayUserId);
	
	/***
	 * Function gets a single collection by collection id and user id
	 * 
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return collection {@link Collection} single collection
	 */
	public Collection getCollectionByIdAndUserId(Long id, Long liferayUserId);
	
	/***
	 * Function that gets a bookmarks or searches list by user id and element id
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param table {@link String} Bookmark or Search
	 * @param elemetId {@link String} the id of the bookmark or the search
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	public List<Collection> getCollectionsByIdAndUserId(Long liferayUserId, String table, String elemetId);
	
	/***
	 * Function returns a list of user collections that NOT contains the elements of the list
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param ids List {@link Long} the list of collection ids
	 * @param pageNumber {@link Integer} the current number of page in the pagination results
	 * @param pageSize {@link Integer} the number of results results per page
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	public List<Collection> getUserCollectionsWithoutIds(Long liferayUserId, List<Long> ids, Integer pageNumber, Integer pageSize);
	
	/***
	 * Function returns the number of user collections that NOT contains the elements of the list
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param ids List {@link Long} the list of collection ids
	 * 
	 * @return ({@link Long})criteria.uniqueResult() if there is a valid user, null if not
	 */
	public Long countUserCollectionsWithoutIds(Long liferayUserId, List<Long> ids);
}
