package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.QueueItem;

public class QueueItemJpaDAO extends AbstractHibernateDAO<QueueItem, Integer> implements QueueItemDAO {

	private final Logger log = Logger.getLogger(getClass());

	public QueueItem getFirstItem() {
		TypedQuery<QueueItem> query = getEntityManager().createQuery(
				"SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 ORDER BY priority desc, id asc",
				QueueItem.class);
		query.setMaxResults(1);
		List<QueueItem> results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	public List<QueueItem> getFirstItems() {
		TypedQuery<QueueItem> query = getEntityManager().createQuery(
				"SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 ORDER BY priority desc, id asc",
				QueueItem.class);
		query.setMaxResults(50);
		return query.getResultList();

	}

	public List<QueueItem> getDisabledItems() {
		TypedQuery<QueueItem> query = getEntityManager().createQuery(
				"SELECT queueItem FROM QueueItem queueItem WHERE priority = 0 AND errors IS NULL ORDER BY priority desc, id asc",
				QueueItem.class);
		query.setMaxResults(50);
		return query.getResultList();

	}
	@Override
	public Long countItems() {
		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT count(queueItem) FROM QueueItem queueItem WHERE priority > 0",
				Long.class);
		query.setMaxResults(1);
		return query.getResultList().get(0);
	}

	
	@Override
	public Long countItems(int aiId) {
		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT count(queueItem) FROM QueueItem queueItem WHERE priority > 0 AND aiId = :aiId",
				Long.class);
		query.setParameter("aiId", aiId);
		query.setMaxResults(1);
		return query.getResultList().get(0);
	}

	public List<QueueItem> getItemsWithErrors() {
		TypedQuery<QueueItem> query = getEntityManager()
				.createQuery(
						"SELECT queueItem FROM QueueItem queueItem WHERE priority = 0 AND errors IS NOT NULL ORDER BY priority desc, id asc",
						QueueItem.class);
		query.setMaxResults(50);
		return query.getResultList();
	}

	@Override
	public boolean hasItemsWithErrors(int aiId) {
		TypedQuery<Long> query = getEntityManager()
				.createQuery(
						"SELECT queueItem.id FROM QueueItem queueItem WHERE priority = 0 AND errors IS NOT NULL AND aiId = :aiId ORDER BY priority desc, id asc",
						Long.class);
		query.setParameter("aiIid", aiId);
		query.setMaxResults(1);
		return query.getResultList().size() > 0;
	}
	public QueueItem getFirstItem(int aiId) {
		TypedQuery<QueueItem> query = getEntityManager().createQuery(
				"SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 AND aiId = :aiId ORDER BY priority desc, id asc",
				QueueItem.class);
		query.setParameter("aiId", aiId);
		query.setMaxResults(1);
		List<QueueItem> results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

    @Override
    public Long getPositionOfFirstItem(int aiId) {
    	QueueItem firstQueueItem = getFirstItem(aiId);
    	if (firstQueueItem == null){
    		return null;
    	}else {
    		TypedQuery<Long> query = getEntityManager().createQuery(
    				"SELECT count(queueItem) FROM QueueItem queueItem WHERE priority > :priority OR (id < :id AND priority = :priority)",
    				Long.class);
    		query.setParameter("id", firstQueueItem.getId());
    		query.setParameter("priority", firstQueueItem.getPriority());
    		query.setMaxResults(1);
    		return query.getResultList().get(0);   		
    	}
    }

}
