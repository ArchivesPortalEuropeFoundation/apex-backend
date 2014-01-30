package eu.archivesportaleurope.harvester.verb;

import org.xml.sax.SAXException;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;
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
public class ListSetsSax extends HarvesterVerbSaxMemory {

    public OaiPmhElements run(String baseURL) throws TransformerException, XMLStreamException, IOException, SAXException {
        return harvest(getRequestURL(baseURL), HarvesterVerbSaxMemory.SET);
    }

    public OaiPmhElements run(String baseURL, String resumptionToken) throws TransformerException, XMLStreamException, IOException, SAXException {
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
