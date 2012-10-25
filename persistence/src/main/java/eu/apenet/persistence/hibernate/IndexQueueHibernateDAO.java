package eu.apenet.persistence.hibernate;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.IndexQueueDAO;
import eu.apenet.persistence.vo.IndexQueue;



public class IndexQueueHibernateDAO extends AbstractHibernateDAO<IndexQueue, Integer> implements IndexQueueDAO{

	private final Logger log = Logger.getLogger(getClass());
	

	public List<IndexQueue> getFirstItems(){
		TypedQuery<IndexQueue> query = getEntityManager().createQuery(
		        "SELECT indexQueue FROM IndexQueue indexQueue ORDER BY indexQueue.id", IndexQueue.class);
		query.setMaxResults(10);
		return query.getResultList();
	}
	


	@SuppressWarnings("unchecked")
	public List<IndexQueue> getFilesWithErrors() {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNotNull("errors"));
		
		return criteria.list();
	}
	
	
}
