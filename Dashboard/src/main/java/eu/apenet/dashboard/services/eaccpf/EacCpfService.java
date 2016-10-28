package eu.apenet.dashboard.services.eaccpf;

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

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.AbstractService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.IngestionprofileDefaultExistingFileAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultNoEadidAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * <p>
 * Class to manage the different EAC-CPF actions in the content manager of the
 * dashboard.
 * <p>
 * Puts the right value of the action in the entry "Queue" of the content
 * manager.
 */
public class EacCpfService extends AbstractService {

    protected static final Logger LOGGER = Logger.getLogger(EacCpfService.class);
    private static final String CURRENT_LANGUAGE_KEY = "currentLanguage";
    // private static final long NOT_USED_TIME = 60 * 60 * 24 * 7;

    /**
     * Updates the table <b>queue</b> and the table <b>eac_cpf</b> in the
     * database.
     *
     * @param eadSearchOptions {@link ContentSearchOptions} An object
     * ContentSeachOptions.
     * @param queueAction {@link QueueAction} An object QueueAction to manage
     * the queue.
     * @throws IOException
     * @see EacCpfDAO#countEacCpfs(ContentSearchOptions)
     * @see EacCpfDAO#getEacCpfs(ContentSearchOptions)
     * @see EacCpfDAO#updateSimple(EacCpf)
     * @see EacCpf#setQueuing(QueuingState)
     * @see EacCpf#setPublished(boolean)
     * @see QueueItemDAO#updateSimple(QueueItem)
     * @see QueueAction#isPublishAction()
     */
    public static void updateEverything(ContentSearchOptions eadSearchOptions, QueueAction queueAction) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        long itemsLeft = eacCpfDAO.countEacCpfs(eadSearchOptions);
        LOGGER.info(itemsLeft + " " + eadSearchOptions.getContentClass().getSimpleName() + " left to add to queue");
        while (itemsLeft > 0) {
            JpaUtil.beginDatabaseTransaction();
            List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(eadSearchOptions);
            int size = 0;
            while ((size = eacCpfs.size()) > 0) {
                EacCpf eacCpf = eacCpfs.get(size - 1);
                QueueItem queueItem = fillQueueItem(eacCpf, queueAction, null, 1);
                eacCpf.setQueuing(QueuingState.READY);
                if (queueAction.isPublishAction()) {
                    eacCpf.setPublished(false);
                }
                eacCpfDAO.updateSimple(eacCpf);
                eacCpfs.remove(size - 1);
                indexqueueDao.updateSimple(queueItem);
            }
            JpaUtil.commitDatabaseTransaction();
            itemsLeft = eacCpfDAO.countEacCpfs(eadSearchOptions);
            LOGGER.info(itemsLeft + " " + eadSearchOptions.getContentClass().getSimpleName() + " left to add to queue");

        }

    }

    /**
     * <p>
     * Manages the actions convert, validate and publish.
     * <p>
     * Puts in the queue these actions.
     *
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @param properties {@link Properties} A property list to process the file.
     * @param currentLanguage String The current language in the Dashboard.
     * @return boolean The EAC-CPF is converted, validated and published.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static boolean convertValidatePublish(Integer id, Properties properties, String currentLanguage) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (!eacCpf.isPublished()) {
            properties.put(CURRENT_LANGUAGE_KEY, currentLanguage);
            addToQueue(eacCpf, QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
        }
        return true;
    }

    /**
     * <p>
     * Manages the actions convert and validate.
     * <p>
     * Puts in the queue these actions.
     *
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @param properties {@link Properties} A property list to process the file.
     * @param currentLanguage String The current language in the Dashboard.
     * @return boolean The EAC-CPF is converted and validated.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static boolean convertValidate(Integer id, Properties properties, String currentLanguage) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        properties.put(CURRENT_LANGUAGE_KEY, currentLanguage);
        addToQueue(eacCpf, QueueAction.CONVERT_VALIDATE, properties);
        return true;
    }

    /**
     * <p>
     * Manages the action convert.
     * <p>
     * Puts in the queue this action.
     *
     * @param xmlType
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @param properties {@link Properties} A property list to process the file.
     * @param currentLanguage String The current language in the Dashboard.
     * @return boolean The EAC-CPF is converted.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    @Override
    public void convert(XmlType xmlType, Integer id, Properties properties) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (ConvertTask.valid(eacCpf)) {
//            properties.put(CURRENT_LANGUAGE_KEY, currentLanguage);
            addToQueue(eacCpf, QueueAction.CONVERT, properties);
        }
    }

    /**
     * <p>
     * Manages the action validate.
     * <p>
     * Puts in the queue this action.
     *
     * @param xmlType
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    @Override
    public void validate(XmlType xmlType, Integer id) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (ValidateTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.VALIDATE, null);
        }
    }

    /**
     * <p>
     * Manages the action publish.
     * <p>
     * Puts in the queue this action.
     *
     * @param xmlType
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    @Override
    public void publish(XmlType xmlType, Integer id) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (PublishTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.PUBLISH, null);
        }
    }

    /**
     * <p>
     * Manages the action unpublish.
     * <p>
     * Puts in the queue this action.
     *
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    @Override
    public void unpublish(XmlType xmlType, Integer id) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (UnpublishTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.UNPUBLISH, null);
        }
    }

    /**
     * <p>
     * Manages the action delete.
     * <p>
     * Puts in the queue this action.
     *
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    @Override
    public void delete(XmlType xmlType, Integer id) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id);
        SecurityContext.get().checkAuthorized(eacCpf);
        addToQueue(eacCpf, QueueAction.DELETE, null);
    }

    /**
     * This method is implemented by some developer to future functionality.
     *
     * @param id {@link Integer}
     */
    public static void deleteEdm(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is implemented by some developer to future functionality.
     *
     * @param id {@link Integer}
     */
    public static void deleteFromEuropeana(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is implemented by some developer to future functionality.
     *
     * @param id {@link Integer}
     */
    public static void deliverToEuropeana(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Deletes from the queue an EAC-CPF and updates the state of the entry
     * <i>queueing</i> in the database.
     *
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @throws IOException
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static void deleteFromQueue(Integer id) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        JpaUtil.beginDatabaseTransaction();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (QueuingState.ERROR.equals(eacCpf.getQueuing()) || QueuingState.READY.equals(eacCpf.getQueuing())) {
            QueueItem queueItem = eacCpf.getQueueItem();
            eacCpf.setQueuing(QueuingState.NO);
            eacCpfDAO.updateSimple(eacCpf);
            deleteFromQueueInternal(queueItem, true);
        }
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
     * @param id {@link Integer} The identifier of the EAC-CPF file.
     * @return {@link File} The file downloading.
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     * @see eu.apenet.persistence.vo.UpFile
     */
    @Override
    public File download(XmlType xmlType, Integer id) {
        EacCpf eacCpf = DAOFactory.instance().getEacCpfDAO().findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        String path = APEnetUtilities.getConfig().getRepoDirPath() + eacCpf.getPath();
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
     * Creates a new entry in the database for the EAC-CPF and deletes the file
     * from the table <b>upfile</b>.
     *
     * @param xmlType {@link XmlType} The type of the file, in this case is an
     * EAC-CPF file.
     * @param upFile {@link UpFile} The object file that is stored in the
     * database in the table <b>upfile</b>.
     * @param aiId {@link Integer} The identifier of the archival institution.
     * @return {@link EacCpf} The object EAC-CPF.
     * @throws Exception
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static EacCpf create(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
        SecurityContext.get().checkAuthorized(aiId);
        EacCpf eac = new CreateEacCpfTask().execute(xmlType, upFile, aiId);
        DAOFactory.instance().getUpFileDAO().delete(upFile);
        return eac;
    }

    /**
     * Uses the profile action associated to the EAC-CPF file.
     *
     * @param upFile {@link UpFile} The object file that is stored in the
     * database.
     * @param preferences {@link Properties} A property list to process the
     * file.
     * @throws Exception
     * @see SecurityContext#checkAuthorized(EacCpf)
     * @see eu.apenet.persistence.vo.QueueItem
     * @see eu.apenet.persistence.dao.QueueItemDAO
     */
    public static void useProfileAction(UpFile upFile, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(upFile.getAiId());
        QueueItem queueItem = fillQueueItem(upFile, QueueAction.USE_PROFILE, preferences);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    /**
     * Method to use the profile action associated to the EAC-CPF file.
     *
     * @param eacCpf {@link EacCpf} EAC-CPF file to process with the
     * preferences.
     * @param preferences {@link Properties} Preferences to process the EAC-CPF.
     *
     * @throws Exception During the process.
     * @see SecurityContext#checkAuthorized(EacCpf)
     * @see eu.apenet.persistence.vo.QueueItem
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.dao.EacCpfDAO
     */
    public static void useProfileAction(EacCpf eacCpf, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(eacCpf.getAiId());
        QueueItem queueItem = fillQueueItem(eacCpf, QueueAction.USE_PROFILE, preferences);
        eacCpf.setQueuing(QueuingState.READY);
        DAOFactory.instance().getEacCpfDAO().store(eacCpf);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    /**
     * <p>
     * Adds an EAC-CPF file to the queue.
     * <p>
     * Stores in the database an EAC-CPF in the table <b>eac_cpf</b>.
     * <p>
     * Fills the table <b>queue</b> with the EAC-CPF's identifier and his
     * preferences.
     *
     * @param eacCpf {@link EacCpf} The EAC-CPF file to ingest in the Dashboard.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Preferences to process the EAC-CPF.
     * @throws IOException
     * @see eu.apenet.persistence.vo.QueueItem
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.dao.EacCpfDAO
     */
    private static void addToQueue(EacCpf eacCpf, QueueAction queueAction, Properties preferences) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        eacCpf.setQueuing(QueuingState.READY);
        eacCpfDAO.store(eacCpf);
        QueueItem queueItem = fillQueueItem(eacCpf, queueAction, preferences);
        indexqueueDao.store(queueItem);
    }

    /**
     * Fills an item <i>queue</i>.
     *
     * @param eacCpf {@link EacCPf} The EAC-CPF file to ingest in the Dashboard.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Preferences to process the EAC-CPF.
     * @return {@link QueueItem> An item <i>queue</i>.
     * @throws IOException
     */
    private static QueueItem fillQueueItem(EacCpf eacCpf, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(eacCpf, queueAction, preferences, 1000);
    }

    /**
     * Fills an item <i>queue</i> with the EAC-CPF's identifier, preferences and
     * priority.
     *
     * @param eacCpf {@link EacCpf} The EAC-CPF file to ingest in the Dashboard.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Preferences to process the EAC-CPF.
     * @param basePriority {@link Integer} The priority in the queue.
     * @return {@link QueueItem} An item <i>queue</i>.
     * @throws IOException
     */
    private static QueueItem fillQueueItem(EacCpf eacCpf, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = eacCpf.getQueueItem();
        if (queueItem == null) {
            queueItem = new QueueItem();
            queueItem.setEacCpf(eacCpf);
            queueItem.setAiId(eacCpf.getAiId());
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
     * @param properties {@link Properties} The preferences to process the
     * EAC-CPF.
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
     * Overwrites an old EAC-CPF file.
     *
     * @param oldEac {@link EacCpf} The old EAC-CPF file.
     * @param upFile {@link UpFile} The file to up.
     * @throws Exception
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static void overwrite(EacCpf oldEac, UpFile upFile) throws Exception {
        SecurityContext.get().checkAuthorized(oldEac);
        addToQueue(oldEac, QueueAction.OVERWRITE, null, upFile);
    }

    /**
     * <p>
     * Adds an EAC-CPF file to the queue.
     * <p>
     * Stores in the database an EAC-CPF in the table <b>eac_cpf</b>.
     * <p>
     * Fills the table <b>queue</b> with the EAC-CPF's identifier, his
     * preferences and the upfile's identifier.
     *
     * @see eu.apenet.persistence.vo.QueueAction
     * @see java.util.Properties
     * @see eu.apenet.persistence.vo.UpFile
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     */
    private static void addToQueue(EacCpf eac, QueueAction queueAction, Properties preferences, UpFile upFile)
            throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacDAO = DAOFactory.instance().getEacCpfDAO();
        eac.setQueuing(QueuingState.READY);
        eacDAO.store(eac);
        QueueItem queueItem = fillQueueItem(eac, queueAction, preferences);
        queueItem.setUpFile(upFile);
        indexqueueDao.store(queueItem);
    }

    /**
     * Process an item from the queue.
     *
     * @see CreateEacCpfTask#execute(XmlType, UpFile, Integer)
     * @see PublishTask#execute(EacCpf, Properties)
     * @see UnpublishTask#execute(EacCpf, Properties)
     * @see ConvertTask#execute(EacCpf, Properties)
     * @see ValidateTask#execute(EacCpf, Properties)
     * @see DeleteTask#execute(EacCpf, Properties)
     * @see eu.apenet.persistence.vo.QueueAction
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see java.util.Properties
     * @see eu.apenet.persistence.vo.UpFile
     * @see eu.apenet.commons.types.XmlType
     */
    public static QueueAction processQueueItem(QueueItem queueItem) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacDAO = DAOFactory.instance().getEacCpfDAO();
        QueueAction queueAction = queueItem.getAction();
        Properties preferences = null;
        if (queueItem.getPreferences() != null) {
            preferences = readProperties(queueItem.getPreferences());
        }

        if (!queueAction.isUseProfileAction()) {
            EacCpf eac = queueItem.getEacCpf();
            XmlType xmlType = XmlType.getContentType(eac);
            LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + " " + eac.getIdentifier()
                    + "(" + xmlType.getName() + ")");

            if (queueAction.isOverwriteAction() || queueAction.isDeleteAction()) {
                boolean eacDeleted = false;
                boolean upFileDeleted = false;
                UpFile upFile = queueItem.getUpFile();
                try {

                    queueItem.setEacCpf(null);
                    queueItem.setUpFile(null);
                    queueItemDAO.store(queueItem);

                    if (queueAction.isOverwriteAction()) {
                        Integer aiId = eac.getAiId();
//                        new DeleteFromEuropeanaTask().execute(eac, preferences);
//                        new DeleteEseEdmTask().execute(eac, preferences);
                        new UnpublishTask().execute(eac, preferences);
                        new DeleteTask().execute(eac, preferences);
                        eacDeleted = true;
                        new CreateEacCpfTask().execute(xmlType, upFile, aiId);
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                        upFileDeleted = true;
                    } else if (queueAction.isDeleteAction()) {
//                        new DeleteFromEuropeanaTask().execute(eac, preferences);
//                        new DeleteEseEdmTask().execute(eac, preferences);
                        new UnpublishTask().execute(eac, preferences);
                        new DeleteTask().execute(eac, preferences);
                        eacDeleted = true;
                    }
                    queueItemDAO.delete(queueItem);
                } catch (Exception e) {
                    if (!eacDeleted) {
                        queueItem.setEacCpf(eac);
                        eac.setQueuing(QueuingState.ERROR);
                        eacDAO.store(eac);
                    }
                    if (!upFileDeleted && upFile != null) {
                        queueItem.setUpFile(upFile);
                    }
                    String err = "EAC-CPF identifier: " + eac.getIdentifier() + " - id: " + eac.getId() + " - type: " + xmlType.getName();
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
            } else {
                try {
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(eac, preferences);
                    }
                    if (queueAction.isConvertAction()) {
                        new ConvertTask().execute(eac, preferences);
                        new ValidateTask().execute(eac, preferences);
                    }
                    
                    if (queueAction.isPublishAction()) {
                        new PublishTask().execute(eac, preferences);
                    }
                    if (queueAction.isRePublishAction()) {
                        new UnpublishTask().execute(eac, preferences);
                        new PublishTask().execute(eac, preferences);
                    }
//                    if (queueAction.isConvertToEseEdmAction()) {
//                        new ConvertToEseEdmTask().execute(eac, preferences);
//                    }
                    if (queueAction.isUnpublishAction()) {
                        new UnpublishTask().execute(eac, preferences);
                    }
//                    if (queueAction.isDeleteFromEuropeanaAction()) {
//                        new DeleteFromEuropeanaTask().execute(eac, preferences);
//                    }
//                    if (queueAction.isDeleteEseEdmAction()) {
//                        new DeleteEseEdmTask().execute(eac, preferences);
//                    }
//                    if (queueAction.isDeliverToEuropeanaAction()) {
//                        new DeliverToEuropeanaTask().execute(eac, preferences);
//                    }
                    eac.setQueuing(QueuingState.NO);
                    eacDAO.store(eac);
                    queueItemDAO.delete(queueItem);
                } catch (APEnetException e) {
                    String err = "identifier: " + eac.getIdentifier() + " - id: " + eac.getId() + " - type: " + xmlType.getName();
                    LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                    queueItem.setErrors(new Date() + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                    eac.setQueuing(QueuingState.ERROR);
                    eacDAO.store(eac);
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
        if (queueItem.getUpFile() != null) {
            processUpFileWithProfile(queueItem, preferences);
        } else {
            processEacCpfWithProfile(queueItem, preferences);
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
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
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
        EacCpfDAO eacDAO = DAOFactory.instance().getEacCpfDAO();
        IngestionprofileDefaultNoEadidAction ingestionprofileDefaultNoEadidAction = IngestionprofileDefaultNoEadidAction.getExistingFileAction(preferences.getProperty(QueueItem.NO_EADID_ACTION));
        IngestionprofileDefaultExistingFileAction ingestionprofileDefaultExistingFileAction = IngestionprofileDefaultExistingFileAction.getExistingFileAction(preferences.getProperty(QueueItem.EXIST_ACTION));
        XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));

        //About existing eac-cpf identifier
        UpFile upFile = queueItem.getUpFile();
        LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + ", upFile id: " + queueItem.getUpFileId() + "(" + xmlType.getName() + ")");
        String upFilePath = upFile.getPath() + upFile.getFilename();
        String identifier = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "eac-cpf/control/recordId", null, true, false).trim();
        if (StringUtils.isEmpty(identifier) || "empty".equals(identifier) || "error".equals(identifier)) {
            if (ingestionprofileDefaultNoEadidAction.isRemove()) {
                LOGGER.info("File will be removed, because it does not have eadid: " + upFilePath);
                deleteFromQueue(queueItem, true);
            } else {
                LOGGER.info("File will be processed manually later, because it does not have eadid: " + upFilePath);
                deleteFromQueue(queueItem, false);
            }
        } else {
            boolean continueTask = true;
            EacCpf eacCpf;
            EacCpf newEacCpf = null;
            if ((eacCpf = doesFileExist(upFile, identifier)) != null) {
                if (ingestionprofileDefaultExistingFileAction.isOverwrite()) {
                    boolean eacCpfDeleted = false;
                    try {
                        queueItem.setEacCpf(null);
                        queueItem.setUpFile(null);
                        queueItemDAO.store(queueItem);

                        Integer aiId = eacCpf.getAiId();
//                        new DeleteFromEuropeanaTask().execute(eacCpf, preferences);
//                        new DeleteEseEdmTask().execute(eacCpf, preferences);
                        new UnpublishTask().execute(eacCpf, preferences);
                        new DeleteTask().execute(eacCpf, preferences);
                        eacCpfDeleted = true;
                        newEacCpf = new CreateEacCpfTask().execute(xmlType, upFile, aiId);
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                    } catch (Exception e) {
                        if (!eacCpfDeleted) {
                            queueItem.setEacCpf(eacCpf);
                            eacCpf.setQueuing(QueuingState.ERROR);
                            eacDAO.store(eacCpf);
                        }
                        queueItem.setUpFile(upFile);
                        String err = "recordId: " + eacCpf.getIdentifier() + " - id: " + eacCpf.getId() + " - type: " + xmlType.getName();
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
                    LOGGER.info("File will be removed, because there is already one with the same recordid: " + upFilePath);
                    deleteFromQueue(queueItem, true);
                    continueTask = false;
                } else if (ingestionprofileDefaultExistingFileAction.isAsk()) {
                    LOGGER.info("Is needed to ask the user for the action, because there is already one with the same recordid: " + upFilePath);
                    deleteFromQueue(queueItem, false);
                    continueTask = false;
                }
            } else {
                newEacCpf = new CreateEacCpfTask().execute(xmlType, upFile, upFile.getAiId());
                queueItem.setUpFile(null);
                queueItemDAO.store(queueItem);
                DAOFactory.instance().getUpFileDAO().delete(upFile);
            }

            if (continueTask) {
                processEacCpf(queueItem, newEacCpf, preferences);
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
     * @see eu.apenet.persistence.vo.EacCpf
     */
    private static void processEacCpfWithProfile(QueueItem queueItem, Properties preferences) throws Exception {
        EacCpf eacCpf = queueItem.getEacCpf();
        processEacCpf(queueItem, eacCpf, preferences);
    }

    /**
     * Method to performs the actions associated to the profile over the file.
     *
     * @param queueItem {@link QueueItem} Current item to process.
     * @param newEacCpf {@link EacCpf} EAC-CPF to process using the profile.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws Exception
     *
     * @see eu.apenet.persistence.vo.QueueAction
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see java.util.Properties
     * @see eu.apenet.commons.types.XmlType
     * @see eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     * @see ConvertTask#execute(EacCpf, Properties)
     * @see ValidateTask#execute(EacCpf)
     * @see PublishTask#execute(EacCpf)
     */
    private static void processEacCpf(QueueItem queueItem, EacCpf newEacCpf, Properties preferences) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacDAO = DAOFactory.instance().getEacCpfDAO();
        IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction.getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
        Boolean daoTypeCheck = "true".equals(preferences.getProperty(QueueItem.DAO_TYPE_CHECK));
        XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));

        newEacCpf.setQueuing(QueuingState.BUSY);
        eacDAO.store(newEacCpf);

        Properties conversionProperties = new Properties();
        conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT, "UNSPECIFIED");
        conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_USE_EXISTING, daoTypeCheck);

        try {
            if (ingestionprofileDefaultUploadAction.isConvert()) {
                new ConvertTask().execute(newEacCpf, conversionProperties);
            } else if (ingestionprofileDefaultUploadAction.isValidate()) {
                new ValidateTask().execute(newEacCpf);
            } else if (ingestionprofileDefaultUploadAction.isConvertValidatePublish() || ingestionprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
                new ValidateTask().execute(newEacCpf);
                new ConvertTask().execute(newEacCpf, conversionProperties);
                new ValidateTask().execute(newEacCpf);
                new PublishTask().execute(newEacCpf);
//                if (ingestionprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
//                    Properties europeanaProperties = createEuropeanaProperties(preferences);
//                    new ConvertToEseEdmTask().execute(newEacCpf, europeanaProperties);
//                    new DeliverToEuropeanaTask().execute(newEacCpf);
//                }
            }
            newEacCpf.setQueuing(QueuingState.NO);
            eacDAO.store(newEacCpf);
            queueItemDAO.delete(queueItem);
        } catch (Exception e) {
            newEacCpf.setQueuing(QueuingState.ERROR);
            eacDAO.store(newEacCpf);

            String err = "recordId: " + newEacCpf.getIdentifier() + " - id: " + newEacCpf.getId() + " - type: " + xmlType.getName();
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
     * identifiers of the EAC-CPF.
     * @param aiId {@link Integer} The identifier of the archival institution.
     * @param queueAction {@link QueueAction} The action in the queue.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     */
    public static void addBatchToQueue(List<Integer> ids, Integer aiId, QueueAction queueAction, Properties preferences) throws IOException {
        ContentSearchOptions eacCpfSearchOptions = new ContentSearchOptions();
        eacCpfSearchOptions.setPageSize(0);
        eacCpfSearchOptions.setContentClass(XmlType.EAC_CPF.getClazz());
        eacCpfSearchOptions.setArchivalInstitionId(aiId);
        if (ids != null && ids.size() > 0) {
            eacCpfSearchOptions.setIds(ids);
        }
        addBatchToQueue(eacCpfSearchOptions, queueAction, preferences);
    }

    /**
     * Adds to {@link ContentSearchOptions} the different options and update the
     * database.
     *
     * @param eacCpfSearchOptions {@link ContentSearchOptions} The options of
     * the EAC-CPF.
     * @param queueAction {@link QueueAction} The actions in the queue.
     * @param preferences {@link Properties} Profile preferences.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see eu.apenet.persistence.dao.QueueItemDAO
     * @see eu.apenet.persistence.vo.QueueItem
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static void addBatchToQueue(ContentSearchOptions eacCpfSearchOptions, QueueAction queueAction, Properties preferences) throws IOException {
        SecurityContext.get().checkAuthorized(eacCpfSearchOptions.getArchivalInstitionId());
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        eacCpfSearchOptions.setPageSize(0);
        if (QueueAction.CONVERT.equals(queueAction)) {
            eacCpfSearchOptions.setConverted(false);
        } else if (QueueAction.VALIDATE.equals(queueAction)) {
            eacCpfSearchOptions.setValidated(ValidatedState.NOT_VALIDATED);
        } else if (QueueAction.PUBLISH.equals(queueAction)) {
            eacCpfSearchOptions.setValidated(ValidatedState.VALIDATED);
            eacCpfSearchOptions.setPublished(false);
        } else if (QueueAction.CONVERT_VALIDATE_PUBLISH.equals(queueAction)) {
            eacCpfSearchOptions.setPublished(false);
        } else if (QueueAction.UNPUBLISH.equals(queueAction)) {
            eacCpfSearchOptions.setPublished(true);
        } else if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)) {
            eacCpfSearchOptions.setEuropeana(EuropeanaState.NOT_CONVERTED);
            eacCpfSearchOptions.setValidated(ValidatedState.VALIDATED);
        } else if (QueueAction.DELIVER_TO_EUROPEANA.equals(queueAction)) {
            eacCpfSearchOptions.setEuropeana(EuropeanaState.CONVERTED);

        } else if (QueueAction.DELETE_FROM_EUROPEANA.equals(queueAction)) {
            eacCpfSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
        } else if (QueueAction.DELETE_ESE_EDM.equals(queueAction)) {
            eacCpfSearchOptions.setEuropeana(EuropeanaState.CONVERTED);
        } else if (QueueAction.CHANGE_TO_STATIC.equals(queueAction)) {
            eacCpfSearchOptions.setDynamic(true);
        } else if (QueueAction.CHANGE_TO_DYNAMIC.equals(queueAction)) {
            eacCpfSearchOptions.setDynamic(false);
        }
        eacCpfSearchOptions.setQueuing(QueuingState.NO);
        JpaUtil.beginDatabaseTransaction();
        List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(eacCpfSearchOptions);
        int size = 0;
        while ((size = eacCpfs.size()) > 0) {
            EacCpf eacCpf = eacCpfs.get(size - 1);
            if (validState(eacCpf, preferences, queueAction)) {
                QueueItem queueItem = fillQueueItem(eacCpf, queueAction, preferences);
                eacCpf.setQueuing(QueuingState.READY);
                eacCpfDAO.updateSimple(eacCpf);
                indexqueueDao.updateSimple(queueItem);
            }
            eacCpfs.remove(size - 1);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    /**
     * Checks the state and the profile future action.
     *
     * @param eac {@link EacCpf} The EAC-CPF to validate.
     * @param preferences {@link Properties} Profile preferences.
     * @param queueAction {@link QueueAction} The action to process.
     * @return boolean The state and the profile future action are rights or
     * not.
     *
     * @see eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction
     */
    private static boolean validState(EacCpf eac, Properties preferences, QueueAction queueAction) {
        boolean state = !queueAction.isUseProfileAction();
        if (!state) {
            String property = preferences.getProperty(QueueItem.UPLOAD_ACTION);
            //each condition returns true if the state and the profile future action are rights
            state = (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT.getId())) && !eac.isConverted())
                    || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.VALIDATE.getId())) && !eac.getValidated().equals(ValidatedState.VALIDATED))
                    || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH.getId())) && !eac.isPublished());
        }
        return state;
    }

    /**
     * Puts in the {@link ContentSearchOptions} the list of the EAC-CPF's
     * identifiers to remove from the content manager.
     *
     * @param ids {@link List}{@code <}{@link Integer}{@code >} List of the
     * EAC-CPF's identifiers.
     * @param aiId {@link Integer} The identifier of the archival institution.
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     */
    public static void deleteBatchFromQueue(List<Integer> ids, Integer aiId) throws IOException {
        ContentSearchOptions eacCpfSearchOptions = new ContentSearchOptions();
        eacCpfSearchOptions.setPageSize(0);
        eacCpfSearchOptions.setContentClass(XmlType.EAC_CPF.getClazz());
        eacCpfSearchOptions.setArchivalInstitionId(aiId);
        if (ids != null && ids.size() > 0) {
            eacCpfSearchOptions.setIds(ids);
        }
        deleteBatchFromQueue(eacCpfSearchOptions);
    }

    /**
     * Deletes from the queue the EAC-CPFs.
     *
     * @param eacCpfSearchOptions {@link ContentSearchOptions} The different
     * options of the EAC-CPF.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.ContentSearchOptions
     * @see SecurityContext#checkAuthorized(EacCpf)
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see eu.apenet.persistence.vo.QueueItem
     */
    public static void deleteBatchFromQueue(ContentSearchOptions eacCpfSearchOptions) throws IOException {
        SecurityContext.get().checkAuthorized(eacCpfSearchOptions.getArchivalInstitionId());
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        eacCpfSearchOptions.setPageSize(0);
        List<QueuingState> queueStates = new ArrayList<QueuingState>();
        queueStates.add(QueuingState.READY);
        queueStates.add(QueuingState.ERROR);
        eacCpfSearchOptions.setQueuing(queueStates);
        JpaUtil.beginDatabaseTransaction();
        List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(eacCpfSearchOptions);
        int size = 0;
        while ((size = eacCpfs.size()) > 0) {
            EacCpf eacCpf = eacCpfs.get(size - 1);
            QueueItem queueItem = eacCpf.getQueueItem();
            eacCpf.setQueuing(QueuingState.NO);
            eacCpfDAO.updateSimple(eacCpf);
            deleteFromQueueInternal(queueItem, true);
            eacCpfs.remove(size - 1);
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
    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(upFile, queueAction, preferences, 1000);
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

    /**
     * Deletes the file from the queue and updates the database.
     *
     * @param queueItem {@link QueueItem} A queue item.
     * @param deleteUpFile boolean If there is to delete the file.
     *
     * @throws IOException
     *
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     */
    private static void deleteFromQueue(QueueItem queueItem, boolean deleteUpFile) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        JpaUtil.beginDatabaseTransaction();
        EacCpf eacCpf = queueItem.getEacCpf();
        if (eacCpf != null) {
            eacCpf.setQueuing(QueuingState.NO);
            eacCpfDAO.updateSimple(eacCpf);
        }
        deleteFromQueueInternal(queueItem, deleteUpFile);
        JpaUtil.commitDatabaseTransaction();
    }

    /**
     * Checks if the EAC-CPF exists in the database.
     *
     * @param upFile {@link UpFile} The file to upload.
     * @param identifier String The identifier of the file.
     * @return {@link EacCpf} An EAC-CPF file.
     */
    private static EacCpf doesFileExist(UpFile upFile, String identifier) {
        return DAOFactory.instance().getEacCpfDAO().getEacCpfByIdentifier(upFile.getArchivalInstitution().getRepositorycode(), identifier, false);
    }

    /**
     * Creates the EAC-CPF's preview.
     *
     * @param xmlType {@link XmlType} The type of the file.
     * @param id {@link Integer} The identifier of the EAC-CPF in the database.
     *
     * @see eu.apenet.persistence.dao.EacCpfDAO
     * @see eu.apenet.persistence.vo.EacCpf
     * @see SecurityContext#checkAuthorized(EacCpf)
     */
    public static void createPreviewHTML(XmlType xmlType, Integer id) {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
    }

    /**
     * Fixes the wrong states in the queue.
     */
    public static void fixWrongQueueStates() {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        ContentSearchOptions searchOptions = new ContentSearchOptions();
        searchOptions.setContentClass(EacCpf.class);
        searchOptions.setQueuing(QueuingState.BUSY);
        List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(searchOptions);
        for (EacCpf eacCpf : eacCpfs) {
            LOGGER.info("Fix wrong queuing state for: " + eacCpf);
            if (eacCpf.getQueueItem() == null) {
                eacCpf.setQueuing(QueuingState.NO);
            } else {
                eacCpf.setQueuing(QueuingState.READY);
            }
            eacCpfDAO.store(eacCpf);
        }

    }
}
