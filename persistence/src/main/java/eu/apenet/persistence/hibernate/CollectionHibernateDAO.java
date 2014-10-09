package eu.apenet.persistence.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CollectionDAO;
import eu.apenet.persistence.vo.Collection;
import eu.archivesportaleurope.util.ApeUtil;

public class CollectionHibernateDAO extends AbstractHibernateDAO<Collection, Long> implements CollectionDAO{
	private static final Logger LOGGER = Logger.getLogger(CollectionHibernateDAO.class);
	@Override
	public List<Collection> getCollectionsByUserId(Long liferayUserId,Integer pageNumber,Integer pageSize) {
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
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

}