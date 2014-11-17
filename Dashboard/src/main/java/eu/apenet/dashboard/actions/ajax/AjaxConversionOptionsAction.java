package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.exceptions.APEnetException;

import org.json.JSONObject;
import java.io.Writer;
import javax.servlet.http.HttpSession;

/**
 * User: Yoann Moranville
 * Date: Feb 9, 2011
 *
 * @author Yoann Moranville
 */
public class AjaxConversionOptionsAction extends AjaxControllerAbstractAction {
    private static final long serialVersionUID = -5384301574672793021L;

    public String checkCurrentConversionOptions() {
        try {
            Writer writer = openOutputWriter();
            try {
            	// Get the conversion options from the session.
                HttpSession session = getServletRequest().getSession();
            	// Options related to DAO type.
                String option_default = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT);
                String option_use_existing = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_USE_EXISTING);
                // Options related to rights statement for digital objects.
                String option_default_rights_digital = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_DIGITAL);
                String option_rights_digital_description = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_DESCRIPTION);
                String option_rights_digital_holder = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_HOLDER);
                // Options related to rights statement for EAD data.
                String option_default_rights_ead = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_EAD);
                String option_rights_ead_description = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_DESCRIPTION);
                String option_rights_ead_holder = (String)session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_HOLDER);

                // Try to add  the options to the response JSON.
                if(option_default != null && option_use_existing != null
                		&& option_default_rights_digital != null
                		&& option_rights_digital_description != null
                		&& option_rights_digital_holder != null
                		&& option_default_rights_ead != null
                		&& option_rights_ead_description != null
                		&& option_rights_ead_holder != null){
                    JSONObject obj = new JSONObject();

                	// Options related to DAO type.
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_DEFAULT, option_default).put(AjaxConversionOptionsConstants.OPTIONS_USE_EXISTING, option_use_existing);
                    // Options related to rights statement for digital objects.
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_DIGITAL, option_default_rights_digital);
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_DESCRIPTION, option_rights_digital_description);
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_HOLDER, option_rights_digital_holder);
                    // Options related to rights statement for EAD data.
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_EAD, option_default_rights_ead);
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_DESCRIPTION, option_rights_ead_description);
                    obj.put(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_HOLDER, option_rights_ead_holder);

                    writer.append(obj.toString());
                } else
                    throw new APEnetException();
            } catch (Exception e){
                LOG.debug("Error when getting the session conversion options", e);
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                writer.append(obj.toString());
            }
            writer.close();
        } catch (Exception ioe){
            LOG.error("Error opening writer", ioe);
        }
        return null;
    }

    public String saveConversionOptions() {
        try {
            Writer writer = openOutputWriter();
            try {
            	// Get the conversion options from the request.
            	// Options related to DAO type.
                String option_default = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_DEFAULT);
                String option_use_existing = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_USE_EXISTING);
                // Options related to rights statement for digital objects.
                String option_default_rights_digital = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_DIGITAL);
                String option_rights_digital_description = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_DESCRIPTION);
                String option_rights_digital_holder = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_HOLDER);
                // Options related to rights statement for EAD data.
                String option_default_rights_ead = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_EAD);
                String option_rights_ead_description = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_DESCRIPTION);
                String option_rights_ead_holder = getServletRequest().getParameter(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_HOLDER);

                // Add the conversion options to the session.
                HttpSession session = getServletRequest().getSession();
            	// Options related to DAO type
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT, option_default);
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_USE_EXISTING, option_use_existing);
                // Options related to rights statement for digital objects.
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_DIGITAL, option_default_rights_digital);
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_DESCRIPTION, option_rights_digital_description);
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_HOLDER, option_rights_digital_holder);
                // Options related to rights statement for EAD data.
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_EAD, option_default_rights_ead);
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_DESCRIPTION, option_rights_ead_description);
                session.setAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_HOLDER, option_rights_ead_holder);

                JSONObject obj = new JSONObject();
                obj.put("error", false);
                writer.append(obj.toString());
            } catch (Exception e){
                LOG.error("Error when setting the session conversion options", e);
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                writer.append(obj.toString());
            }
            writer.close();
        } catch (Exception ioe){
            LOG.error("Error opening writer", ioe);
        }
        return null;
    }
}
