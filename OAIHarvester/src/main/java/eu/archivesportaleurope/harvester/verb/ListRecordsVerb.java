package eu.archivesportaleurope.harvester.verb;

import java.io.File;

import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;


public class ListRecordsVerb extends AbstractListVerb{
	
	public ListRecordsVerb(OaiPmhHttpClient client, String baseURL, String from, String until, String set,
			String metadataPrefix, OaiPmhParser oaiPmhParser, File errorDirectory) {
		super(client, baseURL, from, until, set, metadataPrefix, oaiPmhParser, errorDirectory);
	}

	@Override
	protected String getVerb() {
		return "ListRecords";
	}



}
