package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ValidatedState;

public class EadHibernateDAO extends AbstractHibernateDAO<Ead, Integer> implements EadDAO {

	private static final Logger LOG = Logger.getLogger(EadHibernateDAO.class);

	@Override
	public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
		criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));
		criteria.add(Restrictions.eq("published", true));
		List<Integer> result = criteria.list();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<Ead> getEads(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Ead> cq = criteriaBuilder.createQuery(Ead.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		cq.where(buildWhere(from, eadSearchOptions));
		cq.select(from);
		/*
		 * add ordering
		 */
		if (eadSearchOptions.isOrderByAscending()) {
			cq.orderBy(criteriaBuilder.asc(from.get(eadSearchOptions.getOrderByField())));
		} else {
			cq.orderBy(criteriaBuilder.desc(from.get(eadSearchOptions.getOrderByField())));
		}

		/*
		 * add pagination
		 */
		TypedQuery<Ead> query = getEntityManager().createQuery(cq);
		if (eadSearchOptions.getPageSize() > 0) {
			query.setMaxResults(eadSearchOptions.getPageSize());
			if (eadSearchOptions.getFirstResult() >= 0) {
				query.setFirstResult(((Long) eadSearchOptions.getFirstResult()).intValue());
			} else {
				query.setFirstResult(eadSearchOptions.getPageSize() * (eadSearchOptions.getPageNumber() - 1));
			}
		}
		return query.getResultList();
	}

	@Override
	public boolean existEads(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Ead> cq = criteriaBuilder.createQuery(Ead.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		cq.where(buildWhere(from, eadSearchOptions));
		cq.select(from);

		TypedQuery<Ead> query = getEntityManager().createQuery(cq);
		query.setMaxResults(1);
		return query.getResultList().size() > 0;
	}

	@Override
	public Long countEads(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		cq.where(buildWhere(from, eadSearchOptions));
		cq.select(criteriaBuilder.countDistinct(from));

		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public Long countUnits(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		cq.where(criteriaBuilder.and(buildWhere(from, eadSearchOptions),
				criteriaBuilder.greaterThan(from.<Integer> get("totalNumberOfUnits"), 0)));
		cq.select(criteriaBuilder.sum(from.<Long> get("totalNumberOfUnits")));

		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public Long countDaos(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		cq.where(criteriaBuilder.and(buildWhere(from, eadSearchOptions),
				criteriaBuilder.greaterThan(from.<Integer> get("totalNumberOfDaos"), 0)));
		cq.select(criteriaBuilder.sum(from.<Long> get("totalNumberOfDaos")));

		return getEntityManager().createQuery(cq).getSingleResult();
	}

	private Predicate buildWhere(Root<? extends Ead> from, EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		List<Predicate> whereClause = new ArrayList<Predicate>();
		if (eadSearchOptions.getIds() != null && eadSearchOptions.getIds().size() > 0) {
			List<Predicate> idPredicated = new ArrayList<Predicate>();
			for (Integer id : eadSearchOptions.getIds()) {
				idPredicated.add(criteriaBuilder.equal(from.get("id"), id));
			}
			whereClause.add(criteriaBuilder.or(idPredicated.toArray(new Predicate[0])));
		}
		if (eadSearchOptions.getArchivalInstitionId() != null) {
			whereClause.add(criteriaBuilder.equal(from.get("aiId"), eadSearchOptions.getArchivalInstitionId()));
		}
		if (eadSearchOptions.getPublished() != null) {
			whereClause.add(criteriaBuilder.equal(from.get("published"), eadSearchOptions.getPublished()));
		}
		if (eadSearchOptions.getConverted() != null) {
			whereClause.add(criteriaBuilder.equal(from.get("converted"), eadSearchOptions.getConverted()));
		}
		if (eadSearchOptions.getValidated().size() > 0) {
			List<Predicate> validatedPredicated = new ArrayList<Predicate>();
			for (ValidatedState validateState : eadSearchOptions.getValidated()) {
				validatedPredicated.add(criteriaBuilder.equal(from.get("validated"), validateState));
			}
			whereClause.add(criteriaBuilder.or(validatedPredicated.toArray(new Predicate[0])));
		}
		if (FindingAid.class.equals(eadSearchOptions.getEadClazz())) {
			if (eadSearchOptions.getEuropeana().size() > 0) {
				List<Predicate> europeanaPredicated = new ArrayList<Predicate>();
				for (EuropeanaState europeanaState : eadSearchOptions.getEuropeana()) {
					europeanaPredicated.add(criteriaBuilder.equal(from.get("europeana"), europeanaState));
				}
				whereClause.add(criteriaBuilder.or(europeanaPredicated.toArray(new Predicate[0])));
			}
		}
		if (eadSearchOptions.getQueuing().size() > 0) {
			List<Predicate> queuingPredicated = new ArrayList<Predicate>();
			for (QueuingState queuingState : eadSearchOptions.getQueuing()) {
				queuingPredicated.add(criteriaBuilder.equal(from.get("queuing"), queuingState));
			}
			whereClause.add(criteriaBuilder.or(queuingPredicated.toArray(new Predicate[0])));
		}
		if (eadSearchOptions.getPublishedToAll() != null) {
			List<Predicate> orPredicated = new ArrayList<Predicate>();
			if (eadSearchOptions.getPublishedToAll()) {
				orPredicated.add(criteriaBuilder.equal(from.get("published"), true));
				if (FindingAid.class.equals(eadSearchOptions.getEadClazz())) {
					orPredicated.add(criteriaBuilder.notEqual(from.get("europeana"), EuropeanaState.NOT_CONVERTED));
				}
			} else {
				orPredicated.add(criteriaBuilder.equal(from.get("published"), false));
				if (FindingAid.class.equals(eadSearchOptions.getEadClazz())) {
					orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.NOT_CONVERTED));
				}
			}
			whereClause.add(criteriaBuilder.or(orPredicated.toArray(new Predicate[0])));
		}
		if (StringUtils.isNotBlank(eadSearchOptions.getSearchTerms())) {

			String[] searchTerms = StringUtils.split(eadSearchOptions.getSearchTerms(), " ");
			if ("eadid".equals(eadSearchOptions.getSearchTermsField())) {
				for (String searchTerm : searchTerms) {
					whereClause.add(criteriaBuilder.like(from.<String> get("eadid"), "%" + searchTerm + "%"));
				}
			} else if ("title".equals(eadSearchOptions.getSearchTermsField())) {
				for (String searchTerm : searchTerms) {
					whereClause.add(criteriaBuilder.like(from.<String> get("title"), "%" + searchTerm + "%"));
				}
			} else {
				for (String searchTerm : searchTerms) {
					Predicate titlePredicate = criteriaBuilder.like(from.<String> get("title"), "%" + searchTerm + "%");
					Predicate eadidPredicate = criteriaBuilder.like(from.<String> get("eadid"), "%" + searchTerm + "%");
					whereClause.add(criteriaBuilder.or(titlePredicate, eadidPredicate));
				}

			}
		}
		return criteriaBuilder.and(whereClause.toArray(new Predicate[0]));
	}

	@Override
	public Integer isEadidUsed(String eadid, Integer aiId, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
		criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));
		List<Integer> result = criteria.list();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ead> getEadsByAiId(Class<? extends Ead> clazz, Integer aiId) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));

		List<Ead> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ead getEadByEadid(Class<? extends Ead> clazz, Integer aiId, String eadid) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));
		List<Ead> list = criteria.list();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Ead getEadByEadid(Class<? extends Ead> clazz, String repositorycode, String eadid) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.repositorycode", repositorycode));
		criteria.add(Restrictions.eq("eadid", eadid));
		criteria.setMaxResults(1);
		List<Ead> list = criteria.list();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAllIds(Class<? extends Ead> clazz, Integer aiId) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.setProjection(Projections.property("id"));
		return criteria.list();
	}

	@SuppressWarnings({ "unchecked" })
	public Long getTotalCountOfUnits() {

		Criteria criteria = null;
		List<Long> result = null;
		Long value = 0L;

		criteria = getSession().createCriteria(getPersistentClass(), "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		result = criteria.setProjection(Projections.sum("totalNumberOfUnits")).list();
		if (result.size() > 0) {
			int i = 0;
			while (result.size() > i) {
				if (result.get(i) != null) {
					value += result.get(i);
				}
				i++;
			}
		}
		return value;

	}

	@SuppressWarnings({ "unchecked" })
	public Long getTotalCountOfUnitsWithDao() {

		Criteria criteria = null;
		List<Long> result = null;
		Long value = 0L;

		criteria = getSession().createCriteria(getPersistentClass(), "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		result = criteria.setProjection(Projections.sum("totalNumberOfUnitsWithDao")).list();
		if (result.size() > 0) {
			int i = 0;
			while (result.size() > i) {
				if (result.get(i) != null) {
					value += result.get(i);
				}
				i++;
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long countFilesbyInstitution(Class<? extends Ead> clazz, Integer aiId) {

		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		List<Long> result = criteria.setProjection(Projections.countDistinct("id")).list();
		Long value = 0L;

		if (result.size() > 0)
			value = result.get(0);

		return value;
	}
}
