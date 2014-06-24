package eu.apenet.dashboard.jsp;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.xslt.tags.AbstractEadTag;

public class EadTag extends AbstractEadTag {

	@Override
	protected ResourceBundleSource getResourceBundleSource() {
		return new StrutsResourceBundleSource();
	}

	@Override
	protected String getSolrStopwordsUrl() {
		return null;
	}

	@Override
	protected boolean isDashboard() {
		return true;
	}

	
}
