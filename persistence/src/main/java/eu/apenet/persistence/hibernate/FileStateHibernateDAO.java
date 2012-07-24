package eu.apenet.persistence.hibernate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.FileStateDAO;
import eu.apenet.persistence.vo.FileState;

public class FileStateHibernateDAO extends AbstractHibernateDAO<FileState, Integer> implements FileStateDAO {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public FileState getFileStateByState(String state) {
		long startTime = System.currentTimeMillis();
		FileState result = new FileState();
		Criteria criteria = createFileStateByStateCriteria(state);
		result = (FileState) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}

		return result;
	}
	
	private Criteria createFileStateByStateCriteria(String state) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "fileState");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(state)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("fileState.state", state, MatchMode.EXACT));
		}
		return criteria;
	}

}
