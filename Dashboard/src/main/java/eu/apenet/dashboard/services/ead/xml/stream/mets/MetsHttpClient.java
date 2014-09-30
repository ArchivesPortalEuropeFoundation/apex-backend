package eu.apenet.dashboard.services.ead.xml.stream.mets;

import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

public class MetsHttpClient extends OaiPmhHttpClient{
	@Override
	protected String getName() {
		return "Archives Portal Europe METS Harvester";
	}

}
