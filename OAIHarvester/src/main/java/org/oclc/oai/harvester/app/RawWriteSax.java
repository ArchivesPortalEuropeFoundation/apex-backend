package org.oclc.oai.harvester.app;

import org.oclc.oai.harvester.verb.ListRecordsSax;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class RawWriteSax {
    public static String run_getToken(String baseURL, String from, String until, String metadataPrefix, String setSpec, OutputStream out) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException, XMLStreamException {
        ListRecordsSax listRecordsSax = new ListRecordsSax(baseURL, from, until, setSpec, metadataPrefix);
        List<String> errors = listRecordsSax.getErrors();
        if (errors != null && errors.size() > 0) {
            System.out.println("Found errors");
            for (String item : errors) {
                System.out.println(item);
            }
            return "Error record: " + listRecordsSax.toString();
        }
        out.write(listRecordsSax.toString().getBytes("UTF-8"));
        out.write("\n".getBytes("UTF-8"));
        return listRecordsSax.getResumptionToken();
    }
    public static String run_getToken(String baseURL, String resumptionToken, OutputStream out) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException, XMLStreamException {
        ListRecordsSax listRecordsSax = new ListRecordsSax(baseURL, resumptionToken);
        List<String> errors = listRecordsSax.getErrors();
        if (errors != null && errors.size() > 0) {
            System.out.println("Found errors");
            for (String item : errors) {
                System.out.println(item);
            }
            return "Error record: " + listRecordsSax.toString();
        }
        out.write(listRecordsSax.toString().getBytes("UTF-8"));
        out.write("\n".getBytes("UTF-8"));
        return listRecordsSax.getResumptionToken();
    }
}
