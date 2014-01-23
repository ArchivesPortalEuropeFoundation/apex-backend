package eu.archivesportaleurope.harvester.oaipmh;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhRecord;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.ResultInfo;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.harvester.verb.ListRecordsSaxWriteDirectly;

public class OaiPmhHarvester {
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger LOGGER = Logger.getLogger(OaiPmhHarvester.class);

	public static HarvestResult runOai(String baseURL, String from, String until, String metadataPrefix, String setSpec,
			OaiPmhParser oaiPmhParser, File errorDir, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
		HarvestResult harvestResult = new HarvestResult();
		try {
			int numberOfRecords = 0;
			int number = 0;
			ListRecordsSaxWriteDirectly listRecordsSax = new ListRecordsSaxWriteDirectly(oaiPmhHttpClient);
			ResultInfo resultInfo = listRecordsSax.harvest(baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser,
					errorDir, number);
			boolean hasErrors = false;
			while (resultInfo != null) {
				number++;
				List<String> errors = resultInfo.getErrors();
				if (errors != null && errors.size() > 0) {
					LOGGER.error("Found the following errors:");
					harvestResult.addErrors("\nUrl that contains errors: '" + resultInfo.getRequestUrl() + "'\n");
					int length = errors.size();
					for (int i = 0; i < length; ++i) {
						String item = errors.get(i);
						if (StringUtils.isNotBlank(item)) {
							harvestResult.addErrors(item);
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
					numberOfRecords++;
					LOGGER.info("("+number+"," + numberOfRecords +"): Record with ID: " + record.getIdentifier() + " "+ action +" ("
							+ DATE_TIME_FORMAT.format(record.getTimestamp()) + ")");
					harvestResult.add(record);							}
				String resumptionToken = resultInfo.getNewResumptionToken();
				LOGGER.debug("resumptionToken: '" + resumptionToken + "'");
				if (StringUtils.isBlank(resumptionToken)) {
					resultInfo = null;
				} else {
					resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser, errorDir, number);
				}
			}
			if (hasErrors) {
				LOGGER.error(numberOfRecords + " records harvested, but with errors");
			} else {
				LOGGER.info(numberOfRecords + " records harvested successfully with no errors");
			}
			harvestResult.setNumberOfRecords(numberOfRecords);
		} catch (HarvesterParserException hpe) {
			LOGGER.error("Unable to parse XML response from the OAI-PMH server, look at the XML response file at "
					+ hpe.getNotParsebleResponse().getCanonicalPath());
			throw hpe;
		}
		return harvestResult;
	}
}
