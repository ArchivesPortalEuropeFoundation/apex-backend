package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.UpFile;

public class UpFileJpaDAO extends AbstractHibernateDAO<UpFile, Integer> implements UpFileDAO {

	private final Logger log = Logger.getLogger(getClass());

	@Override
	public List<UpFile> getNewUpFiles(Integer aiId, FileType fileType) {
		String query = "SELECT upFile FROM UpFile upFile WHERE upFile.aiId  = :aiId  AND upFile.fileType = :fileType AND upFile.id NOT IN (SELECT queueItem.upFileId FROM QueueItem queueItem WHERE queueItem.upFileId IS NOT NULL)";
		TypedQuery<UpFile> typedQuery = getEntityManager().createQuery(
				query, UpFile.class);
		typedQuery.setParameter("aiId", aiId);
		typedQuery.setParameter("fileType", fileType);
		return typedQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<UpFile> getUpFiles(Integer aiId) {
		String query = "SELECT upFile FROM UpFile upFile WHERE upFile.aiId  = :aiId ";
		TypedQuery<UpFile> typedQuery = getEntityManager().createQuery(
				query, UpFile.class);
		typedQuery.setParameter("aiId", aiId);
		return typedQuery.getResultList();
	}


}
