package eu.apenet.persistence.hibernate;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class CLevelHibernateDAO extends AbstractHibernateDAO<CLevel, Long> implements CLevelDAO {

	private final static Logger LOG = Logger.getLogger(CLevelHibernateDAO.class);

	public List<CLevel> getTopClevelsByFileId(Integer fileId, Class<? extends Ead> clazz, int firstResult, int maxResult) {
		String propertyName = "faId";
		if (clazz.equals(HoldingsGuide.class))
			propertyName = "hgId";
		else
			propertyName = "sgId";
		TypedQuery<CLevel> query = getEntityManager().createQuery(
				"SELECT clevel FROM CLevel clevel WHERE clevel.eadContent." + propertyName
						+ " = :fileId AND clevel.parentClId IS NULL ORDER BY clevel.orderId ASC", CLevel.class);
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
	public List<CLevel> findChildrenOrderUnitId(Long parentId) {
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
	public List<String> findChildrenLevels(Long parentId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("level"))))
				.add(Restrictions.eq("clevel.parentClId", parentId));
		return criteria.list();
	}

	private Criteria createTopCLevelsCriteria(Long eadContentId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.isNull("clevel.parentClId"));
		criteria.add(Restrictions.eq("clevel.ecId", eadContentId));

		return criteria;
	}

	private Criteria createChildCLevelsCriteria(Long parentId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("clevel.parentClId", parentId));
		return criteria;
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
	public CLevel findByUnitid(String unitid, Long eadContentId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("clevel.ecId", eadContentId));
		criteria.add(Restrictions.eq("clevel.unitid", unitid));
		try {
			return (CLevel) criteria.uniqueResult();
		} catch (Exception e) {
			LOG.error("Error with unitid " + unitid + ", there are more than one, we return the first one");
			return (CLevel) criteria.list().get(0);
		}
	}

	public Long getClIdByUnitid(String unitid, Long eadContentId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setProjection(
				Projections.distinct(Projections.projectionList().add(Projections.property("clId"))));
		criteria.add(Restrictions.eq("clevel.ecId", eadContentId));
		criteria.add(Restrictions.eq("clevel.unitid", unitid));
		try {
			return (Long) criteria.uniqueResult();
		} catch (Exception e) {
			LOG.error("Error with unitid " + unitid + ", there are more than one, we return the first one");
			return (Long) criteria.list().get(0);
		}
	}
	@Override
	public Long countPossibleLinkedCLevels(Integer id, Class<? extends Ead> clazz) {
		String jpaQuery = "SELECT count(clevel)" + buildPossibleLinkedCLevels(id, clazz);
		TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);		
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	private String buildPossibleLinkedCLevels(Integer id, Class<? extends Ead> clazz) {
		String varName = "hgId";
		if (SourceGuide.class.equals(clazz)){
			varName = "sgId";
		}
		return " FROM CLevel clevel JOIN clevel.eadContent eadContent WHERE eadContent." + varName + " = :id AND clevel.hrefEadid IS NOT NULL";
	}


	@Override
	public List<CLevel> getNotLinkedCLevels(Integer id, Class<? extends Ead> clazz) {
		String jpaQuery = "SELECT clevel" + buildNotLinkedCLevelsFromQuery(id, clazz);
		TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
		query.setParameter("id", id);
		return query.getResultList();
	}


	@Override
	public Long countNotLinkedCLevels(Integer id, Class<? extends Ead> clazz) {
		String jpaQuery = "SELECT count(clevel)" + buildNotLinkedCLevelsFromQuery(id, clazz);
		TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);		
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	private String buildNotLinkedCLevelsFromQuery(Integer id, Class<? extends Ead> clazz) {
		String varName = "hgId";
		if (SourceGuide.class.equals(clazz)){
			varName = "sgId";
		}
		return " FROM CLevel clevel JOIN clevel.eadContent eadContent WHERE eadContent." + varName + " = :id AND clevel.hrefEadid IS NOT NULL AND clevel.clId NOT IN (SELECT hgSgFaRelation.hgSgClevelId FROM HgSgFaRelation hgSgFaRelation WHERE hgSgFaRelation." + varName + " = :id)";
	}

	@Override
	public List<CLevel> getClevelsFromSgOrHg(Integer aiId, String eadid) {
		String jpaQuery =  "SELECT clevel FROM CLevel clevel JOIN clevel.eadContent eadContent WHERE clevel.hrefEadid = :eadid AND " +
				"clevel.clId NOT IN (SELECT hgSgFaRelation.hgSgClevelId FROM HgSgFaRelation hgSgFaRelation WHERE hgSgFaRelation.hgSgClevelId =  clevel.clId) AND" +
				"(eadContent.hgId IN " +
				"(SELECT holdingsGuide.id FROM HoldingsGuide holdingsGuide WHERE holdingsGuide.aiId = :aiId)" +
				"OR eadContent.sgId IN " +
				"(SELECT sourceGuide.id FROM SourceGuide sourceGuide WHERE sourceGuide.aiId = :aiId)" +
				")" ;
		TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);		
		query.setParameter("aiId", aiId);
		query.setParameter("eadid", eadid);
		return query.getResultList();
	}

	@Override
	public List<CLevel> getParentCLevels(Long eadContentId) {
		String jpaQuery =  "SELECT clevel FROM CLevel clevel WHERE clevel.leaf = false AND clevel.ecId = :eadContentId  ORDER BY clevel.unittitle" ;
		TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);		
		query.setParameter("eadContentId", eadContentId);
		return query.getResultList();
	}
	

}