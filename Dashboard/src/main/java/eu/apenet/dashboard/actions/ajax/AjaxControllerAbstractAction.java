package eu.apenet.dashboard.actions.ajax;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.AbstractInstitutionAction;

/**
 * User: Yoann Moranville
 * Date: 3/11/11
 *
 * @author Yoann Moranville
 */
public abstract class AjaxControllerAbstractAction extends AbstractInstitutionAction {
    private static final long serialVersionUID = -385844504423841865L;

    public static final String LIST_IDS = "listSelectedFAs";

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
