package eu.apenet.commons.solr;

import eu.apenet.commons.utils.APEnetUtilities;

public class EacCpfSolrServerHolder extends AbstractSolrServerHolder {
	private static EacCpfSolrServerHolder instance;
	private EacCpfSolrServerHolder() {

	}

	public static EacCpfSolrServerHolder getInstance() {
		if (instance == null) {
			instance = new EacCpfSolrServerHolder();
		}
		return instance;
	}
	@Override
	public String getSolrUrl() {
		return APEnetUtilities.getDashboardConfig().getBaseSolrIndexUrl() + "/eac-cpfs";
	}

}
