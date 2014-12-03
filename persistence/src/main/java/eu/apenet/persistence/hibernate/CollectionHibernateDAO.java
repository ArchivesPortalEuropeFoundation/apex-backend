package eu.apenet.persistence.hibernate;

import java.util.Iterator;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CollectionDAO;
import eu.apenet.persistence.vo.Collection;
import eu.apenet.persistence.vo.CollectionContent;
import eu.apenet.persistence.vo.EadSavedSearch;
import eu.archivesportaleurope.util.ApeUtil;

public class CollectionHibernateDAO extends AbstractHibernateDAO<Collection, Long> implements CollectionDAO{
	private static final Logger LOGGER = Logger.getLogger(CollectionHibernateDAO.class);
	private static final String ORDER = "modified_date";
	
	@Override
	public List<Collection> getCollectionsByUserId(Long liferayUserId,Integer pageNumber,Integer pageSize, String sortValue,
			boolean ascending) {
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions. eq("collection.liferayUserId", liferayUserId));
				
				if (sortValue != null && sortValue != "none") {
					if (ascending) {
						criteria.addOrder(Order.asc(sortValue));
					} else {
						criteria.addOrder(Order.desc(sortValue));
					}
				}
				
			}
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
			return criteria.list();
		}
		return null;
	}
	
	
	@Override
	public List<Collection> getCollectionsByIdAndUserId(Long liferayUserId, String table, String elemetId) {
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection"); //collection
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions. eq("collection.liferayUserId", liferayUserId));
				
				if (table.equals("Bookmark"))
					criteria.add(Restrictions.not(Restrictions.eq("collection.collectionContents.id_bookmarks.id", elemetId)));
				else
					criteria.add(Restrictions.not(Restrictions.eq("collection.collectionContents.id_search.id", elemetId)));

			}

			return criteria.list();
		}
		return null;
	}

	@Override
	public Long countCollectionsByUserId(Long liferayUserId) {
		long results = 0;
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.setProjection(Projections.count("collection.id"));
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
			results = (Long)criteria.uniqueResult();
		}
		return results;
	}

	@Override
	public Collection getCollectionByIdAndUserId(Long id, Long liferayUserId) {
		Collection collection = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId!=null && liferayUserId>0){
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
			if(id!=null && id>0){
				criteria.add(Restrictions.eq("collection.id", id));
				collection = (Collection)criteria.uniqueResult();
			}
		}
		return collection;
	}

	@Override
	public Collection getCollectionById(Long id) {
		Collection collection = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(id!=null && id>0){
			criteria.add(Restrictions.eq("collection.id", id));
			collection = (Collection)criteria.uniqueResult();
		}
		return collection;
	}
		
	@Override
	public List<Collection> getCollectionByName(Long liferayUserId, String title, int pageSize) {
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
				criteria.addOrder(Order.desc("modified_date"));
				criteria.setMaxResults(pageSize);
				criteria.add(Restrictions.ilike("collection.title",title, MatchMode.ANYWHERE));
			}
			return criteria.list();
		}
		return null;
	}
	
	public List<Collection> getUserCollectionsWithoutIds(Long liferayUserId, List<Long> ids, Integer pageNumber, Integer pageSize){
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
			criteria.addOrder(Order.desc("modified_date"));	
 			Iterator<Long> itcontents = ids.iterator();
			while(itcontents.hasNext()){
				Long collectionId = itcontents.next();
				criteria.add(Restrictions.not(Restrictions.eq("collection.id", collectionId)));
			}
			
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
			
			return criteria.list();
		}
		return null;
	}
	
	public Long countUserCollectionsWithoutIds(Long liferayUserId, List<Long> ids){
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
 			Iterator<Long> itcontents = ids.iterator();
			while(itcontents.hasNext()){
				Long collectionId = itcontents.next();
				criteria.add(Restrictions.not(Restrictions.eq("collection.id", collectionId)));
			}
			criteria.setProjection(Projections.rowCount());
			return (Long)criteria.uniqueResult();
		}
		return null;
	}
}

