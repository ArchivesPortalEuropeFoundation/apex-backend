package org.oclc.oai.harvester.verb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.oclc.oai.harvester.parser.OaiPmhParser;
import org.oclc.oai.harvester.parser.ResultInfo;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class ListRecordsSaxWriteDirectly extends HarvesterVerbSaxWriteDirectly {
	public ResultInfo harvest(String baseURL, String from, String until, String set, String metadataPrefix, OaiPmhParser oaiPmhParser) throws Exception {
        return harvest(getRequestURL(baseURL, from, until, set, metadataPrefix), oaiPmhParser);
    }

	public ResultInfo harvest(String baseURL, String resumptionToken, OaiPmhParser oaiPmhParser) throws Exception {
		 return harvest(getRequestURL(baseURL, resumptionToken), oaiPmhParser);
    }

    private static String getRequestURL(String baseURL, String from, String until, String set, String metadataPrefix) {
        StringBuffer requestURL = new StringBuffer(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=ListRecords");
        else
            requestURL.append("?verb=ListRecords");
        if (from != null && !from.equals("1970-01-01")) requestURL.append("&from=").append(from);
        if (until != null) requestURL.append("&until=").append(until);
        if (set != null) requestURL.append("&set=").append(set);
        requestURL.append("&metadataPrefix=").append(metadataPrefix);
        return requestURL.toString();
    }

    private static String getRequestURL(String baseURL, String resumptionToken) throws UnsupportedEncodingException {
        StringBuffer requestURL = new StringBuffer(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=ListRecords");
        else
            requestURL.append("?verb=ListRecords");
        requestURL.append("&resumptionToken=").append(URLEncoder.encode(resumptionToken, "UTF-8"));
        return requestURL.toString();
    }
}
