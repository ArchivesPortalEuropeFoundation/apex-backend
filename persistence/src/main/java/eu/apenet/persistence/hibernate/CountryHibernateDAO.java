package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
//import org.hibernate.FetchMode;
//import org.hibernate.Query;
//import org.hibernate.criterion.Projections;
//import org.hibernate.transform.Transformers;

/**
 * @author Patricia
 */

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
		long endTime = System.currentTimeMillis();
		TypedQuery<Country> query = getEntityManager().createQuery("SELECT DISTINCT country FROM ArchivalInstitution ai JOIN ai.country country WHERE ai.containSearchableItems = true", Country.class);
		List<Country> results = query.getResultList();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public List<Country> getCountriesWithContentIndexedOrderByCName() {
		long startTime = System.currentTimeMillis();
		Collection<String> fileStates = Arrays.asList(FileState.INDEXED_FILE_STATES);
		List<Country> results = new ArrayList<Country>();
		
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("cname"));
			
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(ArchivalInstitution.class,"archivalInstitution");
				subQuery2.setProjection(Property.forName("archivalInstitution.countryId"));
				Disjunction disjunction = Restrictions.disjunction();
				
					DetachedCriteria subQuery3 = DetachedCriteria.forClass(FindingAid.class,"findingAid");
					subQuery3.setProjection(Property.forName("findingAid.archivalInstitution.aiId"));
					subQuery3.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					subQuery3 = setFileStates(subQuery3,fileStates, FindingAid.class);
					
				disjunction.add(Subqueries.propertyIn("archivalInstitution.aiId", subQuery3));

					DetachedCriteria subQuery4 = DetachedCriteria.forClass(HoldingsGuide.class,"holdingsGuide");
					subQuery4.setProjection(Property.forName("holdingsGuide.archivalInstitution.aiId"));
					subQuery4.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					subQuery4 = setFileStates(subQuery4,fileStates, HoldingsGuide.class);
				
				disjunction.add(Subqueries.propertyIn("archivalInstitution.aiId", subQuery4));


				subQuery2.add(disjunction);
			criteria.add(Subqueries.propertyIn("couId", subQuery2));
		results = criteria.list();
		
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
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
	
	@SuppressWarnings("rawtypes")
	private DetachedCriteria setFileStates(DetachedCriteria criteria, Collection<String> fileStates, Class clazz) {
		if (fileStates!= null && fileStates.size() > 0) {
			
			if (clazz.equals(FindingAid.class)) {
				criteria = criteria.createAlias("findingAid.fileState", "fileState");				
			}
			else if (clazz.equals(HoldingsGuide.class)) {
				criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");								
			}
			else {
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