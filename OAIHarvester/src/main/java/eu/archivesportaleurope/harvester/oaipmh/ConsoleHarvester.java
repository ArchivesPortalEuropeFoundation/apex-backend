package eu.archivesportaleurope.harvester.oaipmh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.archivesportaleurope.harvester.oaipmh.parser.record.DebugOaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;

public class ConsoleHarvester {
	private static final String TRUE = "true";
	private static final String BASE_DIR_PARAMETER = "baseDir";
	private static final String CONF_FILE_PARAMETER = "confFile";
	private static final String YES = "Yes";
	private static final String SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES = "Save only the metadata record (e.g. EAD or EDM files)";
	private static final String SAVE_FULL_OAI_PMH_RESPONSES = "Save full OAI-PMH responses";

	private Logger logger;
	private File dataDir;
	private String baseUrl;
	private String metadataFormat;
	private String set;
	private String fromDate;
	private String toDate;
	private boolean debug = false;
	private boolean silent = false;
	private Properties properties;

	public ConsoleHarvester(File confDir, File dataDir, Properties properties) {
		logger = Logger.getLogger(ConsoleHarvester.class);
		this.dataDir = dataDir;
		this.properties = properties;
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> parameters = getParameters(args);
		String baseDirString = ".";
		Properties properties = null;
		if (parameters.containsKey(BASE_DIR_PARAMETER)) {
			baseDirString = parameters.get(BASE_DIR_PARAMETER);
		}
		if (parameters.containsKey(CONF_FILE_PARAMETER)) {
			properties = new Properties();
			properties.load(new FileInputStream(new File(parameters.get(CONF_FILE_PARAMETER))));
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
		ConsoleHarvester consoleHarvester = new ConsoleHarvester(confDir, dataDir, properties);
		consoleHarvester.start();
	}

	public void start() {
		logger.info("===============================================");
		logger.info("Start OAI-PMH Harvester " + ConsoleHarvester.getVersion());
		logger.info("===============================================");
		try {
			if (properties == null) {
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
			} else {
				baseUrl = properties.getProperty("oai-pmh.url");
				metadataFormat = properties.getProperty("metadata-format");
				set = properties.getProperty("set");
				fromDate = properties.getProperty("from");
				toDate = properties.getProperty("to");
				debug = TRUE.equals(properties.getProperty("debug"));
				silent = TRUE.equals(properties.getProperty("silent"));

			}
			logger.info("===============================================");
			logger.info("Summary of OAI-PMH Harvester parameters");
			logger.info("===============================================");
			logger.info("Url of the OAI-PMH server:\t\t" + baseUrl);
			logger.info("Metadata format:\t\t\t" + metadataFormat);
			logger.info("Set:\t\t\t\t\t" + set);
			if (fromDate != null)
				logger.info("From date:\t\t\t\t" + fromDate);
			if (toDate != null)
				logger.info("To date:\t\t\t\t" + toDate);
			if (debug) {
				logger.info("Store method:\t\t\t\t" + SAVE_FULL_OAI_PMH_RESPONSES);
			} else {
				logger.info("Store method:\t\t\t\t" + SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES);
			}
			File baseUrlDataDir = new File(dataDir, convertToFilename(baseUrl));
			File metaDataFormatDataDir = new File(baseUrlDataDir, convertToFilename(metadataFormat));
			File outputDir = new File(metaDataFormatDataDir, convertToFilename(set));

			logger.info("Location of the files to be stored:\t" + outputDir.getCanonicalPath());
			logger.info("===============================================");
			List<String> proceedOptions = new ArrayList<String>();
			proceedOptions.add(YES);
			proceedOptions.add("No");
			String proceed = null;
			if (silent) {
				proceed = YES;
			} else {
				proceed = getInput("Do you want to proceed?", proceedOptions);
			}
			if (YES.equals(proceed)) {
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
					long startTime = System.currentTimeMillis();
					OaiPmhHarvester.runOai(baseUrl, fromDate, toDate, metadataFormat, set, oaiPmhParser, errorsDir);
					logger.info("===============================================");
					calcHMS(System.currentTimeMillis(), startTime);
				} catch (HarvesterParserException hpe) {

				}

			}
		} catch (Exception e) {
			logger.error("Unexcepted error occurred: " + e.getMessage(), e);
		}
		logger.info("===============================================");
	}

	private static Map<String, String> getParameters(String[] args) {
		Map<String, String> parameters = new HashMap<String, String>();
		for (String arg : args) {
			if (arg.startsWith("-") && arg.contains("=")) {
				String[] splitted = arg.substring(1).split("=");
				String key = splitted[0];
				String value = splitted[1];
				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
					parameters.put(key, value);
				}

			}
		}
		return parameters;
	}

	private void calcHMS(long stopTime, long startTime) {
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
