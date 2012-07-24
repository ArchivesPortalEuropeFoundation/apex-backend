package eu.apenet.commons.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class PageDescriptionTag extends AbstractPagingTag {

	private Logger log = Logger.getLogger(PageDescriptionTag.class);
	

	public void doTag() throws JspException, IOException {
		try {
		long numberOfItems =  toLong(this.getNumberOfItems());
		int pageNumber =  toInteger(this.getPageNumber());
		int pageSize =  toInteger(this.getPageSize());
		StringBuilder description = new StringBuilder();
		description.append((pageNumber-1)*pageSize + 1);
		description.append(StringEscapeUtils.escapeHtml(" - "));
		if (pageNumber*pageSize < numberOfItems) {
			description.append(pageNumber*pageSize);			
		}else {
			description.append(numberOfItems);
		}

		description.append(StringEscapeUtils.escapeHtml(" / "));
		description.append(numberOfItems);
		this.getJspContext().getOut().print(description);
		} catch (Exception e) {
			log.error("Can't create page description: " + e.getMessage(), e);
			throw new JspException(e);
		}
	}
}
