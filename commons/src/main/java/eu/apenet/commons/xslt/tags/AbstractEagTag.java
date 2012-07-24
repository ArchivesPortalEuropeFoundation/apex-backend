package eu.apenet.commons.xslt.tags;

import java.io.File;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.sf.saxon.s9api.SaxonApiException;

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.xslt.EagXslt;

public abstract class AbstractEagTag extends SimpleTagSupport {
	private String eagUrl;
	private final static Logger LOG = Logger.getLogger(AbstractEagTag.class);

	public void doTag() throws JspException, IOException {
		try {
			EagXslt.displayAiDetails(this.getJspContext().getOut(), new File(eagUrl), getResourceBundleSource());
		} catch (SaxonApiException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected abstract ResourceBundleSource getResourceBundleSource();

	public String getEagUrl() {
		return eagUrl;
	}

	public void setEagUrl(String eagUrl) {
		this.eagUrl = eagUrl;
	}

}
