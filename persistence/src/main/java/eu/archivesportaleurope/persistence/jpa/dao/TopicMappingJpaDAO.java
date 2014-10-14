package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.TopicMappingDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.TopicMapping;

public class TopicMappingJpaDAO extends AbstractHibernateDAO<TopicMapping, Long> implements TopicMappingDAO {

	@Override
	public List<TopicMapping> getTopicMappingsByAiId(Integer aiId) {
		String jpaQuery = "SELECT topicMapping FROM TopicMapping topicMapping WHERE aiId = :aiId";
		TypedQuery<TopicMapping> query = getEntityManager().createQuery(jpaQuery, TopicMapping.class);		
		query.setParameter("aiId", aiId);

		return query.getResultList();
	
	}

	@Override
	public TopicMapping getTopicMappingByAiId(Integer aiId, Long id) {
		String jpaQuery = "SELECT topicMapping FROM TopicMapping topicMapping WHERE aiId = :aiId AND id = :id";
		TypedQuery<TopicMapping> query = getEntityManager().createQuery(jpaQuery, TopicMapping.class);		
		query.setParameter("aiId", aiId);
		query.setParameter("id", id);
		return query.getSingleResult();
	}


}
