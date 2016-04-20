package eu.apenet.commons.solr;

import eu.apenet.commons.utils.APEnetUtilities;

public class EadSolrServerHolder extends AbstractSolrServerHolder {
	private static EadSolrServerHolder instance;


	private EadSolrServerHolder() {

	}

	public static EadSolrServerHolder getInstance() {
		if (instance == null) {
			instance = new EadSolrServerHolder();
		}
		return instance;
	}

	@Override
	public String getSolrUrl() {
		return APEnetUtilities.getDashboardConfig().getBaseSolrIndexUrl() + "/eads";
	}
}
