package eu.apenet.dashboard.language;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ChangeLanguageTag extends SimpleTagSupport {
	private String varSelectedLanguage;
	private String varCurrentAction;
	private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("de", "el", "en", "es", "fr", "ga", "lv",
			"mt", "nl", "pl", "pt", "sl", "fi", "sv");



	public String getVarSelectedLanguage() {
		return varSelectedLanguage;
	}



	public void setVarSelectedLanguage(String varSelectedLanguage) {
		this.varSelectedLanguage = varSelectedLanguage;
	}



	public String getVarCurrentAction() {
		return varCurrentAction;
	}



	public void setVarCurrentAction(String varCurrentAction) {
		this.varCurrentAction = varCurrentAction;
	}



	@Override
	public void doTag() throws JspException, IOException {
		Map<String, String> selectedLanguage = new HashMap<String, String>();
		PageContext pageContext = (PageContext) this.getJspContext();
		ServletRequest servletRequest = pageContext.getRequest();
		Locale locale = pageContext.getRequest().getLocale();
		Locale wsTransI18nLocale = (Locale) pageContext.getSession().getAttribute("WW_TRANS_I18N_LOCALE");
		String language = locale.getLanguage();
		if (wsTransI18nLocale != null) {
			language = wsTransI18nLocale.getLanguage();
		}
		if (SUPPORTED_LANGUAGES.contains(language)) {
			selectedLanguage.put(language, "selected='selected'");
		} else {
			selectedLanguage.put("en", "selected='selected'");
		}
		this.getJspContext().setAttribute(varSelectedLanguage, selectedLanguage);
		String currentAction = (String) servletRequest.getAttribute(ChangeLanguageInterceptor.CURRENT_ACTION_KEY);
		if (currentAction == null){
			currentAction = "index.action";
		}
		this.getJspContext().setAttribute(varCurrentAction, currentAction);
	}

}
