/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.dashboard.actions.content;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public abstract class AbstractTypeActions extends AbstractInstitutionAction {
    protected static final String CONVERT_VALIDATE_PUBLISH = "convert_validate_publish";
    protected static final String CONVERT_VALIDATE = "convert_validate";
    protected static final String DELETE = "delete";
    protected static final String UNPUBLISH = "unpublish";
    protected static final String PUBLISH = "publish";
    protected static final String CONVERT = "convert";
    protected static final String DELETE_ESE_EDM = "deleteEseEdm";
    protected static final String DELETE_FROM_QUEUE = "deleteFromQueue";
    protected static final String DELETE_FROM_EUROPEANA = "deleteFromEuropeana";
    protected static final String DELIVER_TO_EUROPEANA = "deliverToEuropeana";
    /**
     *
     */
    protected static final long serialVersionUID = 8481634493528974541L;
    protected Logger logger = Logger.getLogger(getClass());
    protected Integer xmlTypeId;
    protected String action;

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
        String option_default = (String) session.getAttribute(AjaxControllerAbstractAction.OPTIONS_DEFAULT);
        String option_use_existing = (String) session.getAttribute(AjaxControllerAbstractAction.OPTIONS_USE_EXISTING);
        boolean option_use_existing_bool = true;
        if (option_use_existing != null) {
            option_use_existing_bool = !Boolean.parseBoolean(option_use_existing);
        }
        if (option_default == null) {
            option_default = "UNSPECIFIED";
        }
        parameters.put("defaultRoleType", option_default);
        parameters.put("useDefaultRoleType", Boolean.toString(option_use_existing_bool));
        return parameters;
    }

    protected XmlType getXmlType() {
        return XmlType.getType(xmlTypeId);
    }

    public abstract String deleteEseEdm();

    public abstract String deleteFromEuropeana();

    public abstract String deliverToEuropeana();

    public abstract String deleteFromQueue();

}
