package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.QueueItem;

public class QueueItemHibernateDAO extends AbstractHibernateDAO<QueueItem, Integer> implements QueueItemDAO {

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
		query.setMaxResults(10);
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

	@SuppressWarnings("unchecked")
	public List<QueueItem> getItemsWithErrors() {
		TypedQuery<QueueItem> query = getEntityManager()
				.createQuery(
						"SELECT queueItem FROM QueueItem queueItem WHERE priority = 0 AND errors IS NOT NULL ORDER BY priority desc, id asc",
						QueueItem.class);
		return query.getResultList();
	}

	@Override
	public boolean hasItemsWithErrors(int aiId) {
		// TODO: should be a query
		List<QueueItem> iqList = getItemsWithErrors();

		if (iqList.size() == 0)
			return false;
		else {
			for (int i = 0; i < iqList.size(); i++) {
				if ((iqList.get(i).getFindingAid() != null)
						&& (iqList.get(i).getFindingAid().getArchivalInstitution().getAiId() == aiId))
					return true;
				if ((iqList.get(i).getHoldingsGuide() != null)
						&& (iqList.get(i).getHoldingsGuide().getArchivalInstitution().getAiId() == aiId))
					return true;
				if ((iqList.get(i).getSourceGuide() != null)
						&& (iqList.get(i).getSourceGuide().getArchivalInstitution().getAiId() == aiId))
					return true;
			}
		}
		return false;
	}

}
