package eu.apenet.dashboard.harvest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.User;
import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;
import eu.archivesportaleurope.harvester.oaipmh.OaiPmhHarvester;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterConnectionException;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterInterruptionException;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville Date: 04/12/2013
 * 
 * @author Yoann Moranville
 */
public class DataHarvester {
	private static final Logger LOGGER = Logger.getLogger(DataHarvester.class);
	private static final int MAX_NUMBER_HARVESTED_FILES_FOR_TEST = 10;
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd"); // 1980-01-01

	private long archivalInstitutionOaiPmhId;

	public DataHarvester(long archivalInstitutionOaiPmhId) {
		this.archivalInstitutionOaiPmhId = archivalInstitutionOaiPmhId;
	}

	public void run() {
		ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance()
				.getArchivalInstitutionOaiPmhDAO();
		ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO
				.findById(archivalInstitutionOaiPmhId);

		String baseURL = archivalInstitutionOaiPmh.getUrl();
		String metadataPrefix = archivalInstitutionOaiPmh.getMetadataPrefix();

		String from = null;
		String until = null;
		if (archivalInstitutionOaiPmh.getFrom() != null) {
			from = archivalInstitutionOaiPmh.getFrom();
		}
		Date currentDate = new Date();
		Date newHarvestingDate = new Date(currentDate.getTime() + archivalInstitutionOaiPmh.getIntervalHarvesting());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		String newFrom = DATE_FORMATTER.format(calendar.getTime());
		int days = calendar.get(Calendar.DAY_OF_YEAR) - 1;
		calendar.set(Calendar.DAY_OF_YEAR, days);
		until = DATE_FORMATTER.format(calendar.getTime());
		String harvesterProfileLog = getFullInfo(archivalInstitutionOaiPmh, from, until, currentDate);
		LOGGER.info("Harvested now: " + archivalInstitutionOaiPmh.getUrl() + " (set: "
				+ archivalInstitutionOaiPmh.getSet() + ", metadataPrefix: "
				+ archivalInstitutionOaiPmh.getMetadataPrefix() + ", from:" + from + ", until:" + until + ")");
		String setSpec = null;
		if (archivalInstitutionOaiPmh.getSet() != null) {
			setSpec = archivalInstitutionOaiPmh.getSet();
		}

		ArchivalInstitution archivalInstitution = archivalInstitutionOaiPmh.getArchivalInstitution();
		String subdirectory = APEnetUtilities.FILESEPARATOR + archivalInstitution.getAiId()
				+ APEnetUtilities.FILESEPARATOR + "oai_" + System.currentTimeMillis() + APEnetUtilities.FILESEPARATOR;
		String directory = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + subdirectory;
		File outputDirectory = new File(directory);
		File errorsDirectory = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + "/oai_errors");
		errorsDirectory.mkdirs();
		outputDirectory.mkdirs();
		OaiPmhParser oaiPmhParser = null;
		if (APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
			oaiPmhParser = new OaiPmhParser(outputDirectory);
		} else {
			oaiPmhParser = new OaiPmhParser(outputDirectory, MAX_NUMBER_HARVESTED_FILES_FOR_TEST);
		}
		User partner = archivalInstitution.getPartner();
		if (partner == null) {
			partner = DAOFactory.instance().getUserDAO().getCountryManagerOfCountry(archivalInstitution.getCountry());
		}
		OaiPmhHttpClient oaiPmhHttpClient = null;
		try {
			if (archivalInstitutionOaiPmh.getErrors() != null){
				archivalInstitutionOaiPmh.setErrors(null);
				if (archivalInstitutionOaiPmh.getErrorsResponsePath() != null){
					ContentUtils.deleteFile(archivalInstitutionOaiPmh.getErrorsResponsePath() , false);
				}
				archivalInstitutionOaiPmh.setErrorsResponsePath(null);
				archivalInstitutionOaiPmhDAO.store(archivalInstitutionOaiPmh);
			
			}
			oaiPmhHttpClient = new OaiPmhHttpClient();
			HarvestObject harvestObject = new HarvestObject(archivalInstitutionOaiPmhId);
			HarvesterDaemon.setHarvestObject(harvestObject);
			OaiPmhHarvester.harvestByListRecords(harvestObject, baseURL, from, until, metadataPrefix, setSpec,
					oaiPmhParser, errorsDirectory, oaiPmhHttpClient);
			if (harvestObject.getErrors() != null){
				throw new OaiPmhErrorsException(harvestObject.getErrors());
			}
			archivalInstitutionOaiPmh.setLastHarvesting(new Date());
			archivalInstitutionOaiPmh.setFrom(newFrom);
			if (!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
				archivalInstitutionOaiPmh.setEnabled(false);
			}
			
			Ingestionprofile ingestionprofile = archivalInstitutionOaiPmh.getIngestionprofile();
			Properties properties = retrieveProperties(ingestionprofile);
			LOGGER.info("Start adding files to queue");
			File[] harvestedFiles = outputDirectory.listFiles();
			LOGGER.info("Number of records are " + harvestObject.getNumberOfRecords() + " found files: " + harvestedFiles.length);
			UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();

			JpaUtil.beginDatabaseTransaction();
			for (File file : harvestedFiles) {
				UpFile upFile = createUpFile(subdirectory, file.getName(), UploadMethod.OAI_PMH,
						archivalInstitution.getAiId(), FileType.XML);
				upFile = upFileDAO.insertSimple(upFile);
				EadService.useProfileActionForHarvester(upFile, properties);
			}
			JpaUtil.commitDatabaseTransaction();
			LOGGER.info("Files are added to queue");
			archivalInstitutionOaiPmh.setNewHarvesting(newHarvestingDate);
			archivalInstitutionOaiPmh.setErrors(null);
			archivalInstitutionOaiPmh.setErrorsResponsePath(null);
			archivalInstitutionOaiPmhDAO.store(archivalInstitutionOaiPmh);

			LOGGER.info("Harvest completed: harvested " + harvestObject.getNumberOfRecords() + " EAD files from \nID:"
					+ archivalInstitutionOaiPmh.getId() + "\n" + harvesterProfileLog + "\n --- Oldest file harvested: "
					+ harvestObject.getOldestFileHarvested() + " --- Newest file harvested: "
					+ harvestObject.getNewestFileHarvested());
			UserService.sendEmailHarvestFinished(archivalInstitution, partner, harvestObject.getNumberOfRecords(), harvesterProfileLog,
					harvestObject.getOldestFileHarvested(), harvestObject.getNewestFileHarvested());

		}catch (OaiPmhErrorsException oee){
			String errors = oee.getErrors();
			handleExceptions(partner, harvesterProfileLog, newHarvestingDate, outputDirectory, errors, null);
		}catch (HarvesterInterruptionException hpe){
			String errors = "Last url before the processing is stopped manually: '" + hpe.getRequestUrl() + "'\n\n";
			LOGGER.info(errors);
			handleExceptions(partner, harvesterProfileLog, newHarvestingDate, outputDirectory, errors, null);
		}catch (HarvesterParserException hpe){
			String errors = "Url that contains errors: '" + hpe.getRequestUrl() + "'\n\n";
			errors+= hpe.getCause().getMessage();
			LOGGER.error(errors);
			handleExceptions(partner, harvesterProfileLog, newHarvestingDate, outputDirectory, errors, hpe.getNotParsebleResponse());
		}catch (HarvesterConnectionException e) {
			String errors = "Url that have connection problems: '" + e.getRequestUrl() + "'\n\n";
			errors+= e.getMessage() +" (Time out is 5 minutes)";
			LOGGER.error(errors);
			handleExceptions(partner, harvesterProfileLog, newHarvestingDate, outputDirectory, errors, null);
		}catch (Exception e) {
			String errors = APEnetUtilities.generateThrowableLog(e);
			LOGGER.error(errors);
			handleExceptions(partner, harvesterProfileLog, newHarvestingDate, outputDirectory, errors, null);
		} finally {
			HarvesterDaemon.setHarvestObject(null);
			if (oaiPmhHttpClient != null) {
				try {
					oaiPmhHttpClient.close();
				} catch (IOException io) {
					LOGGER.error("Unexcepted error occurred: " + APEnetUtilities.generateThrowableLog(io));
				}
			}
			try {
				File parentFile = outputDirectory.getParentFile();
				ContentUtils.deleteFile(outputDirectory, false);
				if (parentFile.listFiles().length == 0){
					ContentUtils.deleteFile(parentFile, false);
				}
			} catch (IOException e) {
			}

		}
	}

	private void handleExceptions(User partner, String harvesterProfileLog, Date newHarvestingDate, File outputDirectory, String errors, File errorsResponseFile){
		JpaUtil.rollbackDatabaseTransaction();
		ArchivalInstitutionOaiPmh archivalInstitutionOaiPmhNew = DAOFactory.instance()
				.getArchivalInstitutionOaiPmhDAO().findById(archivalInstitutionOaiPmhId);
		if (archivalInstitutionOaiPmhNew.getErrors() != null) {
			LOGGER.error("Second time harvesting failed for \nID:" + archivalInstitutionOaiPmhNew.getId() + "\n"
					+ harvesterProfileLog);
			archivalInstitutionOaiPmhNew.setEnabled(false);
		} else {
			LOGGER.error("First time harvesting failed for \nID:" + archivalInstitutionOaiPmhNew.getId() + "\n"
					+ harvesterProfileLog);
		}
		archivalInstitutionOaiPmhNew.setNewHarvesting(newHarvestingDate);
		archivalInstitutionOaiPmhNew.setErrors(harvesterProfileLog + "================================================\n\n" + errors);
		if (errorsResponseFile != null){
			try {
				archivalInstitutionOaiPmhNew.setErrorsResponsePath(errorsResponseFile.getCanonicalPath());
			} catch (IOException e1) {
			}
		}
		archivalInstitutionOaiPmhNew.setLastHarvesting(new Date());
		if (!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
			archivalInstitutionOaiPmhNew.setEnabled(false);
		}
		DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().store(archivalInstitutionOaiPmhNew);
		UserService.sendEmailHarvestFailed(archivalInstitutionOaiPmhNew.getArchivalInstitution(), partner, harvesterProfileLog,
				errors, archivalInstitutionOaiPmhNew.getErrorsResponsePath());
	}

	private UpFile createUpFile(String upDirPath, String filePath, String uploadMethodString, Integer aiId,
			FileType fileType) {
		UpFile upFile = new UpFile();
		upFile.setFilename(filePath);
		upFile.setPath(upDirPath);
		upFile.setAiId(aiId);
		upFile.setFileType(fileType);

		UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO()
				.getUploadMethodByMethod(uploadMethodString);
		upFile.setUploadMethod(uploadMethod);

		return upFile;
	}

	private static Properties retrieveProperties(Ingestionprofile ingestionprofile) {
		Properties properties = new Properties();
		properties.setProperty(QueueItem.XML_TYPE, ingestionprofile.getFileType() + "");
		properties.setProperty(QueueItem.NO_EADID_ACTION, ingestionprofile.getNoeadidAction().getId() + "");
		properties.setProperty(QueueItem.EXIST_ACTION, ingestionprofile.getExistAction().getId() + "");
		properties.setProperty(QueueItem.DAO_TYPE, ingestionprofile.getDaoType().getId() + "");
		properties.setProperty(QueueItem.DAO_TYPE_CHECK, ingestionprofile.getDaoTypeFromFile() + "");
		properties.setProperty(QueueItem.UPLOAD_ACTION, ingestionprofile.getUploadAction().getId() + "");
		properties.setProperty(QueueItem.CONVERSION_TYPE, ingestionprofile.getEuropeanaConversionType() + "");
		properties.setProperty(QueueItem.DATA_PROVIDER, ingestionprofile.getEuropeanaDataProvider() + "");
		properties.setProperty(QueueItem.DATA_PROVIDER_CHECK, ingestionprofile.getEuropeanaDataProviderFromFile() + "");
		properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE, ingestionprofile.getEuropeanaDaoType() + "");
		properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE_CHECK, ingestionprofile.getEuropeanaDaoTypeFromFile() + "");
		properties.setProperty(QueueItem.LANGUAGES, ingestionprofile.getEuropeanaLanguages() + "");
		properties.setProperty(QueueItem.LANGUAGE_CHECK, ingestionprofile.getEuropeanaLanguagesFromFile() + "");
		properties.setProperty(QueueItem.LICENSE, ingestionprofile.getEuropeanaLicense() + "");
		properties.setProperty(QueueItem.LICENSE_DETAILS, ingestionprofile.getEuropeanaLicenseDetails() + "");
		properties.setProperty(QueueItem.LICENSE_ADD_INFO, ingestionprofile.getEuropeanaAddRights() + "");
		properties.setProperty(QueueItem.HIERARCHY_PREFIX_CHECK, ingestionprofile.getEuropeanaHierarchyPrefixCheck()
				+ "");
		properties.setProperty(QueueItem.HIERARCHY_PREFIX, ingestionprofile.getEuropeanaHierarchyPrefix() + "");
		properties.setProperty(QueueItem.INHERIT_FILE_CHECK, ingestionprofile.getEuropeanaInheritElementsCheck() + "");
		properties.setProperty(QueueItem.INHERIT_FILE, ingestionprofile.getEuropeanaInheritElements() + "");
		properties.setProperty(QueueItem.INHERIT_ORIGINATION_CHECK, ingestionprofile.getEuropeanaInheritOriginCheck()
				+ "");
		properties.setProperty(QueueItem.INHERIT_ORIGINATION, ingestionprofile.getEuropeanaInheritOrigin() + "");
		return properties;
	}

	private static String getFullInfo(ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh, String from, String until,
			Date now) {
		DisplayHarvestProfileItem displayHarvestProfileItem = new DisplayHarvestProfileItem(archivalInstitutionOaiPmh,
				now);
		String result = "Country: '" + displayHarvestProfileItem.getCountry() + "'\n";
		result += "Archival Institution: '" + displayHarvestProfileItem.getAiname() + "'\n";
		result += "Url: '" + displayHarvestProfileItem.getUrl() + "'\n";
		result += "Set: '" + displayHarvestProfileItem.getSet() + "'\n";
		result += "MetadataPrefix: '" + displayHarvestProfileItem.getMetadataPrefix() + "'\n";
		result += "Harvest from: '" + (from != null ? from : "first harvest") + "' till: '" + until + "'\n";
		result += "Ingestion profile: '" + displayHarvestProfileItem.getIngestionProfile() + "'\n";
		result += "Interval of harvests: '" + displayHarvestProfileItem.getIntervalHarvesting() + " days'\n";
		return result;
	}

	private static class OaiPmhErrorsException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -291842241354495630L;
		private String errors;
		public OaiPmhErrorsException(String errors){
			this.errors = errors;
		}
		public String getErrors(){
			return this.errors;
		}
	}
}
