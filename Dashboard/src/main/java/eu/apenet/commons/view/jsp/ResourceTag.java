package eu.apenet.commons.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.apache.struts2.util.TextProviderHelper;
import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.ValueStack;

public class ResourceTag extends BodyTagSupport {
	  /**
	 * 
	 */
	private static final long serialVersionUID = -6095427587325955129L;
	private Logger log = Logger.getLogger(ResourceTag.class);

	@Override
	public int doEndTag() throws JspException {
		String result = "";
	    if (bodyContent != null && bodyContent.getString() != null)
		        result = bodyContent.getString().trim().toLowerCase();
		try {
			ValueStack valueStack = TagUtils.getStack(pageContext);
			result = TextProviderHelper.getText(result, result, valueStack);
			pageContext.getOut().print(result);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	    return EVAL_PAGE;
	}
	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

}
