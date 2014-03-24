package eu.apenet.dashboard.services.eaccpf;

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

import eu.apenet.persistence.vo.UpFile;

public class EacCpfService {

    protected static final Logger LOGGER = Logger.getLogger(EacCpfService.class);
    private static final long NOT_USED_TIME = 60 * 60 * 24 * 7;

    public static void convertValidatePublish(Integer id, Properties properties) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void convertValidate(Integer id, Properties properties) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void convert(Integer id, Properties properties) throws Exception {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.findById(id, XmlType.EAC_CPF.getClazz());
        SecurityContext.get().checkAuthorized(eacCpf);
        if (ConvertTask.valid(eacCpf)) {
            addToQueue(eacCpf, QueueAction.CONVERT, properties);
        }
    }

    public static void validate(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void publish(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void unpublish(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public static void deleteFromQueue(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static File download(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static EacCpf create(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
        SecurityContext.get().checkAuthorized(aiId);
        EacCpf eac = new CreateEacCpfTask().execute(xmlType, upFile, aiId);
        DAOFactory.instance().getUpFileDAO().delete(upFile);
        return eac;
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

        if (queueAction.isConvertAction() || queueAction.isValidateAction() || queueAction.isPublishAction()) {
            priority += 25;
        } else if (queueAction.isDeleteAction() || queueAction.isOverwriteAction()) {
            priority += 50;
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
                        new DeleteTask().execute(eac, preferences);
                        eacDeleted = true;
                        new CreateEacCpfTask().execute(xmlType, upFile, aiId);
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                        upFileDeleted = true;
                    } else if (queueAction.isDeleteAction()) {
//                        new DeleteFromEuropeanaTask().execute(eac, preferences);
//                        new DeleteEseEdmTask().execute(eac, preferences);
//                        new UnpublishTask().execute(eac, preferences);
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
                    if (queueAction.isConvertAction()) {
                        new ConvertTask().execute(eac, preferences);
                    }
                    eac.setQueuing(QueuingState.NO);
                    eacDAO.store(eac);
                    queueItemDAO.delete(queueItem);
                } catch (Exception e) {
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
            //TODO. Not defined yet

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
}
