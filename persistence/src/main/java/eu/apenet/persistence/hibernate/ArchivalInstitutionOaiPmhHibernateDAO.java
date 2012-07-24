package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;

/**
 * 
 * @author Jara Alvarez
 * 4 Nov 2010
 *
 */

public class ArchivalInstitutionOaiPmhHibernateDAO extends AbstractHibernateDAO<ArchivalInstitutionOaiPmh, Integer> implements ArchivalInstitutionOaiPmhDAO{

	private final Logger log = Logger.getLogger(getClass());
	
	private Criteria createArchivalInstitutionOaiPmhCriteria(Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitutionOaiPmh");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("archivalInstitutionOaiPmh.archivalInstitution", "aiId");

		if (aiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
		}		
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs(
			Integer aiId) {
		long startTime = System.currentTimeMillis();
		List<ArchivalInstitutionOaiPmh> results = new ArrayList<ArchivalInstitutionOaiPmh>();
		Criteria criteria = createArchivalInstitutionOaiPmhCriteria(aiId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}


}