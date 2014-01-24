package eu.archivesportaleurope.harvester.verb;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhMemoryParser;
import eu.archivesportaleurope.harvester.parser.other.metadata.OaiPmhMetadataFormatParser;
import eu.archivesportaleurope.harvester.parser.other.set.OaiPmhSetParser;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public abstract class AbstractMetadateFormatsAndSetsVerb {
    private static final Logger LOG = Logger.getLogger(AbstractMetadateFormatsAndSetsVerb.class);
    public static final String SET = "SET";
    public static final String METADATA_FORMAT = "METADATA_FORMAT";

	private OaiPmhHttpClient client;
	
	public  AbstractMetadateFormatsAndSetsVerb(OaiPmhHttpClient client){
		this.client = client;
	}

    public abstract OaiPmhElements run(String baseURL) throws Exception;
    public abstract OaiPmhElements run(String baseURL, String resumptionToken) throws Exception;

    public OaiPmhElements harvest(String requestURL, String type) throws Exception {
		CloseableHttpResponse closeableHttpResponse = client.get(requestURL);
		try {
			InputStream response = client.getResponseInputStream(closeableHttpResponse);
	        OaiPmhMemoryParser oaiPmhMemoryParser;
	        if(type.equals(METADATA_FORMAT)) {
	            oaiPmhMemoryParser = new OaiPmhMetadataFormatParser();
	        } else {
	            oaiPmhMemoryParser = new OaiPmhSetParser();
	        }

	        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(response, "UTF-8");
	        OaiPmhElements results=  oaiPmhMemoryParser.parse(xmlStreamReader);
	        return results;
		}finally {
			closeableHttpResponse.close();
		}


    }

}
