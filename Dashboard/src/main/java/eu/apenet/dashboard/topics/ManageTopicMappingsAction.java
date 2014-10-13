package eu.apenet.dashboard.topics;
	


import java.util.List;

import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.TopicMapping;

public class ManageTopicMappingsAction extends AbstractInstitutionAction {
	
	private static final long serialVersionUID = -5957772691331439159L;

	
	
	
	@Override
	protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dashboard.menu.topic.mappings"));
	}
	
    /**
	 * <p> Recover and show the information of the logged user. </p>
	 */
	
	public String execute() throws Exception {
		List<TopicMapping> mappings = DAOFactory.instance().getTopicMappingDAO().getTopicMappingsByAiId(this.getAiId());
		this.getServletRequest().setAttribute("topicMappings", mappings);

		return SUCCESS;
	}
	

}

