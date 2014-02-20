package eu.apenet.dashboard.actions.content.ead;

import eu.apenet.dashboard.actions.content.AbstractTypeActions;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;

public abstract class AbstractEadActions extends AbstractTypeActions{
	private static final String VALIDATE = "validate";
	private static final String STATIC = "changeToStatic";
	private static final String DYNAMIC = "changeToDynamic";


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
	public abstract String validateEad();
	public abstract String convertEad(Properties properties);
	public abstract String publishEad();
	public abstract String unpublishEad();
	public abstract String deleteEad();
    public abstract String convertValidateEad(Properties properties);
	public abstract String convertValidatePublishEad(Properties properties);
	public abstract String changeToDynamic();
	public abstract String changeToStatic();
}
