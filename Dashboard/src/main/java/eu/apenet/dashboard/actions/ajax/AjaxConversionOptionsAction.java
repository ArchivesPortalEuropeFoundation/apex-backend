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
                HttpSession session = request.getSession();
                String option_default = (String)session.getAttribute(OPTIONS_DEFAULT);
                String option_use_existing = (String)session.getAttribute(OPTIONS_USE_EXISTING);

                if(option_default != null && option_use_existing != null){
                    JSONObject obj = new JSONObject();
                    obj.put(OPTIONS_DEFAULT, option_default).put(OPTIONS_USE_EXISTING, option_use_existing);
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
                String option_default = request.getParameter(OPTIONS_DEFAULT);
                String option_use_existing = request.getParameter(OPTIONS_USE_EXISTING);
                HttpSession session = request.getSession();
                session.setAttribute(OPTIONS_DEFAULT, option_default);
                session.setAttribute(OPTIONS_USE_EXISTING, option_use_existing);

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
