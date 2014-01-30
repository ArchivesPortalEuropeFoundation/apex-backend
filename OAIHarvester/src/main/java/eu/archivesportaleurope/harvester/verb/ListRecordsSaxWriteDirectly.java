package eu.archivesportaleurope.harvester.verb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import eu.archivesportaleurope.harvester.oaipmh.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.ResultInfo;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class ListRecordsSaxWriteDirectly {
	private OaiPmhHttpClient client;
	
	public  ListRecordsSaxWriteDirectly(OaiPmhHttpClient client){
		this.client = client;
	}
	
	protected ResultInfo harvest(String requestURL, OaiPmhParser oaiPmhParser, File errorDirectory, int numberOfRequests)
			throws Exception {
		if (oaiPmhParser.getMaxNumberOfRequests() != null && numberOfRequests >= oaiPmhParser.getMaxNumberOfRequests()){
			return null;
		}
		CloseableHttpResponse closeableHttpResponse = client.get(requestURL);
		try {
			InputStream response = client.getResponseInputStream(closeableHttpResponse);
			ResultInfo resultInfo =  oaiPmhParser.parse(response, numberOfRequests);
			resultInfo.setRequestUrl(requestURL);
			return resultInfo;
		} catch (Exception e) {
			File errorFile = new File(errorDirectory, (new Date()).getTime() + ".xml");
			CloseableHttpResponse errorCloseableHttpResponse = client.get(requestURL);
			try {
				InputStream response = client.getResponseInputStream(errorCloseableHttpResponse);
				FileOutputStream fileOutputStream = new FileOutputStream(errorFile);
				IOUtils.copy(response, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
			}finally {
				errorCloseableHttpResponse.close();
			}
			throw new HarvesterParserException(errorFile, e);
			
		}finally {
			closeableHttpResponse.close();
		}
	}
	
	public ResultInfo harvest(String baseURL, String from, String until, String set, String metadataPrefix, OaiPmhParser oaiPmhParser, File errorDirectory, int numberOfRequests) throws Exception {
        return harvest(getRequestURL(baseURL, from, until, set, metadataPrefix), oaiPmhParser,errorDirectory, numberOfRequests);
    }

	public ResultInfo harvest(String baseURL, String resumptionToken, OaiPmhParser oaiPmhParser,File errorDirectory, int numberOfRequests) throws Exception {
		 return harvest(getRequestURL(baseURL, resumptionToken), oaiPmhParser, errorDirectory,numberOfRequests);
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
