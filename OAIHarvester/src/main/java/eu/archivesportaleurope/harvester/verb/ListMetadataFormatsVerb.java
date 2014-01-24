package eu.archivesportaleurope.harvester.verb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

/**
 * User: Yoann Moranville
 * Date: 17/10/2013
 *
 * @author Yoann Moranville
 */
public class ListMetadataFormatsVerb extends AbstractMetadateFormatsAndSetsVerb {

    public ListMetadataFormatsVerb(OaiPmhHttpClient client) {
		super(client);
	}

	public OaiPmhElements run(String baseURL) throws Exception {
        return harvest(getRequestURL(baseURL), AbstractMetadateFormatsAndSetsVerb.METADATA_FORMAT);
    }

    public OaiPmhElements run(String baseURL, String resumptionToken) throws Exception {
        return harvest(getRequestURL(baseURL, resumptionToken), AbstractMetadateFormatsAndSetsVerb.METADATA_FORMAT);
    }

    private static String getRequestURL(String baseURL) {
        StringBuilder requestURL =  new StringBuilder(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=ListMetadataFormats");
        else
            requestURL.append("?verb=ListMetadataFormats");
        return requestURL.toString();
    }

    private static String getRequestURL(String baseURL, String resumptionToken) throws UnsupportedEncodingException {
        StringBuilder requestURL =  new StringBuilder(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=ListMetadataFormats");
        else
            requestURL.append("?verb=ListMetadataFormats");
        requestURL.append("&resumptionToken=").append(URLEncoder.encode(resumptionToken, "UTF-8"));
        return requestURL.toString();
    }
}
