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

    public long enableOpenDataByAi(String aiName, int aiId, boolean isEnable) throws SolrServerException {
        if (isAvailable()) {
            try {
                long startTime = System.currentTimeMillis();
                String queryString = "";
                if (this instanceof EagSolrServerHolder) {
                    queryString = SolrFields.ID + ":\"" + aiId + "\" ";

                } else {
                    queryString = SolrFields.AI + ":\"" + aiName + "\\:" + aiId + "\" ";
                }
                queryString += "AND -" + SolrFields.OPEN_DATA_ENABLE + ":" + Boolean.toString(isEnable);

                SolrQuery query = new SolrQuery(queryString);
                int totalNumberOfDocs = (int) solrServer.query(query).getResults().getNumFound();
                query.setRows(totalNumberOfDocs);
                QueryResponse response = solrServer.query(query);
                for (SolrDocument doc : response.getResults()) {
                    SolrInputDocument inputDocument = ClientUtils.toSolrInputDocument(doc);
                    if (inputDocument.getField("openData") != null) {
                        inputDocument.getField("openData").setValue(isEnable, 1);
                    } else {
                        inputDocument.addField("openData", isEnable, 1);
                    }
                    inputDocument.getField("spell").setValue("", 1);
                    solrServer.add(inputDocument);

                }
                return System.currentTimeMillis() - startTime;
            } catch (IOException e) {
                throw new SolrServerException("Could not enable open data for Ai: " + aiName, e);
            }
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
