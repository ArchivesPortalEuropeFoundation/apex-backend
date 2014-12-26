package eu.apenet.dashboard.actions;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.TopicDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Topic;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Simple topics manager.
 * It's a struts2 action with CRUD operations.
 */
public class TopicsAction extends AbstractAction {
	
	private final static Logger log = Logger.getLogger(TopicsAction.class);
	
	private String validate;

	private List<Topic> adminTopics;
	private String topicId;
	private String adminTopicPropertyKey;
	private String adminTopicDescription;
	
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("admin.topic.management"));
	}
	
	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getAdminTopicPropertyKey() {
		return adminTopicPropertyKey;
	}

	public void setAdminTopicPropertyKey(String adminTopicPropertyKey) {
		this.adminTopicPropertyKey = adminTopicPropertyKey;
	}

	public String getAdminTopicDescription() {
		return adminTopicDescription;
	}

	public void setAdminTopicDescription(String adminTopicDescription) {
		this.adminTopicDescription = adminTopicDescription;
	}
	
	public String getTopicId() {
		return this.topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	public List<Topic> getAdminTopics() {
		return this.adminTopics;
	}

	public void setAdminTopics(List<Topic> adminTopics) {
		this.adminTopics = adminTopics;
	}
	
	/** VALIDATIONS **/
	
	@Override
	public void validate() {
		if(this.validate!=null){
			TopicDAO topicDAO = null;
			Topic topic = null;
			//adminTopicPropertyKey
			if(this.adminTopicPropertyKey==null || StringUtils.isEmpty(this.adminTopicPropertyKey)){
				addFieldError("adminTopicPropertyKey",getText("admin.topic.management.validation.empty"));
			}else{
				topicDAO = (topicDAO==null)?DAOFactory.instance().getTopicDAO():topicDAO;
				String description = topicDAO.getDescription(this.adminTopicPropertyKey);
				if(!StringUtils.isEmpty(description)){
					if(this.topicId!=null && this.topicId.matches("\\d*")){
						topic = topicDAO.findById(Long.parseLong(this.topicId));
						if(topic!=null && !topic.getPropertyKey().equals(this.adminTopicPropertyKey)){
							addFieldError("adminTopicPropertyKey",getText("admin.topic.management.validation.notunique"));
						}
					}else{
						addFieldError("adminTopicPropertyKey",getText("admin.topic.management.validation.notunique"));
					}
				}
			}
			//adminTopicDescription
			if(this.adminTopicDescription==null || StringUtils.isEmpty(this.adminTopicDescription)){
				addFieldError("adminTopicDescription",getText("admin.topic.management.validation.empty"));
			}else{
				topicDAO = DAOFactory.instance().getTopicDAO();
				Topic ddbbTopic = topicDAO.getTopicByDescription(this.adminTopicDescription);
				if(ddbbTopic!=null && ( (this.topicId==null) || ddbbTopic.getId()!=Long.parseLong(this.topicId))){
					addFieldError("adminTopicDescription",getText("admin.topic.management.validation.notunique"));
				}
			}
		}
	}
	
	/** ACTIONS **/
	
	public String execute(){
		if(SecurityContext.get()!=null && SecurityContext.get().isAdmin()){
			TopicDAO topicDAO = DAOFactory.instance().getTopicDAO();
			if(topicDAO!=null){
				this.adminTopics = topicDAO.findAll();
				if(this.adminTopics==null || this.adminTopics.size()==0){
					addActionMessage(getText("admin.topic.management.info.notopics"));
				}
				return SUCCESS;
			}
		}
		return ERROR;
	}
	
	public String store(){
		if(SecurityContext.get()!=null && SecurityContext.get().isAdmin()){
			Topic adminTopic = null;
			TopicDAO topicDAO = DAOFactory.instance().getTopicDAO();
			if(this.topicId!=null && this.topicId.matches("\\d*")){
				adminTopic = topicDAO.findById(Long.parseLong(this.topicId));
			}else{
				adminTopic = new Topic();
			}
			if(adminTopic!=null){
				adminTopic.setPropertyKey(this.adminTopicPropertyKey);
				adminTopic.setDescription(this.adminTopicDescription);
		
				try{
					JpaUtil.beginDatabaseTransaction();
					if(this.topicId!=null){
						topicDAO.update(adminTopic);
						addActionMessage(getText("admin.topic.management.info.topicupdated"));
					}else{
						topicDAO.store(adminTopic);
						addActionMessage(getText("admin.topic.management.info.topiccreated"));
					}
					JpaUtil.commitDatabaseTransaction();
				}catch(Exception e){
					addActionError(getText("admin.topic.management.error.topicnotcreatedorupdated"));
					if(e.getCause()!=null && e.getCause().getMessage()!=null){
						if(e.getCause().getMessage().contains("property_key")){ //unique restriction
							addActionError(getText("admin.topic.management.error.propertykeyexists"));
						}else{
							log.error("Unknown exception trying a store/update of topics",e);
						}
					}else{
						log.error("Unknown exception when was tried a update/elete for topics",e);
					}
					JpaUtil.rollbackDatabaseTransaction(); //flush
				}finally{
					if(!JpaUtil.noTransaction()){
						JpaUtil.closeDatabaseSession();
					}
				}
			}else{
				addActionError(getText("admin.topic.management.error.topicnotfound"));
			}
		}
		return execute();
	}
	
	public String createOrEdit(){
		if(SecurityContext.get()!=null && SecurityContext.get().isAdmin()){
			if(this.topicId!=null && this.topicId.matches("\\d*")){
				Topic adminTopic = DAOFactory.instance().getTopicDAO().findById(Long.parseLong(this.topicId));
				if(adminTopic!=null){
					this.topicId = adminTopic.getId().toString();
					this.adminTopicPropertyKey = adminTopic.getPropertyKey();
					this.adminTopicDescription = adminTopic.getDescription();
				}
			}
			return SUCCESS;
		}
		return ERROR;
	}
	
	public String delete(){
		if(SecurityContext.get()!=null && SecurityContext.get().isAdmin()){
			if(this.topicId!=null && this.topicId.matches("\\d*")){
				Topic topic = null;
				try{
					JpaUtil.beginDatabaseTransaction();
					TopicDAO topicDAO = DAOFactory.instance().getTopicDAO();
					topic = topicDAO.findById(Long.parseLong(this.topicId));
					if(topic!=null){
						topicDAO.delete(topic);
						JpaUtil.commitDatabaseTransaction();
						addActionMessage(getText("admin.topic.management.info.topicdeleted"));
					}else{
						addActionError(getText("admin.topic.management.error.topicnotfound"));
					}
				}catch(Exception e){
					//addActionError(getText("admin.topic.management.error.topicnotdeleted"));
					addActionError(getText("admin.topic.management.error.topichastopicmappings"));
					log.error("Unknown exception trying a delete of topics",e);
					JpaUtil.rollbackDatabaseTransaction();
				}finally{
					JpaUtil.closeDatabaseSession();
				}
			}
		}
		return execute();
	}
}
