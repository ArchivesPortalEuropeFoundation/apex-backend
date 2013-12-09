package eu.archivesportaleurope.harvester.oaipmh;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.archivesportaleurope.harvester.oaipmh.parser.record.DebugOaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;

public class ConsoleHarvester {
	private static final String YES = "Yes";
	private static final String SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES = "Save only the metadata record (e.g. EAD or EDM files)";
	private static final String SAVE_FULL_OAI_PMH_RESPONSES = "Save full OAI-PMH responses";

	private Logger logger;
	private File confDir;
	private File dataDir;
	private String baseUrl;
	private String metadataFormat;
	private String set;
	private String fromDate;
	private String toDate;
	private boolean debug = false;

	public ConsoleHarvester(File confDir, File dataDir) {
		logger = Logger.getLogger(ConsoleHarvester.class);
		this.confDir = confDir;
		this.dataDir = dataDir;
	}

	public static void main(String[] args) throws Exception {

		String baseDirString = ".";
		if (args.length > 0) {
			baseDirString = args[0];
		}
		File confDir = null;
		File dataDir = null;
		try {
			File baseDir = new File(baseDirString);
			File logsDir = new File(baseDirString, "logs");
			dataDir = new File(baseDirString, "data");
			logsDir.mkdirs();
			dataDir.mkdirs();
			confDir = new File(baseDir, "conf");
			File log4jXml = new File(confDir, "log4j.xml");
			if (log4jXml.exists()) {
				System.setProperty("harvester.logs", logsDir.getCanonicalPath());
				DOMConfigurator.configure(log4jXml.getAbsolutePath());
			} else {
				System.err.println("Could not start OAI-PMH Harvester");
				System.err.println("Log4j not properly configured. No log4j.xml found at "
						+ log4jXml.getCanonicalPath());
				System.exit(-1);
			}
		} catch (Exception e) {
			System.err.println("Could not start OAI-PMH Harvester");
			System.err.println("Log4j not properly configured. " + e.getMessage());
			System.exit(-1);
		}
		ConsoleHarvester consoleHarvester = new ConsoleHarvester(confDir, dataDir);
		consoleHarvester.start();
		// long startTime = System.currentTimeMillis();
		//
		// HashMap options = getOptions(args);
		// List rootArgs = (List) options.get("rootArgs");
		// if (rootArgs.size() == 0) {
		// throw new IllegalArgumentException();
		// }
		// String baseURL = (String) rootArgs.get(0);
		// String directory = (String) options.get("-outputDirectory");
		// String debugString = (String) options.get("-debug");
		// boolean debug = Boolean.parseBoolean(debugString);
		// String from = (String) options.get("-from");
		// String until = (String) options.get("-until");
		// String metadataPrefix = (String) options.get("-metadataPrefix");
		// if (metadataPrefix == null) {
		// metadataPrefix = "oai_dc";
		// }
		// String resumptionToken = (String) options.get("-resumptionToken");
		// String setSpec = (String) options.get("-setSpec");
		// File outputDirectory = new File(directory);
		// outputDirectory.mkdirs();
		// OaiPmhParser oaiPmhParser;
		// if (debug){
		// oaiPmhParser = new DebugOaiPmhParser(outputDirectory);
		// }else {
		// oaiPmhParser = new OaiPmhParser(outputDirectory);
		// }
		// if (resumptionToken != null) {					
		

		// run(baseURL, resumptionToken, oaiPmhParser);
		// }
		// else {
		// run(baseURL, from, until, metadataPrefix, setSpec, oaiPmhParser);
		// }
		//
		// logger.info(new Date());
		// long stopTime = System.currentTimeMillis();
		// calcHMS(stopTime, startTime);

		// }
		// catch (IllegalArgumentException e) {
		// logger.error("wrong parameters:\nExample: java -jar oaiharvester.jar -outputDirectory OUTPUT_DIR -setSpec SET_SPEC_VALUE [-debug true] [-from 1980-01-01] [-until 1990-01-01] -metadataPrefix METADATAPREFIX OAI_PMH_URL");
		// }
		// catch (Exception e) {
		// logger.error(e.getMessage(),e);
		// System.exit(-1);
		// }
	}

	public void start() throws Exception {
		logger.info("===============================================");
		logger.info("Start OAI-PMH Harvester " + ConsoleHarvester.getVersion());
		logger.info("===============================================");
		if (StringUtils.isBlank(baseUrl)) {
			while (metadataFormat == null) {
				baseUrl = getInput("What is the url of the OAI-PMH server?");
				try {
					List<String> metadataFormats = RetrieveOaiPmhInformation.retrieveMetadataFormats(baseUrl);
					if (metadataFormats == null || metadataFormats.isEmpty()) {
						logger.error("No metadata formats for this URL: " + baseUrl);
					} else {
						metadataFormat = getInput("Which metadata format do you want to use?'", metadataFormats);
					}
					List<String> setsInRepository = RetrieveOaiPmhInformation.retrieveSets(baseUrl);
					set = getInput("Which set do you want to use?'", setsInRepository);
					fromDate = getInputEmptyAllowed("Specify a FROM date or leave empty?(e.g. 2010-12-23)");
					toDate = getInputEmptyAllowed("Specify a TO date or leave empty?(e.g. 2010-12-23)");
					List<String> saveMethods = new ArrayList<String>();
					saveMethods.add(SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES);
					saveMethods.add(SAVE_FULL_OAI_PMH_RESPONSES);				
					String saveMethod = getInput("What do you want to store?'", saveMethods);
					debug = SAVE_FULL_OAI_PMH_RESPONSES.equals(saveMethod);
				} catch (Exception e) {
					logger.error("Sorry, the URL is not a correct repository URL or the repository does not contain any metadata formats...");
				}
			}
		}
		logger.info("===============================================");
		logger.info("Summary of OAI-PMH Harvester parameters");
		logger.info("===============================================");
		logger.info("Url of the OAI-PMH server:\t\t" + baseUrl);
		logger.info("Metadata format:\t\t\t" + metadataFormat);
		if (fromDate != null)
			logger.info("From date:\t\t\t\t" + fromDate);
		if (toDate != null)
			logger.info("To date:\t\t\t\t" + toDate);
		if (debug) {
			logger.info("Store method:\t\t\t" + SAVE_FULL_OAI_PMH_RESPONSES);
		} else {
			logger.info("Store method:\t\t\t" + SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES);
		}
		File baseUrlDataDir = new File(dataDir, convertToFilename(baseUrl));
		File metaDataFormatDataDir = new File(baseUrlDataDir, convertToFilename(metadataFormat));
		File outputDir = new File(metaDataFormatDataDir, convertToFilename(set));
		
		logger.info("Location of the files to be stored:\t" + outputDir.getCanonicalPath());
		List<String> proceedOptions = new ArrayList<String>();
		proceedOptions.add(YES);
		proceedOptions.add("No");
		String proceed = getInput("Do you want to proceed?", proceedOptions);
		if (YES.equals(proceed)){
	 		outputDir.mkdirs();
	 		File errorsDir = new File(dataDir, "errors");
	 		errorsDir.mkdirs();
			OaiPmhParser oaiPmhParser;
			if (debug) {
				oaiPmhParser = new DebugOaiPmhParser(outputDir);
			} else {
				oaiPmhParser = new OaiPmhParser(outputDir);
			}
			try {
				OaiPmhHarvester.runOai(baseUrl, fromDate, toDate, metadataFormat, set, oaiPmhParser,errorsDir);
			}catch (HarvesterParserException hpe){
				logger.error("Unable to parse XML response from the OAI-PMH server, look at the XML response file at " + hpe.getNotParsebleResponse().getCanonicalPath());
			}

		}
	}

	public void calcHMS(long stopTime, long startTime) {
		int hours, minutes, seconds;
		int timeInSeconds = (int) ((stopTime - startTime) / 1000);
		hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		seconds = timeInSeconds;
		logger.info("Elapsed time: " + hours + " hour(s) " + minutes + " minute(s) " + seconds + " second(s)");
	}




	public String getInput(String title, List<String> choices) {

		String choicesLine = "";
		for (int i = 0; i < choices.size(); i++) {
			choicesLine += (i + 1) + "): " + choices.get(i) + "\n";
		}
		String result = null;
		System.out.println(title);
		System.out.print(choicesLine);
		while (result == null) {
			System.out.print("\nChoose one of the following numbers (1-" + choices.size() + "): ");
			try {
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String input = bufferRead.readLine();
				if (StringUtils.isNumeric(input)) {
					int i = Integer.parseInt(input) - 1;
					if (i >= 0 && i < choices.size()) {
						result = choices.get(i);
					}

				}

			} catch (Exception e) {
				logger.error("Unable to read input: " + e.getMessage(), e);
			}
		}

		return result;

	}

	public String getInputEmptyAllowed(String title) {

		String result = null;
		System.out.println(title);
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String input = bufferRead.readLine();
			if (StringUtils.isNotBlank(input)) {
				result = input;

			}

		} catch (Exception e) {
			logger.error("Unable to read input: " + e.getMessage(), e);
		}

		return result;

	}

	public String getInput(String title) {

		String result = null;
		while (result == null) {
			System.out.println(title);
			try {
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String input = bufferRead.readLine();
				if (StringUtils.isNotBlank(input)) {
					result = input;

				}

			} catch (Exception e) {
				logger.error("Unable to read input: " + e.getMessage(), e);
			}
		}

		return result;

	}

	public static String getVersion() {
		String version = ConsoleHarvester.class.getPackage().getImplementationVersion();
		if (StringUtils.isBlank(version)) {
			version = "DEV-VERSION";
		}
		return version;
	}

	public static String convertToFilename(String name) {
		return name.replaceAll("[^a-zA-Z0-9\\-\\.]", "_");
	}
}
