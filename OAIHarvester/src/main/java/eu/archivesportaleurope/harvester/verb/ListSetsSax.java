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
public class ListSetsSax extends HarvesterVerbSaxMemory {

    public ListSetsSax(OaiPmhHttpClient client) {
		super(client);
	}

	public OaiPmhElements run(String baseURL) throws Exception {
        return harvest(getRequestURL(baseURL), HarvesterVerbSaxMemory.SET);
    }

    public OaiPmhElements run(String baseURL, String resumptionToken) throws Exception {
        return harvest(getRequestURL(baseURL, resumptionToken), HarvesterVerbSaxMemory.SET);
    }

    private static String getRequestURL(String baseURL) {
        StringBuilder requestURL =  new StringBuilder(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=ListSets");
        else
            requestURL.append("?verb=ListSets");
        return requestURL.toString();
    }

    private static String getRequestURL(String baseURL, String resumptionToken) throws UnsupportedEncodingException {
        StringBuilder requestURL = new StringBuilder(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=ListSets");
        else
            requestURL.append("?verb=ListSets");
        requestURL.append("&resumptionToken=").append(URLEncoder.encode(resumptionToken, "UTF-8"));
        return requestURL.toString();
    }
}
