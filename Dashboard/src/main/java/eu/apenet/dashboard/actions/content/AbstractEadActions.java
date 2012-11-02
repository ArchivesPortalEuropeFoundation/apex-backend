package eu.apenet.dashboard.actions.content;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;

public abstract class AbstractEadActions extends AbstractInstitutionAction{

	private static final String CONVERT_VALIDATE_PUBLISH = "convert_validate_publish";
	private static final String DELETE = "delete";
	private static final String UNPUBLISH = "unpublish";
	private static final String PUBLISH = "publish";
	private static final String CONVERT = "convert";
	private static final String VALIDATE = "validate";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8481634493528974541L;
	private Integer type;
	private String action;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
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
		}
		return ERROR;
	}
	protected XmlType getXmlType(){
		return XmlType.getType(getType());
	}
	public abstract String validateEad();
	public abstract String convertEad();
	public abstract String publishEad();
	public abstract String unpublishEad();
	public abstract String deleteEad();
	public abstract String convertValidatePublishEad();

}
