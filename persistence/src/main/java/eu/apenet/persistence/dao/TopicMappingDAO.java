package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.TopicMapping;

public interface TopicMappingDAO extends GenericDAO<TopicMapping, Long> {

	public List<TopicMapping> getTopicMappingsByAiId(Integer aiId);
	public List<TopicMapping> getTopicMappingsByCountryId(Integer countryId);
	public TopicMapping getTopicMappingByIdAndAiId( Long id, Integer aiId);
	public TopicMapping getTopicMappingByIdAndCountryId( Long id, Integer countryId);
	public TopicMapping getTopicMappingByTopicIdAndCountryId( Long topicId, Integer countryId);

}
