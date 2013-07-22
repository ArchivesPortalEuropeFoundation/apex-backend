package eu.apenet.dashboard.language;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

/**
 * 
 * @author Eloy
 *
 * This action retrieves the current action that the user is executing when
 * he/she decides to change the interface language
 * This current page is stored in the header
 */
@SuppressWarnings("serial")
public class ChangeLanguageAction extends ActionSupport {
	
	private static final String ATTRIBUTE_FOR_SELECTING_LANGUAGE_IN_TREE = "navTreeLang";
	private static final String INDEX_ACTION = "index.action";
	private static final String LOGIN_ACTION = "login.action";
	private static final String LOGOUT_ACTION = "logout.action";
	private Logger log = Logger.getLogger(ChangeLanguageAction.class);
	private String url;
	private String currentAction;

	public String getUrl()
	{
	 return url;
	}
	
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	public String getCurrentAction() {
		return currentAction;
	}

	public String execute() throws Exception {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		Object language = null;
		
    	if(session.get("WW_TRANS_I18N_LOCALE")==null){
    		language = session.get("org.apache.tiles.LOCALE");
    	}else{    		
    		language = session.get("WW_TRANS_I18N_LOCALE");
    	}

		url = this.changeLanguageInAction(this.getCurrentAction(), language.toString());		    	
		return "redirect";
	}

	private String changeLanguageInAction(String actionStoredInHeader, String languageStoredInSession) {
		String url = null;
		String target = null;
		String replacement = null;
		Integer attributeForSelectingLanguageInTreeLenght = ATTRIBUTE_FOR_SELECTING_LANGUAGE_IN_TREE.length() + 3;
		
		if (actionStoredInHeader == null || actionStoredInHeader.isEmpty()){
			//or there is no current action stored in the header, (maybe it is 
			//necessary to add changeLanguage interceptor 
			//<interceptor-ref name="changeLanguageStack"/> in struts 
			//within the action of the section from which the user wanted to 
			//change the language)
			//In this case, the user is redirected to home page
			actionStoredInHeader = INDEX_ACTION;
			log.info("WARNING: There is an action in which it has not been included the interceptor stack changeLanguageStack");
			
		}
		else if (actionStoredInHeader.equalsIgnoreCase(LOGOUT_ACTION)) {
			//The current action is logout action (the user has just logged out the System)
			//In this case, the user is redirected to login page
			actionStoredInHeader = LOGIN_ACTION;
		}
		if (actionStoredInHeader.contains(ATTRIBUTE_FOR_SELECTING_LANGUAGE_IN_TREE)){
			//It is necessary to change the language stored in the action within
			//the header with the new one stored in session
			replacement = ATTRIBUTE_FOR_SELECTING_LANGUAGE_IN_TREE + "=" + languageStoredInSession;
			target = actionStoredInHeader.substring(actionStoredInHeader.indexOf(ATTRIBUTE_FOR_SELECTING_LANGUAGE_IN_TREE), actionStoredInHeader.indexOf(ATTRIBUTE_FOR_SELECTING_LANGUAGE_IN_TREE) + attributeForSelectingLanguageInTreeLenght);			
			actionStoredInHeader = actionStoredInHeader.replace(target, replacement);
			
		}
		
		url = actionStoredInHeader;

		return url;
	}
	
}

