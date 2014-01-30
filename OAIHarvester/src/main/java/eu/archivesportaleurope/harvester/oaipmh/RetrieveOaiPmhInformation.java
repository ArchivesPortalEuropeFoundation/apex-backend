package eu.archivesportaleurope.harvester.oaipmh;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElement;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.harvester.verb.HarvesterVerbSaxMemory;
import eu.archivesportaleurope.harvester.verb.ListMetadataFormatsSax;
import eu.archivesportaleurope.harvester.verb.ListSetsSax;

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
    public static List<OaiPmhElement> retrieveMetadataFormats(String baseURL, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
        return retrieveElements(baseURL, HarvesterVerbSaxMemory.METADATA_FORMAT, oaiPmhHttpClient);
    }

    public static List<OaiPmhElement> retrieveSets(String baseURL, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
        return retrieveElements(baseURL, HarvesterVerbSaxMemory.SET, oaiPmhHttpClient);
    }

    private static List<OaiPmhElement> retrieveElements(String baseURL, String type, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
        List<OaiPmhElement> definitiveResults = new ArrayList<OaiPmhElement>();

        HarvesterVerbSaxMemory harvesterVerbSaxMemory;
        if(type.equals(HarvesterVerbSaxMemory.SET)) {
            harvesterVerbSaxMemory = new ListSetsSax(oaiPmhHttpClient);
        } else {
            harvesterVerbSaxMemory = new ListMetadataFormatsSax(oaiPmhHttpClient);
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
