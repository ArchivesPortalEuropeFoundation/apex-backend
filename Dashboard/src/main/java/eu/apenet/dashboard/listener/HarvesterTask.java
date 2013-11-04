package eu.apenet.dashboard.listener;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.oclc.oai.harvester.parser.record.DebugOaiPmhParser;
import org.oclc.oai.harvester.parser.record.OaiPmhParser;
import org.oclc.oai.harvester.parser.record.OaiPmhRecord;
import org.oclc.oai.harvester.parser.record.ResultInfo;
import org.oclc.oai.harvester.verb.ListRecordsSaxWriteDirectly;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: Yoann Moranville
 * Date: 23/10/2013
 *
 * @author Yoann Moranville
 */
public class HarvesterTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(HarvesterTask.class);
    private Duration duration;
    private Duration delay;
    private static final Duration INTERVAL = new Duration(0, 10, 0);
    private final ScheduledExecutorService scheduler;
    private static final int MAX_NUMBER_HARVESTED_FILES_FOR_TEST = 2; //todo: Put at 10, but for first tests, it is faster with 2

    public HarvesterTask(ScheduledExecutorService scheduler, Duration maxDuration, Duration delay) {
        this.duration = maxDuration;
        this.scheduler = scheduler;
        this.delay = delay;
        JpaUtil.init();
    }

    @Override
    public void run() {
        LOGGER.info("Harvester process active");
        long endTime = System.currentTimeMillis() + duration.getMilliseconds();
        boolean stopped = false;
        while (!stopped && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
            try {
                HarvesterDaemon.setHarvesterProcessing(true);
                processHarvester(endTime);
            } catch (Throwable e) {
                LOGGER.error("Stopping processing for a while: " + e.getMessage(), e);
                try {
                    JpaUtil.rollbackDatabaseTransaction();
                } catch (Exception de) {
                    LOGGER.error(de.getMessage());
                }
                stopped = true;
            }
            if (!stopped && (System.currentTimeMillis() + INTERVAL.getMilliseconds()) < endTime) {
                cleanUp();
                try {
                    Thread.sleep(INTERVAL.getMilliseconds());
                } catch (InterruptedException e) {
                }
            } else {
                cleanUp();
                stopped = true;
            }
        }
        LOGGER.info("Harvester process inactive");
        if (!scheduler.isShutdown()) {
            scheduler.schedule(new HarvesterTask(scheduler, duration, delay), delay.getMilliseconds(), TimeUnit.MILLISECONDS);
        }
    }

    private static void cleanUp() {
        try {
            HarvesterDaemon.setHarvesterProcessing(false);
            JpaUtil.closeDatabaseSession();
        } catch (Exception de) {
            LOGGER.error(de.getMessage());
        }
    }

    public boolean processHarvester(long endTime) throws Exception {
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhList = archivalInstitutionOaiPmhDAO.findAll();

        //Check for each if the interval is right and which one should be done right now
        for(ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh : archivalInstitutionOaiPmhList) {
            if(scheduler.isShutdown() || System.currentTimeMillis() > endTime) {
                break;
            }
            LOGGER.info("Checking if OAI profile is ready to be harvested: " + archivalInstitutionOaiPmh.getUrl() + " (set: " + archivalInstitutionOaiPmh.getSet() + ", metadataPrefix: " + archivalInstitutionOaiPmh.getMetadataPrefix() + ")");
            if(archivalInstitutionOaiPmh.isEnabled()) {
                if(archivalInstitutionOaiPmh.getLastHarvesting() == null || (archivalInstitutionOaiPmh.getLastHarvesting().getTime() + archivalInstitutionOaiPmh.getIntervalHarvesting() >= System.currentTimeMillis())) { //Ok, do harvest
                    LOGGER.info("This profile will be harvested now");

                    String baseURL = archivalInstitutionOaiPmh.getUrl();
                    String metadataPrefix = archivalInstitutionOaiPmh.getMetadataPrefix();

                    String from = null;
                    String until = null;
                    if(archivalInstitutionOaiPmh.getLastHarvesting() != null) {
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //1980-01-01
                        from = dateFormatter.format(archivalInstitutionOaiPmh.getLastHarvesting());
                    }

                    String setSpec = null;
                    if(archivalInstitutionOaiPmh.getSet() != null) {
                        setSpec = archivalInstitutionOaiPmh.getSet();
                    }

                    ArchivalInstitution archivalInstitution = archivalInstitutionOaiPmh.getArchivalInstitution();
                    String subdirectory = APEnetUtilities.FILESEPARATOR + archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR + "oai_" + System.currentTimeMillis() + APEnetUtilities.FILESEPARATOR;
                    String directory = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + subdirectory;
                    File outputDirectory = new File(directory);
                    outputDirectory.mkdirs();
                    OaiPmhParser oaiPmhParser = new OaiPmhParser(outputDirectory);

                    Integer userId = archivalInstitution.getPartnerId();
                    User partner;
                    if(userId == null) {
                        partner = DAOFactory.instance().getUserDAO().getCountryManagerOfCountry(archivalInstitution.getCountry());
                    } else {
                        partner = DAOFactory.instance().getUserDAO().findById(userId);
                    }

                    try {
                        JpaUtil.beginDatabaseTransaction();
                        runOai(baseURL, from, until, metadataPrefix, setSpec, oaiPmhParser);
                        archivalInstitutionOaiPmh.setLastHarvesting(new Date());
                        if(!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) {
                            archivalInstitutionOaiPmh.setEnabled(false);
                        }

                        File[] harvestedFiles = outputDirectory.listFiles();
                        UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
                        List<UpFile> upFiles = new ArrayList<UpFile>(harvestedFiles.length);
                        for(File file : harvestedFiles) {
                            UpFile upFile = createUpFile(subdirectory, file.getName(), UploadMethod.OAI_PMH, archivalInstitution.getAiId(), FileType.XML);
                            upFileDAO.store(upFile);
                            upFiles.add(upFile);
                        }

                        JpaUtil.commitDatabaseTransaction();


                        //todo: Create QUEUE for those items
                        Userprofile userprofile = archivalInstitutionOaiPmh.getUserprofile();
                        XmlType xmlType = XmlType.getType(userprofile.getFileType());

                        for(UpFile upfile : upFiles) {
                            //About EADID
                            String eadid = ExistingFilesChecker.extractAttributeFromEad(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upfile.getPath() + upfile.getFilename(), "eadheader/eadid", null, true).trim();
                            if(StringUtils.isEmpty(eadid)) {
                                if(userprofile.getNoeadidAction().equals(UserprofileDefaultNoEadidAction.REMOVE)) {
                                    deleteUpFile(upfile);
                                } else if (userprofile.getNoeadidAction().equals(UserprofileDefaultNoEadidAction.ADD_LATER)) {
                                    //todo: Do nothing actually, right? Just leave the file in the existing file checker (page before content manager)
                                }
                            } else {
                                boolean continueTask = true;
                                Ead ead;
                                if((ead = doesFileExist(upfile, eadid, xmlType)) != null) {
                                    if(userprofile.getExistAction().equals(UserprofileDefaultExistingFileAction.OVERWRITE)) {
                                        EadService.overwrite(ead, upfile);
                                    } else if(userprofile.getExistAction().equals(UserprofileDefaultExistingFileAction.KEEP)) {
                                        deleteUpFile(upfile);
                                        continueTask = false;
                                    }
                                } else {
                                    EadService.create(xmlType, upfile, upfile.getAiId());
                                }

//                                if(continueTask) {
//                                    //todo: After, so when the file is inside the EAD tables so we have a problem, we do not have the identifiers...
//                                    UserprofileDefaultDaoType daoType = userprofile.getDaoType();
//                                    if(userprofile.getUploadAction().equals(UserprofileDefaultUploadAction.CONVERT)) {
//                                        EadService.convert(xmlType, );
//                                    } else if(userprofile.getUploadAction().equals(UserprofileDefaultUploadAction.VALIDATE)) {
//                                        EadService.validate(xmlType, );
//                                    } else if(userprofile.getUploadAction().equals(UserprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH)) {
//                                        EadService.convertValidatePublish(xmlType, );
//                                    } else if(userprofile.getUploadAction().equals(UserprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH_EUROPEANA)) {
//                                        //todo: in the QueueAction too
//                                    }
//                                }
                            }
                        }


                        UserService.sendEmailHarvestFinished(true, archivalInstitution, partner);
                    } catch (Exception e) {
                        JpaUtil.rollbackDatabaseTransaction();
                        UserService.sendEmailHarvestFinished(false, archivalInstitution, partner);
                        LOGGER.error("Harvesting failed - should we put an 'error' flag in the DB?");
                    } finally {
                        outputDirectory.delete();
                    }
                }
            }
        }

        return false;
    }

    private void runOai(String baseURL, String from, String until, String metadataPrefix, String setSpec,  OaiPmhParser oaiPmhParser) throws Exception {
        int number = 0;
        ListRecordsSaxWriteDirectly listRecordsSax = new ListRecordsSaxWriteDirectly();
        ResultInfo resultInfo = listRecordsSax.harvest(baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser, number);

        while (resultInfo != null) {
            List<String> errors = resultInfo.getErrors();
            if (errors != null && errors.size() > 0) {
                LOGGER.info("Found errors");
                for (String item : errors) {
                    LOGGER.info(item);
                }
                LOGGER.info("Error record: " + resultInfo.getIdentifier());
                break;
            }

            String resumptionToken = resultInfo.getNewResumptionToken();
            LOGGER.info("resumptionToken: '" + resumptionToken + "'");
            if (resumptionToken == null || resumptionToken.length() == 0) {
                resultInfo = null;
            } else {
                number++;
                resultInfo = listRecordsSax.harvest(baseURL, resumptionToken, oaiPmhParser, number);
                if(!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing() && number > MAX_NUMBER_HARVESTED_FILES_FOR_TEST) {
                    resultInfo = null;
                }
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

    private static void deleteUpFile(UpFile upFile) throws IOException {
        JpaUtil.beginDatabaseTransaction();
        DAOFactory.instance().getUpFileDAO().deleteSimple(upFile);
        JpaUtil.commitDatabaseTransaction();
        ContentUtils.deleteFile(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename());
        File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath());
        if (uploadDir.listFiles().length == 0)
            FileUtils.forceDelete(uploadDir);
    }

    private static Ead doesFileExist(UpFile upFile, String eadid, XmlType xmlType) {
        return DAOFactory.instance().getEadDAO().getEadByEadid(xmlType.getClazz(), upFile.getAiId(), eadid);
    }
}
