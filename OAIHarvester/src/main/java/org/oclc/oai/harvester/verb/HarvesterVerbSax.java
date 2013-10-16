package org.oclc.oai.harvester.verb;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
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
import java.io.*;
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

    private InputStream responseStream = null;
    private OaiStaxParser oaiStaxParser = null;
    private String resultString;
    private String schemaLocation = null;
    private String requestURL = null;
    private HttpClient httpClient = new HttpClient();

    public HarvesterVerbSax(String requestURL, File fileOut) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
        harvest(requestURL, fileOut);
    }

    public void harvest(String requestURL, File fileOut) throws IOException, ParserConfigurationException, SAXException, TransformerException, XMLStreamException {
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

        OutputStream outputStream = new FileOutputStream(fileOut);
        outputStream.write(IOUtils.toByteArray(response));
        outputStream.write("\n".getBytes("UTF-8"));
        outputStream.close();

        response = FileUtils.openInputStream(fileOut);
        oaiStaxParser = new OaiStaxParser(response);
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
}
