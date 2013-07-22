package eu.apenet.dashboard.language;

import java.io.Serializable;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * @author Eloy
 * 
 * This interceptor stores in the request the current section that a user
 * is visiting (actually, the current action with all its parameters)
 *
 */
public class ChangeLanguageInterceptor extends AbstractInterceptor implements Serializable {
  	
	public static final long serialVersionUID = -7617672835240112126L;
	public static final String CURRENT_ACTION_KEY = "CURRENT_ACTION";
	private static final String ACTION_SUFFIX = ".action";
	private static final String QUERY_SYMBOL = "?";

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String parameters = ServletActionContext.getRequest().getQueryString();
		String currentAction = ServletActionContext.getActionMapping().getName();
		String currentActionWithParameters = currentAction;
		
		if (!(parameters == null)) {
			currentActionWithParameters = currentActionWithParameters + ACTION_SUFFIX + QUERY_SYMBOL + parameters;
		}
		else {
			currentActionWithParameters = currentActionWithParameters + ACTION_SUFFIX;
		}
		ServletActionContext.getRequest().setAttribute(CURRENT_ACTION_KEY, currentActionWithParameters);
		return invocation.invoke();
		
	}
	
}
