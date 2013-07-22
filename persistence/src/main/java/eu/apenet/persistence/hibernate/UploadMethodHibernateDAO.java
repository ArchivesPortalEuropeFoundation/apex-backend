package eu.apenet.persistence.hibernate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.UploadMethodDAO;
import eu.apenet.persistence.vo.UploadMethod;

public class UploadMethodHibernateDAO extends AbstractHibernateDAO<UploadMethod, Integer> implements UploadMethodDAO {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public UploadMethod getUploadMethodByMethod(String method) {
		long startTime = System.currentTimeMillis();
		UploadMethod result = new UploadMethod();
		Criteria criteria = createUploadMethodByMethodCriteria(method);
		result = (UploadMethod) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}

		return result;
	}
	
	private Criteria createUploadMethodByMethodCriteria(String method) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "uploadMethod");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(method)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("uploadMethod.method", method, MatchMode.EXACT));
		}
		return criteria;
	}

}
