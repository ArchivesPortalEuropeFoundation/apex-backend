package eu.apenet.dashboard.jsp;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.xslt.tags.AbstractEacTag;
import eu.apenet.commons.xslt.tags.AbstractEadTag;

public class EacTag extends AbstractEacTag {

	@Override
	protected ResourceBundleSource getResourceBundleSource() {
		return new StrutsResourceBundleSource();
	}

	@Override
	protected String getSolrStopwordsUrl() {
		return null;
	}

	@Override
	protected boolean isPreview() {
		return true;
	}

	
}
