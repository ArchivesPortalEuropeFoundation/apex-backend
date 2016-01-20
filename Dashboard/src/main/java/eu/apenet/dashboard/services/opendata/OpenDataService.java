/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.opendata;

import eu.apenet.commons.exceptions.ProcessBusyException;
import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.EadSolrServerHolder;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Mahbub
 */
public class OpenDataService {

    protected static final Logger LOGGER = Logger.getLogger(EacCpfService.class);
    public static final String TOTAL_SOLAR_DOC_KEY = "totalSolarDoc";
    public static final String ENABLE_OPEN_DATA_KEY = "enableOpenData";

    public static boolean openDataPublish(Integer aid, Properties preferences) throws IOException, ProcessBusyException {
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aid);

        SecurityContext.get().checkAuthorized(aid);
        if (archivalInstitution.getUnprocessedSolrDocs()<=0) {
            archivalInstitution.setTotalSolrDocsForOpenData(Long.parseLong(preferences.getProperty(TOTAL_SOLAR_DOC_KEY, "0")));
            archivalInstitution.setUnprocessedSolrDocs(Long.parseLong(preferences.getProperty(TOTAL_SOLAR_DOC_KEY, "0")));
            
            archivalInstitution.setOpenDataEnabled(Boolean.parseBoolean(preferences.getProperty(ENABLE_OPEN_DATA_KEY, "false")));
            
            archivalInstitutionDao.store(archivalInstitution);
            
            addToQueue(archivalInstitution, QueueAction.ENABLE_OPEN_DATA, preferences);
            
            return true;
        } else {
            throw new ProcessBusyException();
        }
    }

    private static void addToQueue(ArchivalInstitution archivalInstitution, QueueAction queueAction, Properties preferences) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();

        QueueItem queueItem = fillQueueItem(archivalInstitution, queueAction, preferences);
        indexqueueDao.store(queueItem);
    }
    
    public static QueueAction processQueueItem(QueueItem queueItem) throws Exception { 
        QueueAction queueAction = queueItem.getAction();
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        Properties preferences = EadService.readProperties(queueItem.getPreferences());
        boolean openData = Boolean.valueOf(preferences.getProperty(OpenDataService.ENABLE_OPEN_DATA_KEY));
        EadSolrServerHolder.getInstance().updateOpenDataByAi(queueItem.getArchivalInstitution().getAiname(), queueItem.getArchivalInstitution().getAiId(), openData);
        EacCpfSolrServerHolder.getInstance().updateOpenDataByAi(queueItem.getArchivalInstitution().getAiname(), queueItem.getArchivalInstitution().getAiId(), openData);
        EagSolrServerHolder.getInstance().updateOpenDataByAi(queueItem.getArchivalInstitution().getAiname(), queueItem.getArchivalInstitution().getAiId(), openData);
        queueItemDAO.delete(queueItem);
        return queueAction;
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
    private static QueueItem fillQueueItem(ArchivalInstitution archivalInstitution, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(archivalInstitution, queueAction, preferences, 1000);
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
    private static QueueItem fillQueueItem(ArchivalInstitution archivalInstitution, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = new QueueItem();
        queueItem.setArchivalInstitution(archivalInstitution);
        queueItem.setAiId(archivalInstitution.getAiId());

        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }
        int priority = basePriority;
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
}
