package eu.apenet.persistence.hibernate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.UserRoleDAO;
import eu.apenet.persistence.vo.UserRole;

public class UserRoleHibernateDAO extends AbstractHibernateDAO<UserRole, Integer> implements UserRoleDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	public UserRole getUserRole(String type){
		long startTime = System.currentTimeMillis();
		UserRole result = new UserRole();
		Criteria criteria = createRoleTypeByTypeCriteria(type);
		result = (UserRole) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}
		return result;
	}
	
	private Criteria createRoleTypeByTypeCriteria(String rtype) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "UserRole");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(rtype)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("UserRole.role", rtype, MatchMode.EXACT));
		}
		return criteria;
	}
}
