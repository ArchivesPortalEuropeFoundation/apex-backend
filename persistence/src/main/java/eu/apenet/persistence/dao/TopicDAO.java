package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Topic;

public interface TopicDAO extends GenericDAO<Topic, Long> {


	public String getDescription(String propertyKey);

}
