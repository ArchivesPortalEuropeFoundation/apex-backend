package eu.archivesportaleurope.harvester.oaipmh;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.oclc.oai.harvester.parser.record.DebugOaiPmhParser;
import org.oclc.oai.harvester.parser.record.OaiPmhParser;
import org.oclc.oai.harvester.parser.record.OaiPmhRecord;
import org.oclc.oai.harvester.parser.record.ResultInfo;
import org.oclc.oai.harvester.verb.ListRecordsSaxWriteDirectly;


public class ConsoleHarvester {
	private static Logger logger;
    public static void main(String[] args) {
    	ConsoleAppender appender = new ConsoleAppender();
    	appender.setThreshold(Priority.INFO);
    	appender.setWriter(new OutputStreamWriter(System.out));
    	appender.setLayout(new PatternLayout("%d{ABSOLUTE} %-5p - %m%n"));
    	BasicConfigurator.configure(appender);
        try {
        	logger = Logger.getLogger(ConsoleHarvester.class);
        	logger.info(new Date());
            long startTime = System.currentTimeMillis();

            HashMap options = getOptions(args);
            List rootArgs = (List) options.get("rootArgs");
            if (rootArgs.size() == 0) {
                throw new IllegalArgumentException();
            }
            String baseURL = (String) rootArgs.get(0);
            String directory = (String) options.get("-outputDirectory");
            String debugString = (String) options.get("-debug");
            boolean debug = Boolean.parseBoolean(debugString);
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
            OaiPmhParser oaiPmhParser;
            if (debug){
            	oaiPmhParser = new DebugOaiPmhParser(outputDirectory);
            }else {
            	oaiPmhParser = new OaiPmhParser(outputDirectory);
            }
            if (resumptionToken != null) {
                run(baseURL, resumptionToken, oaiPmhParser);
            }
            else {
                run(baseURL, from, until, metadataPrefix, setSpec, oaiPmhParser);
            }

            logger.info(new Date());
            long stopTime = System.currentTimeMillis();
            calcHMS(stopTime, startTime);

        }
        catch (IllegalArgumentException e) {
        	logger.error("wrong parameters:\nExample: java -jar oaiharvester.jar -outputDirectory OUTPUT_DIR -setSpec SET_SPEC_VALUE [-debug true] [-from 1980-01-01] [-until 1990-01-01] -metadataPrefix METADATAPREFIX OAI_PMH_URL");
        }
        catch (Exception e) {
        	logger.error(e.getMessage(),e);
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
        logger.info("Elapsed time: " + hours + " hour(s) " + minutes + " minute(s) " + seconds + " second(s)");
    }

    public static void run(String baseURL, String resumptionToken, OaiPmhParser oaiPmhParser)
            throws Exception {
        int number = 0;
        
        ListRecordsSaxWriteDirectly listRecordsSax = new ListRecordsSaxWriteDirectly();
        ResultInfo resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser, number);
        while (resultInfo != null) {
            List<String> errors = resultInfo.getErrors();
            if (errors != null && errors.size() > 0) {
                logger.info("Found errors");
                int length = errors.size();
                for (int i = 0; i < length; ++i) {
                    String item = errors.get(i);
                    logger.info(item);
                }
                logger.info("Error record: " + resultInfo.getIdentifier());
                break;
            }
    		for (OaiPmhRecord record : resultInfo.getRecords()){
    			logger.info("IDENTIFIER: "  + record.getIdentifier() + " - DELETED: " + record.isDeleted() + " - FILENAME: " + record.getFilename());
    			
    		}
            resumptionToken = resultInfo.getNewResumptionToken();
            logger.info("resumptionToken: '" + resumptionToken + "'");
            if (resumptionToken == null || resumptionToken.length() == 0) {
            	resultInfo = null;
            }
            else {
                resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser, number++);
            }
        }
    }

    public static void run(String baseURL, String from, String until,
                           String metadataPrefix, String setSpec,  OaiPmhParser oaiPmhParser)
            throws Exception {
        int number = 0;
        ListRecordsSaxWriteDirectly listRecordsSax = new ListRecordsSaxWriteDirectly();
        ResultInfo resultInfo = listRecordsSax.harvest(baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser, number);

        while (resultInfo != null) {
            List<String> errors = resultInfo.getErrors();
            if (errors != null && errors.size() > 0) {
                logger.info("Found errors");
                int length = errors.size();
                for (int i = 0; i < length; ++i) {
                    String item = errors.get(i);
                    logger.info(item);
                }
                logger.info("Error record: " + resultInfo.getIdentifier());
                break;
            }
    		for (OaiPmhRecord record : resultInfo.getRecords()){
    			logger.info("IDENTIFIER: "  + record.getIdentifier() + " - DELETED: " + record.isDeleted() + " - FILENAME: " + record.getFilename());
    			
    		}
            String resumptionToken = resultInfo.getNewResumptionToken();
            logger.info("resumptionToken: '" + resumptionToken + "'");
            if (resumptionToken == null || resumptionToken.length() == 0) {
            	resultInfo = null;
            }
            else {
                number++;
                resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser, number);
            }
        }
    }
	public static String getInput(String message) {

//		logger.info(message +" : ");
//
//		try {
//			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//			return bufferRead.readLine();
//
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;

	}
}
