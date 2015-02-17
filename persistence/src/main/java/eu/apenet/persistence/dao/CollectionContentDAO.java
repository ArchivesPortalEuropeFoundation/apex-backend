package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Collection;
import eu.apenet.persistence.vo.CollectionContent;

/***
 * interface CollectionContentDAO
 *
 */
public interface CollectionContentDAO extends GenericDAO<CollectionContent, Long>{

	/***
	 * Function to get all collection content from a collection ID
	 * 
	 * @param id {@link long} the collection id
	 * 
	 * @return collectionContents List {@link CollectionContent}  the list of the collection content
	 */
	public List<CollectionContent> getCollectionContentsByCollectionId(long id);
	
	/***
	 * Function to get a list of the elements of a collection from a collection id
	 * 
	 * @param id {@link Long} id for a saved bookmark or a saved search
	 * @param collectionContentType {@link Boolean} true if id comes from a saved search and false if id comes from a saved bookmark
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return criteria.list()
	 */
	public List<CollectionContent> getCollectionContentsByCollectionId(Long id,Boolean collectionContentType,int pageNumber,int pageSize);
	
	/***
	 * Function returns all content that is not in a collection list
	 * 
	 * @param collectionIds List {@link Long} a list of collection content ids
	 * @param id {@link Long} collection content id
	 * 
	 * @return collectionContents List {@link CollectionContent}
	 */
	public List<CollectionContent> getAllCollectionContentWithoutIds(List<Long> collectionIds, Long id);
	
	/***
	 * Function to count the number of elements of a collection
	 * 
	 * @param id {@link Long} id of the collection
	 * @param type {@link Boolean} true if id is from a saved search and false if id is from a saved bookmark
	 * 
	 * @return ({@link Long})criteria.uniqueResult()
	 */
	public Long countCollectionContentsByCollectionId(Long id, Boolean type);
	
	/***
	 * Function to get all content of a collection from a collection content id
	 * 
	 * @param table {@link String} "bookmark" when it comes from a saved bookmark and "search" when it comes from a saved search
	 * @param elemetId {@link String} savedBookmarks id or eadSavedSearch id
	 * 
	 * @return collectionContents List {@link CollectionContent}
	 */
	public List<CollectionContent> getCollectionContentByElementId(String table, String elemetId);
}
