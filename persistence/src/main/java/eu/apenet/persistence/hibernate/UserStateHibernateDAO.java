package eu.apenet.persistence.hibernate;

import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.UserState;


import eu.apenet.persistence.dao.UserStateDAO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
//import eu.apenet.persistence.vo.UserState;;

public class UserStateHibernateDAO extends AbstractHibernateDAO<UserState, Long> implements UserStateDAO{
    private static final Logger LOG = Logger.getLogger(UserStateHibernateDAO.class);

    private Criteria createUserStateCriteria(String state) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "user_state");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (StringUtils.isNotBlank(state)) {
			criteria.add(Restrictions.ilike("user_state.state", state, MatchMode.EXACT));
		}
		return criteria;
	}

	public UserState getUserStateByState(String state) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createUserStateCriteria(state);
		UserState result = (UserState) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}
		return result;
	}

}
