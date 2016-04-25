package eu.apenet.commons.solr;

import eu.apenet.commons.utils.APEnetUtilities;

public class EagSolrServerHolder extends AbstractSolrServerHolder {
	private static EagSolrServerHolder instance;
	private EagSolrServerHolder() {

	}

	public static EagSolrServerHolder getInstance() {
		if (instance == null) {
			instance = new EagSolrServerHolder();
		}
		return instance;
	}
	@Override
	public String getSolrUrl() {
		return APEnetUtilities.getDashboardConfig().getBaseSolrIndexUrl() + "/eags";
	}

}
