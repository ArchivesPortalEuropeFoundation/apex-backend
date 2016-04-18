package eu.apenet.commons.solr;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;

public abstract class AbstractSolrServerHolder {

    private static final int INFINITY_TIMEOUT = 0;
    private final static Logger LOGGER = Logger.getLogger(AbstractSolrServerHolder.class);
    private final static Integer HTTP_TIMEOUT = PropertiesUtil.getInt(PropertiesKeys.APE_SOLR_HTTP_TIMEOUT);
    private final static Integer HTTP_LONG_TIMEOUT = PropertiesUtil.getInt(PropertiesKeys.APE_SOLR_HTTP_LONG_TIMEOUT);

    protected abstract String getSolrUrl();
    private HttpSolrServer solrServer;

    public boolean isAvailable() {
        initSolrServer();
        return solrServer != null;
    }

    public long updateOpenDataByAi(String aiName, int aiId, boolean openDataEnable) throws SolrServerException {
        if (isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
                ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aiId);

                SolrQuery query = genOpenDataByAiSearchQuery(aiName, aiId, openDataEnable);
                //709 which 127th prime, which is 31th prime, which is 11th prime, which is 5th prime, which is 3rd prime, which is 2nd prime, which is 1st prime. >:)
                query.setRows(709);

                int totalNumberOfDocs = (int) solrServer.query(query).getResults().getNumFound();
                while (totalNumberOfDocs > 0) {
                    QueryResponse response = solrServer.query(query);
                    long foundDocsCount = response.getResults().size();

                    for (SolrDocument doc : response.getResults()) {
                        addUnStoredFields(doc, archivalInstitution);
                        
                        SolrInputDocument inputDocument = ClientUtils.toSolrInputDocument(doc);
                        if (inputDocument.getField("openData") == null) {
                            inputDocument.addField("openData", openDataEnable, 1);
                        } else {
                            inputDocument.getField("openData").setValue(openDataEnable, 1);
                        }
                        if (inputDocument.getField("spell") == null) {
                            inputDocument.addField("spell", "", 1);
                        } else {
                            inputDocument.getField("spell").setValue("", 1);
                        }
                        solrServer.add(inputDocument);
                    }

                    solrServer.commit(true, true);
                    totalNumberOfDocs -= foundDocsCount;
                    archivalInstitution.setUnprocessedSolrDocs(archivalInstitution.getUnprocessedSolrDocs() - foundDocsCount);
                    archivalInstitutionDao.store(archivalInstitution);
                }
                return System.currentTimeMillis() - startTime;
            } catch (IOException e) {
                throw new SolrServerException("Could not enable open data for Ai: " + aiName, e);
            }
        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }
    }

    private void addUnStoredFields(SolrDocument doc, ArchivalInstitution archivalInstitution) {
        doc.addField(SolrFields.COUNTRY_ID, archivalInstitution.getCountryId());

        if (this instanceof EadSolrServerHolder) {
            doc.addField(SolrFields.FOND_ID, getIdFromFiled(doc.getFieldValue(SolrFields.TITLE_OF_FOND).toString()));
        }

        if (this instanceof EagSolrServerHolder) {
            ArchivalInstitution ai = archivalInstitution.getParent();
            List<String> ais = new ArrayList<String>();
            while (ai != null) {
                ais.add(ai.getAiId() + "");
                ai = ai.getParent();
            }
            doc.addField(SolrFields.EAG_AI_GROUP_ID, ais);
        } else {
            doc.addField(SolrFields.AI_ID, archivalInstitution.getAiId());
        }
    }

    private String getIdFromFiled(String field) {
        String[] arr = field.split(":");
        return arr[arr.length - 1];
    }

    private SolrQuery genOpenDataByAiSearchQuery(String aiName, int aiId, boolean openDataEnable) throws SolrServerException {
        String queryString = "";
        if (this instanceof EagSolrServerHolder) {
            queryString = SolrFields.ID + ":\"" + aiId + "\" ";

        } else {
            queryString = SolrFields.AI + ":\"" + aiName + "\\:" + aiId + "\" ";
        }
        queryString += "AND -" + SolrFields.OPEN_DATA_ENABLE + ":" + Boolean.toString(openDataEnable);
        SolrQuery query = new SolrQuery(queryString);
        query.setRows(0);

        return query;
    }

    public long getTotalSolrDocsForOpenData(String aiName, int aiId, boolean openDataEnable) throws SolrServerException {
        if (isAvailable()) {
            SolrQuery query = genOpenDataByAiSearchQuery(aiName, aiId, openDataEnable);
            return solrServer.query(query).getResults().getNumFound();
        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }

    }

    public long deleteByQuery(String query) throws SolrServerException {
        if (isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                solrServer.setConnectionTimeout(HTTP_TIMEOUT);
                solrServer.setSoTimeout(HTTP_TIMEOUT);
                solrServer.deleteByQuery(query);
                return System.currentTimeMillis() - startTime;
            } catch (IOException e) {
                throw new SolrServerException("Could not execute query: " + query, e);
            }

        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }
    }

    public long add(Collection<SolrInputDocument> documents) throws SolrServerException {
        if (isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                solrServer.setConnectionTimeout(HTTP_TIMEOUT);
                solrServer.setSoTimeout(HTTP_TIMEOUT);
                solrServer.add(documents);
                return System.currentTimeMillis() - startTime;
            } catch (IOException e) {
                throw new SolrServerException("Could not add documents", e);
            }
        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }
    }

    public long optimize() throws SolrServerException {
        if (isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                solrServer.setConnectionTimeout(INFINITY_TIMEOUT);
                solrServer.setSoTimeout(INFINITY_TIMEOUT);
                solrServer.optimize(true, true);
                LOGGER.info("optimize: " + (System.currentTimeMillis() - startTime) + "ms");
                return System.currentTimeMillis() - startTime;
            } catch (IOException e) {
                throw new SolrServerException("Could not optimize", e);
            }
        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }
    }

    public long hardCommit() throws SolrServerException {
        if (isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                solrServer.setConnectionTimeout(HTTP_LONG_TIMEOUT);
                solrServer.setSoTimeout(HTTP_LONG_TIMEOUT);
                solrServer.commit(true, true, false);
                LOGGER.info("hardcommit: " + (System.currentTimeMillis() - startTime) + "ms");
                return System.currentTimeMillis() - startTime;
            } catch (IOException e) {
                throw new SolrServerException("Could not commit", e);
            }
        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }
    }

    private HttpSolrServer initSolrServer() {
        try {
            if (solrServer == null) {
                LOGGER.debug("Create new solr client: " + getSolrUrl());
                solrServer = new HttpSolrServer(getSolrUrl());
                solrServer.setConnectionTimeout(HTTP_TIMEOUT);
                solrServer.setSoTimeout(HTTP_TIMEOUT);
            }
        } catch (Exception e) {
            LOGGER.error("Solr server " + getSolrUrl() + " is not available", e);
        }
        return solrServer;
    }

    public long rebuildSpellchecker() throws SolrServerException {
        if (isAvailable()) {
            long startTime = System.currentTimeMillis();
            SolrQuery query = new SolrQuery();
            query.setRows(0);
            query.setQuery("*:*");
            query.setRequestHandler("list");
            query.set("spellcheck", "on");
            query.set("spellcheck.build", "true");
            query.set("spellcheck.count", "0");
            solrServer.setConnectionTimeout(INFINITY_TIMEOUT);
            solrServer.setSoTimeout(INFINITY_TIMEOUT);
            solrServer.query(query);
            LOGGER.info("rebuild spellchecker: " + (System.currentTimeMillis() - startTime) + "ms");
            return System.currentTimeMillis() - startTime;
        } else {
            throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
        }
    }

}
