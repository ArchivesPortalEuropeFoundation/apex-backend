package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.EadSavedSearchDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.CollectionContent;
import eu.apenet.persistence.vo.EadSavedSearch;

/***
 * class EadSavedSearchJpaDAO
 *
 */
public class EadSavedSearchJpaDAO extends AbstractHibernateDAO<EadSavedSearch, Long> implements EadSavedSearchDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	/***
	 * This function gets a savedSearch object by an id
	 * 
	 * @param id {@link Long} savedSearch object id
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return savedSearch {@link EadSavedSearch}, null if error
	 */
	@Override
	public EadSavedSearch getEadSavedSearch(Long id, Long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getEadSavedSearch\"");
				
		EadSavedSearch eadSavedSearch =  findById(id);
		if (eadSavedSearch != null){
			if (liferayUserId != null && liferayUserId == eadSavedSearch.getLiferayUserId()){
				return eadSavedSearch;
			}else if (eadSavedSearch.isPublicSearch()){
				if (log.isDebugEnabled()) 
					log.debug("Exit \"getEadSavedSearch\"");
				
				return eadSavedSearch;
			}
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getEadSavedSearch\" with null value");
		
		return null;
	}

	/***
	* This function gets the number of savedSearches of the user by user id
	*
	* @param liferayUserId {@link Long} current user id
	* 
	* @return query.getSingleResult() {@link Long} the number of the collections the user has
	*/
	@Override
	public Long countEadSavedSearches(Long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"countEadSavedSearches\"");
		
		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT COUNT(eadSavedSearch) FROM EadSavedSearch eadSavedSearch WHERE eadSavedSearch.liferayUserId = :liferayUserId", Long.class);
		query.setParameter("liferayUserId", liferayUserId);
		if (log.isDebugEnabled()) 
			log.debug("Exit \"countEadSavedSearches\" with null result");
		
		return query.getSingleResult();
	}

	/***
	 * This functyion gets the saved searches by user id ordered by date descending
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return query.getResultList() List {@link EadSavedSearch}
	 */
	@Override
	public List<EadSavedSearch> getEadSavedSearches(Long liferayUserId, int pageNumber, int pageSize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getEadSavedSearches\"");
		
		TypedQuery<EadSavedSearch> query = getEntityManager().createQuery(
				"SELECT eadSavedSearch FROM EadSavedSearch eadSavedSearch WHERE eadSavedSearch.liferayUserId = :liferayUserId ORDER BY eadSavedSearch.modifiedDate DESC" , EadSavedSearch.class);
		query.setParameter("liferayUserId", liferayUserId);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageSize * (pageNumber - 1));
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getEadSavedSearches\"");
		
		return query.getResultList();
	}

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
	@Override
	public Long countEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"countEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		Criteria criteria = getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(id,liferayUserId,null,null);
		criteria.setProjection(Projections.rowCount());
		if (log.isDebugEnabled()) 
			log.debug("Exit \"countEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		return (Long)criteria.uniqueResult();
	}
	
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
	public List<EadSavedSearch> getEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long collectionId,long liferayUserId, int pageNumber, int pageSize){
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		List<EadSavedSearch> searches = null;
		Criteria criteria = getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(collectionId,liferayUserId,pageNumber,pageSize); 
		searches = criteria.list();
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		return searches;
	}

	/***
	 * This function creates que query which is used in<br/>
	 * getEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser and in<br/>
	 * countEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser
	 * 
	 * @param collectionId {@link Long} the collection id.
	 * @param liferayUserId {@link long} current user id
	 * @param pageNumber {@link Integer} the current number of page in the pagination results.
	 * @param pageSize {@link Integer} the number of results results per page
	 * 
	 * @return criteria {@link Criteria} the list of the savedSearches that are not in a collection by collection id  
	 */
	private Criteria getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long collectionId,long liferayUserId, Integer pageNumber, Integer pageSize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "eadSavedSearch");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId>0){
			criteria.add(Restrictions.eq("eadSavedSearch.liferayUserId", liferayUserId));
		}
		if(collectionId!=null && collectionId>0){
			DetachedCriteria collectionSubquery = DetachedCriteria.forClass(CollectionContent.class, "collectionContent");
			collectionSubquery.setProjection(Projections.property("collectionContent.eadSavedSearch.id"));
			collectionSubquery.add(Restrictions.eq("collectionContent.collection.id",collectionId));
			collectionSubquery.add(Restrictions.isNotNull("collectionContent.eadSavedSearch.id"));
			criteria.add(Subqueries.propertyNotIn("eadSavedSearch.id",collectionSubquery));
		}
		if(pageNumber!=null && pageNumber>0){
			criteria.setFirstResult((pageNumber-1)*pageSize);
		}
		if(pageSize!=null && pageSize>0){
			criteria.setMaxResults(pageSize);
		}

		if (log.isDebugEnabled()) 
			log.debug("Exit \"getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		return criteria;
	}

	/***
	 * This function gets a list of the savedSearches the user has
	 * 
	 * @param eadSavedSearchesIds List {@link Long} savedSearches ids
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return searches List {@link EadSavedSearch} criteria.list()
	 */
	@Override
	public List<EadSavedSearch> getEadSavedSearchByIdsAndUserid(List<Long> eadSavedSearchesIds, Long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getEadSavedSearchByIdsAndUserid\"");
		
		List<EadSavedSearch> searches = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "eadSavedSearch");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId>0){
			criteria.add(Restrictions.eq("eadSavedSearch.liferayUserId", liferayUserId));
			if(eadSavedSearchesIds!=null && eadSavedSearchesIds.size()>0){
				Disjunction restrinction = Restrictions.disjunction();
				for(long targetId:eadSavedSearchesIds){
					restrinction.add(Restrictions.eq("eadSavedSearch.id",targetId));
				}
				criteria.add(restrinction);
			}
			searches = criteria.list();
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getEadSavedSearchByIdsAndUserid\"");
		
		return searches;
	}

}
