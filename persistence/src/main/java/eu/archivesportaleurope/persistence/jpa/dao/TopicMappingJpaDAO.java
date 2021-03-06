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
	public List<TopicMapping> getTopicMappingsByCountryId(Integer countryId) {
		String jpaQuery = "SELECT topicMapping FROM TopicMapping topicMapping WHERE countryId = :countryId";
		TypedQuery<TopicMapping> query = getEntityManager().createQuery(jpaQuery, TopicMapping.class);
		query.setParameter("countryId", countryId);

		return query.getResultList();

	}

	@Override
	public TopicMapping getTopicMappingByIdAndAiId(Long id, Integer aiId) {
		String jpaQuery = "SELECT topicMapping FROM TopicMapping topicMapping WHERE aiId = :aiId AND id = :id";
		TypedQuery<TopicMapping> query = getEntityManager().createQuery(jpaQuery, TopicMapping.class);		
		query.setParameter("aiId", aiId);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public TopicMapping getTopicMappingByIdAndCountryId(Long id, Integer countryId) {
		String jpaQuery = "SELECT topicMapping FROM TopicMapping topicMapping WHERE countryId = :countryId AND id = :id";
		TypedQuery<TopicMapping> query = getEntityManager().createQuery(jpaQuery, TopicMapping.class);
		query.setParameter("countryId", countryId);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public TopicMapping getTopicMappingByTopicIdAndCountryId(Long topicId, Integer countryId) {
		String jpaQuery = "SELECT topicMapping FROM TopicMapping topicMapping WHERE countryId = :countryId AND topicId = :topicId";
		TypedQuery<TopicMapping> query = getEntityManager().createQuery(jpaQuery, TopicMapping.class);
		query.setParameter("countryId", countryId);
		query.setParameter("topicId", topicId);
		List<TopicMapping> topicMappings = query.getResultList();
		if(topicMappings.size() > 0)
			return topicMappings.get(0);
		return null;
	}
}
