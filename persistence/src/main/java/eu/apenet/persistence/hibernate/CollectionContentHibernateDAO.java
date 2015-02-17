package eu.apenet.persistence.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CollectionContentDAO;
import eu.apenet.persistence.vo.CollectionContent;

/***
 * class CollectionContentHibernateDAO
 *
 */
public class CollectionContentHibernateDAO extends AbstractHibernateDAO<CollectionContent, Long> implements CollectionContentDAO{

	private final Logger log = Logger.getLogger(getClass());
	private static final String BOOKMARK = "bookmark";
	
	/***
	 * Function to get all collection content from a collection ID
	 * 
	 * @param id {@link long} the collection id
	 * 
	 * @return collectionContents List {@link CollectionContent}  the list of the collection content
	 */
	@Override
	public List<CollectionContent> getCollectionContentsByCollectionId(long id) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getCollectionContentsByCollectionId\"");
		
		List<CollectionContent> collectionContents = null;
		if(id>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			collectionContents = criteria.list();
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getCollectionContentsByCollectionId\"");
		
		return collectionContents;
	}
	
	/***
	 * Function to count the number of elements of a collection
	 * 
	 * @param id {@link Long} id of the collection
	 * @param type {@link Boolean} true if id is from a saved search and false if id is from a saved bookmark
	 * 
	 * @return ({@link Long})criteria.uniqueResult()
	 */
	@Override
	public Long countCollectionContentsByCollectionId(Long id, Boolean type) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"countCollectionContentsByCollectionId\"");
		
		Criteria criteria = getCriteriaCollectionContentsByCollectionId(id,type,null,null);
		criteria.setProjection(Projections.rowCount());
		if (log.isDebugEnabled()) 
			log.debug("Exit \"countCollectionContentsByCollectionId\"");
		
		return (Long)criteria.uniqueResult();
	}
	
	/***
	 * Function to get a list of the elements of a collection from a collection id
	 * 
	 * @param id {@link Long} id for a saved bookmark or a saved search
	 * @param collectionContentType {@link Boolean} true if id comes from a saved search and false if id comes from a saved bookmark
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return criteria.list() {@link Criteria}
	 */
	@Override
	public List<CollectionContent> getCollectionContentsByCollectionId(Long id,Boolean isSavedSearch, int pageNumber, int pageSize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getCollectionContentsByCollectionId\"");
		
		Criteria criteria = getCriteriaCollectionContentsByCollectionId(id,isSavedSearch,pageNumber,pageSize);
		return criteria.list();
	}

	/***
	 * Function to get a criteria from an id from a saved bookmark or a saved search with pagination
	 * 
	 * @param id {@link Long} id for a saved bookmark or a saved search
	 * @param isSavedSearch {@link Boolean} true if id comes from a saved search and false if id comes from a saved bookmark
	 * @param pageNumber {@link Integer} the current number of page in the pagination results
	 * @param pageSize {@link Integer} the number of results results per page
	 * 
	 * @return criteria {@link Criteria} to be used as a criteria
	 */
	private Criteria getCriteriaCollectionContentsByCollectionId(Long id,Boolean isSavedSearch, Integer pageNumber, Integer pageSize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getCriteriaCollectionContentsByCollectionId\"");
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(id!=null && id>0){
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			if(isSavedSearch!=null){
				if(isSavedSearch){
					criteria.add(Restrictions.isNotNull("collectionContent.eadSavedSearch.id"));
				}else{
					criteria.add(Restrictions.isNotNull("collectionContent.savedBookmarks.id"));
				}
			}
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getCriteriaCollectionContentsByCollectionId\"");
		
		return criteria;
	}

	/***
	 * Function returns all content that is not in a collection list
	 * 
	 * @param collectionIds List {@link Long} a list of collection content ids
	 * @param id {@link Long} collection content id
	 * 
	 * @return collectionContents List {@link CollectionContent}
	 */
	@Override
	public List<CollectionContent> getAllCollectionContentWithoutIds(List<Long> collectionIds, Long id) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getAllCollectionContentWithoutIds\"");
		
		List<CollectionContent> collectionContents = null;
		if(id!=null && id>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			Iterator<Long> iteratorIds = collectionIds.iterator();
			while(iteratorIds.hasNext()){
				criteria.add(Restrictions.not(Restrictions.eq("collectionContent.id", iteratorIds.next())));
			}
			collectionContents = criteria.list();
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getAllCollectionContentWithoutIds\"");
		
		return collectionContents;
	}

	/***
	 * Function to get all content of a collection from a collection content id.
	 * 
	 * @param table {@link String} "bookmark" when it comes from a saved bookmark and "search" when it comes from a saved search.
	 * @param elemetId {@link String} savedBookmarks id or eadSavedSearch id
	 * 
	 * @return collectionContents List {@link CollectionContent}
	 */
	@Override
	public List<CollectionContent> getCollectionContentByElementId(String table, String elemetId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getCollectionContentByElementId\"");
		
		List<CollectionContent> collectionContents = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent"); //collection
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (table.equals(BOOKMARK))
			criteria.add(Restrictions.eq("collectionContent.savedBookmarks.id", Long.parseLong(elemetId)));
		else
			criteria.add(Restrictions.eq("collectionContent.eadSavedSearch.id", Long.parseLong(elemetId)));
	
		collectionContents = criteria.list();
		
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getCollectionContentByElementId\"");
		
		return collectionContents;
	}

}