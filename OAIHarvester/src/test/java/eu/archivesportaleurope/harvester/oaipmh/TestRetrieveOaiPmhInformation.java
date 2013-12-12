package eu.archivesportaleurope.harvester.oaipmh;

import org.junit.Test;

import eu.archivesportaleurope.harvester.oaipmh.RetrieveOaiPmhInformation;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhElement;

import java.lang.Exception;
import java.lang.String;
import java.lang.System;

public class TestRetrieveOaiPmhInformation {
    @Test
    public void test() {
 //       String url = "http://archives.cantal.fr/oai_pmh.cgi";
        String url = "http://www.archieven.nl/pls/oai/!pck_oai_pmh.OAIHandler";
        try {
            System.out.println("Sets:");
            for(OaiPmhElement s : RetrieveOaiPmhInformation.retrieveSets(url)) {
                System.out.println("- " + s);
            }
            System.out.println("Metadata Formats:");
            for(OaiPmhElement mf : RetrieveOaiPmhInformation.retrieveMetadataFormats(url)) {
                System.out.println("- " + mf);
            }
        } catch (Exception e) {
            System.out.println("Error " + e);;
            e.printStackTrace();
        }
    }
}