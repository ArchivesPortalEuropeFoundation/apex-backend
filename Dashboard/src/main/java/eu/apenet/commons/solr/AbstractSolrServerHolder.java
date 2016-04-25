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

    public abstract String getSolrUrl();
    private HttpSolrServer solrServer;

    public boolean isAvailable() {
        initSolrServer();
        return solrServer != null;
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
    
    public QueryResponse executeQuery (SolrQuery query) throws SolrServerException {
        if (isAvailable()) {
            return solrServer.query(query); 
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
