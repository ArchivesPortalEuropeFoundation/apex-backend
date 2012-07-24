package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import eu.apenet.persistence.vo.Lang;
import eu.apenet.persistence.dao.LangDAO;

/**
 * 
 * @author Patricia
 *
 */


public class LangHibernateDAO extends AbstractHibernateDAO<Lang, Integer> implements LangDAO
{
private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<Lang> getLanguages(String isoname) {
		
		long startTime = System.currentTimeMillis();
		List<Lang> results = new ArrayList<Lang>();
		Criteria criteria = createLanguageCriteria(isoname);
		results = criteria.list();
		
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createLanguageCriteria(String isoname) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "lang");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);				

		if (StringUtils.isNotBlank(isoname)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("lang.isoname", isoname, MatchMode.EXACT));
		}
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public Lang getLangByIso2Name(String iso2name) {
		
		long startTime = System.currentTimeMillis();
		Lang result = new Lang();
		Criteria criteria = createLangByIso2NameCriteria(iso2name);
		result = (Lang) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}

		return result;	}

	private Criteria createLangByIso2NameCriteria(String iso2name) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "lang");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(iso2name)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("lang.iso2name", iso2name, MatchMode.EXACT));
		}
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public Lang getLangByIsoname(String isoname) {
		
		long startTime = System.currentTimeMillis();
		Lang result = new Lang();
		Criteria criteria = createLangByIsonameCriteria(isoname);
		result = (Lang) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}

		return result;	}

	private Criteria createLangByIsonameCriteria(String isoname) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "lang");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(isoname)) {			
			criteria.add(Restrictions.ilike("lang.isoname", isoname, MatchMode.EXACT));
		}
		return criteria;
	}

}