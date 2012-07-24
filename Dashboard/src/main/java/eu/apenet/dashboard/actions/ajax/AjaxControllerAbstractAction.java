package eu.apenet.dashboard.actions.ajax;

import eu.apenet.dashboard.AbstractInstitutionAction;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 3/11/11
 *
 * @author Yoann Moranville
 */
public abstract class AjaxControllerAbstractAction extends AbstractInstitutionAction implements ServletRequestAware, ServletResponseAware {
    private static final long serialVersionUID = -385844504423841865L;

    protected static final String LIST_HARVEST = "listHarvestedFiles";
    protected static final String LIST_UPLOADED = "listUploadedFiles";
    public static final String LIST_IDS = "listSelectedFAs";
    protected static final String EAD_METADATA_FORMAT = "ead";
    protected static final String OAI_DC_METADATA_FORMAT = "oai_dc";

    public final static String FI_TYPE = "Finland - France EAD";
    public final static String PT_TYPE = "Portugal OAI_DC";

    public static final String OPTIONS_USE_EXISTING = "optsUseExisting";
    public static final String OPTIONS_DEFAULT = "optsDefault";

    protected static final Logger LOG = Logger.getLogger(AjaxControllerAbstractAction.class);
    protected static final String UTF8 = "utf-8";

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected Writer openOutputWriter() throws IOException {
        request.setCharacterEncoding(UTF8);
        response.setCharacterEncoding(UTF8);
        response.setContentType("application/json");
        return new OutputStreamWriter(response.getOutputStream(), UTF8);
    }

    protected Map<String, String> getConversionParameters(){
        Map<String, String> parameters = new HashMap<String, String>();
        HttpSession session = request.getSession();
        String option_default = (String)session.getAttribute(AjaxControllerAbstractAction.OPTIONS_DEFAULT);
        String option_use_existing = (String)session.getAttribute(AjaxControllerAbstractAction.OPTIONS_USE_EXISTING);
        boolean option_use_existing_bool = !Boolean.parseBoolean(option_use_existing);
        parameters.put("defaultRoleType", option_default);
        parameters.put("useDefaultRoleType", Boolean.toString(option_use_existing_bool));
        return parameters;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    @Override
    public String execute(){
        return SUCCESS;
    }
}
