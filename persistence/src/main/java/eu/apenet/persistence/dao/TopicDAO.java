package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Topic;

public interface TopicDAO extends GenericDAO<Topic, Long> {


	public String getDescription(String propertyKey);
	public List<Topic> getTopicsWithoutMapping(Integer aiId);
}
