package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;

public class CouAlternativeNameHibernateDAO extends AbstractHibernateDAO<CouAlternativeName, Integer> implements CouAlternativeNameDAO{
	private final Logger log = Logger.getLogger(CouAlternativeNameHibernateDAO.class);

	@SuppressWarnings("unchecked")
	public String getLocalizedCountry(String countryIso2name, String languageIso2name) {
		long startTime = System.currentTimeMillis();
		List<CouAlternativeName> results = new ArrayList<CouAlternativeName>();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "couAlternativeName");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("couAlternativeName.lang", "lang");
		criteria = criteria.createAlias("couAlternativeName.country", "country");
		criteria = criteria.add(Restrictions.eq("lang.iso2name", languageIso2name.toUpperCase()));
		criteria = criteria.add(Restrictions.eq("country.isoname", countryIso2name.toUpperCase()));
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		if (results.size() > 0){
			CouAlternativeName couAlternativeName = results.get(0);
			return couAlternativeName.getCouAnName();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CouAlternativeName> getAltNamesbyCountry(Country country){
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "couAlternativeName");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.eq("couAlternativeName.country", country));
		
		return criteria.list();
		
	}

	@Override
	public CouAlternativeName getAltNamebyCountryAndLang(Country country,
			Lang language) {
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "couAlternativeName");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.add(Restrictions.eq("couAlternativeName.country", country));
		criteria = criteria.add(Restrictions.eq("couAlternativeName.lang", language));
		
		return (CouAlternativeName) criteria.uniqueResult();
	}
}