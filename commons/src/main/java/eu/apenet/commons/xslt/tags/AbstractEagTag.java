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
    private String preview;
    private final static Logger LOG = Logger.getLogger(AbstractEagTag.class);

    private String currentAIRepositorCode;
    private String requiredAIRepositorCode;

    public void doTag() throws JspException, IOException {
        try {
            EagXslt.displayAiDetails("true".equals(preview), this.getJspContext().getOut(), new File(eagUrl), this.getResourceBundleSource(), this.getCurrentAIRepositorCode(), this.getRequiredAIRepositorCode());
        } catch (SaxonApiException e) {
            LOG.error(e.getMessage());
        }
    }

    protected abstract ResourceBundleSource getResourceBundleSource();

    public String getEagUrl() {
        return eagUrl;
    }

    public void setEagUrl(String eagUrl) {
        this.eagUrl = eagUrl;
    }

    public String getCurrentAIRepositorCode() {
        return this.currentAIRepositorCode;
    }

    public void setCurrentAIRepositorCode(String currentAIRepositorCode) {
        this.currentAIRepositorCode = currentAIRepositorCode;
    }

    public String getRequiredAIRepositorCode() {
        return this.requiredAIRepositorCode;
    }

    public void setRequiredAIRepositorCode(String requiredAIRepositorCode) {
        this.requiredAIRepositorCode = requiredAIRepositorCode;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

}
