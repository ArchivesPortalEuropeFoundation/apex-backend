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
import eu.archivesportaleurope.harvester.oaipmh.HarvestResult;
import eu.archivesportaleurope.harvester.oaipmh.OaiPmhHarvester;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville
 * Date: 04/12/2013
 *
 * @author Yoann Moranville
 */
public class DataHarvester {
    private static final Logger LOGGER = Logger.getLogger(DataHarvester.class);
    private static final int MAX_NUMBER_HARVESTED_FILES_FOR_TEST = 10;
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd"); //1980-01-01

    private long archivalInstitutionOaiPmhId;
    private boolean isNighlySchedule;

    public DataHarvester(long archivalInstitutionOaiPmhId, boolean isNighlySchedule) {
        this.archivalInstitutionOaiPmhId = archivalInstitutionOaiPmhId;
        this.isNighlySchedule = isNighlySchedule;
    }

    public void run() {
        JpaUtil.beginDatabaseTransaction();
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO.findById(archivalInstitutionOaiPmhId);
        
        String baseURL = archivalInstitutionOaiPmh.getUrl();
        String metadataPrefix = archivalInstitutionOaiPmh.getMetadataPrefix();

        String from = null;
        String until = null;
        if(archivalInstitutionOaiPmh.getFrom() != null) {
            from = archivalInstitutionOaiPmh.getFrom();
        }
        Date currentDate = new Date();
        Date newHarvestingDate = new Date(currentDate.getTime() + archivalInstitutionOaiPmh.getIntervalHarvesting());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        String newFrom = DATE_FORMATTER.format(calendar.getTime());
        int days = calendar.get(Calendar.DAY_OF_YEAR) -1;
        calendar.set(Calendar.DAY_OF_YEAR, days);
        until =  DATE_FORMATTER.format(calendar.getTime());
        
        LOGGER.info("Harvested now: " + archivalInstitutionOaiPmh.getUrl() + " (set: " + archivalInstitutionOaiPmh.getSet() + ", metadataPrefix: " + archivalInstitutionOaiPmh.getMetadataPrefix() + ", from:" + from + ", until:" + until+ ")");
        String setSpec = null;
        if(archivalInstitutionOaiPmh.getSet() != null) {
            setSpec = archivalInstitutionOaiPmh.getSet();
        }

        String currentInfoArchivalInstitutionOaiPmh = archivalInstitutionOaiPmh.toString();

        ArchivalInstitution archivalInstitution = archivalInstitutionOaiPmh.getArchivalInstitution();
        String subdirectory = APEnetUtilities.FILESEPARATOR + archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR + "oai_" + System.currentTimeMillis() + APEnetUtilities.FILESEPARATOR;
        String directory = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + subdirectory;
        File outputDirectory = new File(directory);
        File errorsDirectory = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + "/oai_errors");
        errorsDirectory.mkdirs();
        outputDirectory.mkdirs();
        OaiPmhParser oaiPmhParser = null;
        if(APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
        	oaiPmhParser = new OaiPmhParser(outputDirectory);
        }else {
        	oaiPmhParser = new OaiPmhParser(outputDirectory,MAX_NUMBER_HARVESTED_FILES_FOR_TEST );
        }
        Integer userId = archivalInstitution.getPartnerId();
        User partner;
        if(userId == null) {
            partner = DAOFactory.instance().getUserDAO().getCountryManagerOfCountry(archivalInstitution.getCountry());
        } else {
            partner = DAOFactory.instance().getUserDAO().findById(userId);
        }
        OaiPmhHttpClient oaiPmhHttpClient = null;
        try {
        	oaiPmhHttpClient = new OaiPmhHttpClient();
            HarvestResult harvestResult = OaiPmhHarvester.runOai(baseURL, from, until, metadataPrefix, setSpec, oaiPmhParser, errorsDirectory, oaiPmhHttpClient);
            archivalInstitutionOaiPmh.setLastHarvesting(new Date());
            archivalInstitutionOaiPmh.setFrom(newFrom);
            if(!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
                archivalInstitutionOaiPmh.setEnabled(false);
            }

            File[] harvestedFiles = outputDirectory.listFiles();
            UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();

            Ingestionprofile ingestionprofile = archivalInstitutionOaiPmh.getIngestionprofile();
            Properties properties = retrieveProperties(ingestionprofile);

            for(File file : harvestedFiles) {
                UpFile upFile = createUpFile(subdirectory, file.getName(), UploadMethod.OAI_PMH, archivalInstitution.getAiId(), FileType.XML);
                upFile = upFileDAO.store(upFile);
                EadService.useProfileAction(upFile, properties);
            }
            archivalInstitutionOaiPmh.setNewHarvesting(newHarvestingDate);
            archivalInstitutionOaiPmh.setErrors(false);
            archivalInstitutionOaiPmhDAO.updateSimple(archivalInstitutionOaiPmh);
            JpaUtil.commitDatabaseTransaction();

            int numberEadHarvested = harvestedFiles.length;
            UserService.sendEmailHarvestFinished(true, archivalInstitution, partner, numberEadHarvested, currentInfoArchivalInstitutionOaiPmh, harvestResult.getOldestFileHarvested(), harvestResult.getNewestFileHarvested());
            LOGGER.info("Harvest completed: harvested " + numberEadHarvested + " EAD files from " + currentInfoArchivalInstitutionOaiPmh + " --- Oldest file harvested: " + harvestResult.getOldestFileHarvested() + " --- Newest file harvested: " + harvestResult.getNewestFileHarvested());
        } catch (Exception e) {
        	LOGGER.info("Error: " + e.getMessage(), e);
            JpaUtil.rollbackDatabaseTransaction();
            ArchivalInstitutionOaiPmh archivalInstitutionOaiPmhNew = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(archivalInstitutionOaiPmhId);
            if (archivalInstitutionOaiPmhNew.isErrors()){
            	LOGGER.error("Second time harvesting failed for " + archivalInstitutionOaiPmhNew);
            	archivalInstitutionOaiPmhNew.setEnabled(false);
            }else {
            	LOGGER.error("First time harvesting failed for " + archivalInstitutionOaiPmhNew);
            }
            archivalInstitutionOaiPmhNew.setNewHarvesting(newHarvestingDate);
            archivalInstitutionOaiPmhNew.setErrors(true);
            archivalInstitutionOaiPmhNew.setLastHarvesting(new Date());
            if(!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
                archivalInstitutionOaiPmh.setEnabled(false);
            }
            archivalInstitutionOaiPmhDAO.store(archivalInstitutionOaiPmhNew);
            UserService.sendEmailHarvestFinished(false, archivalInstitution, partner, 0, currentInfoArchivalInstitutionOaiPmh, null, null);
            
            try {
				ContentUtils.deleteFile(outputDirectory, false);
			} catch (IOException e1) {
			}
        } finally {
			if (oaiPmhHttpClient != null){
				try {
					oaiPmhHttpClient.close();
				}catch(  IOException io){
					LOGGER.error("Unexcepted error occurred: " + io.getMessage(), io);
				}
			}
            if(outputDirectory.exists()) {
                outputDirectory.delete();
            }
            if(!isNighlySchedule) {
                HarvesterDaemon.setHarvesterProcessing(false);
            }
        }
    }



    private UpFile createUpFile(String upDirPath, String filePath, String uploadMethodString, Integer aiId, FileType fileType){
        UpFile upFile = new UpFile();
        upFile.setFilename(filePath);
        upFile.setPath(upDirPath);
        upFile.setAiId(aiId);
        upFile.setFileType(fileType);

        UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(uploadMethodString);
        upFile.setUploadMethod(uploadMethod);

        return upFile;
    }

    private static Properties retrieveProperties(Ingestionprofile ingestionprofile) {
        Properties properties = new Properties();
        properties.setProperty(QueueItem.XML_TYPE, ingestionprofile.getFileType()+"");
        properties.setProperty(QueueItem.NO_EADID_ACTION, ingestionprofile.getNoeadidAction().getId()+"");
        properties.setProperty(QueueItem.EXIST_ACTION, ingestionprofile.getExistAction().getId()+"");
        properties.setProperty(QueueItem.DAO_TYPE, ingestionprofile.getDaoType().getId()+"");
        properties.setProperty(QueueItem.DAO_TYPE_CHECK, ingestionprofile.getDaoTypeFromFile()+"");
        properties.setProperty(QueueItem.UPLOAD_ACTION, ingestionprofile.getUploadAction().getId()+"");
        properties.setProperty(QueueItem.CONVERSION_TYPE, ingestionprofile.getEuropeanaConversionType() + "");
        properties.setProperty(QueueItem.DATA_PROVIDER, ingestionprofile.getEuropeanaDataProvider()+"");
        properties.setProperty(QueueItem.DATA_PROVIDER_CHECK, ingestionprofile.getEuropeanaDataProviderFromFile()+"");
        properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE, ingestionprofile.getEuropeanaDaoType()+"");
        properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE_CHECK, ingestionprofile.getEuropeanaDaoTypeFromFile()+"");
        properties.setProperty(QueueItem.LANGUAGES, ingestionprofile.getEuropeanaLanguages()+"");
        properties.setProperty(QueueItem.LANGUAGE_CHECK, ingestionprofile.getEuropeanaLanguagesFromFile()+"");
        properties.setProperty(QueueItem.LICENSE, ingestionprofile.getEuropeanaLicense()+"");
        properties.setProperty(QueueItem.LICENSE_DETAILS, ingestionprofile.getEuropeanaLicenseDetails()+"");
        properties.setProperty(QueueItem.LICENSE_ADD_INFO, ingestionprofile.getEuropeanaAddRights()+"");
        properties.setProperty(QueueItem.HIERARCHY_PREFIX, ingestionprofile.getEuropeanaHierarchyPrefix()+"");
        properties.setProperty(QueueItem.INHERIT_FILE, ingestionprofile.getEuropeanaInheritElements()+"");
        properties.setProperty(QueueItem.INHERIT_ORIGINATION, ingestionprofile.getEuropeanaInheritOrigin()+"");
        return properties;
    }


}
