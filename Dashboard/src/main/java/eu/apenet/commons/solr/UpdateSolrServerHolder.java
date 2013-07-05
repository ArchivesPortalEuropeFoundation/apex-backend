package eu.apenet.commons.solr;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.common.SolrInputDocument;

import eu.apenet.commons.utils.APEnetUtilities;

public class UpdateSolrServerHolder {
	private static final int QUEUE_SIZE = 200;
	private static final int MAX_THREADS = 2;
	private static final Logger LOGGER = Logger.getLogger(UpdateSolrServerHolder.class);
	private ConcurrentUpdateSolrServer solrServer;
	private static UpdateSolrServerHolder instance;
	private String solrIndexUrl;

	private UpdateSolrServerHolder() {

	}

	public static UpdateSolrServerHolder getInstance() {
		if (instance == null) {
			instance = new UpdateSolrServerHolder();
		}
		return instance;
	}

	public boolean isAvailable() {
		initSolrServer();
		return solrServer != null;
	}

	public long deleteByQuery(String query) throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.deleteByQuery(query);
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not execute query: " + query, e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}

	public long add(Collection<SolrInputDocument> documents) throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.add(documents);
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not add documents", e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}
	
	public long hardCommit() throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.commit(true, true,false);
				LOGGER.info("hardcommit: " + (System.currentTimeMillis() - startTime) + "ms");
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not commit", e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}
	public long softCommit() throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.commit(true, true,true);
				LOGGER.info("softcommit: " + (System.currentTimeMillis() - startTime) + "ms");
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not commit", e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}

	private ConcurrentUpdateSolrServer initSolrServer() {
		try {
			LOGGER.debug("Create new solr client");
			solrIndexUrl = APEnetUtilities.getDashboardConfig().getSolrIndexUrl();
			solrServer = new ConcurrentUpdateSolrServer(solrIndexUrl, QUEUE_SIZE, MAX_THREADS);
		} catch (Exception e) {
			LOGGER.error("Solr server " + solrIndexUrl + " is not available", e);
		}
		return solrServer;
	}

}
