package eu.apenet.dashboard.topics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.Topic;
import eu.apenet.persistence.vo.TopicMapping;

public class CreateEditTopicMappingAction extends AbstractInstitutionAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2768649306123342939L;
	private List<SelectItem> topics = new ArrayList<SelectItem>();
	private Long topicId;
	private String topicDescription;
	private Long topicMappingId;
	private List<SelectItem> sourceGuides = new ArrayList<SelectItem>();
	private Integer sourceGuideId;
	private String keywords;

	
	public List<SelectItem> getTopics() {
		return topics;
	}

	public Long getTopicId() {
		return topicId;
	}

	public Long getTopicMappingId() {
		return topicMappingId;
	}

	public List<SelectItem> getSourceGuides() {
		return sourceGuides;
	}

	public Integer getSourceGuideId() {
		return sourceGuideId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setTopics(List<SelectItem> topics) {
		this.topics = topics;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public void setTopicMappingId(Long topicMappingId) {
		this.topicMappingId = topicMappingId;
	}

	public void setSourceGuides(List<SelectItem> sourceGuides) {
		this.sourceGuides = sourceGuides;
	}

	public void setSourceGuideId(Integer sourceGuideId) {
		this.sourceGuideId = sourceGuideId;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getTopicDescription() {
		return topicDescription;
	}

	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}

	protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dashboard.menu.topic.mappings.create"));

	}

	public void validate() {
		if (this.topicMappingId == null){
			if (this.getTopicId() == null){
				addFieldError("topicId", getText("errors.required"));
			}
		}
		if (this.getSourceGuideId() == null && StringUtils.isBlank(keywords)){
			addActionError(getText("topicmapping.sourceguide.keyword.required", new String[]{"<controlaccess><subject>"}));
		}
		if (StringUtils.isNotBlank(keywords) && convertKeywords().length() > 1000){
			addFieldError("keywords", getText("errors.toolong", new String[]{"1000"}));
		}
	}
	

	@Override
	public void prepare() throws Exception {
		if (topicMappingId == null){
			List<Topic> topics = DAOFactory.instance().getTopicDAO().getTopicsWithoutMapping(getAiId());
			for (Topic topic: topics){
				this.topics.add(new SelectItem(topic.getId(), topic.getDescription()));
			}
		}
		ContentSearchOptions options = new ContentSearchOptions();
		options.setArchivalInstitionId(getAiId());
		options.setContentClass(SourceGuide.class);
		options.setPageSize(100);
		List<Ead> eads = DAOFactory.instance().getEadDAO().getEads(options);
		for (Ead ead: eads){
			this.sourceGuides.add(new SelectItem(ead.getId(), ead.getIdentifier() + " - " + ead.getTitle()));
		}
		super.prepare();
	}

	@Override
	public String input() throws Exception {
		if (topicMappingId != null){
			TopicMapping topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndAiId(topicMappingId , getAiId());
			this.sourceGuideId = topicMapping.getSgId();
			this.topicDescription = topicMapping.getTopic().getDescription();
			this.keywords = topicMapping.getControlaccessKeyword();
		}
		return super.input();
	}

	public String execute() {
		TopicMapping topicMapping = new TopicMapping();
		if (topicMappingId == null){
			topicMapping.setTopicId(topicId);
			topicMapping.setAiId(getAiId());
		}else {
			topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndAiId(topicMappingId, getAiId());
		}
		if (topicMapping != null){
			topicMapping.setSgId(sourceGuideId);
			if (StringUtils.isBlank(keywords)){
				topicMapping.setControlaccessKeyword(null);
			}else {
				topicMapping.setControlaccessKeyword(convertKeywords());
			}
			DAOFactory.instance().getTopicMappingDAO().store(topicMapping);
		}
		

		return SUCCESS;
	}


	private String convertKeywords(){
		String input = keywords.trim().toLowerCase();
		input = input.replaceAll("\\s*\\|\\s*", "|");
		return input;
	}
	public String cancel() {
		return SUCCESS;
	}

}
