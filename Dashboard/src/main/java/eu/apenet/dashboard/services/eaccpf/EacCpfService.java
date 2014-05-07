package eu.apenet.dashboard.services.eaccpf;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.EacCpfDAO;

import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.IngestionprofileDefaultExistingFileAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultNoEadidAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;

import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class EacCpfService {

    protected static final Logger LOGGER = Logger.getLogger(EacCpfService.class);
    // private static final long NOT_USED_TIME = 60 * 60 * 24 * 7;

    public static boolean convertValidatePublish(Integer id, Properties properties) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (!eacCpf.isPublished()) {
            addToQueue(eacCpf, QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
        }
        return true;
    }

    public static boolean convertValidate(Integer id, Properties properties) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        addToQueue(eacCpf, QueueAction.CONVERT_VALIDATE, properties);
        return true;
    }

    public static void convert(Integer id, Properties properties) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (ConvertTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.CONVERT, properties);
        }
    }

    public static void validate(Integer id) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (ValidateTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.VALIDATE, null);
        }
    }

    public static void publish(Integer id) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (PublishTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.PUBLISH, null);
        }
    }

    public static void unpublish(Integer id) throws IOException {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (UnpublishTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.UNPUBLISH, null);
        }
    }

    public static void delete(Integer id) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id);
        SecurityContext.get().checkAuthorized(eacCpf);
        addToQueue(eacCpf, QueueAction.DELETE, null);
    }

    public static void deleteEdm(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void deleteFromEuropeana(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void deliverToEuropeana(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

    public static File download(Integer id) {
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

    public static EacCpf create(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
        SecurityContext.get().checkAuthorized(aiId);
        EacCpf eac = new CreateEacCpfTask().execute(xmlType, upFile, aiId);
        DAOFactory.instance().getUpFileDAO().delete(upFile);
        return eac;
    }

    public static void useProfileAction(UpFile upFile, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(upFile.getAiId());
        QueueItem queueItem = fillQueueItem(upFile, QueueAction.USE_PROFILE, preferences);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    private static void addToQueue(EacCpf eacCpf, QueueAction queueAction, Properties preferences) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        eacCpf.setQueuing(QueuingState.READY);
        eacCpfDAO.store(eacCpf);
        QueueItem queueItem = fillQueueItem(eacCpf, queueAction, preferences);
        indexqueueDao.store(queueItem);
    }

    private static QueueItem fillQueueItem(EacCpf eacCpf, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(eacCpf, queueAction, preferences, 1000);
    }

    private static QueueItem fillQueueItem(EacCpf eacCpf, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = new QueueItem();
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }
        int priority = basePriority;
        queueItem.setEacCpf(eacCpf);

		if (queueAction.isDeleteAction() || queueAction.isUnpublishAction() || queueAction.isDeleteFromEuropeanaAction() || queueAction.isDeleteEseEdmAction()) {
			priority += 150;
		}
        queueItem.setPriority(priority);
        return queueItem;
    }

    private static String writeProperties(Properties properties) throws IOException {
        StringWriter stringWriter = new StringWriter();
        properties.store(stringWriter, "");
        String result = stringWriter.toString();
        stringWriter.flush();
        stringWriter.close();
        return result;
    }

    public static void overwrite(EacCpf oldEac, UpFile upFile) throws Exception {
        SecurityContext.get().checkAuthorized(oldEac);
        addToQueue(oldEac, QueueAction.OVERWRITE, null, upFile);
    }

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
                }
            } else {
                try {
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(eac, preferences);
                    }
                    if (queueAction.isConvertAction()) {
                        new ConvertTask().execute(eac, preferences);
                    }
                    if (queueAction.isValidateAction()) {
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
                }
            }
        } else { //USE_PROFILE
            IngestionprofileDefaultNoEadidAction ingestionprofileDefaultNoEadidAction = IngestionprofileDefaultNoEadidAction.getExistingFileAction(preferences.getProperty(QueueItem.NO_EADID_ACTION));
            IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction.getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
            IngestionprofileDefaultExistingFileAction ingestionprofileDefaultExistingFileAction = IngestionprofileDefaultExistingFileAction.getExistingFileAction(preferences.getProperty(QueueItem.EXIST_ACTION));
            Boolean daoTypeCheck = "true".equals(preferences.getProperty(QueueItem.DAO_TYPE_CHECK));
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
//                            new DeleteFromEuropeanaTask().execute(eacCpf, preferences);
//                            new DeleteEseEdmTask().execute(eacCpf, preferences);
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
                        }
                    } else if (ingestionprofileDefaultExistingFileAction.isKeep()) {
                        LOGGER.info("File will be removed, because there is already one with the same eadid: " + upFilePath);
                        deleteFromQueue(queueItem, true);
                        continueTask = false;
                    }
                } else {
                    newEacCpf = new CreateEacCpfTask().execute(xmlType, upFile, upFile.getAiId());
                    queueItem.setUpFile(null);
                    queueItemDAO.store(queueItem);
                    DAOFactory.instance().getUpFileDAO().delete(upFile);
                }

                if (continueTask) {
                    newEacCpf.setQueuing(QueuingState.BUSY);
                    eacDAO.store(newEacCpf);

                    Properties conversionProperties = new Properties();
                    conversionProperties.put("defaultRoleType", "UNSPECIFIED");
                    conversionProperties.put("useDefaultRoleType", daoTypeCheck);

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
//                            if (ingestionprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
//                                Properties europeanaProperties = createEuropeanaProperties(preferences);
//                                new ConvertToEseEdmTask().execute(newEacCpf, europeanaProperties);
//                                new DeliverToEuropeanaTask().execute(newEacCpf);
//                            }
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
                    }
                }
            }
        }
        LOGGER.info("Process queue item finished");
        return queueAction;
    }

    public static Properties readProperties(String string) throws IOException {
        StringReader stringReader = new StringReader(string);
        Properties properties = new Properties();
        properties.load(stringReader);
        stringReader.close();
        return properties;
    }

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
            QueueItem queueItem = fillQueueItem(eacCpf, queueAction, preferences);
            eacCpf.setQueuing(QueuingState.READY);
            eacCpfDAO.updateSimple(eacCpf);
            eacCpfs.remove(size - 1);
            indexqueueDao.updateSimple(queueItem);
        }
        JpaUtil.commitDatabaseTransaction();
    }

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
        return queueItem;
    }

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

    private static EacCpf doesFileExist(UpFile upFile, String identifier) {
        return DAOFactory.instance().getEacCpfDAO().getEacCpfByIdentifier(upFile.getArchivalInstitution().getRepositorycode(), identifier);
    }
}
