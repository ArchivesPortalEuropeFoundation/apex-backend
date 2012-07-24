package eu.apenet.persistence.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.MetadataFormatDAO;
import eu.apenet.persistence.vo.MetadataFormat;

public class MetadataFormatHibernateDAO extends AbstractHibernateDAO<MetadataFormat, Integer> implements MetadataFormatDAO {
	@Override
	public MetadataFormat getMetadataFormatByName(String metadataPrefix) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"metadataFormat");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("format", metadataPrefix));
		return (MetadataFormat) criteria.uniqueResult();
	}
}
