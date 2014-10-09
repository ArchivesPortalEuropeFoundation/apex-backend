package eu.apenet.persistence.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import eu.apenet.persistence.dao.CollectionContentDAO;
import eu.apenet.persistence.vo.CollectionContent;
import eu.archivesportaleurope.util.ApeUtil;

public class CollectionContentHibernateDAO extends AbstractHibernateDAO<CollectionContent, Long> implements CollectionContentDAO{
    
	private static final Logger LOGGER = Logger.getLogger(CollectionContentHibernateDAO.class);

	@Override
	public List<CollectionContent> getCollectionContentsByCollectionId(long id) {
		List<CollectionContent> collectionContents = null;
		if(id>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			collectionContents = criteria.list();
		}
		return collectionContents;
	}

	@Override
	public List<CollectionContent> getAllCollectionContentWithoutIds(List<Long> collectionIds, Long id) {
		List<CollectionContent> collectionContents = null;
		if(id>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			Iterator<Long> iteratorIds = collectionIds.iterator();
			while(iteratorIds.hasNext()){
				criteria.add(Restrictions.not(Restrictions.eq("collectionContent.id", iteratorIds.next())));
			}
			collectionContents = criteria.list();
		}
		return collectionContents;
	}
}