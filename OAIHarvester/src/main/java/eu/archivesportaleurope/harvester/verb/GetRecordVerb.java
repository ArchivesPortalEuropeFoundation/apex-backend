package eu.archivesportaleurope.harvester.verb;

import java.io.File;
import java.io.UnsupportedEncodingException;

import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.ResultInfo;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

public class GetRecordVerb extends AbstractListVerb {

	private String identifier;
	public GetRecordVerb(OaiPmhHttpClient client, String baseURL, String metadataPrefix, OaiPmhParser oaiPmhParser,
			File errorDirectory) {
		super(client, baseURL, null, null, null, metadataPrefix, oaiPmhParser, errorDirectory);
	}

	@Override
	protected String getVerb() {
		return "GetRecord";
	}

	public ResultInfo harvest(HarvestObject harvestObject, String identifier) throws Exception {
		this.identifier = identifier;
		return super.harvest(harvestObject);
	}

	@Override
	public ResultInfo harvest(HarvestObject harvestObject){
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getRequestURL() throws UnsupportedEncodingException {
        StringBuffer requestURL =  new StringBuffer(getBaseURL());
        requestURL.append("?verb=GetRecord");
        requestURL.append("&identifier=").append(identifier);
        requestURL.append("&metadataPrefix=").append(getMetadataPrefix());
        return requestURL.toString();
	}
	

}
