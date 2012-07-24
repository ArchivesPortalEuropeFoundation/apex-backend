package org.oclc.oai.harvester.app;

import org.oclc.oai.harvester.verb.ListRecords;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: Yoann Moranville
 * Date: Aug 11, 2010
 *
 * @author Yoann Moranville
 */
public class RawWriteAPEnet {
    public static String run_getToken(String baseURL, String from, String until, String metadataPrefix, String setSpec, OutputStream out) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException {
        ListRecords listRecords = new ListRecords(baseURL, from, until, setSpec, metadataPrefix);
        NodeList errors = listRecords.getErrors();
        if (errors != null && errors.getLength() > 0) {
            System.out.println("Found errors");
            int length = errors.getLength();
            for (int i = 0; i < length; ++i) {
                Node item = errors.item(i);
                System.out.println(item);
            }
            return "Error record: " + listRecords.toString();
        }
        out.write(listRecords.toString().getBytes("UTF-8"));
        out.write("\n".getBytes("UTF-8"));
        return listRecords.getResumptionToken();
    }
    public static String run_getToken(String baseURL, String resumptionToken, OutputStream out) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException {
        ListRecords listRecords = new ListRecords(baseURL, resumptionToken);
        NodeList errors = listRecords.getErrors();
        if (errors != null && errors.getLength() > 0) {
            System.out.println("Found errors");
            int length = errors.getLength();
            for (int i = 0; i < length; ++i) {
                Node item = errors.item(i);
                System.out.println(item);
            }
            return "Error record: " + listRecords.toString();
        }
        out.write(listRecords.toString().getBytes("UTF-8"));
        out.write("\n".getBytes("UTF-8"));
        return listRecords.getResumptionToken();
    }
}
