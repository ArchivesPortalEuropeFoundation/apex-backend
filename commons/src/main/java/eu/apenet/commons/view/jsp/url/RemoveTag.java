package eu.apenet.commons.view.jsp.url;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.view.jsp.HrefObject;

/**
 * 
 * @author Bastiaan Verhoef
 */
public class RemoveTag extends AbstractUrlTag {
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
		for (String parameter : getParametersArray()) {
			hrefObject.removeParameter(parameter);
		}
		if (StringUtils.isBlank(var)){
			getJspContext().getOut().print(hrefObject.toString());
		}else {
			getJspContext().setAttribute(var, hrefObject.toString());
		}		
	}

}
