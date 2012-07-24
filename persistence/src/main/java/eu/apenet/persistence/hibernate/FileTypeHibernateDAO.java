package eu.apenet.persistence.hibernate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.FileTypeDAO;
import eu.apenet.persistence.vo.FileType;

public class FileTypeHibernateDAO extends AbstractHibernateDAO<FileType, Integer> implements FileTypeDAO {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public FileType getFileTypeByType(String ftype) {
		long startTime = System.currentTimeMillis();
		FileType result = new FileType();
		Criteria criteria = createFileTypeByTypeCriteria(ftype);
		result = (FileType) criteria.uniqueResult();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read 1 object");
		}

		return result;
	}
	
	private Criteria createFileTypeByTypeCriteria(String ftype) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "fileType");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (StringUtils.isNotBlank(ftype)) {
			//ilike --> case-insensitive; like --> case-sensitive
			criteria.add(Restrictions.ilike("fileType.ftype", ftype, MatchMode.EXACT));
		}
		return criteria;
	}

}
