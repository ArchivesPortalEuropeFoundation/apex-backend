package org.oclc.oai.harvester.verb;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.oclc.oai.harvester.parser.record.OaiPmhParser;
import org.oclc.oai.harvester.parser.record.ResultInfo;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public abstract class HarvesterVerbSaxWriteDirectly {
    private static final Logger LOG = Logger.getLogger(HarvesterVerbSaxWriteDirectly.class);
    protected static final String SCHEMA_LOCATION_V2_0 = "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_GET_RECORD = "http://www.openarchives.org/OAI/1.1/OAI_GetRecord http://www.openarchives.org/OAI/1.1/OAI_GetRecord.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_IDENTIFY = "http://www.openarchives.org/OAI/1.1/OAI_Identify http://www.openarchives.org/OAI/1.1/OAI_Identify.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_IDENTIFIERS = "http://www.openarchives.org/OAI/1.1/OAI_ListIdentifiers http://www.openarchives.org/OAI/1.1/OAI_ListIdentifiers.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_METADATA_FORMATS = "http://www.openarchives.org/OAI/1.1/OAI_ListMetadataFormats http://www.openarchives.org/OAI/1.1/OAI_ListMetadataFormats.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_RECORDS = "http://www.openarchives.org/OAI/1.1/OAI_ListRecords http://www.openarchives.org/OAI/1.1/OAI_ListRecords.xsd";
    protected static final String SCHEMA_LOCATION_V1_1_LIST_SETS = "http://www.openarchives.org/OAI/1.1/OAI_ListSets http://www.openarchives.org/OAI/1.1/OAI_ListSets.xsd";

    private String schemaLocation = null;
    private String requestURL = null;
    private HttpClient httpClient = new HttpClient();

    protected ResultInfo harvest(String requestURL, OaiPmhParser oaiPmhParser) throws Exception {
        LOG.debug("requestURL=" + requestURL);
        GetMethod getMethod = new GetMethod(requestURL);
        getMethod.setRequestHeader("User-Agent", "OAIHarvester/2.0");
        getMethod.setRequestHeader("Accept-Encoding", "compress, gzip, identify");
        int httpStatus = httpClient.executeMethod(getMethod);
        if (httpStatus != HttpStatus.SC_OK) {
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

        return oaiPmhParser.parse(response);
    }


    /**
     * Get the xsi:schemaLocation for the OAI response
     *
     * @return the xsi:schemaLocation value
     */
    public String getSchemaLocation() {
        return schemaLocation;
    }


    public String getRequestURL() {
        return requestURL;
    }
}
