package eu.archivesportaleurope.harvester.oaipmh;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterInterruptionException;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhRecord;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.ResultInfo;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.harvester.verb.ListIdentifiersVerb;
import eu.archivesportaleurope.harvester.verb.ListRecordsVerb;

public class OaiPmhHarvester {
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger LOGGER = Logger.getLogger(OaiPmhHarvester.class);

	public static void runOai(HarvestObject harvestObject, String baseURL, String from, String until, String metadataPrefix, String setSpec,
			OaiPmhParser oaiPmhParser, File errorDir, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
		try {
			ListRecordsVerb listRecordsVerb = new ListRecordsVerb(oaiPmhHttpClient);
			ResultInfo resultInfo = listRecordsVerb.harvest(baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser,
					errorDir, harvestObject.getNumberOfRequests());
			boolean hasErrors = false;
			while (resultInfo != null && !harvestObject.isStopHarvesting()) {
				harvestObject.increaseNumberOfRequests();
				List<String> errors = resultInfo.getErrors();
				if (errors != null && errors.size() > 0) {
					LOGGER.error("Found the following errors:");
					harvestObject.addErrors("\nUrl that contains errors: '" + resultInfo.getRequestUrl() + "'\n");
					int length = errors.size();
					for (int i = 0; i < length; ++i) {
						String item = errors.get(i);
						if (StringUtils.isNotBlank(item)) {
							harvestObject.addErrors(item);
							LOGGER.error(item);
						}

					}
					LOGGER.error("Url that contains errors: '" + resultInfo.getRequestUrl() + "'");
					hasErrors = true;
					break;
				}
				for (OaiPmhRecord record : resultInfo.getRecords()) {
					String action = "retrieved";
					if (record.isDeleted()){
						action = "deleted";
					}
					harvestObject.increaseNumberOfRecords();
					harvestObject.setLatestRecordId(record.getIdentifier());
					LOGGER.info("("+harvestObject.getNumberOfRequests()+"," + harvestObject.getNumberOfRecords() +"): Record with ID: " + record.getIdentifier() + " "+ action +" ("
							+ DATE_TIME_FORMAT.format(record.getTimestamp()) + ")");
					harvestObject.add(record);							}
				String resumptionToken = resultInfo.getNewResumptionToken();
				LOGGER.debug("resumptionToken: '" + resumptionToken + "'");
				if (StringUtils.isBlank(resumptionToken)) {
					resultInfo = null;
				} else {
					resultInfo = listRecordsVerb.harvest(baseURL, resumptionToken, oaiPmhParser, errorDir, harvestObject.getNumberOfRequests());
				}
			}
			String logSuffix = "";
			if (harvestObject.isStopHarvesting()){
				logSuffix = "Harvesting is stopped manually.";
			}
			if (hasErrors) {
				LOGGER.error(harvestObject.getNumberOfRecords() + " records harvested, but with errors. " + logSuffix);
			} else {
				LOGGER.info(harvestObject.getNumberOfRecords() + " records harvested successfully with no errors. " + logSuffix);
			}
			if (harvestObject.isStopHarvesting()){
				throw new HarvesterInterruptionException(resultInfo.getRequestUrl(), logSuffix);
			}
		} catch (HarvesterParserException hpe) {
			LOGGER.error("Unable to parse XML response from the OAI-PMH server, look at the XML response file at "
					+ hpe.getNotParsebleResponse().getCanonicalPath());
			throw hpe;
		}
	}
	
	public static void harvestByListIdentifiers(HarvestObject harvestObject, String baseURL, String from, String until, String metadataPrefix, String setSpec,
			OaiPmhParser oaiPmhParser, File errorDir, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
		try {
			ListIdentifiersVerb listIdentifiersVerb = new ListIdentifiersVerb(oaiPmhHttpClient);
			ResultInfo resultInfo = listIdentifiersVerb.harvest(baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser,
					errorDir, harvestObject.getNumberOfRequests());
			boolean hasErrors = false;
			while (resultInfo != null && !harvestObject.isStopHarvesting()) {
				harvestObject.increaseNumberOfRequests();
				List<String> errors = resultInfo.getErrors();
				if (errors != null && errors.size() > 0) {
					LOGGER.error("Found the following errors:");
					harvestObject.addErrors("\nUrl that contains errors: '" + resultInfo.getRequestUrl() + "'\n");
					int length = errors.size();
					for (int i = 0; i < length; ++i) {
						String item = errors.get(i);
						if (StringUtils.isNotBlank(item)) {
							harvestObject.addErrors(item);
							LOGGER.error(item);
						}

					}
					LOGGER.error("Url that contains errors: '" + resultInfo.getRequestUrl() + "'");
					hasErrors = true;
					break;
				}
				for (OaiPmhRecord record : resultInfo.getRecords()) {
					String action = "retrieved";
					if (record.isDeleted()){
						action = "deleted";
					}
					harvestObject.increaseNumberOfRecords();
					harvestObject.setLatestRecordId(record.getIdentifier());
					LOGGER.info("("+harvestObject.getNumberOfRequests()+"," + harvestObject.getNumberOfRecords() +"): Record with ID: " + record.getIdentifier() + " "+ action +" ("
							+ DATE_TIME_FORMAT.format(record.getTimestamp()) + ")");
					harvestObject.add(record);							}
				String resumptionToken = resultInfo.getNewResumptionToken();
				LOGGER.debug("resumptionToken: '" + resumptionToken + "'");
				if (StringUtils.isBlank(resumptionToken)) {
					resultInfo = null;
				} else {
					resultInfo = listIdentifiersVerb.harvest(baseURL, resumptionToken, oaiPmhParser, errorDir, harvestObject.getNumberOfRequests());
				}
			}
			String logSuffix = "";
			if (harvestObject.isStopHarvesting()){
				logSuffix = "Harvesting is stopped manually.";
			}
			if (hasErrors) {
				LOGGER.error(harvestObject.getNumberOfRecords() + " records harvested, but with errors. " + logSuffix);
			} else {
				LOGGER.info(harvestObject.getNumberOfRecords() + " records harvested successfully with no errors. " + logSuffix);
			}
			if (harvestObject.isStopHarvesting()){
				throw new HarvesterInterruptionException(resultInfo.getRequestUrl(), logSuffix);
			}
		} catch (HarvesterParserException hpe) {
			LOGGER.error("Unable to parse XML response from the OAI-PMH server, look at the XML response file at "
					+ hpe.getNotParsebleResponse().getCanonicalPath(),hpe);
			throw hpe;
		}
	}
}
