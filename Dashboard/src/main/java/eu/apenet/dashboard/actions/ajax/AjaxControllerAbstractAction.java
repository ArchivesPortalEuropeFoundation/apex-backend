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
public abstract class AjaxControllerAbstractAction extends AbstractInstitutionAction {
    private static final long serialVersionUID = -385844504423841865L;

    public static final String LIST_IDS = "listSelectedFAs";

    public static final String OPTIONS_USE_EXISTING = "optsUseExisting";
    public static final String OPTIONS_DEFAULT = "optsDefault";

    protected static final Logger LOG = Logger.getLogger(AjaxControllerAbstractAction.class);
    protected static final String UTF8 = "utf-8";


    protected Writer openOutputWriter() throws IOException {
        getServletRequest().setCharacterEncoding(UTF8);
        getServletResponse().setCharacterEncoding(UTF8);
        getServletResponse().setContentType("application/json");
        return new OutputStreamWriter(getServletResponse().getOutputStream(), UTF8);
    }

    @Override
    public String execute(){
        return SUCCESS;
    }
}
