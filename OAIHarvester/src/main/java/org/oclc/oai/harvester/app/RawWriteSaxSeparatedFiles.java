package org.oclc.oai.harvester.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.oclc.oai.harvester.verb.ListRecordsSax;
import org.xml.sax.SAXException;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class RawWriteSaxSeparatedFiles {
    public static void main(String[] args) {
        try {
            System.out.println(new Date());
            long startTime = System.currentTimeMillis();

            HashMap options = getOptions(args);
            List rootArgs = (List) options.get("rootArgs");
            if (rootArgs.size() == 0) {
                throw new IllegalArgumentException();
            }
            String baseURL = (String) rootArgs.get(0);
            String prefix = (String) options.get("-filePrefix");
            String from = (String) options.get("-from");
            String until = (String) options.get("-until");
            String metadataPrefix = (String) options.get("-metadataPrefix");
            if (metadataPrefix == null) {
                metadataPrefix = "oai_dc";
            }
            String resumptionToken = (String) options.get("-resumptionToken");
            String setSpec = (String) options.get("-setSpec");

            if (resumptionToken != null) {
                run(baseURL, resumptionToken, prefix);
            }
            else {
                run(baseURL, from, until, metadataPrefix, setSpec, prefix);
            }

            System.out.println(new Date());
            long stopTime = System.currentTimeMillis();
            calcHMS(stopTime, startTime);
        }
        catch (IllegalArgumentException e) {
            System.err.println("tel_oai_harvester <-from date> <-until date> <-metadataPrefix prefix> <-setSpec setName> <-resumptionToken token> <-filePrefix filePrefix (incl. absolute path)> baseURL");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static HashMap getOptions(String[] args) {
        HashMap<Object, Object> options = new HashMap<Object, Object>();
        ArrayList<Object> rootArgs = new ArrayList<Object>();
        options.put("rootArgs", rootArgs);
        for (int i = 0; i < args.length; ++i) {
            if (args[i].charAt(0) != '-') {
                rootArgs.add(args[i]);
            }
            else if (i + 1 < args.length) {
                options.put(args[i], args[++i]);
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        return options;
    }

    public static void calcHMS(long stopTime, long startTime) {
        int hours, minutes, seconds;
        int timeInSeconds = (int) ((stopTime - startTime) / 1000);
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        System.out.println("Elapsed time: " + hours + " hour(s) " + minutes + " minute(s) " + seconds + " second(s)");
    }

    public static void run(String baseURL, String resumptionToken, String prefix)
            throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchFieldException, XMLStreamException {
        int number = 0;
        File exportFile = new File(prefix + "-" + number + ".xml");
        exportFile.getParentFile().mkdirs();
        ListRecordsSax listRecordsSax = new ListRecordsSax(baseURL, resumptionToken, exportFile);
        while (listRecordsSax != null) {
            List<String> errors = listRecordsSax.getErrors();
            if (errors != null && errors.size() > 0) {
                System.out.println("Found errors");
                int length = errors.size();
                for (int i = 0; i < length; ++i) {
                    String item = errors.get(i);
                    System.out.println(item);
                }
                System.out.println("Error record: " + listRecordsSax.toString());
                break;
            }
            resumptionToken = listRecordsSax.getResumptionToken();
            System.out.println("resumptionToken: " + resumptionToken);
            if (resumptionToken == null || resumptionToken.length() == 0) {
                listRecordsSax = null;
            }
            else {
                number++;
                exportFile = new File(prefix + "-" + number + ".xml");
                listRecordsSax = new ListRecordsSax(baseURL, resumptionToken, exportFile);
            }
        }
    }

    public static void run(String baseURL, String from, String until,
                           String metadataPrefix, String setSpec,
                           String prefix)
            throws IOException, ParserConfigurationException, SAXException, TransformerException,
            NoSuchFieldException, XMLStreamException {
        int number = 0;
        File exportFile = new File(prefix + "-" + number + ".xml");
        exportFile.getParentFile().mkdirs();
        ListRecordsSax listRecordsSax = new ListRecordsSax(baseURL, from, until, setSpec, metadataPrefix, exportFile);

        while (listRecordsSax != null) {
            List<String> errors = listRecordsSax.getErrors();
            if (errors != null && errors.size() > 0) {
                System.out.println("Found errors");
                int length = errors.size();
                for (int i = 0; i < length; ++i) {
                    String item = errors.get(i);
                    System.out.println(item);
                }
                System.out.println("Error record: " + listRecordsSax.toString());
                break;
            }
            String resumptionToken = listRecordsSax.getResumptionToken();
            System.out.println("resumptionToken: " + resumptionToken);
            if (resumptionToken == null || resumptionToken.length() == 0) {
                listRecordsSax = null;
            }
            else {
                number++;
                exportFile = new File(prefix + "-" + number + ".xml");
                listRecordsSax = new ListRecordsSax(baseURL, resumptionToken, exportFile);
            }
        }
    }
}
