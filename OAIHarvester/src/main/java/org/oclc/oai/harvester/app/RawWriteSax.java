package org.oclc.oai.harvester.app;

import org.oclc.oai.harvester.verb.ListRecordsSax;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class RawWriteSax {
    public static String run_getToken(String baseURL, String from, String until, String metadataPrefix, String setSpec, File fileOut) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException, XMLStreamException {
        ListRecordsSax listRecordsSax = new ListRecordsSax(baseURL, from, until, setSpec, metadataPrefix, fileOut);
        List<String> errors = listRecordsSax.getErrors();
        if (errors != null && errors.size() > 0) {
            System.out.println("Found errors");
            for (String item : errors) {
                System.out.println(item);
            }
            InputStream inputStream = new FileInputStream(fileOut);
            return "Error record: " + inputStream.toString();
        }
//        out.write(listRecordsSax.toString().getBytes("UTF-8"));
//        out.write("\n".getBytes("UTF-8"));
        return listRecordsSax.getResumptionToken();
    }
    public static String run_getToken(String baseURL, String resumptionToken, File fileOut) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException, XMLStreamException {
        ListRecordsSax listRecordsSax = new ListRecordsSax(baseURL, resumptionToken, fileOut);
        List<String> errors = listRecordsSax.getErrors();
        if (errors != null && errors.size() > 0) {
            System.out.println("Found errors");
            for (String item : errors) {
                System.out.println(item);
            }
            InputStream inputStream = new FileInputStream(fileOut);
            return "Error record: " + inputStream.toString();
        }
//        out.write(listRecordsSax.toString().getBytes("UTF-8"));
//        out.write("\n".getBytes("UTF-8"));
        return listRecordsSax.getResumptionToken();
    }
}
