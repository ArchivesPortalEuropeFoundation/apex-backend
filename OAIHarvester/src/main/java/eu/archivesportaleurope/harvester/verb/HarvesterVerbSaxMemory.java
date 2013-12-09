package eu.archivesportaleurope.harvester.verb;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhMemoryParser;
import eu.archivesportaleurope.harvester.parser.other.metadata.OaiPmhMetadataFormatParser;
import eu.archivesportaleurope.harvester.parser.other.set.OaiPmhSetParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public abstract class HarvesterVerbSaxMemory {
    private static final Logger LOG = Logger.getLogger(HarvesterVerbSaxMemory.class);
    public static final String SET = "SET";
    public static final String METADATA_FORMAT = "METADATA_FORMAT";

//    private OaiPmhMemoryParser oaiPmhMemoryParser = null;
    private String requestURL = null;
    private HttpClient httpClient = new HttpClient();
//    private List<String> results;

//    public HarvesterVerbSaxMemory() {
//        results = new ArrayList<String>();
//    }

    public abstract OaiPmhElements run(String baseURL) throws TransformerException, XMLStreamException, IOException, SAXException;
    public abstract OaiPmhElements run(String baseURL, String resumptionToken) throws TransformerException, XMLStreamException, IOException, SAXException;

    public OaiPmhElements harvest(String requestURL, String type) throws IOException, SAXException, TransformerException, XMLStreamException {
        this.requestURL = requestURL;
        LOG.debug("requestURL=" + requestURL);
        GetMethod getMethod = new GetMethod(requestURL);
        getMethod.setRequestHeader("User-Agent", "OAIHarvester/2.0");
        getMethod.setRequestHeader("Accept-Encoding", "compress, gzip, identify");
        int httpStatus = httpClient.executeMethod(getMethod);
        if (httpStatus != HttpStatus.SC_OK) {
            if (httpStatus == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                LOG.info("Error HTTP response: " + httpStatus + " but we will try to continue...");
                return null;
            }
            throw new IOException("HTTP response: " + HttpStatus.getStatusText(httpStatus));
        }
        Header contentEncodingHeader = getMethod.getResponseHeader("Content-Encoding");
        String contentEncoding = contentEncodingHeader == null ? "unknown" : contentEncodingHeader.getValue();
        LOG.debug("contentEncoding=" + contentEncoding);
        InputStream response = getMethod.getResponseBodyAsStream();
        if ("compress".equals(contentEncoding)) {
            ZipInputStream zis = new ZipInputStream(response);
            zis.getNextEntry();
            response = zis;
        }
        else if ("gzip".equals(contentEncoding)) {
            response = new GZIPInputStream(response);
        }
        else if ("deflate".equals(contentEncoding)) {
            response = new InflaterInputStream(response);
        }
        OaiPmhMemoryParser oaiPmhMemoryParser;
        if(type.equals(METADATA_FORMAT)) {
            oaiPmhMemoryParser = new OaiPmhMetadataFormatParser();
        } else {
            oaiPmhMemoryParser = new OaiPmhSetParser();
        }

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(response, "UTF-8");
        return oaiPmhMemoryParser.parse(xmlStreamReader);
    }

    public String getRequestURL() {
        return requestURL;
    }
}
