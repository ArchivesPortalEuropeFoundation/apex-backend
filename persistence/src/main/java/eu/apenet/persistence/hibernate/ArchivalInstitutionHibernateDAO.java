package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class ArchivalInstitutionHibernateDAO extends AbstractHibernateDAO<ArchivalInstitution, Integer> implements
		ArchivalInstitutionDAO {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public List<ArchivalInstitution> getArchivalInstitutionsByParentAiId(Integer parentAiId) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
		Criteria criteria = createArchivalInstitutionByParentAiIdCriteria(parentAiId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public List<ArchivalInstitution> getGroupsAndArchivalInstitutionsByCountryId(Integer countryId, String sortValue,
			boolean ascending) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("countryId", countryId));
		if ("countryId".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("countryId"));
			} else {
				criteria.addOrder(Order.desc("countryId"));
			}
		}
		if ("ainame".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("ainame"));
			} else {
				criteria.addOrder(Order.desc("ainame"));
			}
		}
		if ("aiId".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("aiId"));
			} else {
				criteria.addOrder(Order.desc("aiId"));
			}
		}
		if ("alorder".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("alorder"));
			} else {
				criteria.addOrder(Order.desc("alorder"));
			}
		}
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	public Integer countArchivalInstitutionsByCountryId(Integer pId) {
		Criteria criteria = createCountCriteriaByCountryId(pId);
		criteria.setProjection(Projections.rowCount());
		Object object = criteria.list().get(0);

		if (object instanceof Integer) {
			return (Integer) object;
		} else if (object instanceof Long) {
			return ((Long) object).intValue();
		}
		return 0;

	}

	@Override
	public Integer countArchivalInstitutionsByParentAiId(Integer parentAiId) {
		Criteria criteria = createCountCriteriaByParentAiId(parentAiId);
		criteria.setProjection(Projections.rowCount());
		Object object = criteria.uniqueResult();
		if (object instanceof Integer) {
			return (Integer) object;
		} else if (object instanceof Long) {
			return ((Long) object).intValue();
		}
		return 0;

	}

	@Override
	public Long countTotalArchivalInstitutions() {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("group", false));
		criteria.add(Restrictions.isNotNull("eagPath"));
		criteria.setProjection(Projections.countDistinct("archivalInstitution.aiId")); // select
																						// count(aiId)
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArchivalInstitution getArchivalInstitution(Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("aiId", aiId));
		List<ArchivalInstitution> list = criteria.list();
		if (list != null) {
			return (ArchivalInstitution) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ArchivalInstitution getArchivalInstitutionByAiName(String InstitutionName) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("ainame", InstitutionName));
		List<ArchivalInstitution> list = criteria.list();
		if (list != null) {
			return (ArchivalInstitution) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArchivalInstitution> getArchivalInstitutionsByPartnerId(Integer pId) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
		Criteria criteria = createArchivalInstitutionCriteria(false, "ainame", true);
		criteria.add(Restrictions.eq("partnerId", pId));
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArchivalInstitution> getArchivalInstitutionsByCountryId(Integer countryId, boolean onlyWithoutPartnerIds) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
		Criteria criteria = createArchivalInstitutionCriteria(false, "ainame", true);
		criteria.add(Restrictions.eq("countryId", countryId));
		if (onlyWithoutPartnerIds) {
			criteria.add(Restrictions.isNull("partnerId"));
		}
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createArchivalInstitutionCriteria(boolean group, String sortValue, boolean ascending) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("group", group));
		if ("countryId".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("archivalInstitution.countryId"));
			} else {
				criteria.addOrder(Order.desc("archivalInstitution.countryId"));
			}
		}
		if ("pId".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("archivalInstitution.pId"));
			} else {
				criteria.addOrder(Order.desc("archivalInstitution.pId"));
			}
		}
		if ("ainame".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("archivalInstitution.ainame"));
			} else {
				criteria.addOrder(Order.desc("archivalInstitution.ainame"));
			}
		}
		if ("aiId".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("archivalInstitution.aiId"));
			} else {
				criteria.addOrder(Order.desc("archivalInstitution.aiId"));
			}
		}
		if ("alorder".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("archivalInstitution.alorder"));
			} else {
				criteria.addOrder(Order.desc("archivalInstitution.alorder"));
			}
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArchivalInstitution> getRootArchivalInstitutionsByCountryId(Integer countryId) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("countryId", countryId));
		criteria.add(Restrictions.isNull("parent"));
		List<ArchivalInstitution> results =  criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsWithSearchableItems(Integer countryId, Integer parentAiId) {
		long startTime = System.currentTimeMillis();
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArchivalInstitution> cq = criteriaBuilder.createQuery(ArchivalInstitution.class);
		Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
		List<Predicate> whereClause = new ArrayList<Predicate>();
		if (countryId != null){
			whereClause.add(criteriaBuilder.equal(from.get("countryId"), countryId));	
		}
		if (parentAiId == null){
			whereClause.add(criteriaBuilder.isNull(from.get("parentAiId")));
		}else {
			whereClause.add(criteriaBuilder.equal(from.get("parentAiId"), parentAiId));	
		}
		whereClause.add(criteriaBuilder.equal(from.get("containSearchableItems"), true));	
		cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
		cq.orderBy(criteriaBuilder.asc(from.get("alorder")));
		List<ArchivalInstitution> results =  getEntityManager().createQuery(cq).getResultList();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

	private Criteria createArchivalInstitutionByParentAiIdCriteria(Integer parentAiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (parentAiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.parent.aiId", parentAiId.intValue()));
		} else {
			criteria.add(Restrictions.isNull("archivalInstitution.parent.aiId"));
		}

		return criteria;
	}

	private Criteria createCountCriteriaByParentAiId(Integer parentAiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (parentAiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.parent.aiId", parentAiId.intValue()));
		}
		return criteria;
	}

	private Criteria createCountCriteriaByCountryId(Integer countryId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (countryId != null) {
			criteria.add(Restrictions.eq("countryId", countryId));
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<ArchivalInstitution> getArchivalInstitutionsByRepositorycode(String repositorycode) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
		Criteria criteria = createArchivalInstitutionsByRepositorycodeCriteria(repositorycode);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createArchivalInstitutionsByRepositorycodeCriteria(String repositorycode) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (repositorycode != null) {
			criteria.add(Restrictions.eq("repositorycode", repositorycode));
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<ArchivalInstitution> getArchivalInstitutionsByAutform(String autform) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
		Criteria criteria = createArchivalInstitutionsByAutformCriteria(autform);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createArchivalInstitutionsByAutformCriteria(String autform) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (autform != null) {
			criteria.add(Restrictions.eq("autform", autform));
		}
		return criteria;
	}

	@Override
	public ArchivalInstitution getArchivalInstitutionsByCountryIdandAlIdentifier(Integer countryId, String internalAlId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("countryId", countryId));
		if (internalAlId != null) {
			criteria.add(Restrictions.eq("internalAlId", internalAlId));
		}
		return ((ArchivalInstitution) criteria.uniqueResult());
	}

	@Override
	public ArchivalInstitution getArchivalInstitutionByInternalAlId(String identifier) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("archivalInstitution.internalAlId", identifier));
		return (ArchivalInstitution) criteria.uniqueResult();
	}

	// This method retrieves the number of archival institutions which have
	// content indexed in the System
	public Long countArchivalInstitutionsWithContentIndexed() {
		Criteria criteria = createCriteriaForArchivalInstitutionsWithContentIndexed(Arrays
				.asList(FileState.INDEXED_FILE_STATES));
		criteria.setProjection(Projections.count("archivalInstitution.aiId"));
		return (Long) criteria.uniqueResult();
	}

	/*
	 * SELECT ai_id FROM archival_institution WHERE ai_id IN (SELECT DISTINCT
	 * ai_id FROM finding_aid WHERE fs_id > 7 AND fs_id < 15) OR ai_id IN
	 * (SELECT DISTINCT ai_id FROM holdings_guide WHERE fs_id > 7 AND fs_id <
	 * 15) OR ai_id IN (SELECT DISTINCT ai_id FROM source_guide WHERE fs_id > 7
	 * AND fs_id < 15)
	 */
	private Criteria createCriteriaForArchivalInstitutionsWithContentIndexed(Collection<String> fileStates) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Property.forName("archivalInstitution.aiId"));
		Disjunction disjunction = Restrictions.disjunction();
		DetachedCriteria subQuery = DetachedCriteria.forClass(FindingAid.class, "findingAid");
		subQuery.setProjection(Property.forName("findingAid.archivalInstitution.aiId"));
		subQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		subQuery = setFileStates(subQuery, fileStates, FindingAid.class);

		disjunction.add(Subqueries.propertyIn("archivalInstitution.aiId", subQuery));

		DetachedCriteria subQuery2 = DetachedCriteria.forClass(HoldingsGuide.class, "holdingsGuide");
		subQuery2.setProjection(Property.forName("holdingsGuide.archivalInstitution.aiId"));
		subQuery2.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		subQuery2 = setFileStates(subQuery2, fileStates, HoldingsGuide.class);

		disjunction.add(Subqueries.propertyIn("archivalInstitution.aiId", subQuery2));

		DetachedCriteria subQuery3 = DetachedCriteria.forClass(SourceGuide.class, "sourceGuide");
		subQuery3.setProjection(Property.forName("sourceGuide.archivalInstitution.aiId"));
		subQuery3.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		subQuery3 = setFileStates(subQuery3, fileStates, SourceGuide.class);

		disjunction.add(Subqueries.propertyIn("archivalInstitution.aiId", subQuery3));

		criteria.add(disjunction);
		return criteria;
	}

	@SuppressWarnings("rawtypes")
	private DetachedCriteria setFileStates(DetachedCriteria criteria, Collection<String> fileStates, Class clazz) {
		if (fileStates != null && fileStates.size() > 0) {

			if (clazz.equals(FindingAid.class)) {
				criteria = criteria.createAlias("findingAid.fileState", "fileState");
			} else if (clazz.equals(HoldingsGuide.class)) {
				criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");
			} else {
				criteria = criteria.createAlias("sourceGuide.fileState", "fileState");
			}

			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		return criteria;
	}
}