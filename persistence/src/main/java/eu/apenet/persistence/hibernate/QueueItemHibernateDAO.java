package eu.apenet.persistence.hibernate;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.vo.QueueItem;



public class QueueItemHibernateDAO extends AbstractHibernateDAO<QueueItem, Integer> implements QueueItemDAO{

	private final Logger log = Logger.getLogger(getClass());
	

	public List<QueueItem> getFirstItems(){
		TypedQuery<QueueItem> query = getEntityManager().createQuery(
		        "SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 ORDER BY priority desc, id asc", QueueItem.class);
		query.setMaxResults(10);
		return query.getResultList();
	}
	


	@SuppressWarnings("unchecked")
	public List<QueueItem> getFilesWithErrors() {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "queueItem");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNotNull("errors"));
		
		return criteria.list();
	}
	
	
}
