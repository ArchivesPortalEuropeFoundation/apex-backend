package eu.archivesportaleurope.harvester.oaipmh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterConnectionException;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.DebugOaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhElement;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;

public class ConsoleHarvester {

    private static final String TRUE = "true";
    protected static final String BASE_DIR_PARAMETER = "baseDir";
    protected static final String CONF_FILE_PARAMETER = "confFile";
    private static final String YES = "Yes";
    private static final String SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES = "Save only the metadata record (e.g. EAD, EDM or DC files)";
    private static final String SAVE_FULL_OAI_PMH_RESPONSES = "Save full OAI-PMH responses";
    private static final String HARVEST_METHOD_LIST_RECORDS = "Harvest by verb ListRecords";
    private static final String HARVEST_METHOD_LIST_IDENTIFIERS_GETRECORD = "Harvest by verb ListIdentifiers/GetRecord (fail safe)";
    private Logger logger;
    private File dataDir;
    private File outputDir;
    private String baseUrl;
    private String metadataFormat;
    private String set;
    private String fromDate;
    private String toDate;
    private boolean getIdentifiersHarvestMethod = false;
    private boolean debug = false;
    private boolean silent = false;
    private String proxyServer;
    private String proxyUsername;
    private String proxyPassword;
    private Properties properties;

    public ConsoleHarvester(File dataDir, Properties properties) {
        logger = Logger.getLogger(ConsoleHarvester.class);
        this.dataDir = dataDir;
        this.properties = properties;
    }

    public static void main(String[] args) {
        Map<String, String> parameters = getParameters(args);
        String baseDirString = ".";
        Properties properties = null;
        if (parameters.containsKey(BASE_DIR_PARAMETER)) {
            baseDirString = parameters.get(BASE_DIR_PARAMETER);
        }

        File dataDir = null;
        try {
            if (parameters.containsKey(CONF_FILE_PARAMETER)) {
                properties = new Properties();
                properties.load(new FileInputStream(new File(parameters.get(CONF_FILE_PARAMETER))));
            }
            File baseDir = new File(baseDirString);
            File logsDir = new File(baseDirString, "logs");
            dataDir = new File(baseDirString, "data");
            logsDir.mkdirs();
            dataDir.mkdirs();
            File confDir = new File(baseDir, "conf");
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

        ConsoleHarvester consoleHarvester = null;
        String consoleHarvesterClassName = System.getProperty("consoleHarvesterClassName");
        if (consoleHarvesterClassName != null) {
            try {
                Class<?> clazz = Class.forName(consoleHarvesterClassName);
                Constructor<?> ctor = clazz.getConstructor(File.class, Properties.class);
                consoleHarvester = (ConsoleHarvester) ctor.newInstance(new Object[]{dataDir, properties});
            } catch (Exception e) {
                System.err.println("Could not create the Portugal Console Harvester. " + e.getMessage());
                System.exit(-1);
            }
        } else {
            consoleHarvester = new ConsoleHarvester(dataDir, properties);
        }
        consoleHarvester.start();
    }

    public void start() {
        logger.info("===============================================");
        logger.info("Start OAI-PMH Harvester " + ConsoleHarvester.getVersion());
        logger.info("===============================================");
        logger.info("Copyright by Archives Portal Europe");
        logger.info("Visit our website http://www.archivesportaleurope.net");
        logger.info("===============================================");
        OaiPmhHttpClient oaiPmhHttpClient = null;
        try {
            if (properties != null) {
                proxyServer = properties.getProperty("proxy.server");
                proxyUsername = properties.getProperty("proxy.username");
                proxyPassword = properties.getProperty("proxy.password");
                baseUrl = properties.getProperty("oai-pmh.url");
                metadataFormat = properties.getProperty("metadata-format");
                set = properties.getProperty("set");
                fromDate = properties.getProperty("from");
                toDate = properties.getProperty("to");
                debug = TRUE.equals(properties.getProperty("debug"));
                silent = TRUE.equals(properties.getProperty("silent"));
                getIdentifiersHarvestMethod = TRUE.equals(properties.getProperty("harvest-method-getidentifiers"));
            }
            if (StringUtils.isNotBlank(proxyServer)) {
                oaiPmhHttpClient = new OaiPmhHttpClient(proxyServer, proxyUsername, proxyPassword);
            } else {
                oaiPmhHttpClient = new OaiPmhHttpClient();
            }
            if (!silent) {
                while (metadataFormat == null) {
                    baseUrl = getInput("What is the url of the OAI-PMH server?", baseUrl);
                    if (StringUtils.isNotBlank(baseUrl)) {
                        baseUrl = baseUrl.trim();
                    }
                    proxyServer = getInputEmptyAllowed("Specify proxy server or leave empty?(e.g. http://proxyserver:8080)", proxyServer);
                    if (StringUtils.isNotBlank(proxyServer)) {
                        proxyUsername = getInputEmptyAllowed("Specify username or leave empty?(DOMAIN/username or username)", proxyUsername);
                        proxyPassword = getPasswordEmptyAllowed("Specify password or leave empty?", proxyPassword);
                        oaiPmhHttpClient = new OaiPmhHttpClient(proxyServer, proxyUsername, proxyPassword);
                    } else {
                        oaiPmhHttpClient = new OaiPmhHttpClient();
                    }
                    try {
                        List<OaiPmhElement> metadataFormats = RetrieveOaiPmhInformation.retrieveMetadataFormats(baseUrl, oaiPmhHttpClient);
                        if (metadataFormats == null || metadataFormats.isEmpty()) {
                            logger.error("No metadata formats for this URL: " + baseUrl);
                        } else {
                            metadataFormat = getInputFromOaiPmhElements("Which metadata format do you want to use?'", metadataFormats, false);
                        }
                        List<OaiPmhElement> setsInRepository = RetrieveOaiPmhInformation.retrieveSets(baseUrl, oaiPmhHttpClient);
                        if (setsInRepository == null || setsInRepository.isEmpty()) {
                            throw new RuntimeException("No sets with records for " + baseUrl + ". exiting");
                        } else {
                            set = getInputFromOaiPmhElements("Which set do you want to use?'", setsInRepository, true);
                            fromDate = getInputEmptyAllowed("Specify a FROM date or leave empty?(e.g. 2010-12-23)", fromDate);
                            toDate = getInputEmptyAllowed("Specify a TO date or leave empty?(e.g. 2010-12-23)", toDate);
                            List<String> harvesterMethods = new ArrayList<String>();
                            harvesterMethods.add(HARVEST_METHOD_LIST_IDENTIFIERS_GETRECORD);
                            harvesterMethods.add(HARVEST_METHOD_LIST_RECORDS);
                            String harvesterMethod = getInput("What do you want to harvest?'", harvesterMethods);
                            getIdentifiersHarvestMethod = HARVEST_METHOD_LIST_IDENTIFIERS_GETRECORD.equals(harvesterMethod);
                            List<String> saveMethods = new ArrayList<String>();
                            saveMethods.add(SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES);
                            saveMethods.add(SAVE_FULL_OAI_PMH_RESPONSES);
                            String saveMethod = getInput("What do you want to store?'", saveMethods);
                            debug = SAVE_FULL_OAI_PMH_RESPONSES.equals(saveMethod);
                        }
                    } catch (HarvesterConnectionException e) {
                        baseUrl = null;
                        proxyServer = null;
                        logger.error("Unable to connect to the URL: " + e.getCause().getMessage(), e);
                    } catch (RuntimeException e) {
                        baseUrl = null;
                        proxyServer = null;
                        logger.error("No sets with records for this URL: " + baseUrl, e);
                    } catch (Exception e) {
                        baseUrl = null;
                        proxyServer = null;
                        logger.error("Sorry, the URL is not a correct repository URL or the repository does not contain any metadata formats...", e);
                    }
                }
            }
            logger.info("===============================================");
            logger.info("Summary of OAI-PMH Harvester parameters");
            logger.info("===============================================");
            if (StringUtils.isNotBlank(proxyServer)) {
                if (StringUtils.isNotBlank(proxyUsername) && StringUtils.isNotBlank(proxyPassword)) {
                    logger.info("Proxy server with authentication:\t\t" + proxyServer);
                } else {
                    logger.info("Proxy server:\t\t" + proxyServer);
                }
            }
            logger.info("Url of the OAI-PMH server:\t\t" + baseUrl);
            logger.info("Metadata format:\t\t\t" + metadataFormat);
            logger.info("Set:\t\t\t\t\t" + set);
            if (fromDate != null) {
                logger.info("From date:\t\t\t\t" + fromDate);
            }
            if (toDate != null) {
                logger.info("To date:\t\t\t\t" + toDate);
            }
            if (getIdentifiersHarvestMethod) {
                logger.info("Harvest method:\t\t\t\t" + HARVEST_METHOD_LIST_IDENTIFIERS_GETRECORD);
            } else {
                logger.info("Harvest method:\t\t\t\t" + HARVEST_METHOD_LIST_RECORDS);
            }
            if (debug) {
                logger.info("Store method:\t\t\t\t" + SAVE_FULL_OAI_PMH_RESPONSES);
            } else {
                logger.info("Store method:\t\t\t\t" + SAVE_ONLY_THE_METADATA_RECORD_E_G_EAD_OR_EDM_FILES);
            }
            File baseUrlDataDir = new File(dataDir, convertToFilename(minimizeBaseUrlDataDir(baseUrl)));
            File metaDataFormatDataDir = new File(baseUrlDataDir, convertToFilename(metadataFormat));
            outputDir = null;
            if (set == null) {
                outputDir = metaDataFormatDataDir;
            } else {
                outputDir = new File(metaDataFormatDataDir, convertToFilename(set));
            }
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
                    if (getIdentifiersHarvestMethod) {
                        OaiPmhHarvester.harvestByListIdentifiers(new HarvestObject(), baseUrl, fromDate, toDate, metadataFormat, set, oaiPmhParser, errorsDir, oaiPmhHttpClient);
                    } else {
                        OaiPmhHarvester.harvestByListRecords(new HarvestObject(), baseUrl, fromDate, toDate, metadataFormat, set, oaiPmhParser, errorsDir, oaiPmhHttpClient);
                    }
                    logger.info("===============================================");
                    calcHMS(System.currentTimeMillis(), startTime);
                } catch (HarvesterParserException hpe) {

                } catch (HarvesterConnectionException ste) {
                    String errors = "Url that have connection problems: '" + ste.getRequestUrl() + "'\n\n";
                    errors += ste.getMessage() + " (Time out is 60 minutes)";
                    logger.error(errors);

                }

            }
        } catch (Exception e) {
            logger.error("Unexcepted error occurred: " + e.getMessage(), e);
        } finally {
            if (oaiPmhHttpClient != null) {
                try {
                    oaiPmhHttpClient.close();
                } catch (IOException io) {
                    logger.error("Unexcepted error occurred: " + io.getMessage(), io);
                }
            }
        }
        logger.info("===============================================");
        logger.info("OAI-PMH Harvester " + ConsoleHarvester.getVersion() + " finished");
        logger.info("===============================================");
        logger.info("Copyright by Archives Portal Europe");
        logger.info("Visit our website http://www.archivesportaleurope.net");
        logger.info("===============================================");
    }

    protected static Map<String, String> getParameters(String[] args) {
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

    private String minimizeBaseUrlDataDir(String url) {
        String temp = url.replaceAll("https://", "").replaceAll("http://", "");
        String[] splitted = temp.split("/");
        return splitted[0];
    }

    protected void calcHMS(long stopTime, long startTime) {
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
                if (StringUtils.isNotBlank(input) && StringUtils.isNumeric(input)) {
                    int i = Integer.parseInt(input) - 1;
                    if (i >= 0 && i < choices.size()) {
                        result = choices.get(i);
                    }

                }

            } catch (Exception e) {
                logger.error("Unable to read input: " + e.getMessage());
            }
        }

        return result;

    }

    public String getInputFromOaiPmhElements(String title, List<OaiPmhElement> choices, boolean emptyAllowed) {
        if (choices.size() == 0) {
            return null;
        }
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
                if (emptyAllowed && StringUtils.isBlank(input)) {
                    return null;
                }
                if (StringUtils.isNotBlank(input) && StringUtils.isNumeric(input)) {
                    int i = Integer.parseInt(input) - 1;
                    if (i >= 0 && i < choices.size()) {
                        result = choices.get(i).getElement();
                    }

                }

            } catch (Exception e) {
                logger.error("Unable to read input: " + e.getMessage());
            }
        }

        return result;

    }

    public String getInputEmptyAllowed(String title, String defaultValue) {
        if (defaultValue != null) {
            System.out.println(title + "[" + defaultValue + "]");
            return defaultValue;
        }
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

    public String getPasswordEmptyAllowed(String title, String defaultValue) {
        if (defaultValue != null) {
            System.out.println(title + "[************]");
            return defaultValue;
        }
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

    public String getInput(String title, String defaultValue) {
        if (StringUtils.isNotBlank(defaultValue)) {
            System.out.println(title + "[" + defaultValue + "]");
            return defaultValue;
        }
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

    public String getSet() {
        return set;
    }

    protected File getDataDir() {
        return dataDir;
    }

    public File getOutputDir() {
        return outputDir;
    }
}
