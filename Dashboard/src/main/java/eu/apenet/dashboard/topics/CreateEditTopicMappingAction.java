package eu.apenet.dashboard.topics;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.dashboard.AbstractCountryAction;
import eu.apenet.persistence.vo.*;
import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;

public class CreateEditTopicMappingAction extends AbstractCountryAction {

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
	private String keywordsCountryManager;

	
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

	public String getKeywordsCountryManager() {
		return keywordsCountryManager;
	}

	public void setKeywordsCountryManager(String keywordsCountryManager) {
		this.keywordsCountryManager = keywordsCountryManager;
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
			List<Topic> topics;
			if(getAiId() != null) {
				topics = DAOFactory.instance().getTopicDAO().getTopicsWithoutMapping(getAiId());
			} else {
				topics = DAOFactory.instance().getTopicDAO().getTopicsWithoutMappingPerCountry(getCountryId());
			}
			for (Topic topic: topics){
				this.topics.add(new SelectItem(topic.getId(), topic.getDescription()));
			}
		} else {
			TopicMapping topicMapping;
			if(getAiId() != null) {
				topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndAiId(topicMappingId, getAiId());
			} else {
				topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndCountryId(topicMappingId, getCountryId());
			}
			this.topicDescription = topicMapping.getTopic().getDescription();
		}
		if(getAiId() == null) {
			Country country = DAOFactory.instance().getCountryDAO().findById(getCountryId());
			for(ArchivalInstitution archivalInstitution : country.getArchivalInstitutions()) {
				ContentSearchOptions options = new ContentSearchOptions();
				options.setArchivalInstitionId(archivalInstitution.getAiId());
				options.setContentClass(SourceGuide.class);
				options.setPageSize(100);
				List<Ead> eads = DAOFactory.instance().getEadDAO().getEads(options);
				for (Ead ead : eads) {
					this.sourceGuides.add(new SelectItem(ead.getId(), ead.getIdentifier() + " - " + ead.getTitle()));
				}
			}
		} else {
			ContentSearchOptions options = new ContentSearchOptions();
			options.setArchivalInstitionId(getAiId());
			options.setContentClass(SourceGuide.class);
			options.setPageSize(100);
			List<Ead> eads = DAOFactory.instance().getEadDAO().getEads(options);
			for (Ead ead : eads) {
				this.sourceGuides.add(new SelectItem(ead.getId(), ead.getIdentifier() + " - " + ead.getTitle()));
			}
		}
		super.prepare();
	}

	@Override
	public String input() throws Exception {
		if (topicMappingId != null){
			TopicMapping topicMapping;
			if(getAiId() != null) {
				topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndAiId(topicMappingId, getAiId());
				TopicMapping topicMappingCountryManager = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByTopicIdAndCountryId(topicMapping.getTopicId(), getCountryId());
				if(topicMappingCountryManager != null) {
					this.keywordsCountryManager = topicMappingCountryManager.getControlaccessKeyword();
				}
			} else {
				topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndCountryId(topicMappingId, getCountryId());
			}
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
			if(getAiId() != null) {
				topicMapping.setAiId(getAiId());
			} else {
				topicMapping.setCountryId(getCountryId());
			}
		} else {
			if(getAiId() != null) {
				topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndAiId(topicMappingId, getAiId());
			} else {
				topicMapping = DAOFactory.instance().getTopicMappingDAO().getTopicMappingByIdAndCountryId(topicMappingId, getCountryId());
			}
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
