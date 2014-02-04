package eu.archivesportaleurope.harvester.verb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.DebugOaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.ResultInfo;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

public abstract class AbstractListVerb {
	private OaiPmhHttpClient client;
	private String baseURL;
	private String from;
	private String until;
	private String set;
	private String metadataPrefix;
	private OaiPmhParser oaiPmhParser;
	private File errorDirectory;
	private String resumptionToken;
	private Calendar fromCalendar;
	private Calendar untilCalendar;
	
	public  AbstractListVerb(OaiPmhHttpClient client, String baseURL, String from, String until, String set, String metadataPrefix, OaiPmhParser oaiPmhParser, File errorDirectory){
		this.client = client;
		this.baseURL = baseURL;
		this.from = from;
		this.until = until;
		this.metadataPrefix = metadataPrefix;
		this.set = set;
		this.oaiPmhParser = oaiPmhParser;
		this.errorDirectory = errorDirectory;
		if (from != null && !from.equals("1970-01-01")){
			fromCalendar = DatatypeConverter.parseDateTime(from);
		}
		if (until != null){
			untilCalendar = DatatypeConverter.parseDateTime(until +"T23:59:59.999Z");
		}
	}
	protected boolean maxNumberOfRecordsExceed(int numberOfRecords){
		return oaiPmhParser.getMaxNumberOfRecords() != null && numberOfRecords >= oaiPmhParser.getMaxNumberOfRecords();
	}
	
	public ResultInfo harvest(HarvestObject harvestObject)
			throws Exception {
		int numberOfRecords = 0;
		if (oaiPmhParser instanceof DebugOaiPmhParser){
			numberOfRecords = harvestObject.getNumberOfRequests();
		}else {
			numberOfRecords = harvestObject.getNumberOfRecords();
		}
		if (maxNumberOfRecordsExceed(numberOfRecords)){
			return null;
		}
		String requestURL = getRequestURL();
		CloseableHttpResponse closeableHttpResponse = client.get(requestURL);
		try {
			InputStream response = client.getResponseInputStream(closeableHttpResponse);
			ResultInfo resultInfo =  oaiPmhParser.parse(harvestObject, response, numberOfRecords, fromCalendar, untilCalendar);
			resultInfo.setRequestUrl(requestURL);
			resumptionToken = resultInfo.getNewResumptionToken();
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
			throw new HarvesterParserException(requestURL, errorFile, e);
			
		}finally {
			closeableHttpResponse.close();
		}
	}
	
	protected String getRequestURL() throws UnsupportedEncodingException{
		if (resumptionToken == null){
			return getRequestURLWithParams();
		}else {
			return getRequestURLWithResumptionToken();
		}
	}
    private String getRequestURLWithParams() {
        StringBuffer requestURL = new StringBuffer(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=" + getVerb());
        else
            requestURL.append("?verb=" + getVerb());
        if (from != null && !from.equals("1970-01-01")) requestURL.append("&from=").append(from);
        if (until != null) requestURL.append("&until=").append(until);
        if (set != null) requestURL.append("&set=").append(set);
        requestURL.append("&metadataPrefix=").append(metadataPrefix);
        return requestURL.toString();
    }

    private String getRequestURLWithResumptionToken() throws UnsupportedEncodingException {
        StringBuffer requestURL = new StringBuffer(baseURL);
        if(requestURL.indexOf("?") >= 0)
            requestURL.append("&verb=" + getVerb());
        else
            requestURL.append("?verb=" + getVerb());
        requestURL.append("&resumptionToken=").append(URLEncoder.encode(resumptionToken, "UTF-8"));
        return requestURL.toString();
    }
    protected abstract String getVerb();

	protected String getMetadataPrefix() {
		return metadataPrefix;
	}

	protected String getBaseURL() {
		return baseURL;
	}
    
}
