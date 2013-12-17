package eu.archivesportaleurope.harvester.oaipmh;

import org.junit.Ignore;
import org.junit.Test;

import eu.archivesportaleurope.harvester.oaipmh.RetrieveOaiPmhInformation;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhElement;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

import java.lang.Exception;
import java.lang.String;
import java.lang.System;
@Ignore
public class TestRetrieveOaiPmhInformation {
    @Test @Ignore
    public void test() {
       String url = "http://archives.cantal.fr/oai_pmh.cgi";
//        String url = "http://www.archieven.nl/pls/oai/!pck_oai_pmh.OAIHandler";
        try {
        	OaiPmhHttpClient oaiPmhHttpClient = new OaiPmhHttpClient();
            System.out.println("Sets:");
            for(OaiPmhElement s : RetrieveOaiPmhInformation.retrieveSets(url,oaiPmhHttpClient)) {
                System.out.println("- " + s);
            }
            System.out.println("Metadata Formats:");
            for(OaiPmhElement mf : RetrieveOaiPmhInformation.retrieveMetadataFormats(url,oaiPmhHttpClient)) {
                System.out.println("- " + mf);
            }
            oaiPmhHttpClient.close();
        } catch (Exception e) {
            System.out.println("Error " + e);;
            e.printStackTrace();
        }
    }
}