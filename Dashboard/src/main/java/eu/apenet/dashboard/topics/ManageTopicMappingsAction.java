package eu.apenet.dashboard.topics;
	


import java.util.List;

import eu.apenet.dashboard.AbstractCountryAction;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.TopicMappingDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.TopicMapping;

public class ManageTopicMappingsAction extends AbstractCountryAction {
	
	private static final long serialVersionUID = -5957772691331439159L;
	private Long topicMappingId;
	
	
	public Long getTopicMappingId() {
		return topicMappingId;
	}

	public void setTopicMappingId(Long topicMappingId) {
		this.topicMappingId = topicMappingId;
	}

	@Override
	protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dashboard.menu.topic.mappings"));
	}
	
    /**
	 * <p> Recover and show the information of the logged user. </p>
	 */
	
	public String execute() throws Exception {
		List<TopicMapping> mappings;
		if(getAiId() != null) {
			mappings = DAOFactory.instance().getTopicMappingDAO().getTopicMappingsByAiId(this.getAiId());
		} else {
			mappings = DAOFactory.instance().getTopicMappingDAO().getTopicMappingsByCountryId(this.getCountryId());
		}
		this.getServletRequest().setAttribute("topicMappings", mappings);

		return SUCCESS;
	}
	
	public String deleteTopicMapping(){
		TopicMappingDAO topicMappingDAO = DAOFactory.instance().getTopicMappingDAO();
		TopicMapping topicMapping;
		if(getAiId() != null) {
			topicMapping = topicMappingDAO.getTopicMappingByIdAndAiId(topicMappingId, this.getAiId());
		} else {
			topicMapping = topicMappingDAO.getTopicMappingByIdAndCountryId(topicMappingId, this.getCountryId());
		}
		if (topicMapping != null){
			topicMappingDAO.delete(topicMapping);
		}
		return SUCCESS;
	}
}

