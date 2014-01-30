package eu.archivesportaleurope.harvester.oaipmh;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;
import eu.archivesportaleurope.harvester.verb.HarvesterVerbSaxMemory;
import eu.archivesportaleurope.harvester.verb.ListMetadataFormatsSax;
import eu.archivesportaleurope.harvester.verb.ListSetsSax;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 *
 * Class to retrieve information:
 * - metadataPrefix(es) from url
 * - set(s) from url
 */
public abstract class RetrieveOaiPmhInformation {
    private static final Logger LOG = Logger.getLogger(RetrieveOaiPmhInformation.class);
    public static List<String> retrieveMetadataFormats(String baseURL) throws TransformerException, XMLStreamException, IOException, SAXException {
        return retrieveElements(baseURL, HarvesterVerbSaxMemory.METADATA_FORMAT);
    }

    public static List<String> retrieveSets(String baseURL) throws TransformerException, XMLStreamException, IOException, SAXException {
        return retrieveElements(baseURL, HarvesterVerbSaxMemory.SET);
    }

    private static List<String> retrieveElements(String baseURL, String type) throws TransformerException, XMLStreamException, IOException, SAXException {
        List<String> definitiveResults = new ArrayList<String>();

        HarvesterVerbSaxMemory harvesterVerbSaxMemory;
        if(type.equals(HarvesterVerbSaxMemory.SET)) {
            harvesterVerbSaxMemory = new ListSetsSax();
        } else {
            harvesterVerbSaxMemory = new ListMetadataFormatsSax();
        }

        OaiPmhElements oaiPmhElements = harvesterVerbSaxMemory.run(baseURL);

        while(oaiPmhElements.getElements() != null && !oaiPmhElements.getElements().isEmpty()) {
            definitiveResults.addAll(oaiPmhElements.getElements());
            if (oaiPmhElements.getResumptionToken() == null || oaiPmhElements.getResumptionToken().length() == 0) {
                oaiPmhElements.setElements(null);
            } else {
                oaiPmhElements = harvesterVerbSaxMemory.run(baseURL, oaiPmhElements.getResumptionToken());
            }
        }
        return definitiveResults;
    }
}
