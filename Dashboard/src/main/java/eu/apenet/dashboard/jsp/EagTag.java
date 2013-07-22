package eu.apenet.dashboard.jsp;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.xslt.tags.AbstractEagTag;

public class EagTag extends AbstractEagTag {
	@Override
	protected ResourceBundleSource getResourceBundleSource() {
		return new StrutsResourceBundleSource();
	}

}
