package eu.apenet.persistence.hibernate;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;



public class CLevelHibernateDAO extends AbstractHibernateDAO<CLevel, Long> implements CLevelDAO{

	private final static Logger LOG = Logger.getLogger(CLevelHibernateDAO.class);
	
	
    public List<CLevel> getTopClevelsByFileId(Integer fileId, Class<? extends Ead> clazz, int firstResult, int maxResult) {
    	String propertyName = "faId";
    	if(clazz.equals(HoldingsGuide.class))
    		propertyName = "hgId";
        else
        	propertyName = "sgId";
		TypedQuery<CLevel> query = getEntityManager().createQuery("SELECT clevel FROM CLevel clevel WHERE clevel.eadContent." + propertyName + " = :fileId AND clevel.parentClId IS NULL ORDER BY clevel.orderId ASC", CLevel.class);
		query.setParameter("fileId", fileId);
		query.setMaxResults(maxResult);
		query.setFirstResult(firstResult);
		return query.getResultList();   
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<CLevel> findChilds(Long parentId) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createChildCLevelsCriteria(parentId);
		criteria.addOrder(Order.asc("clevel.orderId"));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;		
	}

    @Override
    public List<CLevel> findChildrenOrderUnitId(Long parentId){
        long startTime = System.currentTimeMillis();
		Criteria criteria = createChildCLevelsCriteria(parentId);
        criteria.addOrder(Order.asc("clevel.unitid"));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled())
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		return results;
    }

	@Override
	public Long countChildCLevels(Long parentCLevelId) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createChildCLevelsCriteria(parentCLevelId);
		criteria.setProjection(Projections.countDistinct("clevel.clId"));
		Object object = criteria.list().get(0);

		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to countChildCLevels");
		}
		if (object instanceof Integer) {
			return ((Integer) object).longValue();
		} else if (object instanceof Long) {
			return (Long) object;
		}
		return (Long) new Long(0);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CLevel> findByHrefEadid(String eadid) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("clevel.hrefEadid", eadid));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CLevel> findChildCLevels(Long parentCLevelId, Integer orderId, Integer maxNumberOfItems) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createChildCLevelsCriteria(parentCLevelId);
		criteria.add(Restrictions.ge("clevel.orderId", orderId));
		criteria.setMaxResults(maxNumberOfItems);
		criteria.addOrder(Order.asc("clevel.orderId"));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;	
	}

	@Override
	public Long countTopCLevels(Long eadContentId) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createTopCLevelsCriteria(eadContentId);
		criteria.setProjection(Projections.countDistinct("clevel.clId"));
		Object object = criteria.list().get(0);

		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to countTopClevels");
		}
		if (object instanceof Integer) {
			return ((Integer) object).longValue();
		} else if (object instanceof Long) {
			return (Long) object;
		}
		return (Long) new Long(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CLevel> findTopCLevels(Long eadContentId, Integer orderId, Integer maxNumberOfItems) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createTopCLevelsCriteria(eadContentId);
		criteria.add(Restrictions.ge("clevel.orderId", orderId));
		criteria.setMaxResults(maxNumberOfItems);
		criteria.addOrder(Order.asc("clevel.orderId"));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;	
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CLevel> findTopCLevels(Long eadContentId) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createTopCLevelsCriteria(eadContentId);
		criteria.addOrder(Order.asc("clevel.orderId"));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

    @SuppressWarnings("unchecked")
    public List<String> findChildrenLevels(Long parentId){
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel")
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .setProjection(
                        Projections.distinct(
                                Projections.projectionList().add(Projections.property("level"))
                        )
                )
                .add(Restrictions.eq("clevel.parentClId", parentId));
        return criteria.list();
    }

	private Criteria createTopCLevelsCriteria(Long eadContentId){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.isNull("clevel.parentClId"));
		criteria.add(Restrictions.eq("clevel.ecId", eadContentId));
		
		return criteria;
	}
	private Criteria createChildCLevelsCriteria(Long parentId){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("clevel.parentClId", parentId));
		return criteria;
	}
	@Override
	public CLevel findByHrefEadid(FindingAid findingAid) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("clevel.hrefEadid", findingAid.getEadid()));
		return (CLevel)criteria.uniqueResult();
	}

    @Override
    public List<CLevel> findTopCLevelsOrderUnitid(Long eadContentId) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createTopCLevelsCriteria(eadContentId);
		criteria.addOrder(Order.asc("clevel.unitid"));
		List<CLevel> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled())
			LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		return results;
	}

    @Override
    public CLevel findByUnitid(String unitid, Long eadContentId){
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("clevel.ecId", eadContentId));
        criteria.add(Restrictions.eq("clevel.unitid", unitid));
        try {
            return (CLevel)criteria.uniqueResult();
        } catch (Exception e){
            LOG.error("Error with unitid " + unitid + ", there are more than one, we return the first one");
            return (CLevel)criteria.list().get(0);
        }
    }

    public Long getClIdByUnitid(String unitid, Long eadContentId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("clId"))));
        criteria.add(Restrictions.eq("clevel.ecId", eadContentId));
        criteria.add(Restrictions.eq("clevel.unitid", unitid));
        try {
            return (Long)criteria.uniqueResult();
        } catch (Exception e){
            LOG.error("Error with unitid " + unitid + ", there are more than one, we return the first one");
            return (Long)criteria.list().get(0);
        }
    }

    
	@Override
	public Long countCLevelsByEadId(Integer hgId,Class<? extends Ead> clazz){
		Criteria criteria = createCriteriaCLevelsByEadId(hgId,clazz,false);
		criteria.setProjection(Projections.count("cLevel.ecId"));
		return (Long)criteria.uniqueResult();
	}
	
	
	
	private Criteria createCriteriaCLevelsByEadId(Integer id,Class<? extends Ead> clazz,boolean hrefIsNull) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"cLevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		DetachedCriteria subQuery = DetachedCriteria.forClass(EadContent.class,"eadContent");
			if(clazz.equals(HoldingsGuide.class)){
				subQuery.add(Restrictions.eq("eadContent.holdingsGuide.id", id));
			}else if(clazz.equals(FindingAid.class)){
				subQuery.add(Restrictions.eq("eadContent.findingAid.id", id));
			}else if(clazz.equals(SourceGuide.class)){
				subQuery.add(Restrictions.eq("eadContent.sourceGuide.id", id));
			}
			subQuery.setProjection(Property.forName("eadContent.ecId"));
		criteria.add(Subqueries.propertyIn("cLevel.ecId", subQuery));
		if(hrefIsNull){
			criteria.add(Restrictions.isNotNull("cLevel.hrefEadid"));
		}
		return criteria;
	}

	/**
	 * Returns a List<CLevel> which they are not uploaded into an Institution
	 * 
	 * @param hgId
	 * @return List<CLevel>
	 */
	@Override
	public List<CLevel> getCLevelsOutOfSystemByHoldingsGuideId(Integer hgId,Integer aiId){
		Criteria criteria = criteriaCriteriaCLevelsOutOfSystemByHoldingsGuideId(hgId,aiId);
		return (List<CLevel>)criteria.list();
	}
	
	/**
	 * Returns total number of c_level (FindingAids) included 
	 * into a Holdings Guide 
	 * 
	 * @param hgId
	 * @return List<CLevel>
	 */
	@Override
	public Long countTotalCLevelsByHoldingsGuideId(Integer hgId){
		Criteria criteria = createCriteriaCLevelsByEadId(hgId,HoldingsGuide.class,true);
		criteria.setProjection(Projections.count("cLevel.ecId"));
		return (Long)criteria.uniqueResult();
	}
	
	private Criteria criteriaCriteriaCLevelsOutOfSystemByHoldingsGuideId(Integer hgId, Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"cLevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		DetachedCriteria subQuery = DetachedCriteria.forClass(EadContent.class,"eadContent");
			subQuery.setProjection(Property.forName("eadContent.ecId"));
			subQuery.add(Restrictions.eq("eadContent.holdingsGuide.id", hgId));
		criteria.add(Subqueries.propertyIn("cLevel.ecId", subQuery));
		DetachedCriteria subQuery2 = DetachedCriteria.forClass(FindingAid.class,"findingAid");
			subQuery2.setProjection(Property.forName("findingAid.eadid"));
			subQuery2.add(Restrictions.eq("findingAid.archivalInstitution.aiId",aiId));
		criteria.add(Subqueries.propertyNotIn("cLevel.hrefEadid",subQuery2));
		criteria.add(Restrictions.isNotNull("hrefEadid"));
		return criteria;
	}

	/**
	 * Returns a list of CLevels (FindingAid) into a Holdings Guide indexed
	 */
	@Override
	public List<CLevel> getCLevelsWithinSystemByHoldingsGuideId(Integer hgId) {
		Criteria criteria = createCriteriaCLevelsByEadId(hgId,HoldingsGuide.class,true);
		return (List<CLevel>)criteria.list();
	}

    @Override
    public void setEcIdHql(String unitid, Long eadContentId, Long oldEadContentId){
        String hqlQuery = "update CLevel set ecId = :ecId where (ecId = :oldEcId and unitid = :unitid)";
        Query query = getSession().createQuery(hqlQuery);
        query.setLong("ecId", eadContentId);
        query.setLong("oldEcId", oldEadContentId);
        query.setString("unitid", unitid);
        query.executeUpdate();
    }

    @Override
    public void setEcIdHql(Long clId, Long eadContentId, Long oldEadContentId) {
        String hqlQuery = "update CLevel set ecId = :ecId where (clId = :clId and ecId = :oldEcId)";
        Query query = getSession().createQuery(hqlQuery);
        query.setLong("ecId", eadContentId);
        query.setLong("clId", clId);
        query.setLong("oldEcId", oldEadContentId);
        query.executeUpdate();
    }

    @Override
    public void setEcIdHql(List<Long> clIds, Long eadContentId, Long oldEadContentId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" where (ecId = :oldEcId) and (");
        for(int i = 0; i < clIds.size(); i++) {
            if (i != 0)
                buffer.append(" OR ");
            buffer.append("clId = " + clIds.get(i));
        }

        buffer.append(")");
        String hqlQuery = "update CLevel set ecId = :ecId" + buffer.toString();
        Query query = getSession().createQuery(hqlQuery);
        query.setLong("ecId", eadContentId);
        query.setLong("oldEcId", oldEadContentId);

        query.executeUpdate();
    }
}