package eu.apenet.persistence.hibernate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.UpFileStateDAO;
import eu.apenet.persistence.vo.UpFileState;

public class UpFileStateHibernateDAO extends AbstractHibernateDAO<UpFileState, Integer> implements UpFileStateDAO {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public UpFileState getUpFileStateByState(String state) {
		long startTime = System.currentTimeMillis();
		UpFileState result = new UpFileState();
		Criteria criteria = createUpFileStateByStateCriteria(state);
		result = (UpFileState) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}

		return result;
	}
	
	private Criteria createUpFileStateByStateCriteria(String state) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "upFileState");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(state)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("upFileState.state", state, MatchMode.EXACT));
		}
		return criteria;
	}

}
