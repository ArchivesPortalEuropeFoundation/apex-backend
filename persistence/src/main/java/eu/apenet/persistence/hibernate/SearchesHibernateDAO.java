package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.SearchesDAO;
import eu.apenet.persistence.vo.Searches;

public class SearchesHibernateDAO extends AbstractHibernateDAO<Searches, Long> implements SearchesDAO{
	
	private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<Searches> getSearchesByNormalUser(Long uId) {
		log.debug("SearchesHibernateDAO: getSearchesByNormalUser() with uId:"+uId);
		long startTime = System.currentTimeMillis();
		List<Searches> results = new ArrayList<Searches>();
		Criteria criteria = createSearchessByUidCriteria(uId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}
		return results;
	}

	private Criteria createSearchessByUidCriteria(Long uId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "searches");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (uId != null) {
			criteria.add(Restrictions.eq("normalUser.UId", uId.longValue()));
		}		
		return criteria;
	}
}
