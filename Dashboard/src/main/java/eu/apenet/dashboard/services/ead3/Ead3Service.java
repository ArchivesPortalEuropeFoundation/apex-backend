/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import com.ctc.wstx.exc.WstxParsingException;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import static eu.apenet.dashboard.manual.ExistingFilesChecker.extractAttributeFromXML;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.AbstractService;
import eu.apenet.dashboard.services.opendata.OpenDataService;

import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.QueueItemDAO;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.IngestionprofileDefaultExistingFileAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultNoEadidAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;

import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;

import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.ValidatedState;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kaisar
 */
public class Ead3Service extends AbstractService {

    protected static final Logger LOGGER = Logger.getLogger(Ead3Service.class);
    private static final String CURRENT_LANGUAGE_KEY = "currentLanguage";

    public static boolean isHarvestingStarted() {
        return DAOFactory.instance().getResumptionTokenDAO().containsValidResumptionTokens(new Date());
    }

    public Ead3 create(XmlType xmlType, UpFile upFile, Integer aiId, String ead3Id, String title) throws Exception {
        SecurityContext.get().checkAuthorized(aiId);
        Ead3 ead3 = new CreateEad3Task().execute(xmlType, upFile, aiId, ead3Id, title);
        DAOFactory.instance().getUpFileDAO().delete(upFile);
        return ead3;
    }

    @Override
    public void publish(XmlType xmlType, Integer id) throws Exception {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3 ead3 = ead3DAO.findById(id, xmlType.EAD_3.getClazz());
        SecurityContext.get().checkAuthorized(ead3);
        if (PublishTask.valid(ead3)) {
            addToQueue(ead3, QueueAction.PUBLISH, null);
        }
    }

    @Override
    public void unpublish(XmlType xmlType, Integer id) throws Exception {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3 ead3 = ead3DAO.findById(id, xmlType.EAD_3.getClazz());
        SecurityContext.get().checkAuthorized(ead3);
        if (UnpublishTask.valid(ead3)) {
            addToQueue(ead3, QueueAction.UNPUBLISH, null);
        }
    }

    /**
     * <p>
     * Manages the actions convert, validate and publish.
     * <p>
     * Puts in the queue these actions.
     *
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @param properties {@link Properties} A property list to process the file.
     * @param currentLanguage String The current language in the Dashboard.
     * @return boolean The EAD3 is converted, validated and published.
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    public static boolean convertValidatePublish(Integer id, Properties properties, String currentLanguage) throws IOException {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3 ead3 = ead3DAO.findById(id, XmlType.EAD_3.getClazz());

        SecurityContext.get().checkAuthorized(ead3.getAiId());
        if (!ead3.isPublished()) {
            properties.put(CURRENT_LANGUAGE_KEY, currentLanguage);
            addToQueue(ead3, QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
        }
        return true;
    }

    /**
     * <p>
     * Manages the action validate.
     * <p>
     * Puts in the queue this action.
     *
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    @Override
    public void validate(XmlType xmlType, Integer id) throws IOException {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3 ead3 = ead3DAO.findById(id, XmlType.EAD_3.getClazz());

        SecurityContext.get().checkAuthorized(ead3.getAiId());
        if (ValidateTask.notValidated(ead3)) {
            addToQueue(ead3, QueueAction.VALIDATE, null);
        }
    }

    @Override
    public void convert(XmlType xmlType, Integer id, Properties properties) throws IOException {
    }

    /**
     * <p>
     * Manages the action publish.
     * <p>
     * Puts in the queue this action.
     *
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
//    public static void publish(Integer id) throws IOException {
//        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
//        Ead3 eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
//        SecurityContext.get().checkAuthorized(eacCpf);
//        if (PublishTask.notValidated(eacCpf)) {
//            addToQueue(eacCpf, QueueAction.PUBLISH, null);
//        }
//    }
    /**
     * <p>
     * Manages the action unpublish.
     * <p>
     * Puts in the queue this action.
     *
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
//    public static void unpublish(Integer id) throws IOException {
//        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
//        Ead3 eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
//        SecurityContext.get().checkAuthorized(eacCpf);
//        if (UnpublishTask.notValidated(eacCpf)) {
//            addToQueue(eacCpf, QueueAction.UNPUBLISH, null);
//        }
//    }
    /**
     * <p>
     * Manages the action delete.
     * <p>
     * Puts in the queue this action.
     *
     * @param xmlType
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    @Override
    public void delete(XmlType xmlType, Integer id) throws Exception {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3 ead3 = ead3DAO.findById(id);
        SecurityContext.get().checkAuthorized(ead3.getAiId());
        addToQueue(ead3, QueueAction.DELETE, null);
    }

    /**
     * Deletes from the queue an EAD3 and updates the state of the entry
     * <i>queueing</i> in the database.
     *
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    public static void deleteFromQueue(Integer id) throws IOException {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        JpaUtil.beginDatabaseTransaction();
        Ead3 ead3 = ead3DAO.findById(id, XmlType.EAD_3.getClazz());
        SecurityContext.get().checkAuthorized(ead3.getAiId());
        if (QueuingState.ERROR.equals(ead3.getQueuing()) || QueuingState.READY.equals(ead3.getQueuing())) {
            QueueItem queueItem = ead3.getQueueItem();
            ead3.setQueuing(QueuingState.NO);
            ead3DAO.updateSimple(ead3);
            deleteFromQueueInternal(queueItem, true);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    /**
     * Deletes from the queue an EAD3 and updates the state of the entry
     * <i>queueing</i> in the database.
     *
     * @param queueItem
     * @throws IOException
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    public static void deleteFromQueue(QueueItem queueItem) throws Exception {
        SecurityContext.get().checkAuthorizedToManageQueue();
        Properties preferences = Ead3Service.readProperties(queueItem.getPreferences());
        //delete open-data operation
        if (preferences.containsKey(OpenDataService.ENABLE_OPEN_DATA_KEY)) {
            if (queueItem.getAiId() != null) {
                ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(queueItem.getAiId());
                archivalInstitution.setUnprocessedSolrDocs(0);
                DAOFactory.instance().getArchivalInstitutionDAO().store(archivalInstitution);
            }
            DAOFactory.instance().getQueueItemDAO().delete(queueItem);
        } else { //other
            deleteFromQueue(queueItem, true);
        }
    }

    private static void deleteFromQueue(QueueItem queueItem, boolean deleteUpFile) throws IOException {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        JpaUtil.beginDatabaseTransaction();
        Ead3 ead3 = queueItem.getEad3();
        if (ead3 != null) {
            ead3.setQueuing(QueuingState.NO);
            ead3DAO.updateSimple(ead3);
        }
        deleteFromQueueInternal(queueItem, deleteUpFile);
        JpaUtil.commitDatabaseTransaction();
    }

    /**
     * Deletes the file from the system and the database in the table
     * <b>queue</b>.
     *
     * @param queueItem {@link QueueItem} The item to process.
     * @param deleteUpFile boolean Deletes or not the file.
     * @throws IOException
     * @see eu.apenet.persistence.vo.UpFile
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see ContentUtils#deleteFile(File, boolean)
     */
    private static void deleteFromQueueInternal(QueueItem queueItem, boolean deleteUpFile) throws IOException {
        UpFile upFile = queueItem.getUpFile();
        if (upFile != null && deleteUpFile) {
            String filename = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename();
            File file = new File(filename);
            File aiDir = file.getParentFile();
            ContentUtils.deleteFile(file, false);
            if (aiDir.exists() && aiDir.listFiles().length == 0) {
                ContentUtils.deleteFile(aiDir, false);
            }
        }
        DAOFactory.instance().getQueueItemDAO().deleteSimple(queueItem);
        if (upFile != null && deleteUpFile) {
            DAOFactory.instance().getUpFileDAO().deleteSimple(upFile);
        }
    }

    /**
     * Downloads a file from the content manager.
     *
     * @param xmlType
     * @param id {@link Integer} The identifier of the EAD3 file.
     * @return {@link File} The file downloading.
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     * @see eu.apenet.persistence.vo.UpFile
     */
    @Override
    public File download(XmlType xmlType, Integer id) {
        Ead3 ead3 = DAOFactory.instance().getEad3DAO().findById(id, XmlType.EAD_3.getClazz());
        SecurityContext.get().checkAuthorized(ead3.getAiId());
        String path = APEnetUtilities.getConfig().getRepoDirPath() + ead3.getPath();
        try {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
        } catch (Exception e) {
            LOGGER.error("Download function error, trying to open the file '" + path + "'", e);
        }
        return null;
    }

    /**
     * <p>
     * Adds an EAD3 file to the queue.
     * <p>
     * Stores in the database an EAD3 in the table <b>eac_cpf</b>.
     * <p>
     * Fills the table <b>queue</b> with the EAD3's identifier and his
     * preferences.
     *
     * @param eacCpf {@link Ead3} The EAD3 file to ingest in the Dashboard.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Preferences to process the EAD3.
     * @throws IOException
     * @see eu.apenet.persistence.vo.QueueItem
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.dao.Ead3DAO
     */
    private static void addToQueue(Ead3 ead3, QueueAction queueAction, Properties preferences) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        ead3.setQueuing(QueuingState.READY);
        ead3DAO.store(ead3);
        QueueItem queueItem = fillQueueItem(ead3, queueAction, preferences);
        indexqueueDao.store(queueItem);
    }

    /**
     * Fills an item <i>queue</i>.
     *
     * @param eacCpf {@link EacCPf} The EAD3 file to ingest in the Dashboard.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Preferences to process the EAD3.
     * @return {@link QueueItem> An item <i>queue</i>.
     * @throws IOException
     */
    private static QueueItem fillQueueItem(Ead3 ead3, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(ead3, queueAction, preferences, 1000);
    }

    /**
     * Fills an item <i>queue</i> with the EAD3's identifier, preferences and
     * priority.
     *
     * @param eacCpf {@link Ead3} The EAD3 file to ingest in the Dashboard.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Preferences to process the EAD3.
     * @param basePriority {@link Integer} The priority in the queue.
     * @return {@link QueueItem} An item <i>queue</i>.
     * @throws IOException
     */
    private static QueueItem fillQueueItem(Ead3 ead3, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = ead3.getQueueItem();
        if (queueItem == null) {
            queueItem = new QueueItem();
            queueItem.setEad3(ead3);
            queueItem.setAiId(ead3.getAiId());
        }
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }
        int priority = basePriority;

        if (queueAction.isDeleteAction() || queueAction.isUnpublishAction() || queueAction.isDeleteFromEuropeanaAction() || queueAction.isDeleteEseEdmAction()) {
            priority += 150;
        }
        queueItem.setPriority(priority);
        return queueItem;
    }

    /**
     * Writes in a string buffer the property list.
     *
     * @param properties {@link Properties} The preferences to process the EAD3.
     * @return String The preferences to write.
     * @throws IOException
     */
    private static String writeProperties(Properties properties) throws IOException {
        StringWriter stringWriter = new StringWriter();
        properties.store(stringWriter, "");
        String result = stringWriter.toString();
        stringWriter.flush();
        stringWriter.close();
        return result;
    }

    /**
     * Overwrites an old EAD3 file.
     *
     * @param oldEad3 {@link Ead3} The old EAD3 file.
     * @param upFile {@link UpFile} The file to up.
     * @throws Exception
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    public static void overwrite(Ead3 oldEad3, UpFile upFile) throws Exception {
        SecurityContext.get().checkAuthorized(oldEad3.getAiId());
        addToQueue(oldEad3, QueueAction.OVERWRITE, null, upFile);
    }

    /**
     * <p>
     * Adds an EAD3 file to the queue.
     * <p>
     * Stores in the database an EAD3 in the table <b>ead3</b>.
     * <p>
     * Fills the table <b>queue</b> with the EAD3's identifier, his preferences
     * and the upfile's identifier.
     *
     * @see eu.apenet.persistence.vo.QueueAction
     * @see java.util.Properties
     * @see eu.apenet.persistence.vo.UpFile
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     */
    private static void addToQueue(Ead3 ead3, QueueAction queueAction, Properties preferences, UpFile upFile)
            throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        ead3.setQueuing(QueuingState.READY);
        ead3DAO.store(ead3);
        QueueItem queueItem = fillQueueItem(ead3, queueAction, preferences);
        queueItem.setUpFile(upFile);
        indexqueueDao.store(queueItem);
    }

    /**
     * Process an item from the queue.
     *
     * @param queueItem
     * @return
     * @throws java.lang.Exception
     */
    
    public static QueueAction processQueueItem(QueueItem queueItem) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        QueueAction queueAction = queueItem.getAction();
        Properties preferences = null;
        if (queueItem.getPreferences() != null) {
            preferences = readProperties(queueItem.getPreferences());
        }

        if (!queueAction.isUseProfileAction()) {
            Ead3 ead3 = queueItem.getEad3();
            XmlType xmlType = XmlType.getContentType(ead3);
            LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + " " + ead3.getIdentifier()
                    + "(" + xmlType.getName() + ")");

            if (queueAction.isOverwriteAction() || queueAction.isDeleteAction()) {
                boolean eacDeleted = false;
                boolean upFileDeleted = false;
                UpFile upFile = queueItem.getUpFile();
                try {

                    queueItem.setEad3(null);
                    queueItem.setUpFile(null);
                    queueItemDAO.store(queueItem);

                    if (queueAction.isOverwriteAction()) {
                        Integer aiId = ead3.getAiId();

                        new UnpublishTask().execute(ead3, preferences);
                        new DeleteTask().execute(ead3, preferences);
                        eacDeleted = true;
                        new CreateEad3Task().execute(xmlType, upFile, aiId, ead3.getIdentifier(), ead3.getTitle());
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                        upFileDeleted = true;
                    } else if (queueAction.isDeleteAction()) {
                        if (ead3.isPublished()) {
                            new UnpublishTask().execute(ead3, preferences);
                        }
                        new DeleteTask().execute(ead3, preferences);
                        eacDeleted = true;
                    }
//                    DAOFactory.instance().getQueueItemDAO().deleteSimple(queueItem);
                    queueItemDAO.delete(queueItem);
                } catch (Exception e) {
                    if (!eacDeleted) {
                        queueItem.setEad3(ead3);
                        ead3.setQueuing(QueuingState.ERROR);
                        ead3DAO.store(ead3);
                    }
                    if (!upFileDeleted && upFile != null) {
                        queueItem.setUpFile(upFile);
                    }
                    String err = "EAD3 identifier: " + ead3.getIdentifier() + " - id: " + ead3.getId() + " - type: " + xmlType.getName();
                    LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                    queueItem.setErrors(new Date() + " - " + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                    queueItem.setPriority(0);
//                    DAOFactory.instance().getQueueItemDAO().deleteSimple(queueItem);
                    queueItemDAO.store(queueItem);
                    /*
                     * throw exception when solr has problem, so the queue will stop for a while.
                     */
                    if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                        throw (Exception) e;

                    }
                }
            } else {
                try {
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(ead3, preferences);
                    }
                    if (queueAction.isPublishAction()) {
                        new PublishTask().execute(ead3, preferences);
                    }
                    if (queueAction.isValidatePublishAction()) {
                        new ValidateTask().execute(ead3, preferences);
                        new PublishTask().execute(ead3, preferences);
                    }
//                    if (queueAction.isRePublishAction()) {
//                        new UnpublishTask().execuiidentifierdentifiidentifiererte(eac, preferences);
//                        new PublishTask().execute(eac, preferences);
//                    }
//
                    if (queueAction.isUnpublishAction()) {
                        new UnpublishTask().execute(ead3, preferences);
                        //remove related eac-cpfs
                        for (EacCpf eacCpf : ead3.getEacCpfs()) {
                            if (eacCpf.isPublished()) {
                                new eu.apenet.dashboard.services.eaccpf.UnpublishTask().execute(eacCpf);
                            }
                            new eu.apenet.dashboard.services.eaccpf.DeleteTask().execute(eacCpf);
                        }
                    }

                    ead3.setQueuing(QueuingState.NO);
                    ead3DAO.store(ead3);
//                    DAOFactory.instance().getQueueItemDAO().deleteSimple(queueItem);
                    queueItemDAO.delete(queueItem);
                } catch (APEnetException e) {
                    String err = "identifier: " + ead3.getIdentifier() + " - id: " + ead3.getId() + " - type: " + xmlType.getName();
                    LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                    queueItem.setErrors(new Date() + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                    ead3.setQueuing(QueuingState.ERROR);
                    ead3DAO.store(ead3);
                    queueItem.setPriority(0);
                    queueItemDAO.store(queueItem);
                    /*
                     * throw exception when solr has problem, so the queue will stop for a while.
                     */
                    if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                        throw (Exception) e;

                    }
                }
            }
        } else //USE_PROFILE
        // Checks if the file should be processed as a new upload of as a
        // file already in the system.
        {
            if (queueItem.getUpFile() != null) {
                processUpFileWithProfile(queueItem, preferences);
            } else {
                processEad3(queueItem, queueItem.getEad3(), preferences);
            }
        }

        LOGGER.info("Process queue item finished");
        return queueAction;
    }

    /**
     * Method to process the uploaded file using the selected profile.
     *
     * @param queueItem {@link QueueItem} Current item to process.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws Exception
     *
     * @see eu.apenet.persistence.vo.QueueAction
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see java.util.Properties
     * @see eu.apenet.persistence.vo.UpFile
     * @see eu.apenet.commons.types.XmlType
     * @see eu.apenet.persistence.vo.IngestionprofileDefaultExistingFileAction;
     * @see eu.apenet.persistence.vo.IngestionprofileDefaultNoEadidAction;
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     * @see ExistingFilesChecker#extractAttributeFromXML(String, String, String,
     * boolean, boolean)
     */
    private static void processUpFileWithProfile(QueueItem queueItem, Properties preferences) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        IngestionprofileDefaultNoEadidAction ingestionprofileDefaultNoEadidAction = IngestionprofileDefaultNoEadidAction.getExistingFileAction(preferences.getProperty(QueueItem.NO_EADID_ACTION));
        IngestionprofileDefaultExistingFileAction ingestionprofileDefaultExistingFileAction = IngestionprofileDefaultExistingFileAction.getExistingFileAction(preferences.getProperty(QueueItem.EXIST_ACTION));
        XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));

        //About EADID
        UpFile upFile = queueItem.getUpFile();
        LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + ", upFile id: " + queueItem.getUpFileId() + "(" + xmlType.getName() + ")");
        String upFilePath = upFile.getPath() + upFile.getFilename();
        String eadid = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "ead/control/recordId", null, true, false).trim();
        String title = "";
        try {
            title = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath,
                    "ead/control/filedesc/titlestmt/titleproper", null, true, false);
        } catch (WstxParsingException e) {
            title = "";
        }
        if (StringUtils.isEmpty(eadid) || "empty".equals(eadid) || "error".equals(eadid)) {
            if (ingestionprofileDefaultNoEadidAction.isRemove()) {
                LOGGER.info("File will be removed, because it does not have eadid: " + upFilePath);
                deleteFromQueue(queueItem, true);
            } else {
                LOGGER.info("File will be processed manually later, because it does not have eadid: " + upFilePath);
                deleteFromQueue(queueItem, false);
            }
        } else {
            boolean continueTask = true;
            Ead3 ead3;
            Ead3 newEad3 = null;
            if ((ead3 = doesFileExist(upFile, eadid)) != null) {
                if (ingestionprofileDefaultExistingFileAction.isOverwrite()) {
                    boolean ead3Deleted = false;
                    try {
                        queueItem.setEad(null);
                        queueItem.setUpFile(null);
                        queueItemDAO.store(queueItem);

                        Integer aiId = ead3.getAiId();
//                        new DeleteFromEuropeanaTask().execute(ead, preferences);
//                        new DeleteEseEdmTask().execute(ead, preferences);
                        new UnpublishTask().execute(ead3, preferences);
                        new DeleteTask().execute(ead3, preferences);
                        ead3Deleted = true;
                        newEad3 = new CreateEad3Task().execute(xmlType, upFile, aiId, eadid, title);
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                    } catch (Exception e) {
                        if (!ead3Deleted) {
                            queueItem.setEad3(ead3);
                            ead3.setQueuing(QueuingState.ERROR);
                            ead3DAO.store(ead3);
                        }
                        if (upFile != null) {
                            queueItem.setUpFile(upFile);
                        }
                        String err = "eadid: " + ead3.getIdentifier() + " - id: " + ead3.getId() + " - type: " + xmlType.getName();
                        LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                        queueItem.setErrors(new Date() + " - " + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                        queueItem.setPriority(0);
                        queueItemDAO.store(queueItem);
                        continueTask = false;
                        /*
                         * throw exception when solr has problem, so the queue will stop for a while.
                         */
                        if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                            throw (Exception) e;

                        }
                    }
                } else if (ingestionprofileDefaultExistingFileAction.isKeep()) {
                    LOGGER.info("File will be removed, because there is already one with the same eadid: " + upFilePath);
                    deleteFromQueue(queueItem, true);
                    continueTask = false;
                } else if (ingestionprofileDefaultExistingFileAction.isAsk()) {
                    LOGGER.info("Is needed to ask the user for the action, because there is already one with the same eadid: " + upFilePath);
                    deleteFromQueue(queueItem, false);
                    continueTask = false;
                }
            } else {
                newEad3 = new CreateEad3Task().execute(xmlType, upFile, upFile.getAiId(), eadid, title);
                queueItem.setUpFile(null);
                queueItemDAO.store(queueItem);
                DAOFactory.instance().getUpFileDAO().delete(upFile);
            }

            if (continueTask) {
                processEad3(queueItem, newEad3, preferences);
            }
        }
    }

    /**
     * Method to process the file already in the system using the selected
     * profile.
     *
     * @param queueItem {@link QueueItem} Current item to process.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws Exception
     *
     * @see eu.apenet.persistence.vo.Ead3
     */
//    private static void processEad3WithProfile(QueueItem queueItem, Properties preferences) throws Exception {
//        Ead3 ead3 = queueItem.getEad3();
//        processEad3(queueItem, ead3, preferences);
//    }
    /**
     * Method to performs the actions associated to the profile over the file.
     *
     * @param queueItem {@link QueueItem} Current item to process.
     * @param newEad3 {@link Ead3} EAD3 to process using the profile.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws Exception
     *
     * @see eu.apenet.persistence.vo.QueueAction
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see java.util.Properties
     * @see eu.apenet.commons.types.XmlType
     * @see eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     * @see ConvertTask#execute(Ead3, Properties)
     * @see ValidateTask#execute(Ead3)
     * @see PublishTask#execute(Ead3)
     */
    private static void processEad3(QueueItem queueItem, Ead3 newEad3, Properties preferences) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction.getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
        Boolean daoTypeCheck = "true".equals(preferences.getProperty(QueueItem.DAO_TYPE_CHECK));
        XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));

        newEad3.setQueuing(QueuingState.BUSY);
        ead3DAO.store(newEad3);

        Properties conversionProperties = new Properties();
        conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT, "UNSPECIFIED");
        conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_USE_EXISTING, daoTypeCheck);

        try {
//            if (ingestionprofileDefaultUploadAction.isConvert()) {
////                new ConvertTask().execute(newEad3, conversionProperties);
//            } else 
            if (ingestionprofileDefaultUploadAction.isValidate()) {
                new ValidateTask().execute(newEad3);
            } else if (ingestionprofileDefaultUploadAction.isConvertValidatePublish() || ingestionprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
                new ValidateTask().execute(newEad3);
//                new ConvertTask().execute(newEad3, conversionProperties);
                new ValidateTask().execute(newEad3);
                new PublishTask().execute(newEad3);
            }
            newEad3.setQueuing(QueuingState.NO);
            ead3DAO.store(newEad3);
            queueItemDAO.delete(queueItem);
        } catch (Exception e) {
            newEad3.setQueuing(QueuingState.ERROR);
            ead3DAO.store(newEad3);

            String err = "recordId: " + newEad3.getIdentifier() + " - id: " + newEad3.getId() + " - type: " + xmlType.getName();
            LOGGER.error(APEnetUtilities.generateThrowableLog(e));
            queueItem.setErrors(new Date() + " - " + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
            queueItem.setPriority(0);
            queueItemDAO.store(queueItem);
            /*
             * throw exception when solr has problem, so the queue will stop for a while.
             */
            if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                throw (Exception) e;

            }
        }
    }

    /**
     * Reads the properties in a {@link StringReader}.
     *
     * @param string String The string to load the properties.
     * @return {@link Properties} The properties to process.
     *
     * @throws IOException
     */
    public static Properties readProperties(String string) throws IOException {
        StringReader stringReader = new StringReader(string);
        Properties properties = new Properties();
        properties.load(stringReader);
        stringReader.close();
        return properties;
    }

    /**
     * Adds to {@link ContentSearchOptions} the different options.
     *
     * @param ids {@link List}{@code <}{@link Integer}{@code >} List of the
     * identifiers of the EAD3.
     * @param aiId {@link Integer} The identifier of the archival institution.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     */
    public static void addBatchToQueue(List<Integer> ids, Integer aiId, XmlType xmlType, QueueAction queueAction,
            Properties preferences) throws IOException {
        ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
        eadSearchOptions.setPageSize(0);
        eadSearchOptions.setContentClass(xmlType.getClazz());
        eadSearchOptions.setArchivalInstitionId(aiId);
        if (ids != null && ids.size() > 0) {
            eadSearchOptions.setIds(ids);
        }
        addBatchToQueue(eadSearchOptions, queueAction, preferences);

    }

    /**
     * Adds to {@link ContentSearchOptions} the different options and update the
     * database.
     *
     * @param eacCpfSearchOptions {@link ContentSearchOptions} The options of
     * the EAD3.
     * @param queueAction {@link QueueAction} The actions in the queue.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    public static void addBatchToQueue(ContentSearchOptions eadSearchOptions, QueueAction queueAction,
            Properties preferences) throws IOException {
        SecurityContext.get().checkAuthorized(eadSearchOptions.getArchivalInstitionId());
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        eadSearchOptions.setPageSize(0);
        if (QueueAction.CONVERT.equals(queueAction)) {
            eadSearchOptions.setConverted(false);
        } else if (QueueAction.VALIDATE.equals(queueAction)) {
            eadSearchOptions.setValidated(ValidatedState.NOT_VALIDATED);
        } else if (QueueAction.PUBLISH.equals(queueAction)) {
            eadSearchOptions.setValidated(ValidatedState.VALIDATED);
            eadSearchOptions.setPublished(false);
        } else if (QueueAction.VALIDATE_PUBLISH.equals(queueAction)) {
            eadSearchOptions.setPublished(false);
        } else if (QueueAction.UNPUBLISH.equals(queueAction)) {
            eadSearchOptions.setPublished(true);
        } else if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.NOT_CONVERTED);
            eadSearchOptions.setValidated(ValidatedState.VALIDATED);
        } else if (QueueAction.DELIVER_TO_EUROPEANA.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.CONVERTED);

        } else if (QueueAction.DELETE_FROM_EUROPEANA.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
        } else if (QueueAction.DELETE_ESE_EDM.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.CONVERTED);
        } else if (QueueAction.CHANGE_TO_STATIC.equals(queueAction)) {
            eadSearchOptions.setDynamic(true);
        } else if (QueueAction.CHANGE_TO_DYNAMIC.equals(queueAction)) {
            eadSearchOptions.setDynamic(false);
        }
        eadSearchOptions.setQueuing(QueuingState.NO);
        JpaUtil.beginDatabaseTransaction();
        List<Ead3> eads = ead3DAO.getEad3s(eadSearchOptions);
        int size = 0;
        while ((size = eads.size()) > 0) {
            Ead3 ead = eads.get(size - 1);
            if (validState(ead, preferences, queueAction)) {
                QueueItem queueItem;
                if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)) {
                    Properties properties = preferences;
                    String oaiIdentifier = ead.getArchivalInstitution().getRepositorycode()
                            + APEnetUtilities.FILESEPARATOR + "fa"
                            + APEnetUtilities.FILESEPARATOR + ead.getIdentifier();
                    properties.put("edm_identifier", oaiIdentifier);
                    properties.put("repository_code", ead.getArchivalInstitution().getRepositorycode());
                    queueItem = fillQueueItem(ead, queueAction, properties);
                } else {
                    queueItem = fillQueueItem(ead, queueAction, preferences);
                }
                ead.setQueuing(QueuingState.READY);
                ead3DAO.updateSimple(ead);
                indexqueueDao.updateSimple(queueItem);
            }
            eads.remove(size - 1);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    /**
     * Checks the state and the profile future action.
     *
     * @param eac {@link Ead3} The EAD3 to validate.
     * @param preferences {@link Properties} Profile preferences.
     * @param queueAction {@link QueueAction} The action to process.
     * @return boolean The state and the profile future action are rights or
     * not.
     *
     * @see eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction
     */
    private static boolean validState(Ead3 ead, Properties preferences, QueueAction queueAction) {
        boolean state = !queueAction.isUseProfileAction();
        if (!state) {
//            String property = preferences.getProperty(QueueItem.UPLOAD_ACTION);
//            if (ead.getEadClass().equals(FindingAid.class)) { //FINDING AID
//                //each condition returns true if the state and the profile future action are rights
//                state = (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT.getId())) && !ead.isConverted())
//                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.VALIDATE.getId())) && !ead.getValidated().equals(ValidatedState.VALIDATED))
//                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH.getId())) && !ead.isPublished())
//                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH_EUROPEANA.getId()))
//                        && ((!((FindingAid) ead).getEuropeana().equals(EuropeanaState.DELIVERED) || ((FindingAid) ead).getEuropeana().equals(EuropeanaState.NO_EUROPEANA_CANDIDATE))
//                        || !ead.isPublished()));
//            } else if (ead.getEadClass().equals(HoldingsGuide.class) || ead.getEadClass().equals(SourceGuide.class)) {
//                //HOLDINGS GUIDE or SOURCE GUIDE
//                //each condition returns true if the state and the profile future action are rights
//                state = (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT.getId())) && !ead.isConverted())
//                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.VALIDATE.getId())) && !ead.getValidated().equals(ValidatedState.VALIDATED))
//                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH.getId())) && !ead.isPublished());
//            }
        }
        return true;
    }

    /**
     * Puts in the {@link ContentSearchOptions} the list of the EAD3's
     * identifiers to remove from the content manager.
     *
     * @param ids {@link List}{@code <}{@link Integer}{@code >} List of the
     * EAD3's identifiers.
     * @param aiId {@link Integer} The identifier of the archival institution.
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     */
    public static void deleteBatchFromQueue(List<Integer> ids, Integer aiId) throws IOException {
        ContentSearchOptions eacCpfSearchOptions = new ContentSearchOptions();
        eacCpfSearchOptions.setPageSize(0);
        eacCpfSearchOptions.setContentClass(XmlType.EAD_3.getClazz());
        eacCpfSearchOptions.setArchivalInstitionId(aiId);
        if (ids != null && ids.size() > 0) {
            eacCpfSearchOptions.setIds(ids);
        }
        deleteBatchFromQueue(eacCpfSearchOptions);
    }

    /**
     * Deletes from the queue the EAD3s.
     *
     * @param contentSearchOptions {@link ContentSearchOptions} The different
     * options of the EAD3.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     * @see SecurityContext#checkAuthorized(Ead3)
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see eu.apenet.persistence.vo.QueueItem
     */
    public static void deleteBatchFromQueue(ContentSearchOptions contentSearchOptions) throws IOException {
        SecurityContext.get().checkAuthorized(contentSearchOptions.getArchivalInstitionId());
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        contentSearchOptions.setPageSize(0);
        List<QueuingState> queueStates = new ArrayList<QueuingState>();
        queueStates.add(QueuingState.READY);
        queueStates.add(QueuingState.ERROR);
        contentSearchOptions.setQueuing(queueStates);
        JpaUtil.beginDatabaseTransaction();
        List<Ead3> ead3s = ead3DAO.getEad3s(contentSearchOptions);
        int size = 0;
        while ((size = ead3s.size()) > 0) {
            Ead3 ead3 = ead3s.get(size - 1);
            QueueItem queueItem = ead3.getQueueItem();
            ead3.setQueuing(QueuingState.NO);
            ead3DAO.updateSimple(ead3);
            deleteFromQueueInternal(queueItem, true);
            ead3s.remove(size - 1);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    /**
     * Fills the queue with the item.
     *
     * @param upFile {@link UpFile} The file to upload.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Profile preferences.
     * @return {@link QueueItem} A queue item.
     *
     * @throws IOException
     */
    private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(ead, queueAction, preferences, 1000);
    }

    /**
     * Fills the queue with the item.
     *
     * @param upFile {@link UpFile} The file to upload.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Profile preferences.
     * @param basePriority int The priority in the queue.
     * @return {@link QueueItem} An item queue.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.vo.QueueItem
     */
    private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences, int basePriority)
            throws IOException {
        QueueItem queueItem = ead.getQueueItem();
        int priority = basePriority;
        if (queueItem == null) {
            queueItem = new QueueItem();
            if (ead instanceof FindingAid) {
                queueItem.setFindingAid((FindingAid) ead);
            } else if (ead instanceof HoldingsGuide) {
                queueItem.setHoldingsGuide((HoldingsGuide) ead);
                priority += 100;
            } else if (ead instanceof SourceGuide) {
                queueItem.setSourceGuide((SourceGuide) ead);
                priority += 50;
            }
            queueItem.setAiId(ead.getAiId());
        } else if (ead instanceof HoldingsGuide) {
            priority += 100;
        } else if (ead instanceof SourceGuide) {
            priority += 50;
        }
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }
        if (queueAction.isDeleteAction() || queueAction.isUnpublishAction() || queueAction.isDeleteFromEuropeanaAction() || queueAction.isDeleteEseEdmAction()) {
            priority += 150;
        }
        queueItem.setPriority(priority);
        return queueItem;
    }

    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(upFile, queueAction, preferences, 1000);
    }

    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = new QueueItem();
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }

        queueItem.setUpFile(upFile);

        queueItem.setPriority(basePriority);
        queueItem.setAiId(upFile.getAiId());
        return queueItem;
    }

    public static void useProfileActionForHarvester(UpFile upFile, Properties preferences) throws Exception {
        QueueItem queueItem = fillQueueItem(upFile, QueueAction.USE_PROFILE, preferences);
        DAOFactory.instance().getQueueItemDAO().insertSimple(queueItem);
    }

    public static void useProfileAction(UpFile upFile, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(upFile.getAiId());
        QueueItem queueItem = fillQueueItem(upFile, QueueAction.USE_PROFILE, preferences);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    public static void useProfileAction(Ead3 ead3, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(ead3.getAiId());
        QueueItem queueItem = fillQueueItem(ead3, QueueAction.USE_PROFILE, preferences);
        ead3.setQueuing(QueuingState.READY);
        DAOFactory.instance().getEad3DAO().store(ead3);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    /**
     * Checks if the EAD3 exists in the database.
     *
     * @param upFile {@link UpFile} The file to upload.
     * @param identifier String The identifier of the file.
     * @return {@link Ead3} An EAD3 file.
     */
    private static Ead3 doesFileExist(UpFile upFile, String identifier) {
        return DAOFactory.instance().getEad3DAO().getEad3ByIdentifier(upFile.getArchivalInstitution().getRepositorycode(), identifier, false);
    }

    /**
     * Creates the EAD3's preview.
     *
     * @param xmlType {@link XmlType} The type of the file.
     * @param id {@link Integer} The identifier of the EAD3 in the database.
     *
     * @see eu.apenet.persistence.dao.Ead3DAO
     * @see eu.apenet.persistence.vo.Ead3
     * @see SecurityContext#checkAuthorized(Ead3)
     */
    public static void createPreviewHTML(XmlType xmlType, Integer id) {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3 ead3 = ead3DAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead3.getAiId());
    }

    /**
     * Fixes the wrong states in the queue.
     */
    public static void fixWrongQueueStates() {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        ContentSearchOptions searchOptions = new ContentSearchOptions();
        searchOptions.setContentClass(Ead3.class);
        searchOptions.setQueuing(QueuingState.BUSY);
        List<Ead3> ead3s = ead3DAO.getEad3s(searchOptions);
        for (Ead3 ead3 : ead3s) {
            LOGGER.info("Fix wrong queuing state for: " + ead3);
            if (ead3.getQueueItem() == null) {
                ead3.setQueuing(QueuingState.NO);
            } else {
                ead3.setQueuing(QueuingState.READY);
            }
            ead3DAO.store(ead3);
        }

    }
}
