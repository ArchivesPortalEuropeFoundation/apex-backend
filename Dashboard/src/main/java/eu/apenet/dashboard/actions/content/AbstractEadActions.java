package eu.apenet.dashboard.actions.content;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;

public abstract class AbstractEadActions extends AbstractInstitutionAction implements ServletRequestAware{
    protected Logger logger = Logger.getLogger(getClass());
	private static final String CONVERT_VALIDATE_PUBLISH = "convert_validate_publish";
	private static final String DELETE = "delete";
	private static final String UNPUBLISH = "unpublish";
	private static final String PUBLISH = "publish";
	private static final String CONVERT = "convert";
	private static final String VALIDATE = "validate";
	private static final String DELETE_ESE_EDM = "deleteEseEdm";
	private static final String DELETE_FROM_EUROPEANA = "deleteFromEuropeana";	
	private static final String DELIVER_TO_EUROPEANA = "deliverToEuropeana";	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8481634493528974541L;
	private String type;
	private String action;
	private HttpServletRequest httpServletRequest;
	
	@Override
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public HttpServletRequest getServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String execute() throws Exception {
		if (VALIDATE.equals(action)){
			return validateEad();
		}else if (CONVERT.equals(action)){
			return convertEad();
		}else if (PUBLISH.equals(action)){
			return publishEad();
		}else if (UNPUBLISH.equals(action)){
			return unpublishEad();
		}else if (DELETE.equals(action)){
			return deleteEad();
		}else if(CONVERT_VALIDATE_PUBLISH.equals(action)){
			return convertValidatePublishEad();
		}else if(DELETE_ESE_EDM.equals(action)){
			return deleteEseEdm();
		}else if(DELETE_FROM_EUROPEANA.equals(action)){
			return deleteFromEuropeana();
		}else if(DELIVER_TO_EUROPEANA.equals(action)){
			return deliverToEuropeana();
		}
		return ERROR;
	}
	protected XmlType getXmlType(){
		return XmlType.getTypeBySolrPrefix(getType());
	}
	public abstract String validateEad();
	public abstract String convertEad();
	public abstract String publishEad();
	public abstract String unpublishEad();
	public abstract String deleteEad();
	public abstract String convertValidatePublishEad();
	public abstract String deleteEseEdm();
	public abstract String deleteFromEuropeana();
	public abstract String deliverToEuropeana();
}
