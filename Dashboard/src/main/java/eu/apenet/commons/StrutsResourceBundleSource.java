package eu.apenet.commons;

import java.util.Locale;

import org.apache.struts2.util.TextProviderHelper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;


public class StrutsResourceBundleSource implements ResourceBundleSource {

	private ValueStack valueStack;
	private Locale locale;
	

	public StrutsResourceBundleSource(){
		this.valueStack = ActionContext.getContext().getValueStack();
		this.locale = ActionContext.getContext().getLocale();
	}
	@Override
	public String getString(String key) {
		return getString(key, UNKNOWN + key + UNKNOWN);
	}
	
	
	@Override
	public String getString(String key, String defaultValue) {
		return TextProviderHelper.getText(key, defaultValue, valueStack);
	}
	@Override
	public Locale getLocale() {
		return this.locale;
	}

}
