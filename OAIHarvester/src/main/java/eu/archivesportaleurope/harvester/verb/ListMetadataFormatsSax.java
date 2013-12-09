package eu.archivesportaleurope.harvester.verb;

import org.xml.sax.SAXException;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 17/10/2013
 *
 * @author Yoann Moranville
 */
public class ListMetadataFormatsSax extends HarvesterVerbSaxMemory {

    public OaiPmhElements run(String baseURL) throws TransformerException, XMLStreamException, IOException, SAXException {
        return harvest(getRequestURL(baseURL), HarvesterVerbSaxMemory.METADATA_FORMAT);
    }

    public OaiPmhElements run(String baseURL, String resumptionToken) throws TransformerException, XMLStreamException, IOException, SAXException {
        return harvest(getRequestURL(baseURL, resumptionToken), HarvesterVerbSaxMemory.METADATA_FORMAT);
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
