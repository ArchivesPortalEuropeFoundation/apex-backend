package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.vo.Warnings;

public class WarningsHibernateDAO extends AbstractHibernateDAO<Warnings, Integer> implements WarningsDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<Warnings> getWarnings(Integer faId, Integer hgId) {
		long startTime = System.currentTimeMillis();
		List<Warnings> results = new ArrayList<Warnings>();
		Criteria criteria = createWarningsCriteria(faId, hgId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createWarningsCriteria(Integer faId, Integer hgId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "warnings");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//criteria = criteria.createAlias("warnings.findingAid", "findingAid");

		if (faId != null) {
			criteria.add(Restrictions.eq("findingAid.id", faId.intValue()));
		}

		if (hgId != null) {
			criteria.add(Restrictions.eq("holdingsGuide.id", hgId.intValue()));
		}

		return criteria;
	}
}
