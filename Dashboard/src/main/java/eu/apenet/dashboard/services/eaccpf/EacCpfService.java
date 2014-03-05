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
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

public class EacCpfService {

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

    public static void delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
