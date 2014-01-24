package eu.archivesportaleurope.harvester.verb;

import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;


public class ListIdentifiersVerb extends AbstractListVerb{

	public ListIdentifiersVerb(OaiPmhHttpClient client) {
		super(client);
	}

	@Override
	protected String getVerb() {
		return "ListIdentifiers";
	}



}
