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

import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.vo.UpFile;

public class UpFileHibernateDAO extends AbstractHibernateDAO<UpFile, Integer> implements UpFileDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<UpFile> getUpFiles(Integer aiId, String fileType) {
		long startTime = System.currentTimeMillis();
		List<UpFile> results = new ArrayList<UpFile>();
		Criteria criteria = createUpFilesCriteria(aiId, fileType);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createUpFilesCriteria(Integer aiId, String fileType) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "upFile");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("upFile.archivalInstitution", "archivalInstitution");
		criteria = criteria.createAlias("upFile.fileType", "fileType");

		if (aiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
		}
		if (StringUtils.isNotBlank(fileType)) {
			criteria.add(Restrictions.like("fileType.ftype", fileType, MatchMode.ANYWHERE));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<UpFile> getUpFiles(Integer aiId) {
		long startTime = System.currentTimeMillis();
		List<UpFile> results = new ArrayList<UpFile>();
		Criteria criteria = createUpFilesCriteria(aiId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createUpFilesCriteria(Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "upFile");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("upFile.archivalInstitution", "archivalInstitution");

		if (aiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
		}

		return criteria;
	}

	
	public UpFile getUpFile(Integer fileId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "upFile");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("ufId",fileId));
		UpFile result = null;
		result = (UpFile)criteria.uniqueResult();
		return result;
	}
}
