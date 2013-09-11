package org.oclc.oai.harvester.verb;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.oclc.oai.harvester.parser.OaiStaxParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public abstract class HarvesterVerbSax {
    private static final Logger LOG = Logger.getLogger(HarvesterVerbSax.class);
    protected static final String SCHEMA_LOCATION_V2_0 = "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_GET_RECORD = "http://www.openarchives.org/OAI/1.1/OAI_GetRecord http://www.openarchives.org/OAI/1.1/OAI_GetRecord.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_IDENTIFY = "http://www.openarchives.org/OAI/1.1/OAI_Identify http://www.openarchives.org/OAI/1.1/OAI_Identify.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_IDENTIFIERS = "http://www.openarchives.org/OAI/1.1/OAI_ListIdentifiers http://www.openarchives.org/OAI/1.1/OAI_ListIdentifiers.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_METADATA_FORMATS = "http://www.openarchives.org/OAI/1.1/OAI_ListMetadataFormats http://www.openarchives.org/OAI/1.1/OAI_ListMetadataFormats.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_RECORDS = "http://www.openarchives.org/OAI/1.1/OAI_ListRecords http://www.openarchives.org/OAI/1.1/OAI_ListRecords.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_SETS = "http://www.openarchives.org/OAI/1.1/OAI_ListSets http://www.openarchives.org/OAI/1.1/OAI_ListSets.xsd";

    private InputStream responseStream = null;
    private OaiStaxParser oaiStaxParser = null;
    private String resultString;
    private String schemaLocation = null;
    private String requestURL = null;
    private HttpClient httpClient = new HttpClient();

    public HarvesterVerbSax(String requestURL) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
        harvest(requestURL);
    }

    public void harvest(String requestURL) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
        this.requestURL = requestURL;
        LOG.debug("requestURL=" + requestURL);
        GetMethod getMethod = new GetMethod(requestURL);
        getMethod.setRequestHeader("User-Agent", "OAIHarvester/2.0");
        getMethod.setRequestHeader("Accept-Encoding", "compress, gzip, identify");
        int httpStatus = httpClient.executeMethod(getMethod);
        if (httpStatus != HttpStatus.SC_OK) {
            if (httpStatus == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                LOG.info("Error HTTP response: " + httpStatus + " but we will try to continue...");
                return;
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

        resultString = IOUtils.toString(response, "UTF-8"); //Maybe those 2 lines can be better done...
        responseStream = IOUtils.toInputStream(resultString, "UTF-8");
        oaiStaxParser = new OaiStaxParser(responseStream);
    }

    /**
     * Get the OAI response as a DOM object
     *
     * @return the DOM for the OAI response
     */
    public InputStream getResponseStream() {
        return responseStream;
    }

    /**
     * Get the xsi:schemaLocation for the OAI response
     *
     * @return the xsi:schemaLocation value
     */
    public String getSchemaLocation() {
        return schemaLocation;
    }

    public List<String> getErrors() throws TransformerException {
        return oaiStaxParser.getErrors();
    }

    public String getResumptionToken() {
        return oaiStaxParser.getResumptionToken();
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String toString() {
        return resultString;
    }
}
