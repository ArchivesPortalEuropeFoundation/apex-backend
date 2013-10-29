package eu.apenet.dashboard.actions.content;

import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;

public abstract class AbstractEadActions extends AbstractInstitutionAction{
    protected Logger logger = Logger.getLogger(getClass());
	private static final String CONVERT_VALIDATE_PUBLISH = "convert_validate_publish";
	private static final String CONVERT_VALIDATE = "convert_validate";
	private static final String DELETE = "delete";
	private static final String UNPUBLISH = "unpublish";
	private static final String PUBLISH = "publish";
	private static final String CONVERT = "convert";
	private static final String VALIDATE = "validate";
	private static final String DELETE_ESE_EDM = "deleteEseEdm";
	private static final String DELETE_FROM_QUEUE = "deleteFromQueue";	
	private static final String DELETE_FROM_EUROPEANA = "deleteFromEuropeana";	
	private static final String DELIVER_TO_EUROPEANA = "deliverToEuropeana";	
	private static final String STATIC = "changeToStatic";
	private static final String DYNAMIC = "changeToDynamic";
	/**
	 * 
	 */
	private static final long serialVersionUID = 8481634493528974541L;
	private Integer xmlTypeId;
	private String action;


	public Integer getXmlTypeId() {
		return xmlTypeId;
	}

	public void setXmlTypeId(Integer xmlTypeId) {
		this.xmlTypeId = xmlTypeId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
    protected Properties getConversionParameters() {
        Properties parameters = new Properties();
        HttpSession session = getServletRequest().getSession();
        String option_default = (String)session.getAttribute(AjaxControllerAbstractAction.OPTIONS_DEFAULT);
        String option_use_existing = (String)session.getAttribute(AjaxControllerAbstractAction.OPTIONS_USE_EXISTING);
        boolean option_use_existing_bool = true;
        if(option_use_existing != null)
            option_use_existing_bool = !Boolean.parseBoolean(option_use_existing);
        if(option_default == null)
            option_default = "UNSPECIFIED";
        parameters.put("defaultRoleType", option_default);
        parameters.put("useDefaultRoleType", Boolean.toString(option_use_existing_bool));
        return parameters;
    }


	@Override
	public String execute() throws Exception {
		if (VALIDATE.equals(action)){
			return validateEad();
		}else if (CONVERT.equals(action)){
			return convertEad(getConversionParameters());
		}else if (PUBLISH.equals(action)){
			return publishEad();
		}else if (UNPUBLISH.equals(action)){
			return unpublishEad();
		}else if (DELETE.equals(action)){
			return deleteEad();
        }else if (CONVERT_VALIDATE.equals(action)) {
            return convertValidateEad(getConversionParameters());
		}else if(CONVERT_VALIDATE_PUBLISH.equals(action)){
			return convertValidatePublishEad(getConversionParameters());
		}else if(DELETE_ESE_EDM.equals(action)){
			return deleteEseEdm();
		}else if(DELETE_FROM_EUROPEANA.equals(action)){
			return deleteFromEuropeana();
		}else if(DELIVER_TO_EUROPEANA.equals(action)){
			return deliverToEuropeana();
		}else if(DELETE_FROM_QUEUE.equals(action)){
			return deleteFromQueue();
		}else if(DYNAMIC.equals(action)){
			return changeToDynamic();
		}else if(STATIC.equals(action)){
			return changeToStatic();
		}
		return ERROR;
	}
	protected XmlType getXmlType(){
		return XmlType.getType(xmlTypeId);
	}
	public abstract String validateEad();
	public abstract String convertEad(Properties properties);
	public abstract String publishEad();
	public abstract String unpublishEad();
	public abstract String deleteEad();
    public abstract String convertValidateEad(Properties properties);
	public abstract String convertValidatePublishEad(Properties properties);
	public abstract String deleteEseEdm();
	public abstract String deleteFromEuropeana();
	public abstract String deliverToEuropeana();
	public abstract String deleteFromQueue();
	public abstract String changeToDynamic();
	public abstract String changeToStatic();
}
