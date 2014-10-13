package eu.apenet.dashboard.topics;

import java.util.ArrayList;
import java.util.List;

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
	private Integer topicId;
	private String topicDescription;
	private Long topicMappingId;
	private List<SelectItem> sourceGuides = new ArrayList<SelectItem>();
	private Integer sourceGuideId;
	private String keywords;

	
	public List<SelectItem> getTopics() {
		return topics;
	}

	public Integer getTopicId() {
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

	public void setTopicId(Integer topicId) {
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
		
//		if (existingPartnerId == null) {
//			if (StringUtils.isBlank(this.getFirstName())) {
//				addFieldError("firstName", getText("firstname.required"));
//			}
//
//			if (StringUtils.isBlank(this.getLastName())) {
//				addFieldError("lastName", getText("lastname.required"));
//			}
//
//			if (this.getEmail() != null) {
//				if (StringUtils.isBlank(this.getEmail())) {
//					addFieldError("email", getText("email.required"));
//				} else {
//					String email = this.getEmail().trim();
//					if (UserService.exitsEmailUser(email)) {
//						addFieldError("email", getText("email.notAvailable"));
//					}
//					else {
//                        //RFC regexp for emails
//                        String expression = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
//			            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
//			            Matcher matcher = pattern.matcher(email);
//			            if(!matcher.matches())
//			                addFieldError("email", getText("email.valid"));
//					}
//				}
//			}
//		}

	}
	

	@Override
	public void prepare() throws Exception {
		if (topicMappingId == null){
			List<Topic> topics = DAOFactory.instance().getTopicDAO().findAll();
			for (Topic topic: topics){
				this.topics.add(new SelectItem(topic.getId(), topic.getPropertyKey() + " - " + topic.getDescription()));
			}
		}else {
			TopicMapping topicMapping = DAOFactory.instance().getTopicMappingDAO().findById(topicMappingId);
			this.sourceGuideId = topicMapping.getSgId();
			this.topicDescription = topicMapping.getTopic().getDescription();
			this.keywords = topicMapping.getControlaccessKeyword();
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
		return super.input();
	}

	public String execute() {

		return SUCCESS;
	}


	public String cancel() {
		return SUCCESS;
	}

}
