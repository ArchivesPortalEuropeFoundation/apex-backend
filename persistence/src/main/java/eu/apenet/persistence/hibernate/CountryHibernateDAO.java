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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.vo.Country;

public class CountryHibernateDAO extends AbstractHibernateDAO<Country, Integer> implements CountryDAO
{
private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<Country> getCountries(String isoname) {
		
		long startTime = System.currentTimeMillis();
		List<Country> results = new ArrayList<Country>();
		Criteria criteria = createCountryCriteria(isoname);
		results = criteria.list();
		
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}
	public List<Country> getCountriesWithSearchableItems(){
		long startTime = System.currentTimeMillis();	
		TypedQuery<Country> query = getEntityManager().createQuery("SELECT DISTINCT country FROM ArchivalInstitution ai JOIN ai.country country WHERE ai.containSearchableItems = true", Country.class);
		List<Country> results = query.getResultList();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

	@Override
	public List<Country> getCountriesOrderByName() {
		List<Country> results = new ArrayList<Country>();
		
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("cname"));
		return criteria.list();
	}

	
	
	@SuppressWarnings("unchecked")
	public List<Country> getCountriesWithArchivalInstitutionsWithEAG() {
		long startTime = System.currentTimeMillis();
		List<Country> results = new ArrayList<Country>();
		
		/*
		 * INNER JOIN with createCriteria [Only Country objects will be retrieved from database]. DISTINCT is made on memory
		*/
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria = criteria.createCriteria("archivalInstitutions");
		criteria.add(Restrictions.isNotNull("eagPath"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		results = criteria.list();
		
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

	private Criteria createCountryCriteria(String isoname) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "country");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);				

		if (StringUtils.isNotBlank(isoname)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("country.isoname", isoname, MatchMode.EXACT));
		}
		return criteria;
	}
	private Criteria createCountryCriteria2(String cname) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "country");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);				

		if (StringUtils.isNotBlank(cname)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("country.cname", cname, MatchMode.EXACT));
		}
		return criteria;
	}
	
	public Country getCountryByCname(String cname) {
		long startTime = System.currentTimeMillis();		
		Country result = new Country();
		Criteria criteria = createCountryCriteria2(cname);
		result = (Country) criteria.uniqueResult();		
		
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}
		return result;
	}
	

	@Override
	public List<Country> getCountries(List<Integer> countryIds) {
		if (countryIds != null && countryIds.size() > 0){
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Country> cq = criteriaBuilder.createQuery(Country.class);
			Root<Country> from = cq.from(Country.class);
			List<Predicate> whereClause = new ArrayList<Predicate>();
			for (Integer countryId : countryIds) {
				whereClause.add(criteriaBuilder.equal(from.get("id"), countryId));
			}
			cq.where(criteriaBuilder.or(whereClause.toArray(new Predicate[0])));
			cq.orderBy(criteriaBuilder.asc(from.get("cname")));
			return getEntityManager().createQuery(cq).getResultList();
		}else {
			return new ArrayList<Country>();
		}

	}
}