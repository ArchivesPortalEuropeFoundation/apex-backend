package eu.archivesportaleurope.harvester.verb;

import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;


public class ListRecordsVerb extends AbstractListVerb{

	public ListRecordsVerb(OaiPmhHttpClient client) {
		super(client);
	}

	@Override
	protected String getVerb() {
		return "ListRecords";
	}



}
