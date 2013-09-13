package org.oclc.oai.harvester.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oclc.oai.harvester.parser.OaiPmhParser;
import org.oclc.oai.harvester.parser.OaiPmhRecord;
import org.oclc.oai.harvester.parser.ResultInfo;
import org.oclc.oai.harvester.verb.ListRecordsSaxWriteDirectly;


public class RawWriteSaxOriginalFiles {
    public static void main(String[] args) {
        try {
            System.out.println(new Date());
            getInput("Press key to start");
            long startTime = System.currentTimeMillis();

            HashMap options = getOptions(args);
            List rootArgs = (List) options.get("rootArgs");
            if (rootArgs.size() == 0) {
                throw new IllegalArgumentException();
            }
            String baseURL = (String) rootArgs.get(0);
            String directory = (String) options.get("-outputDirectory");
            String from = (String) options.get("-from");
            String until = (String) options.get("-until");
            String metadataPrefix = (String) options.get("-metadataPrefix");
            if (metadataPrefix == null) {
                metadataPrefix = "oai_dc";
            }
            String resumptionToken = (String) options.get("-resumptionToken");
            String setSpec = (String) options.get("-setSpec");
            File outputDirectory = new File(directory);
            outputDirectory.mkdirs();
            if (resumptionToken != null) {
                run(baseURL, resumptionToken, outputDirectory);
            }
            else {
                run(baseURL, from, until, metadataPrefix, setSpec, outputDirectory);
            }

            System.out.println(new Date());
            long stopTime = System.currentTimeMillis();
            calcHMS(stopTime, startTime);
            getInput("Press key to stop");
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

    public static void run(String baseURL, String resumptionToken, File outputDirectory)
            throws Exception {
        OaiPmhParser oaiPmhParser = new OaiPmhParser(outputDirectory);
        ListRecordsSaxWriteDirectly listRecordsSax = new ListRecordsSaxWriteDirectly();
        ResultInfo resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser);
        while (resultInfo != null) {
            List<String> errors = resultInfo.getErrors();
            if (errors != null && errors.size() > 0) {
                System.out.println("Found errors");
                int length = errors.size();
                for (int i = 0; i < length; ++i) {
                    String item = errors.get(i);
                    System.out.println(item);
                }
                System.out.println("Error record: " + resultInfo.getIdentifier());
                break;
            }
    		for (OaiPmhRecord record : resultInfo.getRecords()){
    			System.out.println(record.getIdentifier() + " - " + record.isDeleted() + " - " + record.getFilename());
    			
    		}
            resumptionToken = resultInfo.getNewResumptionToken();
            System.out.println("resumptionToken: " + resumptionToken);
            if (resumptionToken == null || resumptionToken.length() == 0) {
            	resultInfo = null;
            }
            else {
                resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser);
            }
        }
    }

    public static void run(String baseURL, String from, String until,
                           String metadataPrefix, String setSpec,  File outputDirectory)
            throws Exception {
        int number = 0;
        OaiPmhParser oaiPmhParser = new OaiPmhParser(outputDirectory);
        ListRecordsSaxWriteDirectly listRecordsSax = new ListRecordsSaxWriteDirectly();
        ResultInfo resultInfo = listRecordsSax.harvest(baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser);

        while (resultInfo != null) {
            List<String> errors = resultInfo.getErrors();
            if (errors != null && errors.size() > 0) {
                System.out.println("Found errors");
                int length = errors.size();
                for (int i = 0; i < length; ++i) {
                    String item = errors.get(i);
                    System.out.println(item);
                }
                System.out.println("Error record: " + resultInfo.getIdentifier());
                break;
            }
    		for (OaiPmhRecord record : resultInfo.getRecords()){
    			System.out.println(record.getIdentifier() + " - " + record.isDeleted() + " - " + record.getFilename());
    			
    		}
            String resumptionToken = resultInfo.getNewResumptionToken();
            System.out.println("resumptionToken: " + resumptionToken);
            if (resumptionToken == null || resumptionToken.length() == 0) {
            	resultInfo = null;
            }
            else {
                number++;
                resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser);
            }
        }
    }
	public static String getInput(String message) {

		System.out.println(message +" : ");

		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			return bufferRead.readLine();


		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
