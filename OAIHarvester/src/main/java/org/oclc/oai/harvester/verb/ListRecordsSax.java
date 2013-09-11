package org.oclc.oai.harvester.verb;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class ListRecordsSax extends HarvesterVerbSax {
    public ListRecordsSax(String baseURL, String from, String until, String set, String metadataPrefix, File fileOut) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
        super(getRequestURL(baseURL, from, until, set, metadataPrefix), fileOut);
    }

    public ListRecordsSax(String baseURL, String resumptionToken, File fileOut) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
        super(getRequestURL(baseURL, resumptionToken), fileOut);
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
