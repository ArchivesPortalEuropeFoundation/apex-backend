package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.UpFile;

public class UpFileHibernateDAO extends AbstractHibernateDAO<UpFile, Integer> implements UpFileDAO {

	private final Logger log = Logger.getLogger(getClass());

	@Override
	public List<UpFile> getUpFiles(Integer aiId, FileType fileType) {
		String query = "SELECT upFile FROM UpFile upFile WHERE upFile.aiId  = :aiId  AND upFile.fileType = :fileType AND upFile.id NOT IN (SELECT queueItem.upFileId FROM QueueItem queueItem WHERE queueItem.upFileId IS NOT NULL)";
		TypedQuery<UpFile> typedQuery = getEntityManager().createQuery(
				query, UpFile.class);
		typedQuery.setParameter("aiId", aiId);
		typedQuery.setParameter("fileType", fileType);
		return typedQuery.getResultList();
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
