package eu.archivesportaleurope.persistence.jpa.dao;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.TopicDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Topic;

public class TopicJpaDAO extends AbstractHibernateDAO<Topic, Long> implements TopicDAO {

	@Override
	public String getDescription(String propertyKey) {
		String jpaQuery = "SELECT description FROM Topic topic WHERE propertyKey = :propertyKey";
		TypedQuery<String> query = getEntityManager().createQuery(jpaQuery, String.class);		
		query.setParameter("propertyKey", propertyKey);
		if (query.getResultList().size() > 0){
			return query.getResultList().get(0);
		}else {
			return null;
		}
	}


}
