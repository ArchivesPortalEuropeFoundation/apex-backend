package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.EadSavedSearchDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.CollectionContent;
import eu.apenet.persistence.vo.EadSavedSearch;

public class EadSavedSearchJpaDAO extends AbstractHibernateDAO<EadSavedSearch, Long> implements EadSavedSearchDAO {


	@Override
	public EadSavedSearch getEadSavedSearch(Long id, Long liferayUserId) {
		EadSavedSearch eadSavedSearch =  findById(id);
		if (eadSavedSearch != null){
			if (liferayUserId != null && liferayUserId == eadSavedSearch.getLiferayUserId()){
				return eadSavedSearch;
			}else if (eadSavedSearch.isPublicSearch()){
				return eadSavedSearch;
			}
		}
		return null;
	}

	@Override
	public Long countEadSavedSearches(Long liferayUserId) {
		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT COUNT(eadSavedSearch) FROM EadSavedSearch eadSavedSearch WHERE eadSavedSearch.liferayUserId = :liferayUserId", Long.class);
		query.setParameter("liferayUserId", liferayUserId);
		return query.getSingleResult();
	}

	@Override
	public List<EadSavedSearch> getEadSavedSearches(Long liferayUserId, int pageNumber, int pageSize) {
		TypedQuery<EadSavedSearch> query = getEntityManager().createQuery(
				"SELECT eadSavedSearch FROM EadSavedSearch eadSavedSearch WHERE eadSavedSearch.liferayUserId = :liferayUserId ORDER BY eadSavedSearch.modifiedDate DESC" , EadSavedSearch.class);
		query.setParameter("liferayUserId", liferayUserId);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageSize * (pageNumber - 1));
		return query.getResultList();
	}
	
	@Override
	public Long countEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId) {
		Criteria criteria = getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(id,liferayUserId,null,null);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	public List<EadSavedSearch> getEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long collectionId,long liferayUserId, int pageNumber, int pageSize){
		List<EadSavedSearch> searches = null;
		Criteria criteria = getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(collectionId,liferayUserId,pageNumber,pageSize); 
		searches = criteria.list();
		return searches;
	}

	private Criteria getCriteriaEadSavedSearchOutOfCollectionByCollectionIdAndLiferayUser(Long collectionId,long liferayUserId, Integer pageNumber, Integer pageSize) {
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
		return criteria;
	}

	@Override
	public List<EadSavedSearch> getEadSavedSearchByIdsAndUserid(List<Long> eadSavedSearchesIds, Long liferayUserId) {
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
		return searches;
	}

	

}
