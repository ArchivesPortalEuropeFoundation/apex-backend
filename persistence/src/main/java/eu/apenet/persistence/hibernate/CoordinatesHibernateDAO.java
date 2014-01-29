package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Coordinates;
import eu.apenet.persistence.vo.Country;


public class CoordinatesHibernateDAO extends AbstractHibernateDAO<Coordinates, Integer> implements CoordinatesDAO {
	private final Logger log = Logger.getLogger(getClass());

	@Override
	public List<Coordinates> findCoordinatesByArchivalInstitution(ArchivalInstitution archivalInstitution) {
		log.debug("Finding coordinates related to the institution with name: " + archivalInstitution.getAiname());

		long startTime = System.currentTimeMillis();
		List<Coordinates> results = new ArrayList<Coordinates>();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("archivalInstitution",archivalInstitution));
		results = criteria.list();

		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}
	
	/**
	 * get all repositories with all directions to build Europe landscape
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Coordinates> getCoordinates() {
		long startTime = System.currentTimeMillis();
		List<Coordinates> results = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "coordinates");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("coordinates.id"));	
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}
	
	/**
	 * get all repos from a country to build country landscape
	 */
	@Override
	public List<Coordinates> getCoordinatesByCountryCode(String countryCode) {
		long startTime = System.currentTimeMillis();
		List<Coordinates> results = null;
		if(countryCode!=null && countryCode.length()==2){
	    	Criteria criteria = getSession().createCriteria(getPersistentClass(), "coordinates");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				//begin subquery1 (SELECT aiid FROM archival_institution WHERE aiid IN ...("subquery2")...)
				DetachedCriteria subqueryInstitution = DetachedCriteria.forClass(ArchivalInstitution.class, "archivalInstitution");
				subqueryInstitution.setProjection(Projections.property("archivalInstitution.aiId")); //select aiId
					//begin subquery2 (SELECT id FROM country WHERE isoname LIKE ?)
					DetachedCriteria subqueryCountry = DetachedCriteria.forClass(Country.class, "country");
					subqueryCountry.add(Restrictions.ilike("country.isoname", countryCode)); //where
					subqueryCountry.setProjection(Projections.property("country.id")); //Select id
					//end subquery2
				subqueryInstitution.add(Property.forName("archivalInstitution.countryId").in(subqueryCountry)); //subquery IN
				//end subquery1
			criteria.add(Property.forName("coordinates.aiId").in(subqueryInstitution)); //subquery IN
			results = criteria.list();
		}
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}
    


}