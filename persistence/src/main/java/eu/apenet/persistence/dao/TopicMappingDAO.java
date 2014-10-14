package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.TopicMapping;

public interface TopicMappingDAO extends GenericDAO<TopicMapping, Long> {

	public List<TopicMapping> getTopicMappingsByAiId(Integer aiId);
	public TopicMapping getTopicMappingByIdAndAiId( Long id, Integer aiId);

}
