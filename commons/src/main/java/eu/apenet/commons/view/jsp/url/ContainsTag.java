package eu.apenet.commons.view.jsp.url;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.view.jsp.HrefObject;
/**
 * 
 * @author Bastiaan Verhoef
 */
public class ContainsTag extends AbstractUrlTag {

	private String var;

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public void doTag() throws JspException, IOException {
		HrefObject hrefObject = new HrefObject(getUrl());
		boolean contains = false;
		String[] parameters = getParametersArray(); 
		for (int i = 0; !contains && i < parameters.length; i++) {
			contains = contains || hrefObject.containsParameter(parameters[i]);
		}
		if (StringUtils.isBlank(var)){
			getJspContext().getOut().print(contains);
		}else {
			getJspContext().setAttribute(var, contains);
		}
	}


}
