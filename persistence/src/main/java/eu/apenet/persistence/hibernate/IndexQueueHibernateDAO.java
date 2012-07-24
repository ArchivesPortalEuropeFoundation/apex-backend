package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.IndexQueueDAO;
import eu.apenet.persistence.vo.IndexQueue;



public class IndexQueueHibernateDAO extends AbstractHibernateDAO<IndexQueue, Integer> implements IndexQueueDAO{

	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public IndexQueue getIndexQueueByHg(Integer hgId) {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (hgId !=null)
			criteria.add(Restrictions.eq("holdingsGuide.id", hgId.intValue()));
		
		return (IndexQueue)criteria.uniqueResult();
	}

	@Override
	public IndexQueue getIndexQueueByFa(Integer faId) {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (faId !=null)
			criteria = criteria.add(Restrictions.eq("findingAid.id", faId.intValue()));
		
		return (IndexQueue)criteria.uniqueResult();
	}

    @Override
    public IndexQueue getIndexQueueBySg(Integer sgId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (sgId !=null)
            criteria = criteria.add(Restrictions.eq("sourceGuide.id", sgId.intValue()));
        return (IndexQueue)criteria.uniqueResult();
    }

	@Override
	public Integer getMaxIqId() {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.max("indexqueue.iqId"));
		
		return (Integer)criteria.uniqueResult();		
		
	}

	public Integer getMaxPosition() {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.max("position"));
		
		return (Integer)criteria.uniqueResult();		
		
	}

	public Integer getMaxPositionofHgs() {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNull("findingAid.id"));
		criteria = criteria.setProjection(Projections.max("position"));		
		
		return (Integer)criteria.uniqueResult();		
		
	}

	public Integer getMaxPositionofFas() {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNotNull("findingAid.id"));
		criteria = criteria.setProjection(Projections.max("position"));
		return (Integer)criteria.uniqueResult();
	}

    public Integer getLastPositionOfHgs(int aiId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("indexqueue.holdingsGuide", "holdingsGuide");
        criteria = criteria.add(Restrictions.isNotNull("holdingsGuide.id"));
        criteria = criteria.createAlias("holdingsGuide.archivalInstitution", "archivalInstitution");
        criteria = criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria = criteria.setProjection(Projections.max("position"));
        Integer res;
        if((res = (Integer)criteria.uniqueResult()) != null)
            return res;
        return -1;
    }

    public Integer getLastPositionOfFas(int aiId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("indexqueue.findingAid", "findingAid");
        criteria = criteria.add(Restrictions.isNotNull("findingAid.id"));
        criteria = criteria.createAlias("findingAid.archivalInstitution", "archivalInstitution");
        criteria = criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria = criteria.setProjection(Projections.max("position"));
        Integer res;
        if((res = (Integer)criteria.uniqueResult()) != null)
            return res;
        return -1;
    }
	
	@SuppressWarnings("unchecked")
	public List<IndexQueue> getAllOrderedbyId() {
		
		List<IndexQueue> results = new ArrayList<IndexQueue>();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.addOrder(Order.asc("indexqueue.iqId"));
		
		results = criteria.list();
				
		return results ;
	}
	
	@SuppressWarnings("unchecked")
	public List<IndexQueue> getFilesOrderedbyId(String type) {
		
		long startTime = System.currentTimeMillis();
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (type.equals("hg"))
			criteria = criteria.add(Restrictions.isNull("findingAid.id"));
		else
			criteria = criteria.add(Restrictions.isNull("holdingsGuide.id"));
		
		criteria = criteria.addOrder(Order.asc("iqId"));		
				
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + criteria.list().size() + " objects");
		}
		
		return criteria.list() ;
	}
	
	@SuppressWarnings("unchecked")
	public List<IndexQueue> getFilesbeforeDate(Date date, String sortValue, boolean ascending) {
		
		long startTime = System.currentTimeMillis();
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
		criteria = criteria.add(Restrictions.lt("indexqueue.queueDate", date));
		
		if ("iqId".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("indexqueue.iqId"));
			} else {
				criteria.addOrder(Order.desc("indexqueue.iqId"));
			}
		}
		if ("position".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("indexqueue.position"));
			} else {
				criteria.addOrder(Order.desc("indexqueue.position"));
			}
		}
		
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + criteria.list().size() + " objects");
		}
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<IndexQueue> getFilesFromPosition(Integer position){
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
		criteria = criteria.add(Restrictions.ge("indexqueue.position", position));
		
		return criteria.list();
	}
	
	public IndexQueue getFilebyPosition(Integer position){
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);	
		criteria = criteria.add(Restrictions.eq("indexqueue.position", position));
		
		return (IndexQueue) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<IndexQueue> getFas(){
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNull("holdingsGuide.id"));
		criteria = criteria.add(Restrictions.isNull("sourceGuide.id"));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<IndexQueue> getHgs(){
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNull("findingAid.id"));
		criteria = criteria.add(Restrictions.isNull("sourceGuide.id"));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<IndexQueue> getFilesWithErrors() {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "indexqueue");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.isNotNull("errors"));
		
		return criteria.list();
	}
	
	
}
