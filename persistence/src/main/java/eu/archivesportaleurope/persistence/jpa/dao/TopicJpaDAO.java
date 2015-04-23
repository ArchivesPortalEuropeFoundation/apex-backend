package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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
	public List<Topic> getTopicsWithoutMapping(Integer aiId) {
		String jpaSubQuery = "SELECT topicMapping.topicId FROM TopicMapping topicMapping WHERE topicMapping.aiId = :aiId";
		String jpaQuery = "SELECT topic FROM Topic topic WHERE topic.id NOT IN (" +jpaSubQuery + ") ORDER BY topic.description";
		TypedQuery<Topic> query = getEntityManager().createQuery(jpaQuery, Topic.class);		
		query.setParameter("aiId", aiId);
		return query.getResultList();
	}

	@Override
	public List<Topic> getTopicsWithoutMappingPerCountry(Integer countryId) {
		String jpaSubQuery = "SELECT topicMapping.topicId FROM TopicMapping topicMapping WHERE topicMapping.countryId = :countryId";
		String jpaQuery = "SELECT topic FROM Topic topic WHERE topic.id NOT IN (" +jpaSubQuery + ") ORDER BY topic.description";
		TypedQuery<Topic> query = getEntityManager().createQuery(jpaQuery, Topic.class);
		query.setParameter("countryId", countryId);
		return query.getResultList();
	}

	@Override
	public Topic getTopicByDescription(String topicDescription){
		Topic result = null;
		if(topicDescription!=null){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "topic");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("topic.description", topicDescription));
			result = (Topic) criteria.uniqueResult();
		}
		return result;
	}
}
