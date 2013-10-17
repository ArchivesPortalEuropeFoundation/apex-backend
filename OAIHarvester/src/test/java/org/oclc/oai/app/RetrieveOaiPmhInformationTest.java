package org.oclc.oai.harvester.app;

import org.junit.Test;
import org.oclc.oai.harvester.app.RetrieveOaiPmhInformation;

import java.lang.Exception;
import java.lang.String;
import java.lang.System;

public class RetrieveOaiPmhInformationTest {
    @Test
    public void test() {
        String url = "http://gahetna.nl/archievenoverzicht-oaipmh/oai-pmh";
        try {
            System.out.println("Sets:");
            for(String s : RetrieveOaiPmhInformation.retrieveSets(url)) {
                System.out.println("- " + s);
            }
            System.out.println("Metadata Formats:");
            for(String mf : RetrieveOaiPmhInformation.retrieveMetadataFormats(url)) {
                System.out.println("- " + mf);
            }
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }
}