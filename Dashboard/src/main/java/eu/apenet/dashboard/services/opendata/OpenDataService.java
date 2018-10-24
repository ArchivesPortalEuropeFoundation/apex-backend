/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.opendata;

import eu.apenet.commons.exceptions.ProcessBusyException;
import eu.apenet.commons.solr.AbstractSolrServerHolder;
import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.Ead3SolrServerHolder;
import eu.apenet.commons.solr.EadSolrServerHolder;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.commons.solr.SolrDocUtils;
import eu.apenet.commons.solr.SolrFields;
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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author Mahbub
 */
public class OpenDataService {

    protected static final Logger LOGGER = Logger.getLogger(EacCpfService.class);
    public static final String TOTAL_SOLAR_DOC_KEY = "totalSolarDoc";
    public static final String ENABLE_OPEN_DATA_KEY = "enableOpenData";

    private final List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
//    DocumentObjectBinder binder = new DocumentObjectBinder(); 

    private OpenDataService() {
    }

    private static class OpenDataServiceHolder {

        private static final OpenDataService ODS = new OpenDataService();
    };

    public static OpenDataService getInstance() {
        return OpenDataServiceHolder.ODS;
    }

    public boolean openDataPublish(Integer aid, Properties preferences) throws IOException, ProcessBusyException {
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aid);
        SecurityContext.get().checkAuthorized(aid);

        QueueItem oldQueue = null;

        if (archivalInstitution.getOpenDataQueueId() != null) {
            oldQueue = DAOFactory.instance().getQueueItemDAO().findById(archivalInstitution.getOpenDataQueueId());
        }

        if (oldQueue == null) {
            archivalInstitution.setTotalSolrDocsForOpenData(Long.parseLong(preferences.getProperty(TOTAL_SOLAR_DOC_KEY, "0")));
            archivalInstitution.setUnprocessedSolrDocs(Long.parseLong(preferences.getProperty(TOTAL_SOLAR_DOC_KEY, "0")));

            archivalInstitution.setOpenDataEnabled(Boolean.parseBoolean(preferences.getProperty(ENABLE_OPEN_DATA_KEY, "false")));

            QueueItem queueItem = addToQueue(archivalInstitution, QueueAction.ENABLE_OPEN_DATA, preferences);

            archivalInstitution.setOpenDataQueueId(queueItem.getId());

            archivalInstitutionDao.store(archivalInstitution);
            return true;
        } else {
            throw new ProcessBusyException();
        }
    }

    private QueueItem addToQueue(ArchivalInstitution archivalInstitution, QueueAction queueAction, Properties preferences) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();

        QueueItem queueItem = fillQueueItem(archivalInstitution, queueAction, preferences);
        return indexqueueDao.store(queueItem);
    }

    public QueueAction processQueueItem(QueueItem queueItem) throws Exception {
        QueueAction queueAction = queueItem.getAction();
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        Properties preferences = EadService.readProperties(queueItem.getPreferences());
        boolean openData = Boolean.valueOf(preferences.getProperty(OpenDataService.ENABLE_OPEN_DATA_KEY));
        long updateEadTime = updateOpenDataByAi(EadSolrServerHolder.getInstance(), queueItem.getArchivalInstitution(), openData);
        LOGGER.info("Total time needed for enable opendata for Eads: " + updateEadTime + "ms");
        updateOpenDataByAi(EacCpfSolrServerHolder.getInstance(), queueItem.getArchivalInstitution(), openData);
        updateOpenDataByAi(EagSolrServerHolder.getInstance(), queueItem.getArchivalInstitution(), openData);
        updateOpenDataByAi(Ead3SolrServerHolder.getInstance(), queueItem.getArchivalInstitution(), openData);
        queueItemDAO.delete(queueItem);
        return queueAction;
    }

    public long updateOpenDataByAi(AbstractSolrServerHolder solrHolder, ArchivalInstitution aInstitution, boolean openDataEnable) throws SolrServerException {
        if (solrHolder.isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
                ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aInstitution.getAiId());

                SolrQuery query = genOpenDataByAiSearchQuery(solrHolder, archivalInstitution, openDataEnable);
                //52711 which is 5381th prime, which is 709th prime, which is 127th prime, which is 31th prime, which is 11th prime, which is 5th prime, which is 3rd prime, which is 2nd prime, which is 1st prime. >:)
                query.setRows(52711);

                int totalNumberOfDocs = (int) solrHolder.executeQuery(query).getResults().getNumFound();

                while (totalNumberOfDocs > 0) {
                    QueryResponse response = solrHolder.executeQuery(query);
                    long foundDocsCount = response.getResults().size();
                    docList.clear();
                    for (SolrDocument doc : response.getResults()) {
                        addUnStoredFields(solrHolder, doc, archivalInstitution);

                        SolrInputDocument inputDocument = SolrDocUtils.toSolrInputDocument(doc);
                        if (inputDocument.getField("openData") == null) {
                            inputDocument.addField("openData", openDataEnable);
//                            inputDocument.addField("openData", openDataEnable, 1);
                        } else {
                            inputDocument.getField("openData").setValue(openDataEnable, 1);
                        }
                        if (inputDocument.getField("spell") == null) {
                            inputDocument.addField("spell", "");
                        } else {
                            inputDocument.getField("spell").setValue("", 1);
                        }
                        docList.add(inputDocument);
                    }
                    solrHolder.add(docList);
                    solrHolder.hardCommit();
                    totalNumberOfDocs -= foundDocsCount;
                    archivalInstitution.setUnprocessedSolrDocs(archivalInstitution.getUnprocessedSolrDocs() - foundDocsCount);
                    archivalInstitutionDao.store(archivalInstitution);
                }
                return System.currentTimeMillis() - startTime;
            } catch (Exception e) {
                throw new SolrServerException("Could not enable open data for Ai: " + aInstitution.getAiname(), e);
            }
        } else {
            throw new SolrServerException("Solr server " + solrHolder.getSolrUrl() + " is not available");
        }
    }

    private void addUnStoredFields(AbstractSolrServerHolder solrHolder, SolrDocument doc, ArchivalInstitution archivalInstitution) {
        doc.addField(Ead3SolrFields.COUNTRY_ID, archivalInstitution.getCountryId());

//        if (solrHolder instanceof EadSolrServerHolder) {
//            doc.addField(Ead3SolrFields.ID, getIdFromFiled(doc.getFieldValue(Ead3SolrFields.TITLE_PROPER).toString()));
//        }

        if (solrHolder instanceof EagSolrServerHolder) {
            ArchivalInstitution ai = archivalInstitution.getParent();
            List<String> ais = new ArrayList<String>();
            while (ai != null) {
                ais.add(ai.getAiId() + "");
                ai = ai.getParent();
            }
            doc.addField(SolrFields.EAG_AI_GROUP_ID, ais);
        } else {
            doc.addField(Ead3SolrFields.AI_ID, archivalInstitution.getAiId());
        }
    }

    private String getIdFromFiled(String field) {
        String[] arr = field.split(":");
        return arr[arr.length - 1];
    }

    private SolrQuery genOpenDataByAiSearchQuery(AbstractSolrServerHolder solrHolder, ArchivalInstitution archivalInstitution, boolean openDataEnable) throws SolrServerException {
        String queryString = "";
        if (solrHolder instanceof EagSolrServerHolder) {
            queryString = Ead3SolrFields.ID + ":\"" + archivalInstitution.getAiId() + "\" ";

        } else if (solrHolder instanceof Ead3SolrServerHolder) {
            queryString = Ead3SolrFields.AI_ID + ":\"" + archivalInstitution.getAiId() + "\" ";
        } else {
            queryString = Ead3SolrFields.AI + ":\"" + archivalInstitution.getAiname() + "\\:" + archivalInstitution.getAiId() + "\" ";
        }
        queryString += "AND -" + Ead3SolrFields.OPEN_DATA + ":" + Boolean.toString(openDataEnable);
        SolrQuery query = new SolrQuery(queryString);
        query.setRows(0);

        return query;
    }

    public long getTotalSolrDocsForOpenData(AbstractSolrServerHolder solrHolder, ArchivalInstitution archivalInstitution, boolean openDataEnable) throws SolrServerException, IOException {
        if (solrHolder.isAvailable()) {
            SolrQuery query = genOpenDataByAiSearchQuery(solrHolder, archivalInstitution, openDataEnable);
            return solrHolder.executeQuery(query).getResults().getNumFound();
        } else {
            throw new SolrServerException("Solr server " + solrHolder.getSolrUrl() + " is not available");
        }

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
    private QueueItem fillQueueItem(ArchivalInstitution archivalInstitution, QueueAction queueAction, Properties preferences) throws IOException {
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
    private QueueItem fillQueueItem(ArchivalInstitution archivalInstitution, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
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
    private String writeProperties(Properties properties) throws IOException {
        StringWriter stringWriter = new StringWriter();
        properties.store(stringWriter, "");
        String result = stringWriter.toString();
        stringWriter.flush();
        stringWriter.close();
        return result;
    }
}
