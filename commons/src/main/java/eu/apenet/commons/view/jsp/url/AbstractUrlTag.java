package eu.apenet.commons.view.jsp.url;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
/**
*
* @author Bastiaan Verhoef
*/
public abstract class AbstractUrlTag extends SimpleTagSupport {
	private String url;

	private String parameters;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String[] getParametersArray(){
		if (StringUtils.isNotBlank(parameters)){
			return parameters.split(",");
		}else {
			return new String[] {};
		}
	}
}
