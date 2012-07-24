package eu.apenet.dashboard;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: Aug 20, 2010
 *
 * @author Yoann Moranville
 */
public class IndexAction extends ActionSupport {

    @Override
    public String execute() throws Exception {
//        Map<String, Object> session = ActionContext.getContext().getSession();
//        if(session.containsKey("logged") && session.get("logged").equals(true)){
            return SUCCESS;
//        }
//        return ERROR;
    }

}