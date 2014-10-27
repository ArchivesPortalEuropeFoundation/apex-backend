package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

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

	
	@Override
	public List<Topic> getFirstTopics() {
		String jpaQuery = "SELECT topic FROM Topic topic ORDER BY position";
		TypedQuery<Topic> query = getEntityManager().createQuery(jpaQuery, Topic.class);		
		query.setMaxResults(15);
		return query.getResultList();
	}


	@Override
	public List<Topic> getTopicsWithoutMapping(Integer aiId) {
		String jpaSubQuery = "SELECT topicMapping.topicId FROM TopicMapping topicMapping WHERE topicMapping.aiId = :aiId";
		String jpaQuery = "SELECT topic FROM Topic topic WHERE topic.id NOT IN (" +jpaSubQuery + ") ORDER BY topic.description";
		TypedQuery<Topic> query = getEntityManager().createQuery(jpaQuery, Topic.class);		
		query.setParameter("aiId", aiId);
		return query.getResultList();
	}


}
