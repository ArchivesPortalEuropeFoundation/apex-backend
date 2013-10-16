package org.oclc.oai.harvester.verb;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.oclc.oai.harvester.parser.other.OaiPmhMemoryParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
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
public class HarvesterVerbSaxMemory {
    private static final Logger LOG = Logger.getLogger(HarvesterVerbSaxMemory.class);
    private static final String SET = "SET";
    private static final String METADATA_FORMAT = "METADATA_FORMAT";

    private InputStream responseStream = null;
    private OaiPmhMemoryParser oaiPmhMemoryParser = null;
    private String resultString;
    private String schemaLocation = null;
    private String requestURL = null;
    private HttpClient httpClient = new HttpClient();
    private List<String> results;

    public HarvesterVerbSaxMemory(String requestURL, String type) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
        results = new ArrayList<String>();
        harvest(requestURL, type);
    }

    public void harvest(String requestURL, String type) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
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

//        OutputStream outputStream = new FileOutputStream(fileOut);
//        outputStream.write(IOUtils.toByteArray(response));
//        outputStream.write("\n".getBytes("UTF-8"));
//        outputStream.close();
//
//        response = FileUtils.openInputStream(fileOut);
//        if(type.equals(SET)) {
//            oaiPmhMemoryParser = new OaiPmhMetadataFormatParser();
//        } else {
//            oaiPmhMemoryParser = new OaiPmhSetParser();
//        }
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

//    public List<String> getErrors() throws TransformerException {
//        return oaiStaxParser.getErrors();
//    }
//
//    public String getResumptionToken() {
//        return oaiStaxParser.getResumptionToken();
//    }

    public String getRequestURL() {
        return requestURL;
    }
}
